package com.tm.dto;

public class OrderResponseDTO {

    private Long id;
    private Integer status;
    private Double totalPrice;
    private String menus;
    private String name;
    private String phone;
    private String city;
    private String address;
    private String houseNr;
    private String floor;
    private String apartment;
    private String information;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    // Constructor
    public OrderResponseDTO(Long id, Integer status, Double totalPrice, String menus, String name,
                            String phone, String city, String address, String houseNr,
                            String floor, String apartment, String information) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.menus = menus;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.houseNr = houseNr;
        this.floor = floor;
        this.apartment = apartment;
        this.information = information;
    }

    // Getters and setters...
}
