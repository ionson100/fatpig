package Model;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("one_eat")
public class OneEat {
   // public transient Product product;

    @PrimaryKey("id")
    public int id;

//    @Column("product_id")
//    public int product_id;

    @Column("calorises")
    public double cal;

    @Column("amout")
    public double amount;

    @Column("date")
    public int date;

    @Column("isgramm")
    public boolean isGramm;
}
