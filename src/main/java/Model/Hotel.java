package Model;

import java.util.Date;

public class Hotel {
    public int id;
    public String name;
    public String email;
    public String avatar;
    public String password;
    public Date created_at;

    public Hotel(int id, String name, String email, String avatar, String password, Date created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
        this.created_at = created_at;
    }
}