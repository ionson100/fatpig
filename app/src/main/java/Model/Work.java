package Model;

import orm.Table;

@Table("work")
public class Work extends ProductBase {


    public Work cloneE() {
        Work work = new Work();
        work.name = name;
        work.calorieses = calorieses;
        work.id = id;
        work.preferences = preferences;
        return work;
    }

    @Override
    public void unclone(ProductBase o) {
        this.id = o.id;
        this.name = o.name;
        this.calorieses = o.calorieses;
        this.preferences = o.preferences;
    }


}

