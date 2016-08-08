package Model;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("work")
public class Work {

    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;



    @Column("calorieses")
    public double calorieses;
}
