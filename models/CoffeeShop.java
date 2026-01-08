package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeShop {
    private String name;
    private Menu menu;
    private List<Order> orders;
    private float revenue;

    public CoffeeShop(String name) {
        this.name = name;
        this.menu = new Menu();
        this.orders = new ArrayList<>();
        this.revenue = 0.0f;
    }

    public Menu getMenu() {
        return menu;
    }    
    public void addOrder(Order order) {
        orders.add(order);
        revenue += order.calculateTotal(); 
    }

    public List<Order> getOrders() {
        return orders;
    }

    public float getRevenue() {
        return revenue;
    }

    public void generateReport() {
    float totalRevenue = 0;
    Map<String, Integer> itemCount = new HashMap<>();
    List<String> ordersData = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader("data/gui_orders.txt"))) {
        String line;
        StringBuilder orderDetails = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            ordersData.add(line);

            if (line.startsWith("Total Payable:")) {
                totalRevenue += Float.parseFloat(line.replace("Total Payable: $", "").trim());
            } else if (!line.startsWith("Order Number") && !line.startsWith("-") && !line.isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    itemCount.put(itemName, itemCount.getOrDefault(itemName, 0) + 1);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading orders file: " + e.getMessage());
    }

    StringBuilder report = new StringBuilder();
    report.append("Coffee Shop Report:\n");
    report.append("Total Orders: ").append(ordersData.stream().filter(s -> s.startsWith("Order Number")).count()).append("\n");
    report.append("___________\n");

    for (String data : ordersData) {
        report.append(data).append("\n");
    }

    report.append("___________\n");
    report.append("Total Revenue: $").append(totalRevenue).append("\n");
    report.append("_____________\n");

    report.append("Order Item Count:\n");
    for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
        report.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
    }
    report.append("_________________\n");

    report.append("Menu Items Availability:\n");
    for (Item item : menu.getItems()) {
        report.append(item.getName()).append(" - Available\n");
    }

    System.out.println(report.toString());

    try (PrintWriter writer = new PrintWriter(new FileWriter("data/CoffeeShopReport.txt"))) {
        writer.println(report.toString());
    } catch (IOException e) {
        System.err.println("Error saving report: " + e.getMessage());
    }
}
}