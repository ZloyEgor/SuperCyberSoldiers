package com.stukovegor.scs.InteractiveObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.MainClass;

/**
 * Класс зоны диалога, наследуемый от абстрактного класса {@link InteractiveObject}
 * @see InteractiveObject
 * @see Dialogue
 * @author Стуков Егор
 * @version 1.1
 */

public final class DialogueZone extends InteractiveObject {

    /** Объект класса {@link Dialogue}, который содержит в себе зона диалога*/
    private Dialogue dialogue;

    /**
     * Конструктор - создание объекта класса {@link DialogueZone}
     * @param screen - игровой экран, который будет содержать в себе зону диалога
     * @param x - координата создания тела зоны диалога {@link InteractiveObject#body} по оси x
     * @param y - координата создания тела зоны диалога {@link InteractiveObject#body} по оси y
     * @param id - идентификатор(id), по которому из screen берётся диалог с помощью метода {@link PlayScreen#getDialogue(int)}
     */
    public DialogueZone(PlayScreen screen, float x, float y, int id){
        super(screen, x, y);

        dialogue = screen.getDialogue(id);

        //Определение категории тела зоны диалога
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.MESSAGE_BIT;

        //Определение категорий, с которыми сможет контактировать зона диалога
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;

        fdef.isSensor = true;
        shape.setAsBox(120 / com.stukovegor.scs.MainClass.PPM, 120 / MainClass.PPM);
        fdef.shape = shape;

        //Окончательное создание тела зоны диалога
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //Здесь отрисовывать ничего не надо, оставляем метод пустым
    }

    public Dialogue getDialogue() {
        return dialogue;
    }
}
