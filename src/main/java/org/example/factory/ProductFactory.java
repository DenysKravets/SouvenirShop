package org.example.factory;

import org.example.entity.Product;
import org.json.JSONObject;

public class ProductFactory extends FromJSONFactory<Product> {

    @Override
    public Product create(JSONObject jsonObject) {

        long id = jsonObject.getLong("id");
        String name = jsonObject.getString("name");
        long manufacturerId = jsonObject.getLong("manufacturerId");
        String date = jsonObject.getString("date");
        double price = jsonObject.getDouble("price");

        return new Product.Builder()
                .setId(id)
                .setName(name)
                .setManufacturerId(manufacturerId)
                .setDate(date)
                .setPrice(price)
                .build();
    }
}
