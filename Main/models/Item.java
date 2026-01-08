package models;

public class Item {
    private String id;
    private String name;
    private String category;
    private float price;
    private boolean available;
    private String description;

    // Updated constructor with all fields including description
    public Item(String id, String name, String category, float price, boolean available, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.description = description;
    }

    // Constructor without description defaults to empty description
    public Item(String id, String name, String category, float price, boolean available) {
        this(id, name, category, price, available, "No description provided.");
    }

    // Backward compatible constructor
    public Item(String id, String name, String category, float price) {
        this(id, name, category, price, true, "No description provided.");
    }

    // Getters
    public String getItemId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // For UI display or debugging
    @Override
    public String toString() {
        return name + " (" + category + ") - $" + price + (available ? " [Available]" : " [Unavailable]");
    }
}
