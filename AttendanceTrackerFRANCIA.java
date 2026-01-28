import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class AttendanceTrackerFRANCIA {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceTrackerFRANCIA().start());
    }

    private JTextField nameField, courseField, timeInField, signatureIdField;
    private SignaturePanel signaturePanel;
    private DefaultTableModel model;

    private void start() {
        JFrame frame = new JFrame("Attendance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 420);
        frame.setLayout(new BorderLayout());

        Color bgColor = new Color(30, 30, 45);
        Color panelColor = new Color(45, 45, 70);
        Color accent = new Color(90, 160, 255);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accent), "Attendance Input"));
        inputPanel.setBackground(panelColor);

        nameField = new JTextField();
        courseField = new JTextField();
        timeInField = new JTextField();
        signatureIdField = new JTextField();

        timeInField.setEditable(false);
        signatureIdField.setEditable(false);

        styleField(nameField);
        styleField(courseField);
        styleField(timeInField);
        styleField(signatureIdField);

        inputPanel.add(label("Attendance Name"));
        inputPanel.add(nameField);
        inputPanel.add(label("Course / Year"));
        inputPanel.add(courseField);
        inputPanel.add(label("Time In"));
        inputPanel.add(timeInField);
        inputPanel.add(label("E-Signature ID"));
        inputPanel.add(signatureIdField);

        JButton addBtn = new JButton("ADD ATTENDANCE");
        addBtn.setBackground(accent);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createRaisedBevelBorder());

        inputPanel.add(new JLabel());
        inputPanel.add(addBtn);

        signaturePanel = new SignaturePanel();
        signaturePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accent), "Signature"));

        String[] columns = {"Name", "Course/Year", "Time In", "Signature"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(new Color(35, 35, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(accent);
        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accent), "Attendance List"));

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        DocumentListener timeInListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { setTimeIn(timeFormat); }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        };

        nameField.getDocument().addDocumentListener(timeInListener);
        courseField.getDocument().addDocumentListener(timeInListener);

        addBtn.addActionListener(e -> addAttendance(timeFormat));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(bgColor);
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(signaturePanel, BorderLayout.CENTER);

        frame.getContentPane().setBackground(bgColor);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    private void styleField(JTextField f) {
        f.setBackground(new Color(60, 60, 90));
        f.setForeground(Color.WHITE);
        f.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    private void setTimeIn(DateTimeFormatter tf) {
        if (!nameField.getText().isEmpty() &&
            !courseField.getText().isEmpty() &&
            timeInField.getText().isEmpty()) {

            timeInField.setText(LocalDateTime.now().format(tf));
            signatureIdField.setText(generateLetterSignature());
        }
    }

    private void addAttendance(DateTimeFormatter tf) {

        if (!Pattern.matches("^[A-Z]{2,5}[- ]?[0-9]$", courseField.getText().toUpperCase())) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Course / Year format\nExample: BSIT-1 , BSCS 2");
            return;
        }

        if (nameField.getText().isEmpty() || timeInField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete all required fields!");
            return;
        }

        String sig = signaturePanel.hasSignature() ? "SIGNED" : "NO SIGNATURE";

        model.addRow(new Object[]{
                nameField.getText(),
                courseField.getText().toUpperCase(),
                timeInField.getText(),
                sig
        });

        nameField.setText("");
        courseField.setText("");
        timeInField.setText("");
        signatureIdField.setText("");
        signaturePanel.clear();
    }

    private String generateLetterSignature() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
        StringBuilder sb = new StringBuilder("SIG-");
        for (int i = 0; i < 6; i++)
            sb.append(letters.charAt(r.nextInt(letters.length())));
        return sb.toString();
    }
}

@SuppressWarnings("serial")
class SignaturePanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    private int x, y;
    private boolean signed;

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
            g2 = (Graphics2D) image.getGraphics();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
        }
        g.drawImage(image, 0, 0, null);
    }

    public boolean hasSignature() { return signed; }

    public void clear() {
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(Color.BLACK);
        signed = false;
        repaint();
    }
}
