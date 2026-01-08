package models;

public class StudentDiscountStrategy implements DiscountStrategy {
    @Override
    public float apply(float amount) {
        return amount * 0.85f; // 15% off
    }
}
