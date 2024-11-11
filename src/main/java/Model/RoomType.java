package Model;

import com.google.gson.Gson;

public class RoomType {
    public int id;
    public int hotel_id;
    public String hotel_name;
    public String name;
    public String description;
    public int beds;
    public float area;
    public int price;
    public Utility[] utilities;
    public Image[] images;
    public String booked;
    public String rating;
    public boolean hidden;

    public RoomType(int id, int hotel_id, String name, String description, int beds, float area, int price, Utility[] utilities, Image[] images, boolean hidden) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.name = name;
        this.description = description;
        this.beds = beds;
        this.area = area;
        this.price = price;
        this.utilities = utilities;
        this.images = images;
        this.hidden = hidden;
    }
    public RoomType(int id, int hotel_id, String name, String description, int beds, float area, int price, Utility[] utilities, Image[] images) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.name = name;
        this.description = description;
        this.beds = beds;
        this.area = area;
        this.price = price;
        this.utilities = utilities;
        this.images = images;
    }
    public String getImagesJson(){
        Gson gson = new Gson();
        return gson.toJson(this.images);
    }

    public RoomType(int id, int hotel_id, String hotel_name, String name, String description, int beds, float area, int price, Utility[] utilities, Image[] images, String booked, String rating) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.name = name;
        this.description = description;
        this.beds = beds;
        this.area = area;
        this.price = price;
        this.utilities = utilities;
        this.images = images;
        this.booked = booked;
        this.rating = rating;
    }
}
