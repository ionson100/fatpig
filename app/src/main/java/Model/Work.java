package Model;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("work")
public class Work implements IClone<Work> {

    @PrimaryKey("id")
    public int id;


    @Column("name")
    public String name;



    @Column("calorieses")
    public double calorieses;

    public Work cloneE() {
        Work work=new Work();
        work.name=name;
        work.calorieses=calorieses;
        work.id=id;
        return work;
    }

    public void unclone(Work o) {
        this.id=o.id;
        this.name=o.name;
        this.calorieses=o.calorieses;
    }
}

