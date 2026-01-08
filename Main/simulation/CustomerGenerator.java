package simulation;

import models.Customer;
import models.Menu;
import models.Order;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerGenerator implements Runnable {
    private final OrderQueue orderQueue;
    private final Menu menu;
    private final JTextArea activityLog;
    private final AtomicBoolean isRunning;

    public CustomerGenerator(OrderQueue orderQueue, Menu menu, JTextArea activityLog, AtomicBoolean isRunning) {
        this.orderQueue = orderQueue;
        this.menu = menu;
        this.activityLog = activityLog;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        int customerId = 1;
        while (true) {
            try {
                if (isRunning.get()) {
                    Customer customer = new Customer(customerId++);
                    Order order = customer.placeRandomOrder(menu);
                    orderQueue.addOrder(order);
                    if (activityLog != null) {
                        SwingUtilities.invokeLater(() ->
                                activityLog.append("🧍 Customer #" + customer.getCustomerID() + " placed Order #" + order.getOrderID() + "\n")
                        );
                    }
                }
                Thread.sleep(3000); // simulate delay
            } catch (InterruptedException e) {
                if (activityLog != null) {
                    activityLog.append("⚠️ Customer Generator Interrupted\n");
                }
                break;
            }
        }
    }
}
