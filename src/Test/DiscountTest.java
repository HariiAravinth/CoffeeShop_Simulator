
package Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import models.Discount;

public class DiscountTest {

    @Test
    public void testValidComboDiscount() {
        Discount discount = new Discount("C001", "Combo", 10.0);
        assertEquals("Combo", discount.getType());
        assertEquals(10.0, discount.getValue(), 0.01);
    }

    @Test
    public void testInvalidDiscountCode() {
        try {
            new Discount("INVALID", "Unknown", 5.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
