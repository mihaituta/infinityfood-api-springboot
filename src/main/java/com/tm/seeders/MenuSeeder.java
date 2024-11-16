package com.tm.seeders;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tm.model.Menu;
import com.tm.model.Restaurant;
import com.tm.repository.MenuRepository;
import com.tm.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class MenuSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final Cloudinary cloudinary;

    public MenuSeeder(MenuRepository menuRepository, RestaurantRepository restaurantRepository, Cloudinary cloudinary) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public void run(String... args) {
        if (menuRepository.count() == 0) { // Seed only if the table is empty
            seedMenus();
        }
    }

    private void seedMenus() {
        String[] menus = {
                // Starters
                "Tender chicken nuggets",
                "Breaded zucchini with garlic sauce",
                "Zorba salad",
                "Caesar salad",
                "Beans with fried onions",
                "Eggplant salad",
                "Breaded cheese with sesame",
                // Main Courses
                "Grilled chicken legs",
                "Chicken breast on stick",
                "Pork collar and polenta",
                "Sweet pork ribs with potatoes",
                "Grilled pork tenderloin in sauce",
                "Sarmale in pickled cabbage leaves and polenta",
                "Chicken schnitzel",
                "Turkey schnitzel",
                "The master's table",
                // Fastfood
                "Crispy Burger",
                "Grand Classic Burger",
                "Souvlaki",
                "Persian plateau",
                "Schnitzel menu",
                "Crispy menu",
                "Leonidas Pita",
                "Cheese gyro",
                "Burger Gyros",
                // Pizza
                "Pizza quattro stagioni",
                "Pizza Diavola",
                "Pizza Carnivora",
                "Pizza Hot Chorizo",
                "Pizza Light",
                "Pizza Rustic",
                "Pizza Crudo",
                "Pizza Romana",
                // Dessert
                "Sour cherry jam",
                "Cherries semolina with milk",
                "Cheese pancakes with raspberry filling",
                "Ikea cheese pancake",
                "Apple pie",
                "Black forest",
                "Tiramisu",
                "Dark cake",
                // Drinks
                "Coca cola 330ml can",
                "Coca Cola 330ml",
                "Coca Cola 500ml",
                "Fanta 500ml",
                "Pepsi 330ml can",
                "Pepsi 500ml",
                "Miranda 330ml can",
                "Heineken 500ml",
                "Strongbow Rose Apple 330ml",
                "Strongbow Gold Apple 330ml",
                "Strongbow Red Berries 330ml",
                "Strongbow Gold Apple 330ml can",
                "Strongbow Red Berries 330ml can",
                "Water Bucovina 500ml"
        };

        String[] descriptions = {
                // Starters
                "Chicken, garlic, cheese, ketchup, eggs, black pepper, salt, flour",
                "Zucchini, flour, eggs, mustard, black pepper, salt, garlic, sunflower oil",
                "Tomatoes, cucumber, paprika, cheese, olives, black pepper, sunflower oil",
                "Chicken, cheese, toast, garlic, mayo sauce, sunflower oil, white pepper, salt",
                "Beans, onions, garlic",
                "Eggplant, onions, tomatoes",
                "Cheese, eggs, flour, sunflower oil, salt, sesame",
                // Main Courses
                "Chicken legs, black pepper, salt",
                "Chicken, mushrooms, paprika, tomatoes, onions",
                "Pork collar, onions, mushrooms, garlic, sunflower oil, salt, black pepper",
                "Pork ribs, potatoes, bbq sauce, garlic, black pepper, salt",
                "Pork tenderloin, paprika sauce, garlic, black pepper, salt",
                "Minced meat, rice, pickled cabbage, onions",
                "Chicken, eggs, black pepper, salt, flour",
                "Turkey, eggs, black pepper, salt, flour",
                "Chicken, onions, paprika, tomato sauce, mushrooms, garlic",
                // Fastfood
                "Bacon, lettuce, tomatoes, pickles, Jack's sauce, fries",
                "Beef, cheese, bacon, onions, tomatoes, lettuce, pickles, bbq sauce, fries",
                "Chicken or pork, fries, lettuce, tomatoes, pickles, sour sauce, garlic sauce, veggies sauce, olive sauce, tzatziki, pita",
                "Chicken or pork, pizza sauce, butter, yogurt, paprika, tomatoes, pita",
                "Schnitzel, fries, garlic sauce, white salad, pita",
                "Crispy chicken, fries, lettuce, garlic sauce, sour sauce, pita",
                "Chicken or pork, lettuce, tomatoes, pickles, onions, garlic sauce, olives sauce, sour-sweet sauce, veggies sauce, pita",
                "Chicken or pork, lettuce, tomatoes, pickles, red onions, garlic sauce, olives sauce, sour-sweet sauce, veggies sauce, pita",
                "Chicken or pork, lettuce, pickles, fries, garlic sauce, sour-sweet sauce, pita",
                // Pizza
                "100% mozzarella, ham, chorizo, mushrooms, green paprika",
                "Tomato sauce, bacon, praga ham, mushrooms, olives, paprika, mozzarella",
                "Tomato sauce, bacon, praga ham, sausages, egg, tomatoes, mozzarella",
                "Chorizo, tomatoes, onions, paprika",
                "Paprika, onions, tomatoes, corn, mushrooms",
                "Chicken, olives, onions, paprika",
                "Tomato sauce, mozzarella, parma ham, corn, mushrooms",
                "Tomato sauce, cheese, ham, jalapenos",
                // Dessert
                "Eggs, milk, cherries, sugar",
                "Cherries, milk, semolina, sugar, salt",
                "Cheese, sour cream, eggs, sugar, cherries, flour, lemon peel, orange peel",
                "Cheese, cherries, egg, sour cream, sugar",
                "Apples, sugar, nuts, sunflower oil, vanilla essence",
                "Flour, sugar, vegetal cream, hydrogenated palm oil, emulsifiers, soy lecithin, milk protein",
                "Vegetal cream, sugar, coffee, milk, eggs, cacao, sesame, nuts, emulsifiers",
                "Cacao cream, vegetal cream, sugar, sesame, nuts, pistachio",
                // Drinks
                "Refreshing, carbonated, caffeinated drink",
                "Refreshing, carbonated, caffeinated drink",
                "Refreshing, carbonated, caffeinated drink",
                "Refreshing, carbonated drink with orange juice",
                "Refreshing, carbonated, caffeinated drink",
                "Refreshing, carbonated, caffeinated drink",
                "Refreshing, carbonated, caffeinated drink",
                "Blonde beer",
                "Apple cider",
                "Gold apple cider",
                "Red fruit flavor cider",
                "Gold apple cider",
                "Red fruit flavor cider",
                "Water"
        };

        List<Restaurant> restaurants = restaurantRepository.findAll();


        for (Restaurant restaurant : restaurants) {
            uploadMenuImagesToCloudinary(restaurant.getSlug());
        }

        //uploadMenuImagesToCloudinary("demo");

        for (int i = 0; i < menus.length; i++) {

            for (Restaurant restaurant : restaurants) {
                String slug = generateSlug(menus[i]);

                Menu menu = new Menu(
                        menus[i],  // Name
                        descriptions[i],
                        Math.round(5 + Math.random() * (40 - 5) * 100) / 100.0,  // Random price
                        generateSlug(restaurant.getName()) + "/menu-images/" + slug,  // Image
                        determineType(i),
                        restaurant.getId()
                );
                menuRepository.save(menu);
            }

            /*String slug = generateSlug(menus[i]);
            Menu menu = new Menu(
                    menus[i],  // Name
                    descriptions[i],  // Description
                    Math.round(5 + Math.random() * (40 - 5) * 100) / 100.0,  // Random price
                    "demo/menu-images/" + slug,  // Image
                    determineType(i),  // Type
                    1L  // Store ID
            );
            menuRepository.save(menu);*/

        }
    }

    private void uploadMenuImagesToCloudinary(String restaurantSlug) {
        // Define the Cloudinary folder for the restaurant's menu images
        String cloudinaryFolder = restaurantSlug + "/menu-images/";

        // Get the local 'menu-images' folder path
        File menuImagesFolder = new File("src/main/resources/SeedImages/menu-images");
        if (menuImagesFolder.exists() && menuImagesFolder.isDirectory()) {
            for (File imageFile : Objects.requireNonNull(menuImagesFolder.listFiles())) {
                if (imageFile.isFile()) {
                    try {
                        // Generate the public_id without extension
                        String slugWithoutExtension = generateSlugImageWithoutExtension(imageFile.getName());

                        // Upload each image to Cloudinary in the restaurant's 'menu-images' folder
                        cloudinary.uploader().upload(imageFile, ObjectUtils.asMap(
                                "public_id", cloudinaryFolder + slugWithoutExtension)); // Don't add the extension here
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String determineType(int index) {
        if (index <= 6) return "Starter";
        else if (index <= 15) return "Main Course";
        else if (index <= 24) return "Fastfood";
        else if (index <= 32) return "Pizza";
        else if (index <= 40) return "Dessert";
        else return "Drinks";
    }

    private String generateSlug(String name) {
        // Remove all characters that are not letters, numbers, or spaces
        name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
        // Replace spaces with hyphens and convert to lowercase
        return name.toLowerCase().trim().replaceAll("\\s+", "-");
    }

    private String generateSlugImageWithoutExtension(String filename) {
        // Remove extension from the filename
        return generateSlug(filename.substring(0, filename.lastIndexOf('.')));
    }
}