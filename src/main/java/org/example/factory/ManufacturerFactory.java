package org.example.factory;

import org.example.entity.Manufacturer;
import org.json.JSONObject;

public class ManufacturerFactory extends FromJSONFactory<Manufacturer> {
    @Override
    public Manufacturer create(JSONObject jsonObject) {

        long id = jsonObject.getLong("id");
        String name = jsonObject.getString("name");
        String country = jsonObject.getString("country");

        return new Manufacturer.Builder()
                .setId(id)
                .setName(name)
                .setCountry(country)
                .build();
    }
}
