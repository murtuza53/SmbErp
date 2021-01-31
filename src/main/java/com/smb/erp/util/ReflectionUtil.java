package com.smb.erp.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Utility class to perform some tricks with reflection. This class is
 * particularly handy for configuration utilities, as it allows easy conversion
 * between classes.
 */
public class ReflectionUtil {

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ReflectionUtil() {
    }
    private static final Class[] PRIMITIVES = {boolean.class, byte.class,
        char.class, short.class, int.class, long.class, float.class,
        double.class
    };
    private static final Class[] WRAPPERS = {Boolean.class, Byte.class,
        Character.class, Short.class, Integer.class, Long.class,
        Float.class, Double.class
    };
    private static final Class[][] PARAMETER_CHAINS = {
        {boolean.class, null}, {byte.class, Short.class},
        {char.class, Integer.class}, {short.class, Integer.class},
        {int.class, Long.class}, {long.class, Float.class},
        {float.class, Double.class}, {double.class, null}};
    /**
     * Mapping of primitives to wrappers
     */
    private static Map primitiveMap = new HashMap();
    /**
     * Mapping of wrappers to primitives
     */
    private static Map wrapperMap = new HashMap();
    /**
     * Mapping from wrapper class to appropriate parameter types (in order) Each
     * entry is an array of Classes, the last of which is either null (for no
     * chaining) or the next class to try
     */
    private static Map parameterMap = new HashMap();
    /**
     * Default number format
     */
    private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    static {
        for (int i = 0; i < PRIMITIVES.length; i++) {
            primitiveMap.put(PRIMITIVES[i], WRAPPERS[i]);
            wrapperMap.put(WRAPPERS[i], PRIMITIVES[i]);
            parameterMap.put(WRAPPERS[i], PARAMETER_CHAINS[i]);
        }
    }

    public static Class convertToWrapper(Class cl) {

        if (cl == char.class || cl == String.class) {
            return String.class;
        } else if (cl == boolean.class) {
            return Boolean.class;
        } else if (cl == byte.class) {
            return Byte.class;
        } else if (cl == int.class) {
            return Integer.class;
        } else if (cl == long.class) {
            return Long.class;
        } else if (cl == float.class) {
            return Float.class;
        } else if (cl == double.class) {
            return Double.class;
        } else {
            return cl;
        }
    }

    public static boolean isNumberType(Class cl) {
        return cl == byte.class || cl == Byte.class || cl == int.class || cl == Integer.class || cl == long.class || cl == Long.class
                || cl == float.class || cl == Float.class || cl == double.class || cl == Double.class;
    }

    public static boolean isPremitiveType(Class cl) {
        return isNumberType(cl) || cl == boolean.class || cl == Boolean.class || cl == String.class || cl == Date.class;
    }

    /**
     * Converts a String to an instance of a specified wrapper class, if
     * possible. Note that for boolean targets with a String source, the
     * convertToBoolean method is called, so any valid argument to
     * convertToBoolean is a valid source. The default NumberFormat (as obtained
     * by NumberFormat.getInstance()) is used if the appropriate decode(String)
     * method fails. If parsing with the NumberFormat is successful,
     * convertToWrapper (Number, target, boolean) is called, with a value of
     * "false" for the lossOfAccuracy parameter (so that only lossless
     * conversions are allowed).
     *
     * @param source String to convert
     * @param target wrapper class to convert to
     *
     * @throws NullPointerException if either target or source are null
     * @throws ConversionException if the source cannot be converted
     * @throws IllegalArgumentException if the target is not a wrapper class
     */
    public static Object convertToWrapper(String source, Class target)
            throws ConversionException {
        // Check arguments
        if (target == null) {
            throw new NullPointerException(
                    "Null target class passed to convertToWrapper");
        }

        if (source == null) {
            throw new NullPointerException(
                    "Null source string passed to convertToWrapper");
        }

        if (target == String.class) {
            return source;
        }

        if (!wrapperMap.containsKey(target) && !primitiveMap.containsKey(target)) {
            throw new IllegalArgumentException(
                    "Non-wrapper target class passed to convertToWrapper");
        }

        String str = ((String) source).trim();
        // First try the most obvious method
        try {
            if (target.equals(Boolean.class
            ) || target.equals(boolean.class
            )) {
                return convertToBoolean(str)
                        ? Boolean.TRUE : Boolean.FALSE;
            } else if (target.equals(Byte.class
            ) || target.equals(byte.class
            )) {
                return Byte.decode(str);
            } else if (target.equals(Short.class
            ) || target.equals(short.class
            )) {
                return Short.decode(str);
            } else if (target.equals(Integer.class
            ) || target.equals(int.class
            )) {
                return Integer.decode(str);
            } else if (target.equals(Long.class
            ) || target.equals(long.class
            )) {
                return Long.decode(str);
            } else if (target.equals(Float.class
            ) || target.equals(float.class
            )) {
                return new Float(str);
            } else if (target.equals(Double.class
            ) || target.equals(double.class
            )) {
                return new Double(str);
            } else if (target.equals(Date.class
            )) {
                return new Date(Long.parseLong(source));
            } else if (target.equals(XMLGregorianCalendar.class
            )) {
                return XMLGregorianCalendarConverter.asXMLGregorianCalendar(
                        new Date(Long.parseLong(source)));
            } else if (target.equals(Character.class
            ) || target.equals(char.class
            )) {
                if (((String) source).length()
                        == 1) {
                    return new Character(((String) source).charAt(0));
                }
            }
        } catch (NumberFormatException e) {
        }

        // Then try the NumberFormat...
        try {
            return convertToWrapper(NUMBER_FORMAT.parse(str), target, false);
        } catch (ParseException e) {
            throw new ConversionException("Unable to convert " + source + " to " + target.getName());
        }
    }

