package test;

import models.Item;
import models.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testAddItemAndTotal() {
        Order order = new Order(1, 0, System.currentTimeMillis(), "CUST123");
        Item coffee = new Item("1", "Coffee", "Drink", 3.5f, true);
        Item cake = new Item("2", "Cake", "Food", 5.0f, true);

        order.addItem(coffee);
        order.addItem(cake);

        assertEquals(2, order.getItems().size());
        assertEquals(8.5f, order.calculateTotal(), 0.01);
    }

    @Test
    public void testRemoveItem() {
        Order order = new Order(2, 0, System.currentTimeMillis(), "CUST999");
        Item tea = new Item("3", "Tea", "Drink", 2.0f, true);

        order.addItem(tea);
        order.removeItem(tea);

        assertEquals(0, order.getItems().size());
    }

    @Test
    public void testToggleStatus() {
        Order order = new Order(3, 0, System.currentTimeMillis(), "CUST999");
        assertEquals("Pending", order.getStatus());
        order.toggleStatus();
        assertEquals("Completed", order.getStatus());
    }
}
