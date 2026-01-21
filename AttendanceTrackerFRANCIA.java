import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class AttendanceSheet {

    public static void main(String[] args) {
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new AttendanceSheet().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Attendance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 450);
        frame.setLayout(new BorderLayout());

        // COLORS
        Color bgColor = new Color(245, 245, 245);
        Color headerColor = new Color(40, 120, 200);

        // INPUT PANEL
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Attendance Input"));
        inputPanel.setBackground(bgColor);

        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField timeInField = new JTextField();
        JTextField timeOutField = new JTextField();
        JTextField signatureIdField = new JTextField();

        timeInField.setEditable(false);
        timeOutField.setEditable(false);
        signatureIdField.setEditable(false);

        inputPanel.add(new JLabel("Attendance Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course / Year:"));
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Time In:"));
        inputPanel.add(timeInField);

        inputPanel.add(new JLabel("Time Out:"));
        inputPanel.add(timeOutField);

        inputPanel.add(new JLabel("E-Signature ID:"));
        inputPanel.add(signatureIdField);

        JButton addBtn = new JButton("Add Attendance");
        addBtn.setBackground(headerColor);
        addBtn.setForeground(Color.WHITE);

        inputPanel.add(new JLabel(""));
        inputPanel.add(addBtn);

        // SIGNATURE PANEL
        SignaturePanel signaturePanel = new SignaturePanel();
        signaturePanel.setBorder(BorderFactory.createTitledBorder("Draw Your Signature"));

        // TABLE
        String[] columns = {"Name", "Course/Year", "Time In", "Time Out", "Signature"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Attendance List"));

        // TIME FORMAT
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        // AUTO TIME IN
        DocumentListener timeInListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { setTimeIn(); }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}

            private void setTimeIn() {
                if (!nameField.getText().isEmpty()
                        && !courseField.getText().isEmpty()
                        && timeInField.getText().isEmpty()) {

                    timeInField.setText(LocalDateTime.now().format(timeFormat));
                    signatureIdField.setText(generateLetterSignature());
                }
            }
        };

        nameField.getDocument().addDocumentListener(timeInListener);
        courseField.getDocument().addDocumentListener(timeInListener);

        // ADD BUTTON
        addBtn.addActionListener(e -> {

            if (nameField.getText().isEmpty()
                    || courseField.getText().isEmpty()
                    || timeInField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(frame,
                        "Complete all required fields!");
                return;
            }

            // Time Out must be DIFFERENT
            timeOutField.setText(LocalDateTime.now().format(timeFormat));

            // Check if signature exists
            String signatureStatus =
                    signaturePanel.hasSignature() ? "SIGNED" : "NO SIGNATURE";

            model.addRow(new Object[]{
                    nameField.getText(),
                    courseField.getText(),
                    timeInField.getText(),
                    timeOutField.getText(),
                    signatureStatus
            });

            // Clear after adding
            nameField.setText("");
            courseField.setText("");
            timeInField.setText("");
            timeOutField.setText("");
            signatureIdField.setText("");
            signaturePanel.clear();
        });

        // LAYOUT
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(signaturePanel, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // LETTER-ONLY SIGNATURE
    private static String generateLetterSignature() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("SIG-");
        for (int i = 0; i < 6; i++) {
            sb.append(letters.charAt(rand.nextInt(letters.length())));
        }
        return sb.toString();
    }
}

// SIGNATURE PANEL
class SignaturePanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    private int x, y;
    private boolean signed = false;

    public SignaturePanel() {
        setPreferredSize(new Dimension(350, 150));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (g2 != null) {
                    g2.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    signed = true;
                    repaint();
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null) {
            image = createImage(getWidth(), getHeight());
            if (image != null) {
                g2 = (Graphics2D) image.getGraphics();
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.BLACK);
            }
        }

        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    public boolean hasSignature() {
        return signed;
    }

    public void clear() {
        if (g2 != null) {
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(Color.BLACK);
            signed = false;
            repaint();
        }
    }
}
