package org.example.entity;

import org.json.JSONObject;

public class Manufacturer implements JSONable {

    private long id;
    private String name;
    private String country;

    public Manufacturer() {}

    public static class Builder {

        private final Manufacturer manufacturer;

        public Builder() {
            manufacturer = new Manufacturer();
        }

        public Builder setId(long id) {
            manufacturer.setId(id);
            return this;
        }

        public Builder setName(String name) {
            manufacturer.setName(name);
            return this;
        }

        public Builder setCountry(String country) {
            manufacturer.setCountry(country);
            return this;
        }

        public Manufacturer build() {
            return manufacturer;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("country", country);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
