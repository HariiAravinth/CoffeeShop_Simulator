package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {
    private int customerId;
    private List<Order> orderList = new ArrayList<>();
    private static int counter = 1;

    public Customer() {
        this.customerId = counter++;
    }

    public Customer(int id) {
        this.customerId = id;
    }

    public int getCustomerID() {
        return customerId;
    }

    public void placeOrder(Order order) {
        orderList.add(order);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public Order placeRandomOrder(Menu menu) {
        List<Item> availableItems = menu.getAvailableItems();
        Random rand = new Random();
        List<Item> items = new ArrayList<>();

        int numItems = 1 + rand.nextInt(3); // 1–3 items
        for (int i = 0; i < numItems; i++) {
            Item randomItem = availableItems.get(rand.nextInt(availableItems.size()));
            items.add(randomItem);
        }

        String itemList = items.stream()
                .map(Item::getItemId)
                .reduce((a, b) -> a + "," + b)
                .orElse("EMPTY");

        Order order = new Order(customerId, items.size(), System.currentTimeMillis(), itemList);
        items.forEach(order::addItem);
        return order;
    }
}
