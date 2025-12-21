package com.order.process.system.demo.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Order System");

        // Use a layout manager, e.g., BorderLayout or BoxLayout
        setLayout(new BorderLayout());

        // Example: Center button
        JButton getOrderButton = new JButton("Get Order");
        add(getOrderButton, BorderLayout.CENTER);

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set preferred size
        setSize(600, 400);

        // Make the frame visible
        setLocationRelativeTo(null); // centers on screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}


