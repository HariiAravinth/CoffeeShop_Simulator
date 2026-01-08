package test;

import models.StudentDiscountStrategy;
import models.SeniorDiscountStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountStrategyTest {

    @Test
    public void testStudentDiscount() {
        StudentDiscountStrategy strategy = new StudentDiscountStrategy();
        float originalPrice = 10f;
        float expected = 8.5f; // 15% off
        assertEquals(expected, strategy.apply(originalPrice), 0.01);
    }

    @Test
    public void testSeniorDiscount() {
        SeniorDiscountStrategy strategy = new SeniorDiscountStrategy();
        float originalPrice = 10f;
        float expected = 8.0f; // 20% off
        assertEquals(expected, strategy.apply(originalPrice), 0.01);
    }
}
