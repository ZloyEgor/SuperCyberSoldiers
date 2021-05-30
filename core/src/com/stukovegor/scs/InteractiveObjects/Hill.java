package com.stukovegor.scs.InteractiveObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.MainClass;

/**
 * Класс аптечки, наследуемый от абстрактного класса {@link InteractiveObject}
 * @see InteractiveObject
 * @author Стуков Егор
 * @version 1.3
 */

public final class Hill extends InteractiveObject {

    /**
     * Конструктор - создание новой аптечки
     * @param screen - игровой экран, в мир которого будет помещена аптечка
     * @param x - координата создания тела аптечки {@link InteractiveObject#body} по оси x
     * @param y - координата создания тела аптечки {@link InteractiveObject#body} по оси y
     */
    public Hill(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        //Определение категории тела аптечки
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.HILL_BIT;

        //Определение категорий, с которыми сможет контактировать аптечка
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;

        fdef.isSensor = true;
        setBounds(0, 0, 24 / com.stukovegor.scs.MainClass.PPM, 22 / com.stukovegor.scs.MainClass.PPM);
        shape.setAsBox(12f / com.stukovegor.scs.MainClass.PPM, 11f / MainClass.PPM);
        fdef.shape = shape;

        //Определение текстуры
        setRegion(screen.getManager().getAtlas().findRegion("hillTexture"));

        //Определение позиции текстуры
        setPosition(x - getWidth() / 2, y - getHeight() / 2);

        //Окончательное создание тела аптечки
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(SpriteBatch batch) {

        //Если аптечка не уничтожена, то мы её отрисовываем
        if(!destroyed)
            super.draw(batch);
    }
}
