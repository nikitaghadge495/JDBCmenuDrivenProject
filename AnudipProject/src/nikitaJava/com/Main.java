package nikitaJava.com;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option: ");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Diagnostic");
            System.out.println("3. Manage Lab Technician");
            System.out.println("4. Manage Appointment");
            System.out.println("5. Manage Payment");
            System.out.println("6. Exit");

            int choice = -1;

            // Input validation loop
            while (true) {
                try {
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();  // Clear the buffer after reading the integer input
                    break;  // Exit loop once a valid integer is provided
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();  // Clear the buffer for invalid input
                }
            }

            // Switch case for the menu
            switch (choice) {
                case 1:
                	Patient.patientOptions();
                    break;
                case 2:
                	DiagnosticTest.manageDiagnosticTests();
                    break;
                case 3:
                	LabTechnician.manageLabTechnicians();
                    break;
                case 4:
                	Appointment.manageAppointments();
                    break;
                case 5:
                	Payment.managePayments();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
        
    
}








