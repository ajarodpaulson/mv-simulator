package com.mvsim.model.exception;

public class NoSuchVentilationSettingException extends Exception {
    public NoSuchVentilationSettingException() {
        super("No such setting was found in the settings map keyset.");
    }
}
