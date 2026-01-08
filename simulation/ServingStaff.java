package simulation;

import models.Order;

import javax.swing.*;

public class ServingStaff implements Runnable {
    private final String staffName;
    private final PreparedOrderQueue preparedOrderQueue;
    private final JTextArea activityArea;

    public ServingStaff(String staffName, PreparedOrderQueue preparedOrderQueue, JTextArea activityArea) {
        this.staffName = staffName;
        this.preparedOrderQueue = preparedOrderQueue;
        this.activityArea = activityArea;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = preparedOrderQueue.takePreparedOrder();
                SwingUtilities.invokeLater(() ->
                        activityArea.append("🍽 " + staffName + " serving Order #" + order.getOrderID() + "\n"));
                Thread.sleep(4000); // simulate serving
                order.toggleStatus(); // Now mark as completed
                SwingUtilities.invokeLater(() ->
                        activityArea.append("✅ " + staffName + " completed Order #" + order.getOrderID() + "\n"));
            } catch (InterruptedException e) {
                SwingUtilities.invokeLater(() -> activityArea.append("⚠️ Interrupted: " + staffName + "\n"));
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
