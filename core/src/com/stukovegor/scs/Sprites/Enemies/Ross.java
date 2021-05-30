package com.stukovegor.scs.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Shells.RossShell;
import com.stukovegor.scs.MainClass;

import java.util.Random;

/**
 * Класс Киборга, наследника класса {@link Enemy}
 * @see Enemy
 * @author Стуков Егор
 * @version 2.3
 */

public final class Ross extends Enemy {

    //Дополнительный таймер атаки
    private float attackTimer;

    //Истино-ложное выражение стрельбы в данный момент
    private boolean shooting;

    //Счетчик, нужный для реализации стрельбы очередями
    private int shotRateCount;


    public Ross(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        shotRateCount = 0;

        velocity = new Vector2(-0.8f, 0);

        shooting = false;

    }

    @Override
    protected void defineEnemy() {

        super.defineEnemy();

        shape.setAsBox(76f /2/ com.stukovegor.scs.MainClass.PPM, 153f / (2* com.stukovegor.scs.MainClass.PPM));
        setBounds(0, 0, 96f / com.stukovegor.scs.MainClass.PPM, 153f / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        fdef.isSensor = true;
        EdgeShape frontal = new EdgeShape();
        frontal.set(-50 / com.stukovegor.scs.MainClass.PPM, -70/ com.stukovegor.scs.MainClass.PPM, -50/ com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM);

        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.FRONT_OF_ENEMY_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.OBJECT_BIT;
        fdef.shape = frontal;

        b2body.createFixture(fdef).setUserData(this);

        fdef.isSensor = true;
        EdgeShape weakPoint = new EdgeShape();
        weakPoint.set(-30/ com.stukovegor.scs.MainClass.PPM, 85/ com.stukovegor.scs.MainClass.PPM, 30 / com.stukovegor.scs.MainClass.PPM, 85/ com.stukovegor.scs.MainClass.PPM);
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.WEAK_POINT_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
        fdef.shape = weakPoint;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void createAnimations() {

        Random random = new Random();
        int randChoice = random.nextInt(2);
        if(randChoice == 0)
            region = screen.getManager().getAtlas().findRegion("RossStatesTexture");
        else
            region= screen.getManager().getAtlas().findRegion("RossStatesTexture2");

        int width = 45;
        int height = 66;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Инициализация standing
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i * width, 0, width, height));
        }
        standing = new Animation<TextureRegion>(.45f, frames);
        frames.clear();

        //Инициализация movingLeft
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i * width, height, width, height));
        }
        movingLeft = new Animation<TextureRegion>(.4f, frames);
        frames.clear();

        //Инициализация movingRight
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, width * (2 + i), height, width, height));
        }
        movingRight = new Animation<TextureRegion>(.4f, frames);
        frames.clear();

        //Инициализация shootingLeft
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i* width, 2* height, width, height));
        }
        shootingLeft = new Animation<TextureRegion>(.3f, frames);
        frames.clear();

        //Инициализация shootingRight
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, width * (2 + i), 2* height, width, height));
        }
        shootingRight = new Animation<TextureRegion>(.3f, frames);
        frames.clear();

        //Инициализация damagedAnimationLeft
        for(int i = 0; i < 1; i++){
            frames.add(new TextureRegion(region, width *2, 0, width, height));
        }
        damagedAnimationLeft = new Animation<TextureRegion>(.2f, frames);
        frames.clear();

        //Инициализация damagedAnimationRight
        for(int i = 0; i < 1; i++){
            frames.add(new TextureRegion(region, width *3, 0, width, height));
        }
        damagedAnimationRight = new Animation<TextureRegion>(.2f, frames);
        frames.clear();

        //Инициализация deadAnimation
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(region, 4* width, height *i, width, height));
        }
        deadAnimation = new Animation<TextureRegion>(.23f, frames);
        frames.clear();
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        if(!destroyed) {
            if (setToDestroy) {
                Filter filter = new Filter();
                filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
                filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                destroyed = true;
            } else if (isPlayerVisible()) {
                attackAlgorithm();
            }

            if (b2body.getPosition().x < screen.getOscar().getB2body().getPosition().x)
                turnedToRight = true;

            if (!ableToShoot) {
                shootDeltaTimer += dt;
                if (shotRateCount < 3 && shootDeltaTimer > .9f) {
                    ableToShoot = true;
                    shootDeltaTimer = 0;
                } else if (shotRateCount >= 3 && shootDeltaTimer > 3.1f) {
                    shotRateCount = 0;
                }
            }

            if (shooting) {
                attackTimer += dt;
                if (attackTimer > .6f) {
                    shooting = false;
                    attackTimer = 0;
                }
            }
        }

        setRegion(getFrame(dt));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    @Override
    public void getDamage() {
        damaged = true;
        hp -= (15 + random.nextInt(26));
        if(hp <= 0){
            setToDestroy = true;
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().rossDead, Sound.class).play(screen.getSoundVolume());
        }
    }

    @Override
    public void shoot() {
        if(ableToShoot && !destroyed){
            if(turnedToRight){
                shells.add(new RossShell(this.screen, b2body.getPosition().x + 70 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y - 10 / com.stukovegor.scs.MainClass.PPM, true));
            }
            else {
                shells.add(new RossShell(this.screen, b2body.getPosition().x - 70 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y - 10 / MainClass.PPM));
            }

            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().rossShot, Sound.class).play(screen.getSoundVolume());

            ableToShoot = false;
            shotRateCount++;
        }
        shooting = true;
        ableToShoot = false;
    }

    @Override
    public State getState() {
        if(setToDestroy)
            return State.DEAD;
        else if(damaged)
            return(turnedToRight? State.DAMAGED_RIGHT : State.DAMAGED_LEFT);
        else if(shooting)
            return(turnedToRight? State.SHOOTING_RIGHT : State.SHOOTING_LEFT);
        else if(b2body.getLinearVelocity().x < -0.2f)
            return State.MOVING_LEFT;
        else if(b2body.getLinearVelocity().x > .2f)
            return State.MOVING_RIGHT;
        else
            return State.STANDING;
    }

    private void attackAlgorithm(){
        if(ableToShoot || shooting){
            shoot();
        }
        else
            b2body.setLinearVelocity((turnedToRight? -velocity.x : velocity.x), b2body.getLinearVelocity().y);
    }

    @Override
    public void jump() {
        if(!destroyed) {
            b2body.applyLinearImpulse(new Vector2((turnedToRight? 2.5f : -2.5f), 4.5f), b2body.getWorldCenter(), true);

            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().punkJump, Sound.class).play(screen.getSoundVolume());
        }
    }

    @Override
    public void terminated() {
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().rossDead, Sound.class).play(screen.getSoundVolume());
        setToDestroy = true;
    }

    @Override
    public void collideWithPlayer() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
