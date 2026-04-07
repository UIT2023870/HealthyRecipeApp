package healthyrecipeapp.health_module;

import healthyrecipeapp.recipe_module.Recipe;

public class HealthAnalyzer {

    public int calculateHealthScore(Recipe recipe) {

        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        int score = 0;

        if (recipe.getCalories() < 200) {
            score += 30;
        } else if (recipe.getCalories() < 400) {
            score += 20;
        }

        if (recipe.getProtein() > 20) {
            score += 40;
        } else if (recipe.getProtein() > 10) {
            score += 20;
        }

        if (recipe.isVegetarian()) {
            score += 30;
        }

        return score;
    }

    public String classifyRecipe(Recipe recipe) {
        int score = calculateHealthScore(recipe);

        if (score >= 80)
            return "Excellent";
        else if (score >= 50)
            return "Good";
        else
            return "Unhealthy";
    }

    public boolean isHealthy(Recipe recipe) {
        return calculateHealthScore(recipe) >= 50;
    }
}