package com.smb.erp.util;

/**
 * Class representing internal errors thrown in the execution of
 * any class in the uk.org.skeet hierarchy.
 */
public class JlsInternalError extends Error
{
    public JlsInternalError (String message)
    {
        super (message);
    }
}
