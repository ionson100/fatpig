package Model;


import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("life")
public class Life {

    @PrimaryKey("id")
    public int id;

    @Column("mass")
    public double mass;

    @Column("pressure")
    public String pressure;


    @Column("date")
    public long date;

    @Column("calories")
    public double calories;

    @Column("commentary")
    public String commentary;


}
