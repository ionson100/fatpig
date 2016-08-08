package Model;

import orm.Column;
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
}
