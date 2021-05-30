package com.stukovegor.scs.Shells;

import com.badlogic.gdx.math.Vector2;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Ross;

/**
 * Класс лазеров {@link Ross}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 2.0
 */

public final class RossShell extends Shell {

    public RossShell(com.stukovegor.scs.Screens.PlayScreen screen, float x, float y) {
        super(screen, x, y);
        velocity = new Vector2(-4.5f, 0);
    }

    public RossShell(PlayScreen screen, float x, float y, boolean toRight) {
        super(screen, x, y);
        velocity = new Vector2((toRight? 4.5f : -4.5f), 0);
    }

    {
        region = screen.getManager().getAtlas().findRegion("rossShell");
        defineShell();
    }

    @Override
    protected void defineShell() {
        super.defineShell();

        //Устанавливаем размер тела
        shape.setAsBox(5f / com.stukovegor.scs.MainClass.PPM, 3f / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        setBounds(0, 0, 10 / com.stukovegor.scs.MainClass.PPM, 6 / MainClass.PPM);
    }

}
