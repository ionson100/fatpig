package Model;

import java.util.List;

import orm.Column;
import orm.Configure;
import orm.PrimaryKey;
import orm.Table;

@Table("user")
public class User {

    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;

    @Column("age")
    public int age;

    @Column("growing")
    public int growing;

    @Column("weight")
    public int weight;


    @Column("calorieses")
    public float calorieses;

    @Column("sex")
    private int _sex;

    @Column("delta")
    public double delta = 1.2;


    public Sex getSex() {
        return _sex == 0 ? Sex.men : Sex.women;
    }

    public void setSex(Sex sex) {
        _sex = sex == Sex.men ? 0 : 1;
    }


    public static User getUser() {
        List<User> users = Configure.getSession().getList(User.class, null);
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }

    }
}
