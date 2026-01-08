package models;

public class Bill {
    private String customerID;
    private int orderID;
    private float discountApplied;
    private float subTotal;
    private float tax;
    private float totalCost;

    public Bill(String customerID, int orderID, float discountApplied, float subTotal, float tax) {
        this.customerID = customerID;
        this.orderID = orderID;
        this.discountApplied = discountApplied;
        this.subTotal = subTotal;
        this.tax = tax;
        this.totalCost = (subTotal - discountApplied) + tax;
    }

    public void printBill() {
        System.out.println("Bill Summary:");
        System.out.println("Customer ID: " + customerID);
        System.out.println("Order ID: " + orderID);
        System.out.println("Subtotal: $" + subTotal);
        System.out.println("Discount Applied: $" + discountApplied);
        System.out.println("Tax: $" + tax);
        System.out.println("Total Cost: $" + totalCost);
    }

    public float getTotalCost() {
        return totalCost;
    }
}