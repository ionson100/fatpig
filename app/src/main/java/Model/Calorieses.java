package Model;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("calorieses")
public class Calorieses {

    @PrimaryKey("id")
    public int id;

    @Column("calorieses1")
    public float calorieses1;

    @Column("calorieses2")
    public float calorieses2;

    @Column("calorieses3")
    public float calorieses3;

    @Column("date")
    public int date;
}
