package Model;

import orm.Table;

@Table("button_work")
public class ButtonWork extends ButtonBase {
    @Override
    public ButtonBase cloneE() {
        ButtonWork p = new ButtonWork();
        p.id = id;
        p.name = name;
        p.calories = calories;
        p.ishow = ishow;
        return p;
    }

    @Override
    public void unclone(ButtonBase p) {

        id = p.id;
        name = p.name;
        calories = p.calories;
        ishow = p.ishow;
    }
}
