package test;

import models.Order;
import simulation.OrderQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderQueueTest {

    @Test
    public void testAddAndTakeOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1, 1, System.currentTimeMillis(), "CUST001");

        queue.addOrder(order);
        Order taken = queue.takeOrder();

        assertNotNull(taken);
        assertEquals(order.getOrderID(), taken.getOrderID());
    }
}
