package Model;

import orm.Column;
import orm.PrimaryKey;

/**
 * Created by USER on 01.09.2016.
 */
public abstract class ButtonBase implements IClone<ButtonBase> {

    @PrimaryKey("id")
    public int id;

    @Column("name")
    public String name;

    @Column("calories")
    public double calories;

    @Column("isshow")
    public  boolean ishow;
}
