package org.example.database;

import org.example.entity.Manufacturer;
import org.example.factory.ManufacturerFactory;

public class ManufacturerTableHandler extends TableHandler<Manufacturer> {
    public ManufacturerTableHandler(String filename) {
        super(filename, new ManufacturerFactory());
    }
}
