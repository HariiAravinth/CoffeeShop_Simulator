package utils;

import java.io.*;
import java.util.*;

import models.Item;
import models.Menu;

public class FileInputOutput {

    // Reads lines from a file and returns them as a list
    public static List<String> readFile(String filepath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // Loads menu items from a CSV file into the Menu object
    public static void loadMenu(String filepath, Menu menu) throws IOException {
        List<String> lines = readFile(filepath);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue; // Skip blank lines

            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String id = parts[0].trim();
                String name = parts[1].trim();
                String category = parts[2].trim();
                float price = Float.parseFloat(parts[3].trim());
                boolean available = Boolean.parseBoolean(parts[4].trim());

                Item item = new Item(id, name, category, price);
                item.setAvailable(available); // Ensure this method exists in Item
                menu.addItem(item);
            } else {
                System.err.println("Invalid menu line: " + line);
            }
        }
    }

    // Writes lines to a file
    public static void writeFile(String filepath, List<String> lines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
