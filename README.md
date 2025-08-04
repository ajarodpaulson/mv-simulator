# Mechanical Ventilation Simulator
This application simulates a setup involving a test lung and mechanical ventilator (MV). Users can apply basic parameters to the lung and MV models and observe how this impacts basic ventilation metrics.
<img width="2032" height="1167" alt="Screenshot 2025-08-04 at 12 38 49 PM" src="https://github.com/user-attachments/assets/55c3c1ce-8a8c-4707-9628-5a0aaf5137b7" />

## Who might use this application?
Learners of respiratory physiology.

## Why did I make this project?
I'm a respiratory therapist with a keen interest in respiratory physiology and mechanical ventilation.

## User Stories
1. As a user, I want to parameterize a lung simulator so that I can see how it behaves when ventilated.
2. As a user, I want to parameterize a mechanical ventilation simulator so I can see how the same lung simulator responds to different ventilator modes and settings.
3. As a user, I want to be able to observe the output metrics from ventilating the lung simulator so that I can reason about the mode and settings being used.

## How to run locally
Install the latest version of java
Check the version using the command
`java --version`
Clone the repository from github by typing in the command line
HTTPS: `git clone https://github.com/ajarodpaulson/mv-simulator.git`
SSH: `git clone git@github.com:ajarodpaulson/mv-simulator.git`
Navigate into the project directory
`cd mv-simulator`
Run the application
To run the CLI version of the application:
Run Main.java in the cli folder (mv-simulator/src/main/java/com/mvsim/ui/cli/Main.java)
To run the GUI version of the application:
Run Main.java in the desktop folder (mv-simulator/src/main/java/com/mvsim/ui/desktop/Main.java)
