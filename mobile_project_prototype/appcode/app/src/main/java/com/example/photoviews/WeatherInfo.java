package com.example.photoviews;

public class WeatherInfo {
    String wind_Direction;
    String wind_SortNumber;
    String wind_SortCode;
    String wind_Speed;
    String wind_Name;
    String temp_Min;
    String temp_Max;
    String temp_Now;
    String humidity;
    String cloud_Amount;
    String cloud_Name;
    String weather_Sort;
    String weather_Name;

    public WeatherInfo(String temp_Now,
                       String temp_Min,
                       String temp_Max,
                       String humidity,
                       String wind_Speed,
                       String wind_Name,
                       String wind_Direction,
                       String wind_SortNumber,
                       String wind_SortCode,
                       String cloud_Amount,
                       String cloud_Name,
                       String weather_Sort,
                       String weather_Name)
    {
        this.wind_Direction = wind_Direction;
        this.wind_SortNumber = wind_SortNumber;
        this.wind_SortCode = wind_SortCode;
        this.wind_Speed = wind_Speed;

        if(wind_Name.equals("")) this.wind_Name = "No Info";
        else this.wind_Name = wind_Name;

        this.temp_Min = temp_Min;
        this.temp_Max = temp_Max;
        this.temp_Now = temp_Now;
        this.humidity = humidity;

        this.cloud_Amount = cloud_Amount;
        this.cloud_Name = cloud_Name;

        this.weather_Sort = weather_Sort;
        this.weather_Name = weather_Name;
    }

    public String getWind_Speed() {
        return wind_Speed;
    }

    public String getWind_Name() {
        return wind_Name;
    }

    public String getTemp_Min() {
        return temp_Min;
    }

    public String getTemp_Max() {
        return temp_Max;
    }

    public String getTemp_Now() {
        return temp_Now;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getCloud_Amount() {
        return cloud_Amount;
    }

    public String getCloud_Name() {
        return cloud_Name;
    }

    public String getWeather_Name() {
        return weather_Name;
    }

    public void setWind_Name(String wind_Name){
        this.wind_Name = wind_Name;
    }

    public void setCloud_Name(String cloud_Name){
        this.cloud_Name = cloud_Name;
    }

    public void setWeather_Name(String weather_Name){
        this.weather_Name = weather_Name;
    }
}
