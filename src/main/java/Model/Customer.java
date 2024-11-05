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
    public boolean is_blocked;
    public String token;
    public Date created_at;

    public Customer(int id, String email, String password, boolean is_verified, boolean is_blocked) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.is_verified = is_verified;
        this.is_blocked = is_blocked;
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

    public Customer(int id, String name, String email, String phone, Date dob, String avatar, String password, boolean is_verified, String token, Date created_at, boolean is_blocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.avatar = avatar;
        this.password = password;
        this.is_verified = is_verified;
        this.token = token;
        this.created_at = created_at;
        this.is_blocked = is_blocked;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", avatar='" + avatar + '\'' +
                ", password='" + password + '\'' +
                ", is_verified=" + is_verified +
                ", token='" + token + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
