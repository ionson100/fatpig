package Model;


import android.widget.ProgressBar;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("product")
public class Product extends ProductBase  {



    @Override
    public Product cloneE() {
        Product p=new Product();
        p.id=id;
        p.name=name;
        p.calorieses=calorieses;
        p.preferences=preferences;
        return p;
    }

    @Override
    public void unclone(ProductBase p) {
        id=p.id;
        name=p.name;
        calorieses=p.calorieses;
        preferences=p.preferences;
    }


}
