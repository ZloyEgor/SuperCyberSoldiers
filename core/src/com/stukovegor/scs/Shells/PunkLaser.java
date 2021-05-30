package com.stukovegor.scs.Shells;

import com.badlogic.gdx.math.Vector2;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Punk;

/**
 * Класс лазеров {@link Punk}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 2.0
 */

public final class PunkLaser extends Shell {

    public PunkLaser(com.stukovegor.scs.Screens.PlayScreen screen, float x, float y) {
        super(screen, x, y);
        velocity = new Vector2(-7f, 0);
    }

    public PunkLaser(PlayScreen screen, float x, float y, boolean toRight) {
        super(screen, x, y);
        velocity = new Vector2((toRight? 7f : -7f), 0);
    }

    {
        region = screen.getManager().getAtlas().findRegion("punkLaser");
        defineShell();
    }

    @Override
    protected void defineShell() {
        super.defineShell();

        //Устанавливаем размер тела
        shape.setAsBox(10 / com.stukovegor.scs.MainClass.PPM, 1.5f / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        setBounds(0, 0, 20 / com.stukovegor.scs.MainClass.PPM, 3 / MainClass.PPM);
    }

}
