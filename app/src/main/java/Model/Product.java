package Model;


import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("product")
public class Product {


    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;

    @Column("squirrels")
    public double squirrels;

    @Column("fat")
    public double fat;

    @Column("carbohydrates")
    public double carbohydrates;

    @Column("calorieses")
    public double calorieses;

    @Column("preferences")
    public boolean preferences;
}
