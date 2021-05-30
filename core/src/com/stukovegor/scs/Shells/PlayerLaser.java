package com.stukovegor.scs.Shells;

import com.badlogic.gdx.math.Vector2;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Oscar;

/**
 * Класс лазеров {@link Oscar}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 2.0
 */
public final class PlayerLaser extends Shell {

    public PlayerLaser(com.stukovegor.scs.Screens.PlayScreen screen, float x, float y) {
        super(screen, x, y);
        velocity = new Vector2(8, 0);
    }

    public PlayerLaser(PlayScreen screen, float x, float y, boolean toLeft) {
        super(screen, x, y);
        velocity = new Vector2(toLeft? -8 : 8, 0);
    }

    {
        region =  screen.getManager().getAtlas().findRegion("laser");
        defineShell();
    }

    @Override
    protected void defineShell() {

        super.defineShell();

        //Устанавливаем размер тела
        shape.setAsBox(11.5f / com.stukovegor.scs.MainClass.PPM, 1.5f / com.stukovegor.scs.MainClass.PPM);

        //Определение категории тела снаряда
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.LASER_OF_PLAYER_BIT;

        //Определение категорий тел, с которыми сможет контактировать снаряд
        fdef.filter.maskBits =  com.stukovegor.scs.MainClass.OBJECT_BIT |
                                com.stukovegor.scs.MainClass.ENEMY_BIT  |
                                com.stukovegor.scs.MainClass.LASER_OF_ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        setBounds(0, 0, 23 / com.stukovegor.scs.MainClass.PPM, 3 / MainClass.PPM);
    }

}
