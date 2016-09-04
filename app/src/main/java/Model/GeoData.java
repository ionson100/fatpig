package Model;


import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("track_data")
public class GeoData {

    @PrimaryKey("id")
    public int id;

    @Column("track_name")
    public int trackName;

    @Column("latitude")
    public double latitude;

    @Column("longitude")
    public double longitude;

    @Column("date")
    public long date;

    @Column("speed")
    public float speed;

    @Column("altitude")
    public double altitude;
}
