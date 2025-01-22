import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HMSystemUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalUI().setVisible(true));
    }
}

class HospitalUI extends JFrame {
    static JTextField idField, nameField, ageField, diseaseField;
    static JTextArea resultArea;
    static JButton addButton, showButton, updateButton, deleteButton, showAllButton;

    public HospitalUI() {
        setTitle("Hospital Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));

        idField = createLabeledField(inputPanel, "Patient ID:");
        nameField = createLabeledField(inputPanel, "Patient Name:");
        ageField = createLabeledField(inputPanel, "Patient Age:");
        diseaseField = createLabeledField(inputPanel, "Disease:");
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        addButton = createButton(buttonPanel, "Add Patient");
        showButton = createButton(buttonPanel, "Show Patient");
        updateButton = createButton(buttonPanel, "Update Patient");
        deleteButton = createButton(buttonPanel, "Delete Patient");
        showAllButton = createButton(buttonPanel, "Show All Patients");
        add(buttonPanel, BorderLayout.SOUTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addPatient());
        showButton.addActionListener(e -> showPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        showAllButton.addActionListener(e -> showAllPatients());
    }

    private JTextField createLabeledField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    private JButton createButton(JPanel panel, String buttonText) {
        JButton button = new JButton(buttonText);
        panel.add(button);
        return button;
    }

    private void addPatient() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO patient (id, name, age, disease) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idField.getText()));
            pstmt.setString(2, nameField.getText());
            pstmt.setInt(3, Integer.parseInt(ageField.getText()));
            pstmt.setString(4, diseaseField.getText());
            pstmt.executeUpdate();
            resultArea.setText("Patient added successfully!");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showPatient() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM patient WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                resultArea.setText("ID: " + rs.getInt("id") +
                                   "\nName: " + rs.getString("name") +
                                   "\nAge: " + rs.getInt("age") +
                                   "\nDisease: " + rs.getString("disease"));
            } else {
                resultArea.setText("Patient not found.");
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void updatePatient() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE patient SET name = ?, age = ?, disease = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, nameField.getText());
            pstmt.setInt(2, Integer.parseInt(ageField.getText()));
            pstmt.setString(3, diseaseField.getText());
            pstmt.setInt(4, Integer.parseInt(idField.getText()));
            int rowsUpdated = pstmt.executeUpdate();
            resultArea.setText(rowsUpdated > 0 ? "Patient updated successfully!" : "Patient not found.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void deletePatient() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM patient WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idField.getText()));
            int rowsDeleted = pstmt.executeUpdate();
            resultArea.setText(rowsDeleted > 0 ? "Patient deleted successfully!" : "Patient not found.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("null")
	private static void showAllPatients() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM patient";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);


            String[] columnNames = {"Patient ID", "Patient Name", "Age", "Disease"};
            

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);


            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String disease = rs.getString("disease");
                

                model.addRow(new Object[]{id, name, age, disease});
            }


            JTable table = new JTable(model);
            

            JScrollPane scrollPane = new JScrollPane(table);
            
  
            resultArea.setText(""); 
            resultArea.setLayout(new BorderLayout());
            resultArea.add(scrollPane, BorderLayout.CENTER);

            Window frame = null;
            
            frame.setSize(600, 400);

            con.close();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }
}