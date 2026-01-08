package models;

import java.util.List;
import java.util.Map;

public class Discount {
    private String discountID;
    private String type;
    private int percent;
    private List<String> itemsApplied;

    // Main constructor
    public Discount(String discountID, String type, int percent, List<String> itemsApplied) {
        this.discountID = discountID;
        this.type = type;
        this.percent = percent;
        this.itemsApplied = itemsApplied;
    }

    // Overloaded constructor for compatibility with test cases using 'double'
    public Discount(String discountID, String type, double percent) {
        this.discountID = discountID;
        this.type = type;
        this.percent = (int) percent;
        this.itemsApplied = null;
    }

    // Getter methods for testing
    public String getType() {
        return this.type;
    }

    public int getValue() {
        return this.percent;
    }

    public String getDiscountID() {
        return discountID;
    }

    public List<String> getItemsApplied() {
        return itemsApplied;
    }

    // Applies the percentage discount
    public float applyDiscount(float subtotal) {
        return (subtotal * percent) / 100;
    }

    // Static method to calculate conditional discounts
    public static float calculateDiscount(float totalAmount, Map<String, Integer> itemCount) {
        float discount = 0;

        if (containsBeverage(itemCount)) {
            int foodItems = getFoodItemCount(itemCount);
            if (foodItems >= 2) {
                discount += totalAmount * 0.20f;
            }
        }

        if (totalAmount > 20) {
            discount += totalAmount * 0.10f;
        }

        if (itemCount.getOrDefault("Chocolate Chip Cookie", 0) >= 3) {
            discount += 2.5f;
        }

        if (totalAmount > 50) {
            discount += 5.5f;
        }

        return discount;
    }

    // Checks if the cart contains a beverage
    private static boolean containsBeverage(Map<String, Integer> itemCount) {
        return itemCount.containsKey("Latte") || itemCount.containsKey("Mocha") ||
                itemCount.containsKey("Coffee") || itemCount.containsKey("Espresso");
    }

    // Counts total food items from selected categories
    private static int getFoodItemCount(Map<String, Integer> itemCount) {
        int foodItems = 0;
        for (String item : itemCount.keySet()) {
            if (item.equals("Brownie") || item.equals("Croissant") ||
                    item.equals("Cookie") || item.equals("Muffin")) {
                foodItems += itemCount.get(item);
            }
        }
        return foodItems;
    }
}
