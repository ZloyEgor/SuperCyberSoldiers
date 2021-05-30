package com.stukovegor.scs.Shells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;

/**
 * Абстрактный класс всех снарядов в игре.
 * Является наследником класса {@link Sprite}
 * @author Стуков Егор
 * @version 2.1
 */
public abstract class Shell extends Sprite {

    /** Мир представляет из себя контейнер, в котором происходят все "физические" взаимодейтсвия тел*/
    World world;

    /** Игровой экран*/
    protected PlayScreen screen;

    /** Тело снаряда, являющееся объектом класса {@link Body}*/
    protected Body b2body;

    /**
     * Объект класса {@link FixtureDef}, определяющий свойства {@link Shell#b2body},
     * такие как масса, трение, упругость
     */
    FixtureDef fdef;

    /**
     * Объект класса {@link PolygonShape}, определяющий фигуру тела {@link Shell#b2body}
     */
    PolygonShape shape;

    /**
     * Объект класса {@link Vector2}, определяющий скорость и направление движения снаряда
     */
    protected Vector2 velocity;

    /**
     * Объект класса {@link Texture}, определяющий текстуру снаряда
     */
    protected TextureRegion region;

    /**
     * Истино-ложные выражения
     * setToDestroy - "приговорен к уничтожению"
     * destroyed - "уничтожен"
     */
    boolean destroyed, setToDestroy;

    /**
     * Конструктор - объявление игрового экрана и мира
     * @param screen - игровой экран, в мир которого будет помещено тело {@link Shell#b2body}
     * @param x - координата создания по оси x
     * @param y - координата создания по оси y
     */
    public Shell(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setToDestroy = false;
        destroyed = false;
        setPosition(x, y);
    }

    /**
     * Метод, непосредственно создающий тело {@link Shell#b2body},
     * и задающий для тела общие характеристики каждого снаряда в игре
     */
    protected void defineShell(){

        /*
         * Объект класса BodyDef, позволяющий определить
         * тип тела b2body и его положение в мире world
         */
        BodyDef bdef = new BodyDef();

        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = .0f;

        b2body = world.createBody(bdef);
        fdef = new FixtureDef();

        //Определение категории тела снаряда
        fdef.filter.categoryBits = MainClass.LASER_OF_ENEMY_BIT;

        //Определение категорий тел, с которыми сможет контактировать снаряд
        fdef.filter.maskBits =  MainClass.OBJECT_BIT |
                MainClass.PLAYER_BIT |
                MainClass.LASER_OF_PLAYER_BIT;

        fdef.isSensor = true;
        shape = new PolygonShape();
        setRegion(region);
    }

    /** Метод, обновляющий снаряд в игре */
    public  void update(){

        //Если тело "приговорено к уничтожению", но не уничтожено, мы уничтожаем тело
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(!destroyed){

            //Если снаряд вылетел за пределы игровой камеры, то мы его "готовим к уничтожению"
            if (b2body.getPosition().x > screen.getGameCamera().position.x + screen.getGameCamera().viewportWidth / 2
                    || b2body.getPosition().x < screen.getGameCamera().position.x - screen.getGameCamera().viewportWidth / 2)
                setToDestroy = true;

            b2body.setLinearVelocity(velocity);
            setRegion(region);
            setPosition(b2body.getPosition().x - getWidth() / 2,
                    b2body.getPosition().y - getHeight() / 2);
        }
    }

    /**
     * Метод, отрисовывающий снаряд в игре
     * @param batch - отрисовщик
     */

    public void draw(Batch batch) {
        //Если снаряд не уничтожен, мы его отрисовываем
        if(!destroyed)
            super.draw(batch);
    }

    public void collideWithTarget(){
        /* При контакте с целью снаряд "готовим к уничтожению"
         * При попытке уничтожения тела в тот момент, когда произошло столкновение,
         * обязательно произойдет крах программы - именно поэтому мы никогда не
         * уничтожаем тела сразу в момент их столкновения
         */
        setToDestroy = true;
    }

    public boolean isSetToDestroy(){
        return setToDestroy;
    }

    public boolean isDestroyed(){
        return destroyed;
    }




}
