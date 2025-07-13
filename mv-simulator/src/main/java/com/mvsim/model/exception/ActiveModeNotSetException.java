package com.mvsim.model.exception;

public class ActiveModeNotSetException extends Exception {
    public ActiveModeNotSetException() {
        super("Need to pick a mode before starting ventilation");
    }
}
