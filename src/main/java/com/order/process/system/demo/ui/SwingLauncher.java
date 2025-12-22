/*package com.order.process.system.demo.ui;

import com.order.process.system.demo.ui.OrderManagementUI;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import javax.swing.SwingUtilities;

@Component
public class SwingLauncher {

    @EventListener(ApplicationReadyEvent.class)
    public void launchUI() {
        // Swing UI must run on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new OrderManagementUI().setVisible(true); // MainFrame is your Swing JFrame class
        });
    }
}*/

