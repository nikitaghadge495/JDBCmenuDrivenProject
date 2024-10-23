package nikitaJava.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Appointment {

	private static Scanner scanner = new Scanner(System.in);
     //Method to book an appointment
    
	public static void bookAppointment() {
	    try (Connection conn = DatabaseConnection.getConnection()) {

	        // Input for Patient ID
	        System.out.print("Enter Patient ID: ");
	        int patientID = scanner.nextInt();
	        if (!idExists(conn, "patient", "PatientID", patientID)) {
	            System.out.println("Error: Patient ID does not exist.");
	            return;
	        }

	        // Input for Test ID
	        System.out.print("Enter Test ID: ");
	        int testID = scanner.nextInt();
	        if (!idExists(conn, "diagnostictest", "TestID", testID)) {
	            System.out.println("Error: Test ID does not exist.");
	            return;
	        }

	        // Input for Technician ID
	        System.out.print("Enter Technician ID: ");
	        int technicianID = scanner.nextInt();
	        if (!idExists(conn, "labtechnician", "TechnicianID", technicianID)) {
	            System.out.println("Error: Technician ID does not exist.");
	            return;
	        }
	        scanner.nextLine(); // Consume the newline character

	        // Input for Appointment Date and Time
	        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
	        String appointmentDate = scanner.nextLine();
	        System.out.print("Enter Appointment Time (HH:MM:SS): ");
	        String appointmentTime = scanner.nextLine();

	        // Prepare SQL Insert Statement
	        String sql = "INSERT INTO Appointment (AppointmentDate, AppointmentTime, PatientID, TestID, TechnicianID) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setString(1, appointmentDate);
	        statement.setString(2, appointmentTime);
	        statement.setInt(3, patientID);
	        statement.setInt(4, testID);
	        statement.setInt(5, technicianID);

	        // Execute Insert and Check if Successful
	        int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Appointment booked successfully!");
	        } else {
	            System.out.println("Failed to book the appointment.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	// Helper method to check if an ID exists in a table
	private static boolean idExists(Connection conn, String tableName, String columnName, int id) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
	    try (PreparedStatement statement = conn.prepareStatement(sql)) {
	        statement.setInt(1, id);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            return resultSet.getInt(1) > 0;
	        }
	    }
	    return false;
	}


    // Method to update an appointment
    public static void updateAppointment() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Appointment ID to update: ");
            int appointmentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Patient ID: ");
            int patientID = scanner.nextInt();
            System.out.print("Enter new Test ID: ");
            int testID = scanner.nextInt();
            System.out.print("Enter new Technician ID: ");
            int technicianID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Appointment Date (YYYY-MM-DD): ");
            String appointmentDate = scanner.nextLine();
            System.out.print("Enter new Appointment Time (HH:MM:SS): ");
            String appointmentTime = scanner.nextLine();
            System.out.print("Enter new Status: ");
            String status = scanner.nextLine();

            String sql = "UPDATE Appointment SET AppointmentDate = ?, AppointmentTime = ?, PatientID = ?, TestID = ?, TechnicianID = ?, Status = ? WHERE AppointmentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, appointmentDate);
            statement.setString(2, appointmentTime);
            statement.setInt(3, patientID);
            statement.setInt(4, testID);
            statement.setInt(5, technicianID);
            statement.setString(6, status);
            statement.setInt(7, appointmentID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Appointment updated successfully!");
            } else {
                System.out.println("Failed to update the appointment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
 // Method to cancel an appointment
    public static void cancelAppointment() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Appointment ID to cancel: ");
            int appointmentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Check if the Appointment ID exists
            if (!appointmentExists(conn, appointmentID)) {
                System.out.println("Error: The appointment ID you entered does not exist.");
                return;
            }

            String sql = "DELETE FROM Appointment WHERE AppointmentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, appointmentID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Appointment canceled successfully!");
            } else {
                System.out.println("Failed to cancel the appointment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if an appointment exists
    private static boolean appointmentExists(Connection conn, int appointmentID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Appointment WHERE AppointmentID = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, appointmentID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0; // Return true if appointment exists
        }
        return false;
    }  
    // Method to view all appointments in a table format
    public static void viewAppointments() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Appointment";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Print table headers
            System.out.printf("%-15s %-17s %-20s %-14s %-10s %-17s %-17s%n", 
                "AppointmentID", "AppointmentDate", "AppointmentTime", "PatientID", "TestID", "TechnicianID", "Status");
            System.out.println("------------------------------------------------------------------------------------------------------------------");

            // Loop through and display all appointment records
            while (resultSet.next()) {
                System.out.printf("%-15d %-17s %-20s %-15d %-12d %-17d %-10s%n",
                    resultSet.getInt("AppointmentID"),
                    resultSet.getDate("AppointmentDate"),
                    resultSet.getTime("AppointmentTime"),
                    resultSet.getInt("PatientID"),
                    resultSet.getInt("TestID"),
                    resultSet.getInt("TechnicianID"),
                    resultSet.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void manageAppointments() {
        while (true) {
            System.out.println("\nAppointment Management:");
            System.out.println("1. Book Appointment");
            System.out.println("2. Update Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Back to Main Menu");

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

            // Switch case for appointment management
            switch (choice) {
                case 1:
                    Appointment.bookAppointment();
                    break;
                case 2:
                    Appointment.updateAppointment();
                    break;
                case 3:
                    Appointment.cancelAppointment();
                    break;
                case 4:
                    Appointment.viewAppointments();
                    break;
                case 5:
                    return;  // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}





