package com.tm.seeders;

import com.tm.model.Restaurant;
import com.tm.repository.RestaurantRepository;
import com.tm.util.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantSeeder implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private String generateSlug(String name) {
        // Remove all characters that are not letters, numbers, or spaces
        name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
        // Replace spaces with hyphens and convert to lowercase
        return name.toLowerCase().trim().replaceAll("\\s+", "-");
    }

    @Override
    public void run(String... args) {
        if(restaurantRepository.count() == 0) {
            List<String> storeNames = List.of("Demo", "La Familiar", "Ramen Korewa", "Pizza Hut", "Spartan", "McDonald's", "Domino's Pizza", "KFC", "Pizza Delivery");
            List<String> cities = List.of("Craiova", "Bucharest", "Cluj", "Iasi");
            List<Long> userIds = List.of(2L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
            List<String> previewDescriptions = List.of(
                    "Fast-food | Drinks | Desert",
                    "Fast-food | Drinks | Romanian",
                    "Soup | Traditional Japanese | Sushi",
                    "Pizza | Italian | Drinks",
                    "Fast-food | Greek | Drinks",
                    "Fast-food | International | Desert",
                    "Pizza | International | Italian",
                    "Fast-food | International | Drinks",
                    "Pizza | Italian | Drinks"
            );

            for(int i = 0; i < storeNames.size(); i++) {
                String storeName = storeNames.get(i);
                String slug =  generateSlug(storeName);
                String city = cities.get((int) (Math.random() * cities.size()));
                Long userId = userIds.get(i);

                // Upload images to Cloudinary
                //String previewImage = cloudinaryService.uploadImage("path/to/" + slug + "/preview.jpg", slug + "/restaurant-images", "previewImage");
                //String backgroundImage = cloudinaryService.uploadImage("path/to/" + slug + "/bg.jpg", slug + "/restaurant-images", "backgroundImage");
                //String logoImage = cloudinaryService.uploadImage("path/to/" + slug + "/logo.png", slug + "/restaurant-images", "logoImage");

                String previewImage = slug + "/restaurant-images/previewImage";
                String backgroundImage = slug + "/restaurant-images/backgroundImage";
                String logoImage = slug + "/restaurant-images/logoImage";

                // Create and save the restaurant
                Restaurant restaurant = new Restaurant();
                restaurant.setName(storeName);
                restaurant.setSlug(slug);
                restaurant.setUserId(userId);
                restaurant.setCity(city);
                restaurant.setPreviewDescription(previewDescriptions.get(i));
                restaurant.setPreviewImage(previewImage);
                restaurant.setBackgroundImage(backgroundImage);
                restaurant.setLogoImage(logoImage);
                restaurant.setContactText("We enjoy talking with those who order food on " + storeName + " because that's how we find out what we should do better. That's why we encourage you to tell us what you like and what you don't when you order food from us.");
                restaurant.setPhone1("0761234567");
                restaurant.setPhone2("0763034589");
                restaurant.setMail1("support@" + slug + ".ro");
                restaurant.setMail2("client@" + slug + ".ro");
                restaurant.setAboutText("What does " + storeName + " offer? Possibility to enjoy delicious dishes from restaurants in your area. A wide range of delicious dishes are at your disposal with just a few clicks.");

                restaurantRepository.save(restaurant);
            }
        }
    }
}
