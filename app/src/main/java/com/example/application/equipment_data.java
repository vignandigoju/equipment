package com.example.application;
import java.io.Serializable;

public class equipment_data implements Serializable {
    private String equipmentName;

    public equipment_data(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getequipmentName() {
        return equipmentName;
    }

}
