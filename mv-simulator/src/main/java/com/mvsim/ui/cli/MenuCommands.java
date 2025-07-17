package com.mvsim.ui.cli;

import com.mvsim.ui.cli.exception.MenuCommandNotFoundException;

/**
 * Represents the menu command-message pairs in the CLI app
 */
public enum MenuCommands {
    CREATE_NEW_LUNG_SIM("Create a new lung simulator", "n"),
    SELECT_VENT_MODE_AND_SETTINGS("Select a ventilation mode and settings", "m"),
    START_VENTILATION("Start ventilation", "b"),
    CHANGE_VENTILATION("Change ventilation settings",  "c"),
    QUIT_APPLICATION("Exit the application", "q");
    
    private final String message;
    private final String command;
    
    MenuCommands(String message, String command) {
        this.message = message;
        this.command = command;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getCommand() {
        return command;
    }
    
    // Method to find enum value from command string
    public static MenuCommands fromCommand(String userInputtedCommand) throws MenuCommandNotFoundException {
        for (MenuCommands mc : values()) {
            if (mc.command.equals(userInputtedCommand)) {
                return mc;
            }
        }
        throw new MenuCommandNotFoundException(userInputtedCommand + " is not a valid command. Please try again.");
    }
}
