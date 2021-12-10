package coffeMachine;

import java.util.ArrayList;
import java.util.Scanner;

public class CoffeeMachine {
    private static Recipe [] recipeList = new Recipe[] {loadRecipe("REGULAR_COFFEE")};
    private static IngredientBin ingredientBin01= new IngredientBin("INGREDIENT_BIN_1");

    public static void main(String[] args) {
        for (Recipe recipe : recipeList) {
            ingredientBin01.supply(recipe);
            //supplyForecast(recipe);
            yieldForecast(recipe);
        }

        //displayMessage("COFFEE_PREPARATION");



    }

    public static Recipe loadRecipe(String name){
        Recipe recipe = new Recipe();

        switch (name) {
            case "REGULAR_COFFEE":
                recipe.setName("Regular Coffee");
                recipe.setPreparationTime(15);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                Ingredient ingredient = new Ingredient("water", "ml", 200F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("milk", "ml", 50F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("coffee beans", "g", 15F);
                ingredients.add(ingredient);
                recipe.setIngredients(ingredients);

                recipe.setPackingType(PackingType.CUP_OF_300_ML);
                recipe.setYield(1);
                break;
        }
        return recipe;
    }

    private static void displayMessage(String message) {

        switch (message) {
            case "COFFEE_PREPARATION":
                message = "Starting to make a coffee\n" +
                        "Grinding coffee beans\n" +
                        "Boiling water\n" +
                        "Mixing boiled water with crushed coffee beans\n" +
                        "Pouring coffee into the cup\n" +
                        "Pouring some milk into the cup\n" +
                        "Coffee is ready!";
                break;
        }
        System.out.println(message);
    }

    public boolean supplyIngredientBin(String recipeName){

        return true;
    }

    public boolean deploy(Recipe recipe, float quantity){
        return false;

    }

    public static void supplyForecast(Recipe recipe){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Write how many cups of coffee you will need:");
        int demand = scanner.nextInt();
        String message = String.format("For %d cups of coffee you will need:\n",demand);
        for (Ingredient item : recipe.getIngredients()) {
            message += String.format("%d %s of %s\n", Math.round(item.getQuantity() * demand), item.getUnitOfMeasurement(),item.getName());
        }

        displayMessage(message);

    }

    public static void yieldForecast(Recipe recipe){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write how many cups of coffee you will need:");
        int demand = scanner.nextInt();
        int minYield = Integer.MAX_VALUE;

        for (Ingredient itemRecipe : recipe.getIngredients()) {
            for (Ingredient itemBin : ingredientBin01.getStock()) {
                if (itemRecipe.getName().equals(itemBin.getName())) {
                    minYield = (int) Math.min(minYield, Math.floor (itemBin.getQuantity() / itemRecipe.getQuantity()));
                }
            }
        }
        if (demand == minYield) {
            displayMessage("Yes, I can make that amount of coffee");
        } else if (demand > minYield) {
            displayMessage(String.format("No, I can make only %s cup(s) of coffee", minYield));
        } else {
            displayMessage(String.format("Yes, I can make that amount of coffee (and even %s more than that)", minYield - demand));
        }
    }

}

class Recipe {
    private String name;
    private int preparationTime;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private PackingType packingType;
    private int yield;

       public void setName(String name) {
        this.name = name;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setPackingType(PackingType packingType) {
        this.packingType = packingType;
    }

    public String getName() {
        return name;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public PackingType getPackingType() {
        return packingType;
    }

    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }
}

enum PackingType {
    CUP_OF_300_ML("cupOf300Ml", "cup");

    String longDescription;
    String shortDescription;

    PackingType(String longDescription, String shortDescription) {
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
    }
}

class Ingredient {
    private String name = "";
    private String unitOfMeasurement = "";
    private float quantity = 0;

    public Ingredient() {

    }

    public Ingredient(String name, String unitOfMeasurement, float quantity) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}

class IngredientBin {
    private Scanner scanner = new Scanner(System.in);
    private String name;
    private ArrayList<Ingredient> stock = new ArrayList<>();

    public IngredientBin(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getStock() {
        return stock;
    }

    public void setStock(ArrayList<Ingredient> stock) {
        this.stock = stock;
    }

    public boolean queue(Ingredient ingredient) {
        return false;

    }

    public boolean supply(Recipe recipe) {

        float input;
        for (Ingredient item : recipe.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(item.getName());
            ingredient.setUnitOfMeasurement(item.getUnitOfMeasurement());
            System.out.printf("Write how many %s of %s the coffee machine has:", item.getUnitOfMeasurement(), item.getName());
            input = scanner.nextFloat();
            ingredient.setQuantity(input);
            stock.add(ingredient);
        }

        return true;
    }
}