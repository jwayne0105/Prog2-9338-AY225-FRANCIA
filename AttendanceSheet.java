import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AttendanceSheet {

    public static void main(String[] args) {

        // Create main JFrame
        JFrame frame = new JFrame("Attendance Sheet");
        frame.setSize(950, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel with GridLayout: 6 rows, 7 columns
        JPanel panel = new JPanel(new GridLayout(6, 7, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add headers
        panel.add(new JLabel("Name (Time In)"));
        panel.add(new JLabel("Course / Year"));
        panel.add(new JLabel("Time In"));
        panel.add(new JLabel("Name (Time Out)"));
        panel.add(new JLabel("Time Out"));
        panel.add(new JLabel("Signature"));
        panel.add(new JLabel("Clear"));

        // Formatter for time display
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

        // Create 5 rows for students
        for (int i = 0; i < 5; i++) {

            JTextField nameInField = new JTextField();
            JTextField courseField = new JTextField();
            JTextField timeInField = new JTextField();
            JTextField nameOutField = new JTextField();
            JTextField timeOutField = new JTextField();

            SignaturePanel signaturePanel = new SignaturePanel();
            JButton clearBtn = new JButton("Clear");

            timeInField.setEditable(false);
            timeOutField.setEditable(false);

            // Auto Time In
            DocumentListener timeInListener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { setTimeIn(); }
                public void removeUpdate(DocumentEvent e) {}
                public void changedUpdate(DocumentEvent e) {}

                private void setTimeIn() {
                    if (!nameInField.getText().isEmpty()
                            && !courseField.getText().isEmpty()
                            && timeInField.getText().isEmpty()) {
                        timeInField.setText(LocalTime.now().format(timeFormat));
                    }
                }
            };

            // Auto Time Out
            DocumentListener timeOutListener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { setTimeOut(); }
                public void removeUpdate(DocumentEvent e) {}
                public void changedUpdate(DocumentEvent e) {}

                private void setTimeOut() {
                    if (!nameOutField.getText().isEmpty()
                            && timeOutField.getText().isEmpty()) {
                        timeOutField.setText(LocalTime.now().format(timeFormat));
                    }
                }
            };

            clearBtn.addActionListener(e -> signaturePanel.clear());

            nameInField.getDocument().addDocumentListener(timeInListener);
            courseField.getDocument().addDocumentListener(timeInListener);
            nameOutField.getDocument().addDocumentListener(timeOutListener);

            panel.add(nameInField);
            panel.add(courseField);
            panel.add(timeInField);
            panel.add(nameOutField);
            panel.add(timeOutField);
            panel.add(signaturePanel);
            panel.add(clearBtn);
        }

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// ================= SIGNATURE PANEL =================

class SignaturePanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    private int x, y;

    public SignaturePanel() {
        setPreferredSize(new Dimension(120, 40));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (g2 != null) {
                    g2.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    repaint();
                }
            }
        });
    }

    @Override
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

    public void clear() {
        if (g2 != null) {
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(Color.BLACK);
            repaint();
        }
    }
}