    /**
     * Converts a Number to an instance of a specified wrapper class, if
     * possible. If any loss of accuracy would be involved, the conversion is
     * rejected if allowLossOfAccuracy is false.
     *
     * @param source String to convert
     * @param target wrapper class to convert to
     * @param allowLossOfAccuracy if true, "lossy" conversions are allowed; if
     * false, "lossy" conversions will throw an IllegalArgumentException
     *
     * @throws NullPointerException if either target or source are null
     * @throws ConversionException if the source cannot be converted to the
     * target
     * @throws IllegalArgumentException if the target is not a wrapper class
     */
    public static Object convertToWrapper(Number source, Class target,
            boolean allowLossOfAccuracy) throws ConversionException {
        // Check arguments
        if (target == null) {
            throw new NullPointerException(
                    "Null target class passed to convertToWrapper");
        }

        if (source == null) {
            throw new NullPointerException(
                    "Null source string passed to convertToWrapper");
        }

        if (!wrapperMap.containsKey(target)) {
            throw new IllegalArgumentException(
                    "Non-wrapper target class passed to convertToWrapper");
        }

        // Check for no-op case
        if (target.isInstance(source)) {
            return source;
        }

        // Check for character case (special as it's not a Number)
        if (target.equals(Character.class
        )) {
            if (source.doubleValue()
                    != source.longValue()) {
                throw new ConversionException("Unable to convert " + source + " to a character");
            }

            long l = source.longValue();
            if ((l < Character.MIN_VALUE || l > Character.MAX_VALUE) && !allowLossOfAccuracy) {
                throw new ConversionException("Unable to convert " + source + " to a character without loss of accuracy");
            }

            return new Character(
                    (char) l
            );
        }

        Number result = null;
        if (target.equals(Byte.class
        )) {
            result = new Byte(source.byteValue());
        } else if (target.equals(Short.class
        )) {
            result = new Short(source.shortValue());
        } else if (target.equals(Integer.class
        )) {
            result = new Integer(source.intValue());
        } else if (target.equals(Long.class
        )) {
            result = new Long(source.longValue());
        } else if (target.equals(Float.class
        )) {
            result = new Float(source.floatValue());
        } else if (target.equals(Double.class
        )) {
            result = new Double(source.doubleValue());
        } else {
            throw new IllegalArgumentException("Unhandled wrapper class " + target.getName());
        }

        if ((result.doubleValue() != source.doubleValue() || result.longValue() != source.longValue()) && !allowLossOfAccuracy) {
            throw new ConversionException("Conversion of " + source + " to " + target.getName() + " would result in a loss of accuracy");
        }

        return result;
    }

