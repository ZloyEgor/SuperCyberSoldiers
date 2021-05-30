package com.stukovegor.scs.InteractiveObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.stukovegor.scs.Screens.PlayScreen;

/**
 * Абстрактный класс, от которого наследуются все интерактивны объекты на карте,
 * например аптечки {@link Hill} и зоны диалогов {@link DialogueZone}
 * @author Стуков Егор
 * @version 1.4
 */

public abstract class InteractiveObject extends Sprite {

    /** Мир представляет из себя контейнер, в котором происходят все "физические" взаимодейтсвия*/
    private World world;

    /** Игровой экран*/
    protected PlayScreen screen;

    /** Тело интерактивного объекта, являющееся объектом класса {@link Body}*/
    Body body;

    /**
     * Объект класса {@link FixtureDef}, определяющий свойства {@link InteractiveObject#body},
     * такие как масса, трение, упругость
     */
    protected FixtureDef fdef = new FixtureDef();

    /**
     * Объект класса {@link PolygonShape}, определяющий фигуру тела {@link InteractiveObject#body}
     */
    protected PolygonShape shape = new PolygonShape();

    /**
     * Истино-ложные выражения
     * setToDestroy - "приговорен к уничтожению"
     * destroyed - "уничтожен"
     */
    private boolean setToDestroy;
    protected boolean destroyed;

    /**
     * Конструктор - создание нового интерактивного объекта
     * @param screen - игровой экран, в мир которого будет помещено тело {@link InteractiveObject#body}
     * @param x - координата создания тела {@link InteractiveObject#body} по оси x
     * @param y - координата создания тела {@link InteractiveObject#body} по оси y
     */
    InteractiveObject(PlayScreen screen, float x, float y){
        this.screen = screen;
        world = screen.getWorld();

        /* Объект класса BodyDef, позволяющий определить
           тип тела b2body и его положение в мире */

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);
        body = world.createBody(bdef);
    }

    /**
     * Метод обновления тела {@link InteractiveObject#body} в мире {@link InteractiveObject#world}
     */
    public void update(){

        //Если тело "приговорено к уничтожению", но не уничтожено, мы уничтожаем тело
        if(setToDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    /**
     * Метод, позволяющий отрисовывать интерактивные объекты
     * @param batch - отрисовщик
     */
    public void draw(SpriteBatch batch){
        super.draw(batch);
    }

    /**
     * Метод, вызываемый при соприкосновении интерактивного объекта с игроком
     */
    public void collideWithPlayer(){
        setToDestroy = true;
    }

    public Body getBody(){
        return body;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

}
