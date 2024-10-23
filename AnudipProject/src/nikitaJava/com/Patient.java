package nikitaJava.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patient {

    private static Scanner scanner = new Scanner(System.in);

    // Method to add a new patient
    public static void addPatient() {
        try (Connection conn = DatabaseConnection.getConnection()) {

        	 System.out.println("Enter Patient Name: ");
             String name = scanner.next();
             
             while (name.isEmpty()) {
                 System.out.println("Patient name cannot be empty. Please enter a valid name: ");
                 name = scanner.nextLine();
             }
             scanner.nextLine();
            String dateOfBirth = getDateInput("Enter Date of Birth (YYYY-MM-DD): ");
            String gender = getGenderInput();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            String phoneNumber = getPhoneNumberInput();
            String email = getEmailInput();

            String sql = "INSERT INTO Patient (Name, DateOfBirth, Gender, Address, PhoneNumber, Email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, dateOfBirth);
            statement.setString(3, gender);
            statement.setString(4, address);
            statement.setString(5, phoneNumber);
            statement.setString(6, email);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing patient
    public static void updatePatient() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            int patientID = getIntInput("Enter Patient ID to update: ");

            if (!patientExists(conn, patientID)) {
                System.out.println("Error: The patient ID you entered does not exist. Please try again.");
                return;
            }

            System.out.println("Enter new Patient Name: ");
            String name = scanner.next();
            
            while (name.isEmpty()) {
                System.out.println("Patient name cannot be empty. Please enter a valid name: ");
                name = scanner.nextLine();
            }
            scanner.nextLine();
            String dateOfBirth = getDateInput("Enter new Date of Birth (YYYY-MM-DD): ");
            String gender = getGenderInput();
            System.out.print("Enter new Address: ");
            String address = scanner.nextLine();
            String phoneNumber = getPhoneNumberInput();
            String email = getEmailInput();

            String sql = "UPDATE Patient SET Name = ?, DateOfBirth = ?, Gender = ?, Address = ?, PhoneNumber = ?, Email = ? WHERE PatientID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, dateOfBirth);
            statement.setString(3, gender);
            statement.setString(4, address);
            statement.setString(5, phoneNumber);
            statement.setString(6, email);
            statement.setInt(7, patientID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient updated successfully!");
            } else {
                System.out.println("Failed to update patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a patient
    public static void deletePatient() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            int patientID = getIntInput("Enter Patient ID to delete: ");

            if (!patientExists(conn, patientID)) {
                System.out.println("Error: The patient ID you entered does not exist. Please try again.");
                return;
            }

            String sql = "DELETE FROM Patient WHERE PatientID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, patientID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("Failed to delete patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all patients in a table format
    public static void viewPatients() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Select all columns from the Patient table
            String sql = "SELECT PatientID, Name, DateOfBirth, Gender, Address, PhoneNumber, Email FROM Patient";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Print table headers
            System.out.printf("%-12s %-20s %-15s %-10s %-30s %-15s %-30s%n", 
                "PatientID", "Name", "DateOfBirth", "Gender", "Address", "PhoneNumber", "Email");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");

            // Loop through the result set and display each patient's details
            while (resultSet.next()) {
                // Debugging to ensure 'Name' is fetched correctly
                String name = resultSet.getString("Name");
                if (name == null || name.isEmpty()) {
                    System.out.println("Error: Name is missing or null for PatientID: " + resultSet.getInt("PatientID"));
                } else {
                    System.out.printf("%-12d %-20s %-15s %-10s %-30s %-15s %-30s%n",
                        resultSet.getInt("PatientID"),
                        name, // Name column should display here
                        resultSet.getString("DateOfBirth"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Address"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("Email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Helper method to check if a patient exists
    private static boolean patientExists(Connection conn, int patientID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Patient WHERE PatientID = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, patientID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    // Helper method to get a valid integer input
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    // Helper method to get a valid date input
    private static String getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateInput = scanner.nextLine();
            try {
                LocalDate.parse(dateInput);
                return dateInput;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
            }
        }
    }

    // Helper method to get a valid gender input
    private static String getGenderInput() {
        while (true) {
            System.out.print("Enter Gender (Male/Female): ");
            String gender = scanner.nextLine();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                return gender;
            } else {
                System.out.println("Invalid gender input. Please enter 'Male' for Male or 'Female' for Female.");
            }
        }
    }

    // Helper method to get a valid phone number
    private static String getPhoneNumberInput() {
        while (true) {
            System.out.print("Enter Phone Number (10 digits): ");
            String phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d{10}")) {
                return phoneNumber;
            } else {
                System.out.println("Invalid phone number. Please enter a 10-digit number.");
            }
        }
    }

    // Helper method to get a valid email address
    private static String getEmailInput() {
        while (true) {
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return email;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
    }

    // Method to display patient options menu
    public static void patientOptions() {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add Patient");
            System.out.println("2. Update Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. View Patients");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    updatePatient();
                    break;
                case 3:
                    deletePatient();
                    break;
                case 4:
                    viewPatients();
                    break;
                case 5:
                    return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
