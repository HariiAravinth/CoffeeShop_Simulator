
package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ReportGenerator {
    public static void generateReport(Map<String, Integer> itemSales, double totalRevenue) {
        try (FileWriter writer = new FileWriter("data/CoffeeShopReport.txt")) {
            writer.write("Coffee Shop Summary Report\n");
            writer.write("---------------------------\n");
            for (Map.Entry<String, Integer> entry : itemSales.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + " orders\n");
            }
            writer.write("Total Revenue: £" + String.format("%.2f", totalRevenue) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
