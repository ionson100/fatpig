package Model;


import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("Product")
public class Product {


    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;

    @Column("squirrels")
    public float squirrels;

    @Column("fat")
    public float fat;

    @Column("carbohydrates")
    public float carbohydrates;

    @Column("calorieses")
    public float calorieses;

    @Column("preferences")
    public boolean preferences;
}
