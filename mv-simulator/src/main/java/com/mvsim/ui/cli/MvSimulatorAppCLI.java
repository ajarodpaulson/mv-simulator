package com.mvsim.ui.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.Units;
import com.mvsim.model.exception.PreconditionViolatedException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.ventilator.ModeType;
import com.mvsim.model.ventilator.settings.Setting;
import com.mvsim.ui.cli.exception.MenuCommandNotFoundException;

public class MvSimulatorAppCLI {
    private Scanner scanner;
    private boolean isProgramRunning;
    private SimulationManager simMgr;

    /**
     * Creates an instance of the MV Simulator application
     */
    public MvSimulatorAppCLI() {
        init();

        printDivider();
        System.out.println("Welcome to the Mechanical Ventilation Simulator app!");
        printDivider();

        while (this.isProgramRunning) {
            handleMainMenu();
        }
    }

    public void init() {
        this.simMgr = SimulationManager.getInstance();
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
    }

    public void handleMainMenu() {
        displayMainMenu();
        String input = this.scanner.nextLine();
        try {
            processMainMenuCommand(MenuCommands.fromCommand(input));
        } catch (MenuCommandNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayMainMenu() {
        System.out.println("Please select an option:\n");

        for (MenuCommands cmd : MenuCommands.values()) {
            System.out.println(cmd.getCommand() + ": " + cmd.getMessage());
        }

        printDivider();
    }

    public void processMainMenuCommand(MenuCommands cmd) {
        printDivider();
        switch (cmd) {
            case CREATE_NEW_LUNG_SIM:
                createNewLungSim();
                break;
            case SELECT_VENT_MODE_AND_SETTINGS:
                try {
                    createNewVentilationSimulator();
                } catch (PreconditionViolatedException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case START_VENTILATION:
                startVentilation();
                break;
            case STOP_VENTILATION:
                stopVentilation();
                break;
            case QUIT_APPLICATION:
                System.out.println("Thanks for using the Mechanical Ventilation simulator!");
                this.isProgramRunning = false;
                break;
            default:
                break;
        }
        printDivider();
    }

    private void stopVentilation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopVentilation'");
    }

    private void startVentilation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startVentilation'");
    }

    /**
     * Displays a sequence of input-requiring questions in order to configure the
     * mode of the ventilation simulator.
     * 
     * @throws PreconditionViolatedException if lung simulator has not yet been
     *                                       configured
     */
    private void createNewVentilationSimulator() throws PreconditionViolatedException {
        if (simMgr.getLungSim() == null) {
            throw new PreconditionViolatedException("Must configure lung simulator first.");
        }
        chooseMode();
        chooseSettings();
    }

    /**
     * Uses the selected mode to display a sequence of input-requiring questions in
     * order to configure the settings for the selected mode of the ventilation
     * simulator.
     */
    /*
     * XXX: Use a custom iterator here? As opposed to having an if-else statement
     * that, for each mode, has a hardcoded question for each of the mode's settings
     * If you change one of the settings later (e.g., I:E ratio instead of
     * inspiratory time), this function will break
     * An iterator alows us to iterate over a collection witout knowing anything
     * about the collection's internal structure
     * 
     * This implementation only works for numeric settings. What about, for example,
     * trigger, where you want to be able to choose between a flow and a pressure
     * trigger? What about settings that are a boolean (e.g., sigh breaths on,
     * volume guarantee on)?
     */
    private void chooseSettings() {
        for (Setting setting : simMgr.getVtr().getActiveMode().getSettings()) {
            System.out.println("Enter the desired setting for the " + setting.getName() + ".");
            setting.setValue(this.scanner.nextFloat());
            this.scanner.nextLine();
        }
    }

    /**
     * A sequence of input-requiring questions to configure the lung simulator.
     */
    private void createNewLungSim() {
        System.out.println("Enter the lung compliance in " + Units.COMPLIANCE.getNotation() + " (normal is 50-170):");
        float compliance = this.scanner.nextFloat();
        this.scanner.nextLine();

        System.out.println("Enter the lung resistance in " + Units.RESISTANCE.getNotation() + " (normal is 0.6-2.4):");
        float resistance = this.scanner.nextFloat();
        this.scanner.nextLine();

        this.simMgr.setLungSim(new LungSim(compliance, resistance));

        System.out.println("The lung sim is configured!");
    }

    private void chooseMode() {
        System.out.println("Available ventilation modes:");

        List<ModeType> availableModes = new ArrayList<>(simMgr.getVtr().getAvailableModes());

        for (int i = 0; i < availableModes.size(); i++) {
            ModeType mode = availableModes.get(i);
            System.out.println((i + 1) + ": " + mode.toString());
        }

        System.out.println("\nEnter mode number:");
        int selection = scanner.nextInt();
        scanner.nextLine();

        if (selection > 0 && selection <= availableModes.size()) {
            ModeType selectedMode = availableModes.get(selection - 1);
            simMgr.getVtr().setActiveMode(simMgr.getVtr().getModeTable().getMode(selectedMode));
            System.out.println("Selected mode: " + selectedMode);
        } else {
            System.out.println("Invalid selection. Please try again.");
        }
    }

    /**
     * Prints out a line of dashes to act as a divider
     */
    private void printDivider() {
        System.out.println("------------------------------------");
    }
}