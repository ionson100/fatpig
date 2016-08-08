package Model;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("buttons")
public class Buttons {

    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;

    @Column("work_id")
    public int workId;

    @Column("isShow")
    public boolean isShow;
}
