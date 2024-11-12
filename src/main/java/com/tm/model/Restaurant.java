package com.tm.model;
import jakarta.persistence.*;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String slug;
    private Integer user_id;
    private String city;
    private String aboutText;
    private String previewDescription;
    private String previewImage;
    private String logoImage;
    private String contactText;
    private String phone1;
    private String phone2;
    private String mail1;
    private String mail2;
}
