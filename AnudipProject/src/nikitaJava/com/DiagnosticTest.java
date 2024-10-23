package nikitaJava.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DiagnosticTest {

    private static Scanner scanner = new Scanner(System.in);

    // Method to add a diagnostic test
    public static void addTest() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Input for diagnostic test details
            System.out.print("Enter Test Name: ");
            String testName = scanner.nextLine();

            System.out.print("Enter Test Description: ");
            String testDescription = scanner.nextLine();

            System.out.print("Enter Test Price: ");
            double testPrice = scanner.nextDouble();
            scanner.nextLine();  // Clear buffer after nextDouble()

            // SQL query to insert diagnostic test data
            String sql = "INSERT INTO DiagnosticTest (TestName, TestDescription, TestPrice) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, testName);
            statement.setString(2, testDescription);
            statement.setDouble(3, testPrice);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Diagnostic Test added successfully!");
            } else {
                System.out.println("Error occurred while adding the diagnostic test.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all diagnostic tests
    public static void viewTests() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // SQL query to select all diagnostic tests
            String sql = "SELECT * FROM DiagnosticTest";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Print the results
            System.out.printf("%-10s %-20s %-70s %-10s%n", 
                "TestID", "TestName", "TestDescription", "TestPrice");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            
            while (resultSet.next()) {
                int testID = resultSet.getInt("TestID");
                String testName = resultSet.getString("TestName");
                String testDescription = resultSet.getString("TestDescription");
                double testPrice = resultSet.getDouble("TestPrice");
                System.out.printf("%-10d %-20s %-70s %-10.2f%n", testID, testName, testDescription, testPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a diagnostic test
    public static void updateTest() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Test ID to update: ");
            int testID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Test Name: ");
            String testName = scanner.nextLine();

            System.out.print("Enter new Test Description: ");
            String testDescription = scanner.nextLine();

            System.out.print("Enter new Test Price: ");
            double testPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            String sql = "UPDATE DiagnosticTest SET TestName = ?, TestDescription = ?, TestPrice = ? WHERE TestID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, testName);
            statement.setString(2, testDescription);
            statement.setDouble(3, testPrice);
            statement.setInt(4, testID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Diagnostic Test updated successfully!");
            } else {
                System.out.println("Failed to update Diagnostic Test. Make sure the Test ID exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a diagnostic test
    public static void deleteTest() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Test ID to delete: ");
            int testID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String sql = "DELETE FROM DiagnosticTest WHERE TestID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, testID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Diagnostic Test deleted successfully!");
            } else {
                System.out.println("Failed to delete Diagnostic Test. Make sure the Test ID exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void manageDiagnosticTests() {
        while (true) {
            System.out.println("\nDiagnostic Test Management:");
            System.out.println("1. Add Test");
            System.out.println("2. Update Test");
            System.out.println("3. Delete Test");
            System.out.println("4. View Tests");
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

            // Switch case for diagnostic test management
            switch (choice) {
                case 1:
                    DiagnosticTest.addTest();
                    break;
                case 2:
                    DiagnosticTest.updateTest();
                    break;
                case 3:
                    DiagnosticTest.deleteTest();
                    break;
                case 4:
                    DiagnosticTest.viewTests();
                    break;
                case 5:
                    return;  // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
