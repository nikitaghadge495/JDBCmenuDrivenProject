package nikitaJava.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LabTechnician {

    
	private static Scanner scanner = new Scanner(System.in);
	
	// Method to add a lab technician
    public static void addTechnician() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Technician Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Specialty: ");
            String specialty = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            String sql = "INSERT INTO LabTechnician (Name, Specialty, PhoneNumber, Email) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, specialty);
            statement.setString(3, phoneNumber);
            statement.setString(4, email);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Lab Technician added successfully!");
            } else {
                System.out.println("Failed to add Lab Technician.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a lab technician's details
    public static void updateTechnician() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Technician ID to update: ");
            int technicianID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new Specialty: ");
            String specialty = scanner.nextLine();
            System.out.print("Enter new Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter new Email: ");
            String email = scanner.nextLine();

            String sql = "UPDATE LabTechnician SET Name = ?, Specialty = ?, PhoneNumber = ?, Email = ? WHERE TechnicianID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, specialty);
            statement.setString(3, phoneNumber);
            statement.setString(4, email);
            statement.setInt(5, technicianID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Lab Technician updated successfully!");
            } else {
                System.out.println("Failed to update Lab Technician.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a lab technician
    public static void deleteTechnician() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Technician ID to delete: ");
            int technicianID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String sql = "DELETE FROM LabTechnician WHERE TechnicianID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, technicianID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Lab Technician deleted successfully!");
            } else {
                System.out.println("Failed to delete Lab Technician.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all lab technicians in a table format
    public static void viewTechnicians() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM LabTechnician";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Print table headers
            System.out.printf("%-15s %-20s %-25s %-17s %-25s%n", 
                "TechnicianID", "Name", "Specialty", "PhoneNumber", "Email");
            System.out.println("--------------------------------------------------------------------------------------------------------");

            // Loop through and display all lab technician records
            while (resultSet.next()) {
                System.out.printf("%-15d %-20s %-25s %-17s %-25s%n",
                    resultSet.getInt("TechnicianID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Specialty"),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getString("Email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void manageLabTechnicians() {
        while (true) {
            System.out.println("\nLab Technician Management:");
            System.out.println("1. Add Technician");
            System.out.println("2. Update Technician");
            System.out.println("3. Delete Technician");
            System.out.println("4. View Technicians");
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

            // Switch case for lab technician management
            switch (choice) {
                case 1:
                    LabTechnician.addTechnician();
                    break;
                case 2:
                    LabTechnician.updateTechnician();
                    break;
                case 3:
                    LabTechnician.deleteTechnician();
                    break;
                case 4:
                    LabTechnician.viewTechnicians();
                    break;
                case 5:
                    return;  // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
