package com.mvsim.ui.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mvsim.model.Setting;
import com.mvsim.model.SimulationManager;
import com.mvsim.model.Units;
import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.exception.PreconditionViolatedException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.lungsim.LungSimSetting;
import com.mvsim.model.ventilator.mode.ModeTAG;
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
                configureLungSim();
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
            case CHANGE_VENTILATION:
                changeVentilation();
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

    private void changeVentilation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeVentilation'");
    }

    /**
     * Starts the ventilator using the active mode and settings and prints out the
     * peak pressure, delivered flow rate, and delivered tidal volume, respectively,
     * on three different lines. The user may change or stop ventilation at any
     * time.
     */
    /*
     * jobs:
     * call start ventilation on the ventilator
     * while the ventilator is ventilating
     * whenever a control loop tick finishes, obtain the pressure, flowrate, and
     * tidal volume data and print it
     * Whenever inspiration finishes and expiration, update the peak pressure and
     * inspired tidal volume
     * Whenever expiration finishes and inspiration starts, update the expired tidal
     * volume and measured Peep
     * Every 60s update respiratory rate and minute volume (TODO: use more accurate
     * calculation like rolling avg or calculating time between breaths)
     */

    private void startVentilation() {
        try {
            simMgr.startSimulation();
        } catch (ActiveModeNotSetException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Press Enter to stop ventilation...");

        Thread displayThread = new Thread(() -> {
            while (simMgr.isVentilating()) {
                String pressureData = String.format("%.2f", simMgr.getCurrentSystemPressure());
                String flowData = String.format("%.2f", simMgr.getCurrentSystemFlowrate());
                String volumeData = String.format("%.2f", simMgr.getCurrentSystemVolumeChange());

                synchronized (System.out) {
                    // Clear screen and return to home position
                    System.out.print("\033[H\033[2J");  
                    System.out.printf("Pressure: %s\n", pressureData);
                    System.out.printf("Flow:     %s\n", flowData);
                    System.out.printf("Volume:   %s\n", volumeData);
                    System.out.println("\nPress Enter to stop ventilation...");
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // This thread's only job is to wait for input
        Thread inputThread = new Thread(() -> {
            scanner.nextLine();
            simMgr.stopSimulation();
        });

        displayThread.start();
        inputThread.start();

        try {
            displayThread.join();
            inputThread.join();
            System.out.println("Ventilation stopped.");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\nVentilation was interrupted.");
        }
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
     * 
     * TODO: enforce that all settings set before proceeding i.e., allowing
     * ventilation to be enabled
     */
    private void chooseSettings() {
        for (Setting setting : simMgr.getVtrController().getActiveMode().getSettings()) {
            System.out.println("Enter the desired setting for the " + setting.getName() + ".");
            setting.setValue(this.scanner.nextFloat());
            this.scanner.nextLine();
        }
    }

    /**
     * A sequence of input-requiring questions to configure the lung simulator.
     */
    private void configureLungSim() {
        System.out.println("Enter the lung compliance in " + Units.COMPLIANCE.getNotation() + " (normal is 50-170):");
        float compliance = this.scanner.nextFloat();
        this.scanner.nextLine();

        this.simMgr.configureLungSim(LungSimSetting.COMPLIANCE, compliance);

        System.out.println("Enter the lung resistance in " + Units.RESISTANCE.getNotation() + " (normal is 0.6-2.4):");
        float resistance = this.scanner.nextFloat();
        this.scanner.nextLine();

        this.simMgr.configureLungSim(LungSimSetting.RESISTANCE, resistance);

        

        System.out.println("The lung sim is configured!");
    }

    private void chooseMode() {
        System.out.println("Available ventilation modes:");

        List<ModeTAG> availableModes = new ArrayList<>(simMgr.getVtrController().getAvailableModes());

        for (int i = 0; i < availableModes.size(); i++) {
            ModeTAG m = availableModes.get(i);
            System.out.println((i + 1) + ": " + m.toString());
        }

        System.out.println("\nEnter mode number:");
        int selection = scanner.nextInt();
        scanner.nextLine();

        if (selection > 0 && selection <= availableModes.size()) {
            ModeTAG selectedModeTAG = availableModes.get(selection - 1);
            simMgr.getVtrController().setActiveModeWithCorrespondingDefaultSettings(selectedModeTAG);
            System.out.println("Selected mode: " + selectedModeTAG);
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