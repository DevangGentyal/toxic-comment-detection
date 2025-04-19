import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.net.*;

public class TwoWayChatApp {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane scrollPane;
    public static int user_count = 0;
    
    // Network components
    private DatagramSocket socket;
    private final int localPort;
    private final int remotePort;
    private final String remoteHost;
    
    // Toxicity detector function
    private boolean isToxic(String message) {
        try {
            URL url = new URL("http://localhost:5000/predict");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            // JSON payload
            String jsonInput = "{\"message\": \"" + message.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            String json = response.toString().toLowerCase();

            if (json.contains("\"result\":\"toxic\"")) {
                return true;
            } else if (json.contains("\"result\":\"not toxic\"")) {
                return false;
            } else {
                System.err.println("⚠ Unexpected API response: " + json);
                return false; // fallback
            }       

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Fail-safe: assume not toxic on error
        }
    }
    
    public TwoWayChatApp(int localPort, String remoteHost, int remotePort) {
        user_count+=1;
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        
        setupUI();
        setupNetwork();
    }
    
    private void setupUI() {
        // Set up the main frame
        frame = new JFrame("Chat Application - User " + TwoWayChatApp.user_count);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
        
        // Chat panel to display messages
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        
        // Scroll pane for chat panel
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Input panel at bottom
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        
        // Message field with placeholder
        messageField = new JTextField();
        messageField.setText("Enter Message Here....");
        messageField.setForeground(Color.GRAY);
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        messageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageField.getText().equals("Enter Message Here....")) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (messageField.getText().isEmpty()) {
                    messageField.setText("Enter Message Here....");
                    messageField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Custom send button with image icon
        sendButton = new JButton();
        sendButton.setPreferredSize(new Dimension(40, 40));
        sendButton.setBackground(new Color(79, 148, 245));
        sendButton.setBorder(BorderFactory.createEmptyBorder());
        sendButton.setFocusPainted(false);
        sendButton.setContentAreaFilled(false);

        // Load and set image icon
        ImageIcon icon = new ImageIcon("send-icon.png"); // Path to your image
        Image scaledImage = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        sendButton.setIcon(new ImageIcon(scaledImage));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        
        // Add components to input panel
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        // Add panels to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        
        // Make frame visible
        frame.setVisible(true);
    }
    
    private void setupNetwork() {
        try {
            socket = new DatagramSocket(localPort);
            
            // Start listener thread
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "Error starting chat: " + e.getMessage(), 
                "Network Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void receiveMessages() {
        try {
            byte[] buffer = new byte[1024];
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String received = new String(packet.getData(), 0, packet.getLength());
                
                // Update UI with received message
                SwingUtilities.invokeLater(() -> addReceivedMessage(received));
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
            }
        }
    }
    
    private void sendMessage() {
        String message = messageField.getText();
        if (message.equals("Enter Message Here....") || message.trim().isEmpty()) {
            return;
        }
        
        if (isToxic(message)) {
            // Show toxic message warning without sending
            addToxicMessage( message);
        } else {
            // Send the message
            try {
                byte[] data = message.getBytes();
                InetAddress address = InetAddress.getByName(remoteHost);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, remotePort);
                socket.send(packet);
                
                // Show sent message in UI
                addSentMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, 
                    "Failed to send message: " + e.getMessage(), 
                    "Send Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Clear message field
        messageField.setText("");
    }
    
    private void addSentMessage(String message) {
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        containerPanel.setOpaque(false);
        
        // Create custom bubble panel
        BubblePanel bubblePanel = new BubblePanel(message, new Color(83, 152, 255), "Sent");
        containerPanel.add(bubblePanel);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // Reduced vertical spacing

        chatPanel.add(containerPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
        
        // Scroll to bottom
        scrollToBottom();
    }
    
    private void addReceivedMessage(String message) {
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        containerPanel.setOpaque(false);
        
        // Create custom bubble panel
        BubblePanel bubblePanel = new BubblePanel(message, new Color(122, 77, 255), "Received");
        containerPanel.add(bubblePanel);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // Reduced vertical spacing

        chatPanel.add(containerPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
        
        // Scroll to bottom
        scrollToBottom();
    }
    
    private void addToxicMessage(String message) {
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        containerPanel.setOpaque(false);
        
        // Create custom toxic message panel
        ToxicBubblePanel toxicPanel = new ToxicBubblePanel(message);
        containerPanel.add(toxicPanel);
        
        chatPanel.add(containerPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
        
        // Scroll to bottom
        scrollToBottom();
    }
    
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    // Custom bubble message panel for normal messages
    class BubblePanel extends JPanel {
    public BubblePanel(String message, Color backgroundColor, String sender) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(new Font("Arial", Font.BOLD, 11));
        senderLabel.setForeground(Color.GRAY);
        senderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(senderLabel);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Arial", Font.BOLD, 14));
        messageArea.setForeground(Color.WHITE);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setOpaque(false);
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel bubble = new JPanel(new BorderLayout());
        bubble.add(messageArea, BorderLayout.CENTER);
        bubble.setBackground(backgroundColor);
        bubble.setOpaque(true);
        bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bubble.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        bubble.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(2));
        add(bubble);
    }

    public BubblePanel(String message, Color backgroundColor) {
        this(message, backgroundColor, ""); // fallback constructor
    }
}

    // Custom toxic message panel
    private static class ToxicBubblePanel extends JPanel {
        private String message;
        private static final int ARC_SIZE = 20;
        private static final int PADDING = 10;
        
        public ToxicBubblePanel(String message) {
            this.message = message;
            
            setOpaque(false);
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(250, 100));
            setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw red bubble background
            g2.setColor(Color.RED);
            RoundRectangle2D bubble = new RoundRectangle2D.Float(
                0, 0, getWidth() - 1, getHeight() - 1, ARC_SIZE, ARC_SIZE);
            g2.fill(bubble);
            
            try {
                ImageIcon warningIcon = new ImageIcon("warning-icon.png");
                Image warningImage = warningIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                g2.drawImage(warningImage, getWidth() - 30, 10, null);
            } catch (Exception e) {
                e.printStackTrace(); // In case the image is missing
            }
            
            // Draw message text and warning
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(message, PADDING, 25);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Toxic Message detected", PADDING, 55);
            g2.drawString("and this can't be sent!!", PADDING, 75);
            
            g2.dispose();
        }
    }
    
    public void cleanup() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // For testing: create two chat instances on different ports
        SwingUtilities.invokeLater(() -> {

            // First chat window (with slight position offset)
            TwoWayChatApp app1 = new TwoWayChatApp(9001, "localhost", 9002);
            
            // Second chat window (with slight position offset)
            TwoWayChatApp app2 = new TwoWayChatApp(9002, "localhost", 9001);
            app2.frame.setLocation(420, 0);


        });
    }
}