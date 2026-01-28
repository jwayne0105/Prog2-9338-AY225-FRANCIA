import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class PrelimLabWork3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrelimGUI3D());
    }
}

class PrelimGUI3D extends JFrame {

    JTextField attendanceField, lw1Field, lw2Field, lw3Field;
    JTextArea outputArea;
    JButton computeBtn;

    final int TOTAL_MEETINGS = 30;

    public PrelimGUI3D() {
        setTitle("Prelim Lab Work 3 - Java Edition");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // ===== HEADER =====
        JLabel title = new JLabel("PRELIM GRADE CALCULATOR", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== INPUT CARD (GridBagLayout para pantay lahat) =====
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Attendance
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        cardPanel.add(label("Number of Attendances:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        attendanceField = styledField();
        cardPanel.add(attendanceField, gbc);

        // Lab Work 1
        gbc.gridx = 0; gbc.gridy = 1;
        cardPanel.add(label("Lab Work 1 Grade:"), gbc);
        gbc.gridx = 1;
        lw1Field = styledField();
        cardPanel.add(lw1Field, gbc);

        // Lab Work 2
        gbc.gridx = 0; gbc.gridy = 2;
        cardPanel.add(label("Lab Work 2 Grade:"), gbc);
        gbc.gridx = 1;
        lw2Field = styledField();
        cardPanel.add(lw2Field, gbc);

        // Lab Work 3
        gbc.gridx = 0; gbc.gridy = 3;
        cardPanel.add(label("Lab Work 3 Grade:"), gbc);
        gbc.gridx = 1;
        lw3Field = styledField();
        cardPanel.add(lw3Field, gbc);

        // ===== CENTER WRAPPER =====
        JPanel centerWrapper = new JPanel(new BorderLayout(0, 20));
        centerWrapper.setOpaque(false);
        centerWrapper.add(cardPanel, BorderLayout.CENTER);

        computeBtn = new JButton("COMPUTE PRELIM DATA");
        computeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        computeBtn.setBackground(new Color(52, 152, 219));
        computeBtn.setForeground(Color.WHITE);
        computeBtn.setFocusPainted(false);
        computeBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(41, 128, 185)),
            BorderFactory.createEmptyBorder(12, 0, 12, 0)
        ));
        centerWrapper.add(computeBtn, BorderLayout.SOUTH);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // ===== OUTPUT AREA =====
        outputArea = new JTextArea(12, 20);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(new TitledBorder(new LineBorder(new Color(149, 165, 166)), "OFFICIAL REPORT"));
        mainPanel.add(scroll, BorderLayout.SOUTH);

        computeBtn.addActionListener(e -> computeGrades());

        setVisible(true);
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(52, 73, 94));
        return lbl;
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(100, 30));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(new MatteBorder(0, 0, 2, 0, new Color(189, 195, 199)));
        return tf;
    }

    private void computeGrades() {
        try {
            int att = Integer.parseInt(attendanceField.getText());
            double lw1 = Double.parseDouble(lw1Field.getText());
            double lw2 = Double.parseDouble(lw2Field.getText());
            double lw3 = Double.parseDouble(lw3Field.getText());

            if (att > TOTAL_MEETINGS || lw1 > 100 || lw2 > 100 || lw3 > 100 || att < 0 || lw1 < 0 || lw2 < 0 || lw3 < 0) {
                outputArea.setForeground(Color.RED);
                outputArea.setText("⚠️ INVALID INPUT DETECTED!");
                return;
            }

            // Calculations based on instructions
            double attendanceScore = (att / (double) TOTAL_MEETINGS) * 100;
            double labAverage = (lw1 + lw2 + lw3) / 3;
            double classStanding = (attendanceScore * 0.40) + (labAverage * 0.60);

            // PRELIM GRADE = (EXAM * 0.3) + (CS * 0.7)
            double reqPass = (75 - (classStanding * 0.70)) / 0.30;
            double reqExce = (100 - (classStanding * 0.70)) / 0.30;

            // Color Logic
            if (classStanding >= 80) {
                outputArea.setForeground(new Color(39, 174, 96)); // Green
            } else if (classStanding >= 75) {
                outputArea.setForeground(new Color(211, 160, 0)); // Yellow
            } else {
                outputArea.setForeground(new Color(192, 57, 43)); // Red
            }

            outputArea.setText(String.format(
                "Attendance Score: %.2f%%\n" +
                "Lab Work Average: %.2f\n" +
                "Class Standing:   %.2f\n" +
                "------------------------------\n" +
                "REQUIRED PRELIM EXAM SCORE:\n" +
                "To Pass (75):     %.2f\n" +
                "To Excel (100):   %.2f",
                attendanceScore, labAverage, classStanding, reqPass, reqExce
            ));

        } catch (NumberFormatException ex) {
            outputArea.setForeground(Color.RED);
            outputArea.setText("ERROR: Numbers only!");
        }
    }
}