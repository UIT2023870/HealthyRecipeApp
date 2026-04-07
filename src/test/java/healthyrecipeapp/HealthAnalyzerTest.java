package healthyrecipeapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import healthyrecipeapp.recipe_module.Recipe;
import healthyrecipeapp.health_module.HealthAnalyzer;

import java.util.Arrays;

public class HealthAnalyzerTest {

    HealthAnalyzer analyzer = new HealthAnalyzer();

    // ✅ Test 1: High protein + low calories + veg → Excellent
    @Test
    public void testExcellentRecipe() {
        Recipe r = new Recipe("Salad",
                Arrays.asList("lettuce", "tomato"),
                150, 25);

        int score = analyzer.calculateHealthScore(r);

        assertTrue(score >= 80);
        assertEquals("Excellent", analyzer.classifyRecipe(r));
    }

    // ✅ Test 2: Medium case → Good
    @Test
    public void testGoodRecipe() {
        Recipe r = new Recipe("Paneer",
                Arrays.asList("paneer", "spices"),
                300, 15);

        int score = analyzer.calculateHealthScore(r);

        assertTrue(score >= 50 && score < 80);
        assertEquals("Good", analyzer.classifyRecipe(r));
    }

    // ✅ Test 3: Unhealthy case
    @Test
    public void testUnhealthyRecipe() {
        Recipe r = new Recipe("Burger",
                Arrays.asList("meat", "bread"),
                600, 5);

        int score = analyzer.calculateHealthScore(r);

        assertTrue(score < 50);
        assertEquals("Unhealthy", analyzer.classifyRecipe(r));
    }

    // ✅ Test 4: isHealthy() method
    @Test
    public void testIsHealthy() {
        Recipe r = new Recipe("Soup",
                Arrays.asList("vegetables"),
                200, 12);

        assertTrue(analyzer.isHealthy(r));
    }

    // ✅ Test 5: Null input (Exception test)
    @Test
    public void testNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> {
            analyzer.calculateHealthScore(null);
        });
    }

    // ✅ Test 6: Score exactly 80 → Excellent (boundary)
    @Test
    public void testScoreExactly80IsExcellent() {
        // calories=150 → +30, protein=11 → +20, veg → +30 = 80
        Recipe r = new Recipe("BoundaryExcellent",
                Arrays.asList("tofu"),
                150, 11);
        assertEquals(80, analyzer.calculateHealthScore(r));
        assertEquals("Excellent", analyzer.classifyRecipe(r));
    }

    // ✅ Test 7: Score exactly 50 → Good (boundary)
    @Test
    public void testScoreExactly50IsGood() {
        // calories=300 → +20, protein=15 → +20, veg → +30 = 70... adjust:
        // calories=300 → +20, protein=5 → +0, veg → +30 = 50
        Recipe r = new Recipe("BoundaryGood",
                Arrays.asList("spinach"),
                300, 5);
        assertEquals(50, analyzer.calculateHealthScore(r));
        assertEquals("Good", analyzer.classifyRecipe(r));
    }

    // ✅ Test 8: isHealthy() returns false
    @Test
    public void testIsNotHealthy() {
        Recipe r = new Recipe("Junk",
                Arrays.asList("meat"),
                600, 5);
        assertFalse(analyzer.isHealthy(r));
    }

    // ✅ Test 9: Calories exactly 200 → gets +20 not +30 (boundary)
    @Test
    public void testCaloriesExactly200GetsLowerBonus() {
        // calories=200 is NOT < 200, so +20; protein=5 → +0; veg → +30 = 50
        Recipe r = new Recipe("CalBoundary",
                Arrays.asList("lentils"),
                200, 5);
        assertEquals(50, analyzer.calculateHealthScore(r));
    }

    // ✅ Test 10: Calories exactly 400 → gets 0 calorie points (boundary)
    @Test
    public void testCaloriesExactly400GetsNoCalorieBonus() {
        // calories=400 is NOT < 400, so +0; protein=5 → +0; veg → +30 = 30
        Recipe r = new Recipe("CalBoundary400",
                Arrays.asList("rice"),
                400, 5);
        assertEquals(30, analyzer.calculateHealthScore(r));
    }

    // ✅ Test 11: Protein exactly 10 → gets 0 protein points (boundary)
    @Test
    public void testProteinExactly10GetsNoProteinBonus() {
        // calories=150 → +30; protein=10 is NOT > 10, so +0; veg → +30 = 60
        Recipe r = new Recipe("ProteinBoundary10",
                Arrays.asList("cucumber"),
                150, 10);
        assertEquals(60, analyzer.calculateHealthScore(r));
    }

    // ✅ Test 12: Protein exactly 20 → gets +20 not +40 (boundary)
    @Test
    public void testProteinExactly20GetsLowerBonus() {
        // calories=150 → +30; protein=20 is NOT > 20, so +20; veg → +30 = 80
        Recipe r = new Recipe("ProteinBoundary20",
                Arrays.asList("chickpeas"),
                150, 20);
        assertEquals(80, analyzer.calculateHealthScore(r));
    }

    // ✅ Test 13: Non-veg recipe loses vegetarian bonus
    @Test
    public void testNonVegRecipeLosesVegBonus() {
        // Same as an excellent recipe but with chicken → loses +30
        // calories=150 → +30; protein=25 → +40; non-veg → +0 = 70
        Recipe r = new Recipe("Chicken Salad",
                Arrays.asList("chicken", "lettuce"),
                150, 25);
        assertEquals(70, analyzer.calculateHealthScore(r));
        assertEquals("Good", analyzer.classifyRecipe(r));
    }

    // ✅ Test 14: Zero score recipe
    @Test
    public void testZeroScore() {
        // calories=500 → +0; protein=5 → +0; non-veg → +0 = 0
        Recipe r = new Recipe("Deep Fried Fish",
                Arrays.asList("fish", "oil"),
                500, 5);
        assertEquals(0, analyzer.calculateHealthScore(r));
        assertEquals("Unhealthy", analyzer.classifyRecipe(r));
    }
}
