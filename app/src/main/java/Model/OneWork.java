package Model;

import com.omsk.bitnic.fatpig.Utils;

import java.util.Date;

import orm.Column;
import orm.PrimaryKey;
import orm.Table;

@Table("one_work")
public class OneWork {


    @PrimaryKey("id")
    public int id;


    @Column("date_start")
    public long date_start;

    @Column("date_finish")
    public long date_finish;


    @Column("calories")
    public double calories;

    public double getCalories(User user) {
        if(user==null)
            return 0d;
        long delta;

        if(date_start!=0d&&date_finish!=0d&&date_start==date_finish){
            return calories;
        }


        if(date_finish==0d){
            delta=new Date().getTime()-date_start;
        }else{
            delta=date_finish-date_start;
        }
        double houer=delta/1000d/60d/60d;
        double res=houer*calories*user.weight;
        return Utils.round(res,2);
    }
}
