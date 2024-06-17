package order;

import java.util.ArrayList;
import java.util.List;

public class IngredientsGenerator {

    public static Ingredients getIngredients() {
        List<String> ingredientIds = new ArrayList<>();
        ingredientIds.add("61c0c5a71d1f82001bdaaa6c");
        ingredientIds.add("61c0c5a71d1f82001bdaaa70");
        return new Ingredients(ingredientIds.toArray(new String[0]));
    }

    public static Ingredients getIngredientsEmpty() {
        return new Ingredients(new String[]{});
    }

    public static Ingredients getIngredientsIncorrect() {
        return new Ingredients(new String[]{"111", "222"});
    }
}