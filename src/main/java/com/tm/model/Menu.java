package com.tm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "menus")
public class Menu {

    public static final String TYPE_STARTER = "Felul întâi";
    public static final String TYPE_MAIN = "Fel principal";
    public static final String TYPE_FASTFOOD = "Fastfood";
    public static final String TYPE_PIZZA = "Pizza";
    public static final String TYPE_DESSERT = "Desert";
    public static final String TYPE_DRINK = "Băuturi";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column
    private String image;

    @Column(nullable = false)
    private String type;

    // @JsonIgnore
    @Column(name = "restaurantId", nullable = false)
    private Long restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId", nullable = false, insertable = false, updatable = false)
    private Restaurant restaurant;

    public Menu(String name, String description, Double price, String image, String type, Long restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.type = type;
        this.restaurantId = restaurantId;
    }

    public Menu() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}