    /**
     * Attempts to create an instance of the given class from the object passed
     * in, in a generous fashion. If the object is already of the appropriate
     * type, it is returned as-is. Otherwise, the following steps are taken:
     * <ol>
     * <li>If the target is a "primitive class" (eg int.class) it is converted
     * into the appropriate wrapper class.
     * <li>Wrapped classes are special-cased using the convertToWrapper methods
     * - if the source is a Number, only lossless conversions are allowed.
     * <li>An attempt is made to find a public constructor of the desired class
     * which takes a single parameter of the type of object specified (or a
     * supertype). For instance, passing in a String in order to create a
     * java.text.MessageFormat will cause the method to call
     * java.text.MessageFormat's constructor taking a String.
     * <li>An attempt is made to find a public static method of the desired
     * class which takes a single parameter of the type of object specified (or
     * a supertype) and returns an instance of the target class (or a subtype).
     * For instance, passing in any object to be converted into a String (and
     * assuming that there isn't an appropriate String constructor) will invoke
     * String.valueOf(source).
     * </ol>
     * Note that if the source is an instance of a wrapper class, any attempts
     * to find appropriate methods to call with the source as a parameter will
     * also attempt to find a similar method to call with the primitive value of
     * the wrapper (eg if an Integer is passed in, attempts to find methods with
     * a single int parameter will also be made). Attempts will be made to find
     * other compatible methods with primitives (eg if an Integer is passed in,
     * an attempt will be made to find methods with a single long parameter.) In
     * the case of long->double conversion, this may introduce a loss of
     * accuracy. If a more specific method is present, however, it will always
     * be called - for instance, if an Integer is passed in, a method taking an
     * Integer parameter will be used in preference to one taking an int
     * parameter, which will be used in preference to one taking a long
     * parameter. The most specific possible method is also used in cases where
     * there is more than one matching method and the source is a normal object.
     * For instance, new Foo (String) is preferred to new Foo (Object) if the
     * source is a String. However, if there is no most-specific method (if the
     * source implements multiple interfaces, for instance) the order of
     * preference between unrelated interfaces is unspecified. If multiple
     * methods with different names are found in step 4, they are sorted by
     * specificity first, then alphabetically.
     * <p>
     * If any one attempt for conversion fails (eg an exception is thrown),
     * further attempts will be made. This should be considered carefully for
     * side-effects, as it means that many methods may be called while trying to
     * find a valid conversion.
     *
     * @param source object to be "converted" to the target class
     * @param target class of requested object
     *
     * @return an instance of the target class representing the source object
     * (or an instance of the appropriate wrapper class if the target class is a
     * primitive class). null may be returned if an appropriate-looking static
     * method has been found but it returns null.
     *
     * @throws NullPointerException if either target or source are null.
     * @throws ConversionException if no way of converting the source to the
     * target class can be found
     */
    public static Object convertObject(Object source, Class target)
            throws ConversionException {
        // Check arguments
        if (target == null) {
            throw new NullPointerException(
                    "Null target class passed to convertObject");
        }

        if (source == null) {
            throw new NullPointerException(
                    "Null source object passed to convertObject");
        }

        // Convert primitive classes into wrappers
        if (target.isPrimitive()) {
            Class realTarget = (Class) primitiveMap.get(target);
            if (realTarget == null) {
                throw new ConversionException("Unhandled primitive class " + target.getName());
            }
            target = realTarget;
        }

        // Check for no-op case
        if (target.isInstance(source)) {
            return source;
        }

        Class sourceClass = source.getClass();
        String sourceClassName = sourceClass.getName();

        // Special-case wrapper targets
        if (wrapperMap.containsKey(target)) {
            if (source instanceof String) {
                return convertToWrapper((String) source, target);
            }
            if (source instanceof Number) {
                return convertToWrapper((Number) source, target, false);
            }
            throw new ConversionException("Unable to convert " + source + " to " + target.getName());
        }

        // Map of converted values from the source to each of the appropriate
        // wrapper classes for which the source is a valid type to
        // call a method on (eg with a source of Integer, there would be
        // entries in here from int.class to Integer, long.class to Long
        // etc).
        // For non-wrapper sources, this will be null.
        Map convertedPrimitives = createPrimitiveMap(source);

        // List of types of parameters we've found - used for
        // sorting.
        Set foundParameterTypes = new HashSet();

        // Get *all* the methods and constructors...
        Invokable[] allInvokables = Invokable.getInvokables(target);

        // Now filter them...
        List filteredInvokables = new ArrayList();
        for (int i = 0; i < allInvokables.length; i++) {
            Invokable inv = allInvokables[i];

            // Filter out instance methods
            if (inv.isMethod() && !Modifier.isStatic(inv.getModifiers())) {
                continue;
            }

            // Now filter by parameter types:
            Class[] paramTypes = inv.getParameterTypes();

            // Must have exactly one parameter
            if (paramTypes.length != 1) {
                continue;
            }

            // Must be a compatible parameter type
            if (!(paramTypes[0].isInstance(source) || convertedPrimitives.containsKey(paramTypes[0]))) {
                continue;
            }

            // Success - add it to the two lists. Note
            // that the order isn't important
            filteredInvokables.add(inv);
            foundParameterTypes.add(paramTypes[0]);
        }

        // Now sort the parameter types by specificity
        List sortedTypes = sortBySpecificity(foundParameterTypes);

        // Now (for speed of sorting the filtered invokables)
        // create a map from the parameter type to the index within
        // sortedTypes
        final Map sortIndexMap = new HashMap();
        for (int i = 0; i < sortedTypes.size(); i++) {
            sortIndexMap.put(sortedTypes.get(i), new Integer(i));
        }

        // Sort the filtered invokables
        Collections.sort(filteredInvokables, new Comparator() {

            public int compare(Object o1, Object o2) {
                Invokable inv1 = (Invokable) o1;
                Invokable inv2 = (Invokable) o2;

                // Constructors go first
                if (inv1.isConstructor() && inv2.isMethod()) {
                    return -1;
                }
                if (inv2.isConstructor() && inv1.isMethod()) {
                    return 1;
                }

                // Both the same type, so check the parameters (there
                // will definitely be exactly one each due to previous
                // filtering)
                Class param1 = inv1.getParameterTypes()[0];
                Class param2 = inv2.getParameterTypes()[0];

                // If the parameter types are the same, compare
                // the methods by name.
                if (param1.equals(param2)) {
                    return inv1.getName().compareTo(inv2.getName());
                }

                // Different parameter types, so compare by index within
                // sortedTypes
                int i1 = ((Integer) sortIndexMap.get(param1)).intValue();
                int i2 = ((Integer) sortIndexMap.get(param2)).intValue();

                return i1 - i2;
            }
        });

        try {
            return invokeInOrder(filteredInvokables, null,
                    new Object[]{source});
        } catch (NoSuchMethodException e) {
            throw new ConversionException("Unable to convert " + source + " to " + target.getName());
        }
    }

