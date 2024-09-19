package Model;


import java.util.Date;
public class Customer {
    public int id;
    public String name;
    public String email;
    public String phone;
    public Date dob;
    public String avatar;
    public String password;
    public boolean is_verified;
    public String token;

    public Customer(int id, String email, String password, boolean is_verified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.is_verified = is_verified;
    }

    public Customer(int id, String name, String email, String phone, Date dob, String avatar, boolean is_verified, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.avatar = avatar;
        this.is_verified = is_verified;
        this.token = token;
    }

    public Customer(int id, String name, String email, String phone, Date dob, String avatar, String password, boolean is_verified, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.avatar = avatar;
        this.password = password;
        this.is_verified = is_verified;
        this.token = token;
    }
}