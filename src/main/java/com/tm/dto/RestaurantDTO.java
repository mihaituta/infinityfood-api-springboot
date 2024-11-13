package com.tm.dto;

import org.jetbrains.annotations.NotNull;

public class RestaurantDTO {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String slug;

    @NotNull
    private Long userId;

    @NotNull
    private String city;

    @NotNull
    private String aboutText;

    @NotNull
    private String previewDescription;

    @NotNull
    private String backgroundImage;

    @NotNull
    private String previewImage;

    @NotNull
    private String logoImage;

    @NotNull
    private String contactText;

    @NotNull
    private String phone1;

    @NotNull
    private String phone2;

    @NotNull
    private String mail1;

    @NotNull
    private String mail2;

    public RestaurantDTO(@NotNull Long id, @NotNull String name, @NotNull String slug, @NotNull Long userId, @NotNull String city, @NotNull String aboutText, @NotNull String previewDescription, @NotNull String previewImage, @NotNull String backgroundImage, @NotNull String logoImage, @NotNull String contactText, @NotNull String phone1, @NotNull String phone2, @NotNull String mail1, @NotNull String mail2) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.userId = userId;
        this.city = city;
        this.aboutText = aboutText;
        this.previewDescription = previewDescription;
        this.previewImage = previewImage;
        this.backgroundImage = backgroundImage;
        this.logoImage = logoImage;
        this.contactText = contactText;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.mail1 = mail1;
        this.mail2 = mail2;
    }

    // Getters and Setters
    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getSlug() {
        return slug;
    }

    public void setSlug(@NotNull String slug) {
        this.slug = slug;
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull String getCity() {
        return city;
    }

    public void setCity(@NotNull String city) {
        this.city = city;
    }

    public @NotNull String getAboutText() {
        return aboutText;
    }

    public void setAboutText(@NotNull String aboutText) {
        this.aboutText = aboutText;
    }

    public @NotNull String getPreviewDescription() {
        return previewDescription;
    }

    public void setPreviewDescription(@NotNull String previewDescription) {
        this.previewDescription = previewDescription;
    }

    public @NotNull String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(@NotNull String previewImage) {
        this.previewImage = previewImage;
    }

    public @NotNull String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(@NotNull String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public @NotNull String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(@NotNull String logoImage) {
        this.logoImage = logoImage;
    }

    public @NotNull String getContactText() {
        return contactText;
    }

    public void setContactText(@NotNull String contactText) {
        this.contactText = contactText;
    }

    public @NotNull String getPhone1() {
        return phone1;
    }

    public void setPhone1(@NotNull String phone1) {
        this.phone1 = phone1;
    }

    public @NotNull String getPhone2() {
        return phone2;
    }

    public void setPhone2(@NotNull String phone2) {
        this.phone2 = phone2;
    }

    public @NotNull String getMail1() {
        return mail1;
    }

    public void setMail1(@NotNull String mail1) {
        this.mail1 = mail1;
    }

    public @NotNull String getMail2() {
        return mail2;
    }

    public void setMail2(@NotNull String mail2) {
        this.mail2 = mail2;
    }
}