    /**
     * Creates a map from primitive classes to wrapper instances where the value
     * of the instance is equivalent (as far as possible) to the source object.
     * Only "upwardly compatible" classes are mapped, ie giving an Integer as a
     * source maps int, long, float, double, whereas giving a Float as a source
     * maps just float and double. The purpose of this method is to find
     * possible values for method invocation - for instance, as an int variable
     * can be passed as a parameter to a method requiring a double, there is a
     * mapping created for the double primitive class. If there is no mapping
     * for a primitive class, it means that the source object could not have
     * been a wrapper for a primitive type that could legally be used to call a
     * method with that value as an actual parameter and that primitive class as
     * the parameter type.
     *
     * @param source source object, which needn't be a wrapper instance but
     * should be non-null
     *
     * @throws NullPointerException if source is null
     *
     * @return a Map containing all appropriate mappings from primitive classes
     * to wrapper instances. If the source object isn't a wrapper instance, this
     * map will be empty.
     */
    public static Map createPrimitiveMap(Object source) {
        Map ret = new HashMap();

        // If the source is not a wrapper, return the empty map.
        if (!wrapperMap.containsKey(source.getClass())) {
            return ret;
        }

        while (true) {
            Class[] chainLink = (Class[]) parameterMap.get(source.getClass());
            if (chainLink == null || chainLink.length != 2 || chainLink[0] == null) {
                throw new JlsInternalError(
                        "Error traversing parameter chain with source of type " + source.getClass());
            }

            ret.put(chainLink[0], source);

            if (chainLink[1] == null) {
                break;
            }

            // Character is a special-case. Convert it straight
            // to an Integer
            if (source.getClass().equals(Character.class
            )) {
                source = new Integer(((Character) source).charValue());
            } else {
                try {
                    source = convertToWrapper((Number) source, chainLink[1],
                            true);
                } catch (ConversionException e) {
                    throw new JlsInternalError(
                            "ConversionException where it should be impossible: " + e.getMessage());
                }
            }
        }
        return ret;
    }

