package Model;


import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("life")
public class Life implements IClone<Life> {

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


    @Override
    public Life cloneE() {
        Life p = new Life();
        p.id = id;
        p.mass = mass;
        p.pressure = pressure;
        p.date = date;
        p.calories = calories;
        p.commentary = commentary;
        return p;
    }

    @Override
    public void unclone(Life p) {

        id = p.id;
        mass = p.mass;
        pressure = p.pressure;
        date = p.date;
        calories = p.calories;
        commentary = p.commentary;

    }
}
