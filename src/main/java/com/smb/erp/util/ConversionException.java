package com.smb.erp.util;

/**
 * This exception may be thrown by classes attempting (and failing)
 * to convert between two types.
 */
public class ConversionException extends Exception
{

    /**
     * Constructor for ConversionException
     */
    public ConversionException()
    {
        super();
    }

    /**
     * Constructor for ConversionException
     */
    public ConversionException(String message)
    {
        super(message);
    }
}

