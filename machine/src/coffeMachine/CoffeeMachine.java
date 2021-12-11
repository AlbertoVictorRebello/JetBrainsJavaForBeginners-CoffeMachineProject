package coffeMachine;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class CoffeeMachine {
    private static Recipe[] recipeList = new Recipe[] {
            loadRecipe("SETUP"),
            loadRecipe("ESPRESSO"),
            loadRecipe("LATTE"),
            loadRecipe("CAPPUCCINO")};
    private static Money[] moneyBucket = new Money[] {loadMoney("ONE_DOLLAR_BILL")};
    private static IngredientBin ingredientBin01= new IngredientBin("INGREDIENT_BIN_1");
    private static MoneyBin moneyBin01 = new MoneyBin("MONEY_BIN_01");
    private static int moneyBin02;
    private static int disposableCups;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ingredientBin01.supply(recipeList[0]);
        disposableCups = recipeList[0].getYield();
        moneyBin02 = 550;
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        String action = scanner.next();
        while (!"exit".equals(action)) {
            switch (action) {
                case "buy":
                    System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                    String option = scanner.next();
                    if(!"back".equals(option)) {
                        int product = Integer.parseInt(option);
                        if (yieldForecast(recipeList[product], true, 1) > 0 && disposableCups > 0) {
                            moneyBin02 += Math.round(recipeList[product].getPrice());
                            disposableCups--;
                            ingredientBin01.deploy(recipeList[product]);
                        }
                    }
                    break;

                case "fill":
                    ingredientBin01.supply();
                    System.out.println("Write how many disposable cups of coffee you want to add:");
                    disposableCups += scanner.nextInt();
                    break;

                case "take":
                    System.out.printf("I gave you $%d\n\n", moneyBin02);
                    moneyBin02 = 0;
                    break;

                case "remaining":
                    ingredientBin01.displayStock();
                    System.out.println(disposableCups + " disposable cups");
                    System.out.println("$" + moneyBin02 + " of money");
                    break;
            }
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            action = scanner.next();
        }
        //supplyForecast(recipeList[0]);



        for (Money money : moneyBucket) {
            moneyBin01.supply(money);
        }

        //displayMessage("COFFEE_PREPARATION");



    }

    public static Money loadMoney(String name) {
        Money money = new Money();
        switch (name) {
            case "ONE_DOLLAR_BILL":
                money.setName("one dollar bill");
                money.setUnitOfMeasurement("USD");
                money.setQuantity(1);
                break;
        }
        return money;

    }

    public static Recipe loadRecipe(String name){
        Recipe recipe = new Recipe();
        Ingredient ingredient;
        ArrayList<Ingredient> ingredients;

        switch (name) {
            case "SETUP":
                recipe.setName("setup");
                recipe.setPreparationTime(6000);

                ingredients = new ArrayList<>();
                ingredient = new Ingredient("water", "ml", 400F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("milk", "ml", 540F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("coffee beans", "g", 120F);
                ingredients.add(ingredient);
                recipe.setIngredients(ingredients);

                recipe.setPackingType(PackingType.CUP_OF_300_ML);
                recipe.setYield(9);
                recipe.setPrice(550);
                break;

                case "ESPRESSO":
                recipe.setName("espresso");
                recipe.setPreparationTime(10);

                ingredients = new ArrayList<>();
                ingredient = new Ingredient("water", "ml", 250F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("milk", "ml", 0F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("coffee beans", "g", 16F);
                ingredients.add(ingredient);
                recipe.setIngredients(ingredients);

                recipe.setPackingType(PackingType.CUP_OF_300_ML);
                recipe.setYield(1);
                recipe.setPrice(4);
                break;

            case "LATTE":
                recipe.setName("latte");
                recipe.setPreparationTime(15);

                ingredients = new ArrayList<>();
                ingredient = new Ingredient("water", "ml", 350F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("milk", "ml", 75F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("coffee beans", "g", 20F);
                ingredients.add(ingredient);
                recipe.setIngredients(ingredients);

                recipe.setPackingType(PackingType.CUP_OF_300_ML);
                recipe.setYield(1);
                recipe.setPrice(7);
                break;

            case "CAPPUCCINO":
                recipe.setName("cappuccino");
                recipe.setPreparationTime(15);

                ingredients = new ArrayList<>();
                ingredient = new Ingredient("water", "ml", 200F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("milk", "ml", 100F);
                ingredients.add(ingredient);
                ingredient = new Ingredient("coffee beans", "g", 12F);
                ingredients.add(ingredient);
                recipe.setIngredients(ingredients);

                recipe.setPackingType(PackingType.CUP_OF_300_ML);
                recipe.setYield(1);
                recipe.setPrice(6);
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

    public static int yieldForecast(Recipe recipe, boolean verbose, int... quantity){
        Scanner scanner = new Scanner(System.in);

        int demand;
        int yield;
        ArrayList<String> missingIngredients = new ArrayList<>();
        if (quantity.length == 1) {
            demand = quantity[0];
        } else {
            System.out.println("Write how many cups of coffee you will need:");
            demand = scanner.nextInt();
        }

        int minYield = Integer.MAX_VALUE;

        for (Ingredient itemRecipe : recipe.getIngredients()) {
            for (Ingredient itemBin : ingredientBin01.getStock()) {
                if (itemRecipe.getName().equals(itemBin.getName())) {
                    yield = (int) Math.floor (itemBin.getQuantity() / itemRecipe.getQuantity());
                    minYield = Math.min(minYield, yield);
                    if (demand > yield) {
                        missingIngredients.add(itemBin.getName());
                    }
                }
            }
        }
        if (demand == minYield) {
            if (verbose) {
                //displayMessage("Yes, I can make that amount of coffee");
                displayMessage("I have enough resources, making you a coffee!");
            }
            return minYield;

        } else if (demand > minYield) {
            if (verbose) {
                //displayMessage(String.format("No, I can make only %s cup(s) of coffee", minYield));
                for (String item : missingIngredients) {
                    System.out.printf("Sorry, not enough %s!\n", item);
                }
            }
            return 0;
        } else {
            if (verbose) {
                //displayMessage(String.format("Yes, I can make that amount of coffee (and even %s more than that)", minYield - demand));
                displayMessage("I have enough resources, making you a coffee!");
            }
            return minYield - demand;
        }
    }

}

class Recipe {
    private String name;
    private int preparationTime;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private PackingType packingType;
    private int yield;
    private float price;

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

    public void setPrice(float price) {this.price = price;}

    public float getPrice() { return price;    }

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
    CUP_OF_300_ML("disposable cup of 300Ml", "disposable cup");

    String longDescription;
    String shortDescription;

    PackingType(String longDescription, String shortDescription) {
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
    }
}

class Money {
    private String name = "";
    private String unitOfMeasurement = "";
    private float quantity = 0;

    public Money() {

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

    public void displayStock() {
        System.out.println("The coffee machine has:");
        for (Ingredient item : stock) {
            System.out.printf("%d %s of %s\n",Math.round(item.getQuantity()), item.getUnitOfMeasurement(), item.getName());
        }

    }


    public boolean supply(Recipe recipe) {

        float input;
        for (Ingredient item : recipe.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(item.getName());
            ingredient.setUnitOfMeasurement(item.getUnitOfMeasurement());
            //System.out.printf("Write how many %s of %s the coffee machine has:", item.getUnitOfMeasurement(), item.getName());
            //input = scanner.nextFloat();
            input = item.getQuantity();
            ingredient.setQuantity(input);
            stock.add(ingredient);
        }
        return true;
    }

    public boolean supply() {
        float input;
        for (Ingredient item : stock) {
            System.out.printf("Write how many %s of %s you want to add:", item.getUnitOfMeasurement(), item.getName());
            input = scanner.nextInt();
            item.setQuantity(item.getQuantity() + input);
        }

        return true;
    }

    public boolean deploy(Recipe recipe) {

        float input;
        for (Ingredient item : recipe.getIngredients()) {
            for (Ingredient ingredient : stock) {
                if (ingredient.getName().equals(item.getName())) {
                    ingredient.setQuantity(ingredient.getQuantity() - item.getQuantity());
                }
            }
        }
        return true;
    }
}

class MoneyBin {
    private Scanner scanner = new Scanner(System.in);
    private String name;
    private Map<Money, Integer> stock = new Hashtable<>();

    public MoneyBin(String name) {
        this.name = name;
    }

    public int getStock(String moneyName) {
        for (Map.Entry<Money, Integer> entry : stock.entrySet()) {
            if (entry.getKey().equals(moneyName)) {
                return entry.getValue();
            }
        }

        return 0;
    }

    public boolean withDraw(String moneyName) {
        return false;

    }

    public boolean supply(Money money) {
        int quantity = 550;
        stock.put(money, quantity);

        return true;
    }
}

