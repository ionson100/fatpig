package Model;

import Model.ButtonBase;
import orm.Table;

@Table("button_eat")
public class ButtonEat extends ButtonBase {


    @Override
    public ButtonBase cloneE() {
        ButtonEat p=new ButtonEat();
        p.id=id;
        p.name=name;
        p.calories=calories;
        p.ishow=ishow;
        return p;
    }

    @Override
    public void unclone(ButtonBase p) {
        id=p.id;
        name=p.name;
        calories=p.calories;
        ishow=p.ishow;
    }
}
