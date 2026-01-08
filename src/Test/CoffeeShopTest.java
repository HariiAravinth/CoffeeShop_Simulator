package Testing.models;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import models.*;

import java.util.HashMap;
import java.util.Map;

public class CoffeeShopTest {

    // CoffeeShop Tests
    @Test
    void testCoffeeShopAddOrder() {
        CoffeeShop coffeeShop = new CoffeeShop("Croissant Cafe");
        Order order = new Order(1, 1, System.currentTimeMillis(), "CUST123");
        coffeeShop.addOrder(order);
        assertEquals(1, coffeeShop.getOrders().size());
        assertEquals(order.calculateTotal(), coffeeShop.getRevenue());
    }

    @Test
    void testCoffeeShopGenerateReport() {
        CoffeeShop coffeeShop = new CoffeeShop("Croissant Cafe");
        coffeeShop.generateReport();

    }

    // Customer Tests
    @Test
    void testCustomerPlaceOrder() {
        Customer customer = new Customer(1);
        Order order = new Order(1, 1, System.currentTimeMillis(), "CUST123");
        customer.placeOrder(order);
        assertEquals(1, customer.getOrderList().size());
    }

    // Discount Tests
    @Test
    void testDiscountCalculateDiscount() {
        Map<String, Integer> itemCount = new HashMap<>();
        itemCount.put("Latte", 1);
        itemCount.put("Brownie", 2);
        itemCount.put("Chocolate Chip Cookie", 3);

        float discount = Discount.calculateDiscount(100.0f, itemCount);
        assertTrue(discount > 0);
    }

    @Test
    void testDiscountApplyDiscount() {
        Discount discount = new Discount("DISC1", "Percentage", 10, null);
        float discountedAmount = discount.applyDiscount(100.0f);
        assertEquals(10.0f, discountedAmount);
    }

    // Item Tests
    @Test
    void testItemCreation() {
        Item item = new Item("ITEM1", "Latte", "Beverage", 4.5f, true);
        assertEquals("ITEM1", item.getItemId());
        assertEquals("Latte", item.getName());
        assertEquals("Beverage", item.getCategory());
        assertEquals(4.5f, item.getPrice());
        assertTrue(item.isAvailable());
    }

    // Menu Tests
    @Test
    void testMenuAddItem() {
        Menu menu = new Menu();
        Item item = new Item("ITEM1", "Latte", "Beverage", 4.5f, true);
        menu.addItem(item);
        assertEquals(1, menu.getItems().size());
    }

    @Test
    void testMenuGetItemByID() {
        Menu menu = new Menu();
        Item item = new Item("ITEM1", "Latte", "Beverage", 4.5f, true);
        menu.addItem(item);
        assertEquals(item, menu.getItemByID("ITEM1"));
    }

    @Test
    void testMenuGetAvailableItems() {
        Menu menu = new Menu();
        Item item1 = new Item("ITEM1", "Latte", "Beverage", 4.5f, true);
        Item item2 = new Item("ITEM2", "Croissant", "Food", 3.0f, false);
        menu.addItem(item1);
        menu.addItem(item2);
        assertEquals(1, menu.getAvailableItems().size());
    }

    // Order Tests
    @Test
    void testOrderAddItem() {
        Order order = new Order(1, 1, System.currentTimeMillis(), "CUST123");
        Item item = new Item("ITEM1", "Latte", "Beverage", 4.5f, true);
        order.addItem(item);
        assertEquals(1, order.getItems().size());
        assertEquals(4.5f, order.calculateTotal());
    }


    @Test
    void testBillPrintBill() {
        Bill bill = new Bill("CUST123", 1, 5.0f, 50.0f, 2.5f);
        bill.printBill(); 
    }
}