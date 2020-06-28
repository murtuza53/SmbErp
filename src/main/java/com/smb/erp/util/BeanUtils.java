package com.smb.erp.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class BeanUtils {

    public BeanUtils() {

    }

    /*
	 * Copy bean properties from source to target.
	 * Properites is source and target property names
     */
    public static void copyProperties(Object sourceBean, Object targetBean, Properties properties) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Object prop : properties.keySet()) {
            Object value = null;
            try {
                value = ReflectionUtil.readProperty(sourceBean, prop.toString());
                ReflectionUtil.writeProperty(targetBean, properties.get(prop).toString(), value);
            } catch (NullPointerException err) {
                //ignore if value read was null

                //value = ReflectionUtil.defaultValue(value, ReflectionUtil.getReadMethod(sourceBean.getClass(), prop.toString()).getReturnType());
                //System.out.println(prop + " " + value);
            }
        }
    }

    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     */
    public static Object clone(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos
                    = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in
                    = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
}
