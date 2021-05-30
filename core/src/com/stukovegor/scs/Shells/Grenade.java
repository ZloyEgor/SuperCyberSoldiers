package com.stukovegor.scs.Shells;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Explo;

/**
 * Класс гранат {@link Explo}, являющийся наследником класса {@link Shell}
 * @see Shell
 * @author Стуков Егор
 * @version 1.0
 */

public final class Grenade extends Shell {

    /** Дополнительные истино-ложные выражения */
    private boolean timeToExplode, exploded;

    /** Таймер продолжительности взрыва */
    private float explosionDurationTimer;

    public Grenade(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        velocity = new Vector2(0, 0);
        region = screen.getManager().getAtlas().findRegion("grenade");
        defineShell();
    }

    /** В отличие от других снарядов, имеющих постоянную скорость,
     *  граната лишь получает силовой импульс и летит по дуге*/
    @Override
    protected void defineShell() {

        BodyDef bdef = new BodyDef();

        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 1f;

        b2body = world.createBody(bdef);
        fdef = new FixtureDef();

        //Определение категории тела снаряда
        fdef.filter.categoryBits = MainClass.LASER_OF_ENEMY_BIT;

        //Определение категорий тел, с которыми сможет контактировать снаряд
        fdef.filter.maskBits =  MainClass.OBJECT_BIT |
                                MainClass.GROUND_BIT |
                                MainClass.PLAYER_BIT;

        fdef.isSensor = false;
        shape = new PolygonShape();

        shape.setAsBox(4 / MainClass.PPM, 8 / MainClass.PPM);
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
        b2body.applyLinearImpulse(new Vector2(-5, 4.2f), b2body.getWorldCenter(), true);

        setBounds(0, 0, 8 / MainClass.PPM, 16 / MainClass.PPM);
        setRegion(new TextureRegion(region, 0, 0, 3, 6));
    }

    /** Метод, отвечающий за взрыв гранаты */
    private void explode(){

        if(!exploded){
            exploded = true;

            //Получаем текущее положение гранаты в мире...
            Vector2 currentPosition = b2body.getPosition();
            //...и уничтожаем имеющееся тело
            world.destroyBody(b2body);

            //Создаём новое тело
            BodyDef bdef = new BodyDef();
            bdef.position.set(currentPosition);
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.gravityScale = 0;
            b2body = world.createBody(bdef);

            //Устанавливаем текстуру
            setBounds(0, 0, 68 / MainClass.PPM, 74 / MainClass.PPM);
            setRegion(new TextureRegion(region, 4, 0, 34, 37));
            setPosition(b2body.getPosition().x, b2body.getPosition().y);

            FixtureDef fdef= new FixtureDef();
            fdef.isSensor= true;

            CircleShape shape = new CircleShape();
            shape.setRadius(17f / MainClass.PPM);
            fdef.shape = shape;

            //Определяем категорию взрыва и категорий, с которыми взрыв может взаимодействовать
            fdef.filter.categoryBits = MainClass.LASER_OF_ENEMY_BIT;

            fdef.filter.maskBits =  MainClass.PLAYER_BIT |
                                    MainClass.OBJECT_BIT;

            //Звуковое сопровождение взрыва
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().grenadeExplosion, Sound.class).play(screen.getSoundVolume());
        }
    }

    /**
     * Метод, отвечающий за обновление состояния гранаты
     * @param dt - изменение времени
     */
    public void update(float dt) {

        //Если граната "приговорена к уничтожению" и не уничтожена, мы её уничтожаем
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

        //Если граната уже должна взорваться но пока не взорвалась, мы её взрываем
        if(timeToExplode && !exploded){
            explode();
        }

        //Взрыв может длиться только пока explosionDurationTimer не достигнет 0.3 секунд, после этого мы приговариваем снаряд к уничтожению
        if(exploded && !destroyed){
            explosionDurationTimer += dt;
        }

        if(explosionDurationTimer > .3f)
            setToDestroy = true;

        //Пока снаряд не уничтожен(полностью), мы ставим текстуру на его тело
        if(!setToDestroy){
            setPosition(b2body.getPosition().x - getWidth() / 2,
                    b2body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
    public void collideWithTarget() {
        timeToExplode = true;
    }
}
