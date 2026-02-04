/**
 * Programmer Identifier: [Your Name] - [Your Student ID]
 * Program Description: Java Swing application for managing student records.
 */

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentRecordSystem extends JFrame {
    private JTable recordTable;
    private DefaultTableModel tableModel;
    private JTextField txtID, txtName, txtGrade;
    private JButton btnAdd, btnDelete;

    public StudentRecordSystem() {
        // Application Identity and Window Settings
        this.setTitle("Student Records - [Your Name] [Your ID]");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Table Setup
        String[] columnHeaders = {"Student ID", "Full Name", "Prelim Grade"};
        tableModel = new DefaultTableModel(columnHeaders, 0);
        recordTable = new JTable(tableModel);
        this.add(new JScrollPane(recordTable), BorderLayout.CENTER);

        // Input Panel with English Labels
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        inputPanel.add(new JLabel("Student ID:"));
        txtID = new JTextField();
        inputPanel.add(txtID);

        inputPanel.add(new JLabel("Full Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Prelim Grade:"));
        txtGrade = new JTextField();
        inputPanel.add(txtGrade);

        btnAdd = new JButton("Add Record");
        btnDelete = new JButton("Delete Row");
        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);

        this.add(inputPanel, BorderLayout.SOUTH);

        // CORRECTED FILE LOADING
        // Use double backslashes for Windows paths in Java
        String filePath = "C:\\folder ko toh boi\\Prog2-9338-AY225-FRANCIA\\-PRELIM-EXAM\\JAVA\\data.csv";
        loadDataFromCSV(filePath);

        // Action Listeners
        btnAdd.addActionListener(e -> {
            if (!txtID.getText().isEmpty() && !txtName.getText().isEmpty()) {
                tableModel.addRow(new Object[]{txtID.getText(), txtName.getText(), txtGrade.getText()});
                txtID.setText(""); txtName.setText(""); txtGrade.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Fill in all fields.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = recordTable.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a record to delete.");
            }
        });
    }

    private void loadDataFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // Skip CSV Header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7) {
                    tableModel.addRow(new Object[]{values[0], values[1] + " " + values[2], values[6]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "System Message: Could not locate " + filename);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordSystem().setVisible(true));
    }
}