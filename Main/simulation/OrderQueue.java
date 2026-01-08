package simulation;

import models.Order;
import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {
    private final Queue<Order> orderQueue = new LinkedList<>();
    private final int MAX_CAPACITY = 20;

    public synchronized void addOrder(Order order) throws InterruptedException {
        while (orderQueue.size() >= MAX_CAPACITY) {
            wait();
        }
        orderQueue.offer(order);
        System.out.println("🛒 Order #" + order.getOrderID() + " added to OrderQueue.");
        notifyAll();
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (orderQueue.isEmpty()) {
            wait();
        }
        Order order = orderQueue.poll();
        System.out.println("📤 Order #" + order.getOrderID() + " taken from OrderQueue.");
        notifyAll();
        return order;
    }

    public synchronized int getQueueSize() {
        return orderQueue.size();
    }

    public synchronized String[] getOrderList() {
        return orderQueue.stream()
                .map(order -> "Order #" + order.getOrderID() + " | Customer: " + order.getCustomerID())
                .toArray(String[]::new);
    }
}
