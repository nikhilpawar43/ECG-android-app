package com.dissertation.easycommuteguideonline.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.Flower;

public class FlowerJSONParser {
	
	private static List<Flower> flowers;
	
	public static List<Flower> parseFeed (String content) {
			
		try {
			JSONArray jsonArray = new JSONArray(content);
			flowers = new ArrayList<>();
			
			for (int i=0; i<jsonArray.length(); i++) {
				
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Flower flower = new Flower();
				
				flower.setProduct_id(jsonObject.getInt("productId"));
				flower.setName(jsonObject.getString("name"));
				flower.setCategory(jsonObject.getString("category"));
				flower.setInstruction(jsonObject.getString("instructions"));
				flower.setPrice(jsonObject.getDouble("price"));
				flower.setPhoto(jsonObject.getString("photo"));
				
				flowers.add(flower);
			}
			
			return flowers;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
