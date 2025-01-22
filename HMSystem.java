import java.sql.*;
import java.util.Scanner;

public class HMSystem {
    static Scanner sc;
    public static void main(String[] args) {
        int opt = 0;
        sc = new Scanner(System.in);
        do {
            System.out.println("\n1. Add a new Patient");
            System.out.println("2. Show Specific Patient");
            System.out.println("3. Show All Patients");
            System.out.println("4. Update Patient Details");
            System.out.println("5. Delete Patient");
            System.out.println("6. Exit");
            System.out.print("Enter your option: ");
            opt = sc.nextInt();
            switch (opt) {
                case 1: addPatient(); break;
                case 2: showPatient(); break;
                case 3: showAllPatients(); break;
                case 4: updatePatient(); break;
                case 5: deletePatient(); break;
                case 6: break;
                default: System.out.println("Invalid option");
            }
        } while (opt != 6);
    }

    private static void addPatient() {
        try {
            // Get connection from MyConnection class
            Connection con = DBConnection.getConnection();
            System.out.println("Connection established");

            // Prepare SQL query
            String query = "INSERT INTO patient (id, name, age, disease) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);

            // Get patient details
            System.out.print("Enter patient ID: ");
            int id = sc.nextInt();
            sc.nextLine();  // Consume newline character
            System.out.print("Enter patient name: ");
            String name = sc.nextLine();
            System.out.print("Enter patient age: ");
            int age = sc.nextInt();
            sc.nextLine();  // Consume newline character
            System.out.print("Enter disease: ");
            String disease = sc.nextLine();

            // Set values in the query
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, disease);

            // Execute the query
            pstmt.executeUpdate();
            System.out.println("Patient added successfully");

            // Close the connection
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void showPatient() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM patient WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);

            System.out.print("Enter patient ID to search: ");
            int id = sc.nextInt();
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Patient ID: " + rs.getInt("id"));
                System.out.println("Patient Name: " + rs.getString("name"));
                System.out.println("Patient Age: " + rs.getInt("age"));
                System.out.println("Disease: " + rs.getString("disease"));
            } else {
                System.out.println("No patient found with ID: " + id);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void showAllPatients() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM patient";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("+----+------------------+-----+---------------------+");
            System.out.println("| ID | Name             | Age | Disease             |");
            System.out.println("+----+------------------+-----+---------------------+");

            while (rs.next()) {
                System.out.printf("| %-2d | %-16s | %-3d | %-19s |\n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("disease"));
            }

            System.out.println("+----+------------------+-----+---------------------+");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void updatePatient() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE patient SET name = ?, age = ?, disease = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);

            System.out.print("Enter patient ID to update: ");
            int id = sc.nextInt();

            sc.nextLine();  // Consume newline character
            System.out.print("Enter the new patient name: ");
            String name = sc.nextLine();
            System.out.print("Enter the new patient age: ");
            int age = sc.nextInt();
            sc.nextLine();  // Consume newline character
            System.out.print("Enter the new disease: ");
            String disease = sc.nextLine();

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, disease);
            pstmt.setInt(4, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient details updated successfully");
            } else {
                System.out.println("Patient not found with ID: " + id);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void deletePatient() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM patient WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);

            System.out.print("Enter the patient ID to delete: ");
            int id = sc.nextInt();
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Patient deleted successfully");
            } else {
                System.out.println("No patient found with ID: " + id);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}