package gui;

import models.*;
import simulation.*;
import utils.FileInputOutput;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

public class CoffeeShopGUI {
    private final CoffeeShop coffeeShop;
    private final OrderQueue orderQueue = new OrderQueue();
    private final PreparedOrderQueue preparedOrderQueue = new PreparedOrderQueue();

    private JFrame frame;
    private DefaultListModel<String> menuModel, orderQueueModel, preparedQueueModel;
    private JList<String> menuList, orderQueueList, preparedQueueList;
    private JTextArea orderSummary;
    private float finalAmount = 0;
    private String currentCustomerID;
    private List<Item> currentOrderItems = new ArrayList<>();

    private final Map<String, JTextArea> staffActivityAreas = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(CoffeeShopGUI.class.getName());

    private AtomicBoolean isCustomerGenRunning = new AtomicBoolean(true);
    private Thread customerThread;

    public CoffeeShopGUI(CoffeeShop shop) {
        this.coffeeShop = shop;
        setupLogger();
        setTheme();
        initializeGUI();
        loadOrderQueueFromFile();
        startSimulation(); // ✅ No longer missing
    }

    // Logger setup
    private void setupLogger() {
        try {
            FileHandler fh = new FileHandler("logs/coffee_shop_log.txt", true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("Logger setup failed.");
        }
    }

    // Color theme
    private void setTheme() {
        UIManager.put("Panel.background", new Color(242, 226, 211));
        UIManager.put("Button.background", new Color(205, 133, 63));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Label.foreground", new Color(101, 67, 33));
        UIManager.put("TextArea.background", new Color(255, 248, 220));
        UIManager.put("List.background", new Color(255, 239, 213));
        UIManager.put("ScrollPane.background", new Color(255, 248, 220));
    }

    // GUI initialization
    private void initializeGUI() {
        frame = new JFrame("☕ Coffee Shop Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 850);
        frame.setLayout(new BorderLayout());

        menuModel = new DefaultListModel<>();
        orderQueueModel = new DefaultListModel<>();
        preparedQueueModel = new DefaultListModel<>();

        menuList = new JList<>(menuModel);
        orderQueueList = new JList<>(orderQueueModel);
        preparedQueueList = new JList<>(preparedQueueModel);

        orderSummary = new JTextArea(10, 30);
        orderSummary.setEditable(false);

        JButton addToOrder = new JButton("➕ Add Item");
        JButton removeFromOrder = new JButton("➖ Remove Item");
        JButton viewDescription = new JButton("🔍 View Description");
        JButton checkout = new JButton("✔ Checkout");
        JButton toggleStatusButton = new JButton("🔄 Toggle Status");
        JToggleButton toggleSimulation = new JToggleButton("⏸ Pause Simulation");
        JButton exit = new JButton("📋 Show Report & Exit");

        addToOrder.addActionListener(e -> addSelectedItemToOrder());
        removeFromOrder.addActionListener(e -> removeLastItemFromOrder());
        viewDescription.addActionListener(e -> showSelectedItemDescription());
        checkout.addActionListener(e -> handleCheckoutProcess());
        toggleStatusButton.addActionListener(e -> toggleSelectedOrderStatus());
        toggleSimulation.addActionListener(e -> {
            boolean paused = toggleSimulation.isSelected();
            isCustomerGenRunning.set(!paused);
            toggleSimulation.setText(paused ? "▶ Resume Simulation" : "⏸ Pause Simulation");
        });
        exit.addActionListener(e -> {
            displayReportPopup();
            saveOrderQueueToFile();
            coffeeShop.generateReport();
            System.exit(0);
        });

        JPanel west = new JPanel(new BorderLayout());
        JPanel buttonBox = new JPanel(new GridLayout(3, 1));
        buttonBox.add(addToOrder);
        buttonBox.add(removeFromOrder);
        buttonBox.add(viewDescription);
        west.add(new JLabel("Menu"), BorderLayout.NORTH);
        west.add(new JScrollPane(menuList), BorderLayout.CENTER);
        west.add(buttonBox, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("Order Summary"), BorderLayout.NORTH);
        center.add(new JScrollPane(orderSummary), BorderLayout.CENTER);

        JPanel east = new JPanel(new GridLayout(1, 2));
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(new JLabel("Order Queue (To Kitchen)"), BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(orderQueueList), BorderLayout.CENTER);
        JPanel preparedPanel = new JPanel(new BorderLayout());
        preparedPanel.add(new JLabel("Prepared Queue (To Server)"), BorderLayout.NORTH);
        preparedPanel.add(new JScrollPane(preparedQueueList), BorderLayout.CENTER);
        east.add(orderPanel);
        east.add(preparedPanel);

        JPanel bottom = new JPanel();
        bottom.add(checkout);
        bottom.add(toggleStatusButton);
        bottom.add(toggleSimulation);
        bottom.add(exit);

        JPanel staffPanel = new JPanel(new GridLayout(0, 1));
        for (String name : List.of("Server-1", "Server-2", "Server-3", "Kitchen-1", "Kitchen-2")) {
            JTextArea area = new JTextArea(5, 25);
            area.setEditable(false);
            staffActivityAreas.put(name, area);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(name));
            panel.add(new JScrollPane(area), BorderLayout.CENTER);
            staffPanel.add(panel);
        }

        JPanel south = new JPanel(new BorderLayout());
        south.add(bottom, BorderLayout.NORTH);
        south.add(new JScrollPane(staffPanel), BorderLayout.CENTER);

        frame.add(west, BorderLayout.WEST);
        frame.add(center, BorderLayout.CENTER);
        frame.add(east, BorderLayout.EAST);
        frame.add(south, BorderLayout.SOUTH);

        loadMenuItems();
        frame.setVisible(true);
    }

    // ✅ SIMULATION LOGIC
    private void startSimulation() {
        customerThread = new Thread(() -> {
            int customerId = 1;
            while (true) {
                try {
                    if (isCustomerGenRunning.get()) {
                        Customer customer = new Customer(customerId++);
                        Order order = customer.placeRandomOrder(coffeeShop.getMenu());
                        coffeeShop.addOrder(order);
                        orderQueue.addOrder(order);

                        JTextArea area = staffActivityAreas.get("Server-1");
                        if (area != null) {
                            SwingUtilities.invokeLater(() ->
                                    area.append("🧹 Customer #" + customer.getCustomerID() + " placed Order #" + order.getOrderID() + "\n"));
                        }

                        SwingUtilities.invokeLater(() ->
                                orderQueueModel.addElement("Order #" + order.getOrderID() + " | " + order.getCustomerID()));
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        customerThread.start();

        for (int i = 1; i <= 2; i++) {
            String name = "Kitchen-" + i;
            JTextArea log = staffActivityAreas.get(name);
            if (log != null) {
                new Thread(new KitchenStaff(name, orderQueue, preparedOrderQueue, log, preparedQueueModel)).start();
            }
        }

        for (int i = 1; i <= 3; i++) {
            String name = "Server-" + i;
            JTextArea log = staffActivityAreas.get(name);
            if (log != null) {
                new Thread(new ServingStaff(name, preparedOrderQueue, log)).start();
            }
        }
    }

    private void handleCheckoutProcess() {
        if (currentOrderItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No items selected.");
            return;
        }

        currentCustomerID = "CUST" + new Random().nextInt(10000);
        float subtotal = 0;
        for (Item item : currentOrderItems) subtotal += item.getPrice();

        DiscountStrategy strategy = null;
        String[] options = {"Student (15%)", "Senior (20%)", "None"};
        String selected = (String) JOptionPane.showInputDialog(frame, "Select Discount", "Discount",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if ("Student (15%)".equals(selected)) strategy = new StudentDiscountStrategy();
        else if ("Senior (20%)".equals(selected)) strategy = new SeniorDiscountStrategy();

        float comboDiscount = Discount.calculateDiscount(subtotal, getItemCountMap());
        float afterCombo = subtotal - comboDiscount;
        float finalTotal = (strategy != null) ? strategy.apply(afterCombo) : afterCombo;

        String[] payOptions = {"Cash", "Card", "UPI"};
        String payment = (String) JOptionPane.showInputDialog(frame, "Choose Payment", "Payment Method",
                JOptionPane.PLAIN_MESSAGE, null, payOptions, payOptions[0]);

        Order order = new Order(coffeeShop.getOrders().size() + 1, currentOrderItems.size(),
                System.currentTimeMillis(), currentCustomerID);
        for (Item item : currentOrderItems) order.addItem(item);

        coffeeShop.addOrder(order);
        try {
            orderQueue.addOrder(order);
        } catch (InterruptedException e) {
            LOGGER.warning("Manual order interrupted: " + e.getMessage());
        }

        orderQueueModel.addElement("Order #" + order.getOrderID() + " | " + order.getCustomerID());

        JOptionPane.showMessageDialog(frame,
                "Customer ID: " + currentCustomerID +
                        "\nSubtotal: $" + subtotal +
                        "\nCombo Discount: $" + comboDiscount +
                        "\nExtra Discount: $" + (afterCombo - finalTotal) +
                        "\nFinal Total: $" + finalTotal +
                        "\nPayment Method: " + payment);

        orderSummary.setText("");
        currentOrderItems.clear();
    }

    private Map<String, Integer> getItemCountMap() {
        Map<String, Integer> map = new HashMap<>();
        for (Item item : currentOrderItems) {
            map.put(item.getName(), map.getOrDefault(item.getName(), 0) + 1);
        }
        return map;
    }

    private void addSelectedItemToOrder() {
        String selected = menuList.getSelectedValue();
        if (selected != null) {
            orderSummary.append(selected + "\n");
            String[] parts = selected.split("-");
            currentOrderItems.add(new Item(UUID.randomUUID().toString(), parts[1].trim(), "Misc",
                    Float.parseFloat(parts[2].replace("$", "").trim()), true, ""));
        }
    }

    private void removeLastItemFromOrder() {
        if (!currentOrderItems.isEmpty()) {
            currentOrderItems.remove(currentOrderItems.size() - 1);
            String[] lines = orderSummary.getText().split("\\n");
            StringBuilder updated = new StringBuilder();
            for (int i = 0; i < lines.length - 1; i++) {
                updated.append(lines[i]).append("\n");
            }
            orderSummary.setText(updated.toString());
        }
    }

    private void showSelectedItemDescription() {
        String selected = menuList.getSelectedValue();
        if (selected != null) {
            String itemId = selected.split("-")[0].trim();
            Item item = coffeeShop.getMenu().getItemByID(itemId);
            if (item != null) {
                JOptionPane.showMessageDialog(frame,
                        "Name: " + item.getName() +
                                "\nCategory: " + item.getCategory() +
                                "\nPrice: $" + item.getPrice() +
                                "\nDescription: " + item.getDescription(),
                        "Item Description", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void toggleSelectedOrderStatus() {
        JOptionPane.showMessageDialog(frame, "⚠️ Order status is now automatically updated by processing staff.");
    }

    private void loadMenuItems() {
        menuModel.clear();
        for (Item item : coffeeShop.getMenu().getItems()) {
            menuModel.addElement(item.getItemId() + " - " + item.getName() + " - $" + item.getPrice());
        }
    }

    private void loadOrderQueueFromFile() {
        File file = new File("data/gui_orders.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orderQueueModel.addElement(line.trim());
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to load queue.");
        }
    }

    private void saveOrderQueueToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/gui_orders.txt"))) {
            for (int i = 0; i < orderQueueModel.size(); i++) {
                writer.println(orderQueueModel.get(i));
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to save queue.");
        }
    }

    private void displayReportPopup() {
        StringBuilder report = new StringBuilder();
        report.append("Coffee Shop Report Summary:\n");
        report.append("Total Orders: ").append(coffeeShop.getOrders().size()).append("\n\n");

        for (Order order : coffeeShop.getOrders()) {
            report.append("Order #").append(order.getOrderID())
                    .append(" | Customer: ").append(order.getCustomerID())
                    .append(" | Items: ").append(order.getQuantity()).append("\n");
        }

        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        JOptionPane.showMessageDialog(frame, scrollPane, "📋 Final Report", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        CoffeeShop shop = new CoffeeShop("Main Cafe");
        try {
            FileInputOutput.loadMenu("data/menu.txt", shop.getMenu());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Menu file missing!");
        }
        new CoffeeShopGUI(shop);
    }
}
