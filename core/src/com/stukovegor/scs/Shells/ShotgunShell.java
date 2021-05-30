package com.stukovegor.scs.Shells;

import com.badlogic.gdx.math.Vector2;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Shotgun;

/**
 * Класс лазеров {@link Shotgun}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 2.0
 */

public final class ShotgunShell extends Shell {

    public ShotgunShell(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        velocity = new Vector2(-5.3f, 0);
    }

    {
        region = screen.getManager().getAtlas().findRegion("slug");
        defineShell();
    }

    @Override
    protected void defineShell() {
        super.defineShell();

        //Устанавливаем размер тела
        shape.setAsBox(8 / com.stukovegor.scs.MainClass.PPM, 3 / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        setBounds(0, 0, 16 / com.stukovegor.scs.MainClass.PPM, 6 / MainClass.PPM);

    }
}
