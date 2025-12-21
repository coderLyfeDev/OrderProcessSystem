package com.order.process.system.demo.ui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementUI extends JFrame {
    private JTextField customerIdField;
    private JPanel itemsPanel;
    private List<ItemRow> itemRows;
    private JTextField orderIdField;
    private JTextArea responseArea;
    private HttpClient httpClient;
    private static final String BASE_URL = "http://localhost:8080/order";

    public OrderManagementUI() {
        httpClient = HttpClient.newHttpClient();
        itemRows = new ArrayList<>();
        setupUI();
    }

    private void setupUI() {
        setTitle("Order Management System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Create Order
        JPanel createOrderPanel = createOrderPanel();
        tabbedPane.addTab("Create Order", createOrderPanel);

        // Tab 2: Get Order
        JPanel getOrderPanel = getOrderPanel();
        tabbedPane.addTab("Get Order", getOrderPanel);

        // Tab 3: Update Status
        JPanel updateStatusPanel = updateStatusPanel();
        tabbedPane.addTab("Update Status", updateStatusPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Response area at bottom
        responseArea = new JTextArea(8, 50);
        responseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Response"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel createOrderPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for customer ID
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Customer ID:"));
        customerIdField = new JTextField(10);
        customerIdField.setDocument(new NumberOnlyDocument());
        topPanel.add(customerIdField);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for items
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Items"));

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel);
        itemsScrollPane.setPreferredSize(new Dimension(600, 200));
        centerPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // Add item button
        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> addItemRow());
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButtonPanel.add(addItemButton);
        centerPanel.add(addButtonPanel, BorderLayout.SOUTH);

        // Add initial item row
        addItemRow();

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for create button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createButton = new JButton("Create Order");
        createButton.addActionListener(e -> createOrder());
        bottomPanel.add(createButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addItemRow() {
        ItemRow itemRow = new ItemRow();
        itemRows.add(itemRow);
        itemsPanel.add(itemRow.getPanel());
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private JPanel getOrderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        orderIdField = new JTextField(20);
        orderIdField.setDocument(new NumberOnlyDocument());
        panel.add(orderIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton getButton = new JButton("Get Order");
        getButton.addActionListener(e -> getOrder());
        panel.add(getButton, gbc);

        return panel;
    }

    private JPanel updateStatusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        JTextField updateOrderIdField = new JTextField(20);
        updateOrderIdField.setDocument(new NumberOnlyDocument());
        panel.add(updateOrderIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update Status");
        updateButton.addActionListener(e -> updateStatus(updateOrderIdField.getText()));
        panel.add(updateButton, gbc);

        return panel;
    }

    private void createOrder() {
        try {
            String customerId = customerIdField.getText();

            if (customerId.isEmpty()) {
                responseArea.setText("Error: Customer ID is required");
                return;
            }

            // Build items JSON
            StringBuilder itemsJson = new StringBuilder("[");
            boolean first = true;
            for (ItemRow row : itemRows) {
                String itemId = row.getItemId();
                String qty = row.getQuantity();

                if (!itemId.isEmpty() && !qty.isEmpty()) {
                    if (!first) itemsJson.append(",");
                    itemsJson.append(String.format("{\"id\": %s, \"qty\": %s}", itemId, qty));
                    first = false;
                }
            }
            itemsJson.append("]");

            String json = String.format(
                    "{\"customerId\": %s, \"items\": %s}",
                    customerId, itemsJson.toString()
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/createOrder"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            responseArea.setText("Status: " + response.statusCode() + "\n\n" +
                    formatJson(response.body()));

        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    private void getOrder() {
        try {
            String orderId = orderIdField.getText();

            if (orderId.isEmpty()) {
                responseArea.setText("Error: Order ID is required");
                return;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/get/OrderStatus/" + orderId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            responseArea.setText("Status: " + response.statusCode() + "\n\n" +
                    formatJson(response.body()));

        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateStatus(String orderId) {
        try {
            if (orderId.isEmpty()) {
                responseArea.setText("Error: Order ID is required");
                return;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/updateStatus/" + orderId))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            responseArea.setText("Status: " + response.statusCode() + "\n\n" +
                    formatJson(response.body()));

        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    private String formatJson(String json) {
        return json.replace(",", ",\n  ")
                .replace("{", "{\n  ")
                .replace("}", "\n}");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderManagementUI ui = new OrderManagementUI();
            ui.setVisible(true);
        });
    }

    // Inner class for each item row
    private class ItemRow {
        private JComboBox<String> itemCombo;
        private JTextField qtyField;
        private JPanel panel;

        public ItemRow() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            panel.add(new JLabel("Item ID:"));
            itemCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});
            itemCombo.setPreferredSize(new Dimension(100, 25));
            panel.add(itemCombo);

            panel.add(new JLabel("Quantity:"));
            qtyField = new JTextField(10);
            qtyField.setDocument(new NumberOnlyDocument());
            panel.add(qtyField);

            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(e -> {
                itemRows.remove(this);
                itemsPanel.remove(panel);
                itemsPanel.revalidate();
                itemsPanel.repaint();
            });
            panel.add(removeButton);
        }

        public JPanel getPanel() {
            return panel;
        }

        public String getItemId() {
            return (String) itemCombo.getSelectedItem();
        }

        public String getQuantity() {
            return qtyField.getText();
        }
    }

    // Document filter to allow only positive numbers
    private static class NumberOnlyDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;

            // Only allow digits
            if (str.matches("\\d+")) {
                super.insertString(offset, str, attr);
            }
        }
    }
}