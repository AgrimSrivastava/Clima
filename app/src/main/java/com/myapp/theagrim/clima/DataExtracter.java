package com.myapp.theagrim.clima;

import org.json.JSONException;
import org.json.JSONObject;

public class DataExtracter {

    private String temperature;
    private int condition;
    private String city;
    private String iconname;

    public static DataExtracter extracter(JSONObject jsonObject){
        DataExtracter dataExtracter=new DataExtracter();

        try {
            dataExtracter.city=jsonObject.getString("name");
            dataExtracter.condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            dataExtracter.iconname=updateWeatherIcon(dataExtracter.condition);
            dataExtracter.temperature=(jsonObject.getJSONArray("main").getJSONObject(0).getDouble("temp")-273.15)+"";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataExtracter;
    }

        private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    public String getTemperature() {
        return temperature+"°";
    }

    public String getCity() {
        return city;
    }

    public String getIconname() {
        return iconname;
    }
}
