package Model;

import orm.Column;
import orm.PrimaryKey;


public abstract class ProductBase implements IClone<ProductBase> {
    @PrimaryKey("id")
    public int id;

    @Column("name")
    public String name;

    @Column("calorieses")
    public double calorieses;

    @Column("preferences")
    public boolean preferences;

}
