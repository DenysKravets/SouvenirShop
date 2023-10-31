package org.example.entity;

import org.json.JSONObject;

public class Product implements JSONable {

    private long id;
    private String name;
    private long manufacturerId;
    private String date;
    private double price;

    public Product() {}

    public static class Builder {

        private final Product product;

        public Builder() {
            this.product = new Product();
        }

        public Builder setId(long id) {
            product.id = id;
            return this;
        }

        public Builder setName(String name) {
            product.name = name;
            return this;
        }

        public Builder setManufacturerId(long manufacturerId) {
            product.manufacturerId = manufacturerId;
            return this;
        }

        public Builder setDate(String date) {
            product.date = date;
            return this;
        }

        public Builder setPrice(double price) {
            product.price = price;
            return this;
        }

        public Product build() {
            return product;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("manufacturerId", manufacturerId);
        jsonObject.put("date", date);
        jsonObject.put("price", price);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", date='" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
