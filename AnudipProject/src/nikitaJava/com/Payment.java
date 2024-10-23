package nikitaJava.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Payment {

	private static Scanner scanner = new Scanner(System.in);
    // Method to make a payment (add payment)
	
    public static void makePayment() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Appointment ID: ");
            int appointmentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!appointmentExists(conn, appointmentID)) {
                System.out.println("Error: The appointment ID you entered does not exist. Please try again.");
                return;
            }

            System.out.print("Enter Payment Amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter Payment Method (e.g., Cash, Credit Card, etc.): ");
            String paymentMethod = scanner.nextLine();

            String sql = "INSERT INTO Payment (AppointmentID, PaymentDate, PaymentAmount, PaymentMethod) VALUES (?, CURDATE(), ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, appointmentID);
            statement.setDouble(2, paymentAmount);
            statement.setString(3, paymentMethod);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Payment made successfully!");
            } else {
                System.out.println("Error occurred while making the payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a payment
    public static void updatePayment() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Payment ID to update: ");
            int paymentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Appointment ID: ");
            int appointmentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!appointmentExists(conn, appointmentID)) {
                System.out.println("Error: The appointment ID you entered does not exist. Please try again.");
                return;
            }

            System.out.print("Enter new Payment Amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new Payment Method (e.g., Cash, Credit Card, etc.): ");
            String paymentMethod = scanner.nextLine();

            String sql = "UPDATE Payment SET AppointmentID = ?, PaymentAmount = ?, PaymentMethod = ? WHERE PaymentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, appointmentID);
            statement.setDouble(2, paymentAmount);
            statement.setString(3, paymentMethod);
            statement.setInt(4, paymentID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Payment updated successfully!");
            } else {
                System.out.println("Error occurred while updating the payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a payment
    public static void deletePayment() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.print("Enter Payment ID to delete: ");
            int paymentID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String sql = "DELETE FROM Payment WHERE PaymentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, paymentID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Payment deleted successfully!");
            } else {
                System.out.println("Error occurred while deleting the payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all payments in a table format
    public static void viewPayments() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Payment";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Print table headers
            System.out.printf("%-10s %-15s %-15s %-20s %-20s%n", 
                "PaymentID", "AppointmentID", "PaymentDate", "PaymentAmount", "PaymentMethod");
            System.out.println("------------------------------------------------------------------------------------------------");

            // Loop through and display all payment records
            while (resultSet.next()) {
                System.out.printf("%-10d %-15d %-15s %-20f %-20s%n",
                    resultSet.getInt("PaymentID"),
                    resultSet.getInt("AppointmentID"),
                    resultSet.getDate("PaymentDate"),
                    resultSet.getDouble("PaymentAmount"),
                    resultSet.getString("PaymentMethod"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if the appointment exists
    private static boolean appointmentExists(Connection conn, int appointmentID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Appointment WHERE AppointmentID = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, appointmentID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }

        return false;
    }
    
    public static void managePayments() {
        while (true) {
            System.out.println("\nPayment Management:");
            System.out.println("1. Add Payment");
            System.out.println("2. Update Payment");
            System.out.println("3. Delete Payment");
            System.out.println("4. View Payments");
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

            // Switch case for payment management
            switch (choice) {
                case 1:
                    Payment.makePayment();
                    break;
                case 2:
                    Payment.updatePayment();
                    break;
                case 3:
                    Payment.deletePayment();
                    break;
                case 4:
                    Payment.viewPayments();
                    break;
                case 5:
                    return;  // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
