import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToxicChatApp {

    private JPanel chatPanel;
    private JTextField inputField;
    private JScrollPane scrollPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToxicChatApp().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Toxic Chat App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel name = new JLabel("<html><b>Anna Griffin</b><br><small>Piano Teacher</small></html>");
        JLabel onlineDot = new JLabel("●");
        onlineDot.setForeground(Color.GREEN);
        onlineDot.setFont(new Font("Arial", Font.PLAIN, 18));
        header.add(name, BorderLayout.CENTER);
        header.add(onlineDot, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        // Chat panel
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input field + Send button
        JPanel inputPanel = new RoundedPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setBorder(null);
        inputPanel.add(inputField, BorderLayout.CENTER);

        JButton sendButton = new JButton("➤");
        sendButton.setForeground(Color.BLUE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 18));
        sendButton.setContentAreaFilled(false);
        sendButton.setBorderPainted(false);
        sendButton.addActionListener(e -> processMessage());

        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void processMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) return;

        String result = checkToxicity(message);

        if ("Toxic".equalsIgnoreCase(result)) {
            showBotMessage("⚠️ Toxic message detected!");
        } else if ("Not Toxic".equalsIgnoreCase(result)) {
            showUserMessage(message);
            // simulate a bot reply
            showBotMessage("Got it!");
        } else {
            showBotMessage("❌ Error connecting to server.");
        }

        inputField.setText("");
        chatPanel.revalidate();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    private void showUserMessage(String message) {
        JLabel msg = createBubble(message, new Color(0, 123, 255), Color.WHITE, FlowLayout.RIGHT);
        chatPanel.add(msg);
    }

    private void showBotMessage(String message) {
        JLabel msg = createBubble(message, new Color(230, 230, 230), Color.BLACK, FlowLayout.LEFT);
        chatPanel.add(msg);
    }

    private JLabel createBubble(String text, Color bgColor, Color fgColor, int alignment) {
        JLabel label = new JLabel("<html><p style='width: 200px;'>" + text + "</p></html>");
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel wrapper = new JPanel(new FlowLayout(alignment));
        wrapper.setOpaque(false);
        wrapper.add(label);

        chatPanel.add(wrapper);
        return label;
    }

    private String checkToxicity(String message) {
        try {
            URL url = new URL("http://localhost:5000/predict");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            String jsonInput = "{\"message\": \"" + message.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            String json = response.toString();
            if (json.contains("\"result\":\"Not Toxic\"")) return "Not Toxic";
            else if (json.contains("\"result\":\"Toxic\"")) return "Toxic";
            else return "Unknown";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    // Custom RoundedPanel for input box
    static class RoundedPanel extends JPanel {
        private static final int RADIUS = 30;

        public RoundedPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            super.paintComponent(g);
        }
    }
}