    /**
     * Sorts the given set of Classes by specificity and returns it as a list;
     * more specific classes will come before less specific classes (eg
     * FileInputStream comes before InputStream comes before Object). Normal
     * classes come before interfaces, which come before primitive classes. The
     * ordering within interfaces is partially undefined if two or more
     * unrelated classes/interfaces are involved, but each subclass will come
     * before its superclass, etc. The current implementation of this method is
     * speed O(n^2) and space O(n) so care should be taken not to call it with
     * huge lists. Expected usage is for small lists (for instance parameters of
     * possible methods) so this shouldn't be a problem. I may rewrite this to
     * sort in-place and in a rather faster time at a later date. If you wish to
     * do this yourself, I'd be grateful if you'd mail me the code at <a
     * href="mailto:skeet@pobox.com">skeet@pobox.com</a> if you don't mind me
     * including it in future releases (with appropriate credits, of course).
     */
    public static List sortBySpecificity(Set classes) {
        List sorted = new ArrayList(classes.size());

        for (Iterator it = classes.iterator(); it.hasNext();) {
            Class current = (Class) it.next();

            // We'll add the primitives right at the end, straight from the list
            if (current.isPrimitive()) {
                continue;
            }

            boolean ifaceCurrent = current.isInterface();

            ListIterator tests = null;
            for (tests = sorted.listIterator(); tests.hasNext();) {
                Class test = (Class) tests.next();

                boolean ifaceTest = test.isInterface();

                if (ifaceCurrent && !ifaceTest) {
                    continue;
                }
                if (!ifaceCurrent && ifaceTest) {
                    tests.previous();
                    break;
                }

                if (test.isAssignableFrom(current)) {
                    tests.previous();
                    break;
                }
            }
            tests.add(current);
        }

        for (int i = 0; i < PRIMITIVES.length; i++) {
            if (classes.contains(PRIMITIVES[i])) {
                sorted.add(PRIMITIVES[i]);
            }
        }

        return sorted;
    }

    /**
     * Invokes a list of Invokables in turn with the same parameters each time
     * (or the appropriate wrappers for primitive classes) until one succeeds
     * (ie doesn't throw an exception during invocation).
     *
     * @param invokables list of Invokable objects
     * @param obj object to invoke methods on
     * @param params array of parameter objects. Each is converted to wrappers
     * for primitive classes where needed.
     *
     * @throws IllegalArgumentException if any of the Invokables for which
     * invocation is attempted takes the wrong number of parameters or if any of
     * the target parameters cannot be converted from the parameters given. Note
     * that this is only thrown directly within the method itself - any
     * IllegalArgumentExceptions caused by the actual invocation are swallowed.
     *
     * @throws NullPointerException if invokables or params is null, or if any
     * element of invokables for which invocation is attempted is null
     *
     * @throws ClassCastException if any element of invokables is not an
     * Invokable
     *
     * @throws NoSuchMethodException if none of the given Invokables succeeded
     *
     * @return the result of the first successful invocation
     */
    public static Object invokeInOrder(List invokables, Object obj,
            Object[] params) throws NoSuchMethodException {
        if (invokables == null) {
            throw new NullPointerException(
                    "Null invokables parameter passed to invokeInOrder");
        }

        if (params == null) {
            throw new NullPointerException(
                    "Null params parameter passed to invokeInOrder");
        }

        // Create conversion maps
        Map[] conversionMaps = new Map[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                conversionMaps[i] = createPrimitiveMap(params[i]);
            } else {
                conversionMaps[i] = new HashMap();
            }
        }

        Object[] parameters = new Object[params.length];

