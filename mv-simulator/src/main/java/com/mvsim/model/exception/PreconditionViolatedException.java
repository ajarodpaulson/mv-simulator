package com.mvsim.model.exception;

/**
 * Represents a checked exception that is thrown when a method is invoked 
 */
public class PreconditionViolatedException extends Exception {
    public PreconditionViolatedException(String msg) {
        super(msg);
    }
}
