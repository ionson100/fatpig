package Model;


import android.widget.ProgressBar;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("product")
public class Product implements IClone<Product> {


    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;

//    @Column("squirrels")
//    public double squirrels;
//
//    @Column("fat")
//    public double fat;
//
//    @Column("carbohydrates")
//    public double carbohydrates;

    @Column("calorieses")
    public double calorieses;

    @Column("preferences")
    public boolean preferences;

    @Override
    public Product cloneE() {
        Product p=new Product();
        p.id=id;
        p.name=name;
        p.calorieses=calorieses;
        p.preferences=preferences;
        return p;
    }

    @Override
    public void unclone(Product p) {

        id=p.id;
        name=p.name;
        calorieses=p.calorieses;
        preferences=p.preferences;
    }
}
