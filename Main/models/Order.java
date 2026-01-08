package models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int quantity;
    private long timestamp;
    private String customerID;
    private List<Item> items;
    private String status; // "Pending" or "Completed"

    // New fields for billing info
    private float subtotal;
    private float comboDiscount;
    private float strategyDiscount;
    private float finalTotal;
    private String paymentMethod;

    public Order(int orderId, int quantity, long timestamp, String customerID) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.customerID = customerID;
        this.items = new ArrayList<>();
        this.status = "Pending"; // Default status
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item); // removes first matching occurrence
    }

    public int getOrderID() {
        return orderId;
    }

    public String getCustomerID() {
        return customerID;
    }

    public List<Item> getItems() {
        return items;
    }

    public float calculateTotal() {
        float total = 0;
        for (Item item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void toggleStatus() {
        if ("Pending".equals(status)) {
            status = "Completed";
        } else {
            status = "Pending";
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    // New setters and getters for billing info

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setComboDiscount(float comboDiscount) {
        this.comboDiscount = comboDiscount;
    }

    public float getComboDiscount() {
        return comboDiscount;
    }

    public void setStrategyDiscount(float strategyDiscount) {
        this.strategyDiscount = strategyDiscount;
    }

    public float getStrategyDiscount() {
        return strategyDiscount;
    }

    public void setFinalTotal(float finalTotal) {
        this.finalTotal = finalTotal;
    }

    public float getFinalTotal() {
        return finalTotal;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
