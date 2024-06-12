package Order;

public class IngredientsGenerator {

    public static Ingredients getIngredients() {
        return new Ingredients(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70"});
    }

    public static Ingredients getIngredientsEmpty() {
        return new Ingredients(new String[]{});
    }

    public static Ingredients getIngredientsIncorrect() {
        return new Ingredients(new String[]{"123", "456"});
    }
}
