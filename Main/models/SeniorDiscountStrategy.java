package models;

public class SeniorDiscountStrategy implements DiscountStrategy {
    @Override
    public float apply(float amount) {
        return amount * 0.80f; // 20% off
    }
}
