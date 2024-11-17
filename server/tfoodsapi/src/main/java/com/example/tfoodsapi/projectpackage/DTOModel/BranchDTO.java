package com.example.tfoodsapi.projectpackage.DTOModel;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class BranchDTO {
    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String address;

    // Constructors
    public BranchDTO() {
    }

    public BranchDTO(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
