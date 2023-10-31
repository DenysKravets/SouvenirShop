package org.example.factory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class FromJSONFactory<T> {

    public List<T> createAll(String json) {
        List<T> returnArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            returnArray.add(create(jsonObject));
        }
        return returnArray;
    }

    public abstract T create(JSONObject jsonObject);
}
