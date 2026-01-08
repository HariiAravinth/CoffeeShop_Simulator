package simulation;

import models.Order;

import java.util.LinkedList;
import java.util.Queue;

public class PreparedOrderQueue {
    private final Queue<Order> preparedOrders = new LinkedList<>();
    private final int MAX_CAPACITY = 20;

    public synchronized void addPreparedOrder(Order order) throws InterruptedException {
        while (preparedOrders.size() >= MAX_CAPACITY) {
            wait();
        }
        preparedOrders.offer(order);
        System.out.println("🍳 Order #" + order.getOrderID() + " added to PreparedOrderQueue.");
        notifyAll();
    }

    public synchronized Order takePreparedOrder() throws InterruptedException {
        while (preparedOrders.isEmpty()) {
            wait();
        }
        Order order = preparedOrders.poll();
        System.out.println("🛎️ Order #" + order.getOrderID() + " taken from PreparedOrderQueue.");
        notifyAll();
        return order;
    }

    public synchronized int getQueueSize() {
        return preparedOrders.size();
    }
}