        for (Iterator it = invokables.iterator(); it.hasNext();) {
            Invokable inv = (Invokable) it.next();
            if (inv == null) {
                throw new NullPointerException(
                        "Null element in invokables parameter passed to invokeInOrder");
            }

            Class[] paramClasses = inv.getParameterTypes();

            if (paramClasses.length != params.length) {
                throw new IllegalArgumentException("Invokable parameter takes " + paramClasses.length + "parameters; " + params.length + " parameters given.");
            }

            // Set up the parameters
            for (int i = 0; i < params.length; i++) {
                if (primitiveMap.containsKey(paramClasses[i])) {
                    parameters[i] = conversionMaps[i].get(paramClasses[i]);
                    if (parameters[i] == null) {
                        throw new IllegalArgumentException(
                                "Unable to convert parameter " + i + " of specified parameters to " + paramClasses[i].getName());
                    }
                } else {
                    parameters[i] = params[i];
                }
            }

            try {
                return inv.invoke(obj, parameters);
            } catch (Throwable e) {
                // Keep going...
            }
        }
        throw new NoSuchMethodException("Unable to invoke any listed method.");
    }

    /**
     * Attempts to convert a String parameter to a boolean value. Valid
     * parameters are "on", "true", "yes", "off", "false" and "no", and the
     * parameter is trimmed and turned into lower case before being converted.
     * While this is not strictly a method involving reflection, it is in this
     * class as it fits in well with the other methods.
     *
     * @throws IllegalArgumentException if the value cannot be converted.
     *
     * @return the boolean value of the string
     */
    public static boolean convertToBoolean(String value)
            throws IllegalArgumentException {
        String val = value.trim().toLowerCase();
        if (val.equals("true") || val.equals("on") || val.equals("yes") || val.equals("1")) {
            return true;
        }
        if (val.equals("false") || val.equals("off") || val.equals("no") || val.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("Invalid boolean val " + value);
    }

    /**
     * Helper method to find if <code>field</code> is editable
     *
     * @author MNV
     */
    public static boolean isEditable(Class cl, Field field) {
        try {
            cl.getMethod(makeSetter(field.getName()), new Class[]{field.getType()});
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    /**
     * Helper method to find if <code>propertyName</code> is editable
     *
     * @author MNV
     */
    public static boolean isEditable(Class cl, String propertyName,
            Class propertyClass) {
        try {
            cl.getMethod(makeSetter(propertyName),
                    new Class[]{propertyClass});
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    /**
     * Helper method to find if <code>field</code> is readable
     *
     * @author MNV
     */
    public static boolean isReadable(Class cl, Field field) {
        try {
            if (field.getType() == boolean.class
                    || field.getType() == Boolean.class) {
                cl.getMethod(makeIs(field.getName()));
            } else {
                cl.getMethod(makeGetter(field.getName()));
            }
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    /**
     * Prepare setXXX() method string from property name based on JavaBean
     * convention
     */
    public static String makeSetter(String fieldName) {

        return "set" + upperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /**
     * Prepare getXXX() method string from property name based on JavaBean
     * convention
     */
    public static String makeGetter(String fieldName) {

        return "get" + upperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /**
     * Returns read method for the given property
     *
     */
    public static Method getReadMethod(Class clazz, String propertyName) {
        Method readMethod = null;
        String base = capitalize(propertyName);

        // Since there can be multiple setter methods but only one getter
        // method, find the getter method first so that you know what the
        // property type is. For booleans, there can be "is" and "get"
        // methods. If an "is" method exists, this is the official
        // reader method so look for this one first.
        try {
            readMethod = clazz.getMethod("is" + base, null);
        } catch (Exception getterExc) {
            try {
                // no "is" method, so look for a "get" method.
                readMethod = clazz.getMethod("get" + base, null);
            } catch (Exception e) {
                // no is and no get, we will return null
            }
        }

        return readMethod;
    }

    /**
     * Returns write method for the given property
     *
     */
    public static Method getWriteMethod(Class clazz, String propertyName) {

        Class propertyType = getReadMethod(clazz, propertyName).getReturnType();

        Method writeMethod = null;
        String base = capitalize(propertyName);

        Class params[] = {propertyType};
        try {
            writeMethod = clazz.getMethod("set" + base, params);
        } catch (Exception e) {
            // no write method
            System.out.println("No write method for " + propertyName + "\n" + e.toString());
        }

        return writeMethod;
    }

    /**
     * Returns write method for the given property
     *
     */
    public static Method getWriteMethod(Class clazz, String propertyName,
            Class propertyType) {
        Method writeMethod = null;
        String base = capitalize(propertyName);

        Class params[] = {propertyType};
        try {
            writeMethod = clazz.getMethod("set" + base, params);
        } catch (Exception e) {
            // no write method
            System.out.println("No write method for " + propertyName + "\n" + e.toString());
        }

        return writeMethod;
    }

    /**
     * Retrieves the type of the property with the given name of the given
     * Class.<br>
     * Supports nested properties following bean naming convention.
     *
     * "foo.bar.name"
     *
     * @see PropertyUtils#getPropertyDescriptors(Class)
     *
     * @param clazz
     * @param propertyName
     *
     * @return Null if no property exists.
     */
    public static Class<?> getPropertyType(Class<?> clazz, String propertyName) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz must not be null.");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("PropertyName must not be null.");
        }

        final String[] path = propertyName.split("\\.");

        for (int i = 0; i < path.length; i++) {
            propertyName = path[i];
            final PropertyDescriptor[] propDescs = PropertyUtils.getPropertyDescriptors(clazz);
            for (final PropertyDescriptor propDesc : propDescs) {
                if (propDesc.getName().equals(propertyName)) {
                    clazz = propDesc.getPropertyType();
                    if (i == path.length - 1) {
                        return clazz;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns all field names of Number type
     *
     */
    public static List<String> getNumberFields(Class cls) {
        List<String> ret = new LinkedList<String>();
        for (Field f : cls.getDeclaredFields()) {
            if (isNumberType(f.getType())) {
                ret.add(f.getName());
            }
        }
        return ret;
    }

    public static List<String> getAllFields(Class cls) {
        List<String> ret = new LinkedList<String>();
        addFieldsAndSubField(cls, ret);
        return ret;
    }

    public static List<String> addFieldsAndSubField(Class cls, List<String> ret) {
        for (Field f : cls.getDeclaredFields()) {
            if (!isClassCollection(f.getType())) {
                if (isPremitiveType(f.getType())) {
                    ret.add(f.getName());
                } else {
                    addFieldsAndSubField(f.getType(), ret);
                }
            }
        }
        return ret;
    }

    public static List<BeanField> getFields(Class cls, int stopDepth) {
        List<BeanField> ret = new LinkedList<BeanField>();
        int currentDepth = 1;
        String prefix = "";
        addFields(cls, ret, currentDepth, stopDepth, prefix);
        return ret;
    }

    public static List<BeanField> addFields(Class cls, List<BeanField> ret, int currentDepth, int stopDepth, String prefix) {
        currentDepth = currentDepth + 1;
        for (Field f : cls.getDeclaredFields()) {
            if (!isClassCollection(f.getType())) {
                if (isPremitiveType(f.getType())) {
                    ret.add(new BeanField(prefix + f.getName(), f.getType()));
                } else {
                    if (currentDepth <= stopDepth) {
                        addFields(f.getType(), ret, currentDepth, stopDepth, prefix + f.getName() + ".");
                    } else {
                        ret.add(new BeanField(prefix + f.getName(), f.getType()));
                    }
                }
            }
        }
        return ret;
    }

    public static boolean isClassCollection(Class c) {
        return Collection.class.isAssignableFrom(c) || Map.class.isAssignableFrom(c) || c.isArray();
    }

    public static List<BeanField> getFields_ListType(Class cls) {
        List<BeanField> ret = new LinkedList<BeanField>();
        for (Field f : cls.getDeclaredFields()) {
            if (isClassCollection(f.getType())) {
                ret.add(new BeanField(f.getName(), getListClass(f)));
            }
        }
        return ret;
    }

    public static Class getListClass(Field field) {
        try {
            //Field field = this.getClass().getDeclaredField(fieldName);
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            Class<?> clazz = (Class<?>) type.getActualTypeArguments()[0];
            return clazz;
        } //catch (NoSuchFieldException ex) {
        //    Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        //} 
        catch (SecurityException ex) {
            Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns method name with correct character convention
     */
    private static String capitalize(String s) {
        if (s.length() == 0) {
            return s;
        } else {
            char chars[] = s.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return String.valueOf(chars);
        }
    }

    /**
     * Prepare isXXX() method string from property name based on JavaBean
     * convention
     */
    public static String makeIs(String fieldName) {

        return "is" + upperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /**
     * Constructs property name from a method name based on JavaBean convention
     *
     * @param methodName
     * @return
     */
    public static String makeColumnName(String methodName) {
        if (methodName.startsWith("is") == true) {
            return methodName.substring(2);
        } else {
            return methodName.substring(3);
        }
    }

    /**
     * Prepare presentable column header name from the property name
     */
    public static char upperCase(char c) {
        return (c + "").toUpperCase().charAt(0);
    }

    /**
     * Reads the property value from the bean
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object readProperty(Object bean, String propertyName)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return getReadMethod(bean.getClass(), propertyName).invoke(bean);
    }

    /**
     * Write the property value to the bean provided its editable else throws
     * Exception
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void writeProperty(Object bean, String propertyName,
            Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        getWriteMethod(bean.getClass(), propertyName).invoke(bean, new Object[]{value});
    }

    /*
     * Returns default value of the if and only if value is null else return
     * value
     */
    public static Object defaultValue(Object value, Class cl) {
        if (value == null) {
            if (cl
                    == char.class
                    || cl == String.class) {

                return "";
            } else if (cl
                    == boolean.class) {
                return Boolean.FALSE;
            } else if (cl
                    == byte.class) {

                return 0;
            } else if (cl
                    == int.class) {

                return 0;
            } else if (cl
                    == long.class) {

                return 0l;
            } else if (cl
                    == float.class) {

                return 0f;
            } else if (cl
                    == double.class) {

                return 0.0;
            } else {
                return null;
            }
        } else if (value.toString().length() == 0) {
            if (cl
                    == char.class
                    || cl == String.class) {

                return "";
            } else if (cl
                    == boolean.class) {
                return Boolean.FALSE;
            } else if (cl
                    == byte.class) {

                return 0;
            } else if (cl
                    == int.class) {

                return 0;
            } else if (cl
                    == long.class) {

                return 0l;
            } else if (cl
                    == float.class) {

                return 0f;
            } else if (cl
                    == double.class) {

                return 0.0;
            } else {
                return null;
            }
        } else {
            return value;
        }
    }

    /*
     * Clones the object including the class heirarchy
     */
    public static Object clone(Object o) {
        Object clone = null;

        try {
            clone = o.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Walk up the superclass hierarchy
        for (Class obj = o.getClass(); !obj.equals(Object.class
        ); obj = obj.getSuperclass()) {
            Field[] fields = obj.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    // for each class/suerclass, copy all fields
                    // from this object to the clone
                    fields[i].set(clone, fields[i].get(o));
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
        return clone;
    }

    /*
     * Creates and returns new instance of ClassName
     */
    public static Object createObject(String className) {
        Object object = null;
        try {
            Class classDefinition = Class.forName(className);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return object;
    }

    /*
     * This method creates instance of Object and populates with data as per
     * fields.
     * 
     * Pre-requisite is to have the required object class file in the path.
     */
    public static Object createObject(Class cls, String[] fields,
            Object[] values) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Object bean = createObject(cls.getName());

        for (int i = 0; i < fields.length; i++) {
            Class typeClass = getReadMethod(cls, fields[i]).getReturnType();
            Object value = null;
            if (values[i] == null) {
                value = defaultValue(values[i], typeClass);
            }
            writeProperty(bean, fields[i], value);
        }

        return bean;
    }

    /*
     * This method creates instance of Object and populates with data as per
     * fields.
     * 
     * Pre-requisite is to have the required object class file in the path.
     */
    public static Object createObject(Class cls, String[] fields,
            String[] values, String dateFormatPattern) throws Exception {
        Object bean = createObject(cls.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

        for (int i = 0; i < fields.length; i++) {
            //System.out.println(fields[i]);
            Class typeClass = getReadMethod(cls, fields[i]).getReturnType();
            Object value = null;
            if (values[i] == null || values[i].equalsIgnoreCase("null")) {
                value = defaultValue(null, typeClass);
            } else {
                if (typeClass == java.util.Date.class) {
                    value = dateFormat.parseObject(values[i]);
                } else if (typeClass == XMLGregorianCalendar.class) {
                    value = XMLGregorianCalendarConverter.asXMLGregorianCalendar(dateFormat.parse(values[i]));
                } else {
                    value = convertToWrapper(values[i], typeClass);
                }
            }
            writeProperty(bean, fields[i], value);
        }

        return bean;
    }

    public static Object[] createArrayFromObject(Class cls, Object bean, String[] fields) {
        if (bean == null) {
            return null;
        }
        if (fields == null || fields.length == 0) {
            fields = new String[cls.getDeclaredFields().length];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = cls.getDeclaredFields()[i].getName();
            }
        }

        Object[] array = new Object[fields.length];
        for (int j = 0; j < fields.length; j++) {
            try {
                array[j] = readProperty(bean, fields[j]);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        return array;
    }

    public static Object[][] createArrayFromObject(Class cls, List beans, String[] fields) {
        if (beans == null || beans.size() == 0) {
            return null;
        }
        if (fields == null || fields.length == 0) {
            fields = new String[cls.getDeclaredFields().length];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = cls.getDeclaredFields()[i].getName();
            }
        }

        Object[][] array = new Object[beans.size()][fields.length];
        for (int i = 0; i < beans.size(); i++) {
            for (int j = 0; j < fields.length; j++) {
                try {
                    array[i][j] = readProperty(beans.get(i), fields[j]);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }

        return array;
    }

}
