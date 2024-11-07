package Model;

import java.util.Date;

public class Hotel {
    public int id;
    public String name;
    public String email;
    public String avatar;
    public String address;
    public String gg_map_link;
    public String password;
    public Date created_at;
    public boolean is_verified;
    public boolean is_blocked;

    public Hotel(int id, String name, String email, String avatar, String password, Date created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
        this.created_at = created_at;
    }

    public Hotel(int id, String name, String email, String avatar, String address, String gg_map_link, String password, Date created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.address = address;
        this.gg_map_link = gg_map_link;
        this.password = password;
        this.created_at = created_at;
    }

    public Hotel(int id, String name, String email, String avatar, String address, String gg_map_link, String password, Date created_at, boolean is_verified, boolean is_blocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.address = address;
        this.gg_map_link = gg_map_link;
        this.password = password;
        this.created_at = created_at;
        this.is_verified = is_verified;
        this.is_blocked = is_blocked;
    }

    public Hotel(int id, String name, String email, String avatar, String password, Date created_at, boolean is_verified, boolean is_blocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
        this.created_at = created_at;
        this.is_verified = is_verified;
        this.is_blocked = is_blocked;
    }
}
