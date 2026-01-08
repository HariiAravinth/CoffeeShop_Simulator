package simulation;

import models.Order;

import javax.swing.*;

public class KitchenStaff implements Runnable {
    private final String staffName;
    private final OrderQueue orderQueue;
    private final PreparedOrderQueue preparedOrderQueue;
    private final JTextArea activityArea;
    private final DefaultListModel<String> preparedQueueModel;

    public KitchenStaff(String staffName, OrderQueue orderQueue, PreparedOrderQueue preparedOrderQueue,
                        JTextArea activityArea, DefaultListModel<String> preparedQueueModel) {
        this.staffName = staffName;
        this.orderQueue = orderQueue;
        this.preparedOrderQueue = preparedOrderQueue;
        this.activityArea = activityArea;
        this.preparedQueueModel = preparedQueueModel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = orderQueue.takeOrder();
                SwingUtilities.invokeLater(() ->
                        activityArea.append("👨‍🍳 " + staffName + " preparing Order #" + order.getOrderID() + "\n"));
                Thread.sleep(5000); // simulate preparation
                preparedOrderQueue.addPreparedOrder(order);

                SwingUtilities.invokeLater(() -> {
                    activityArea.append("✔️ " + staffName + " prepared Order #" + order.getOrderID() + "\n");
                    preparedQueueModel.addElement("Prepared Order #" + order.getOrderID() + " | " + order.getCustomerID());
                });
            } catch (InterruptedException e) {
                SwingUtilities.invokeLater(() -> activityArea.append("⚠️ Interrupted: " + staffName + "\n"));
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
