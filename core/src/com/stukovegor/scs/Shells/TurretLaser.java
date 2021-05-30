package com.stukovegor.scs.Shells;

import com.badlogic.gdx.math.Vector2;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Turret;

/**
 * Класс лазеров {@link Turret}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 2.0
 */
public final class TurretLaser extends Shell{


    public TurretLaser(PlayScreen screen, float x, float y, boolean toRight){
        super(screen, x, y);
        velocity = new Vector2((toRight? 6.4f : -6.4f), 0);
        region = screen.getManager().getAtlas().findRegion("turretLaser");
        defineShell();
    }

    @Override
    protected void defineShell() {
        super.defineShell();

        //Устанавливаем размер тела
        shape.setAsBox(12 / com.stukovegor.scs.MainClass.PPM, 2 / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        setBounds(0, 0, 24 / com.stukovegor.scs.MainClass.PPM, 4 / MainClass.PPM);
    }
}
