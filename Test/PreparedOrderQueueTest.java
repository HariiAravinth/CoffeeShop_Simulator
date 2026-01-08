package test;

import models.Order;
import simulation.PreparedOrderQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PreparedOrderQueueTest {

    @Test
    public void testAddAndTakePreparedOrder() throws InterruptedException {
        PreparedOrderQueue queue = new PreparedOrderQueue();
        Order order = new Order(10, 2, System.currentTimeMillis(), "CUST_X");

        queue.addPreparedOrder(order);
        Order taken = queue.takePreparedOrder();

        assertNotNull(taken);
        assertEquals(order.getCustomerID(), taken.getCustomerID());
    }
}
