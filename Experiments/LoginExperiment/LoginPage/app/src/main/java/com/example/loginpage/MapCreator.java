package com.example.loginpage;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

public class MapCreator extends Application
{
    public static void createMap()
    {
        JSONObject map = new JSONObject();
        try
        {
            map.put("X", 0);
            map.put("Y", 0);
            map.put("sprite", "x");
            map.put("walkable", false);
            map.put("dungeon", 0);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        System.out.println(map);
    }
}
