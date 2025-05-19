package com.example.blood;

public class Donor {
    private String name;
    private String bloodGroup;
    private String location;

    // Default constructor
    public Donor() {
    }

    // Parameterized constructor
    public Donor(String name, String bloodGroup, String location) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.location = location;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "Donor{" +
                "name='" + name + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
