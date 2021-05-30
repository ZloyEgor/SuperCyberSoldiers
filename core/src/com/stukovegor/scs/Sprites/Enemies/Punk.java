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
import com.stukovegor.scs.Shells.PunkLaser;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Sprites.Oscar;

import java.util.Random;

/**
 * Класс Панка, наследника класса {@link com.stukovegor.scs.Sprites.Enemies.Enemy}
 * @see com.stukovegor.scs.Sprites.Enemies.Enemy
 * @author Стуков Егор
 * @version 2.2
 */

public final class Punk extends Enemy {

    /** Дополнительный таймер атаки */
    private float attackTimer;

    /** Истино-ложные выражения прыжка и стрельбы в данный момент */
    private boolean jumping, shooting;


    public Punk(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        id = com.stukovegor.scs.MainClass.PUNK_ID;
        velocity = new Vector2(-1.5f, 0);
    }

    @Override
    protected void defineEnemy(){

        super.defineEnemy();

        shape.setAsBox(75f / 2 / com.stukovegor.scs.MainClass.PPM, 155f / 2 / com.stukovegor.scs.MainClass.PPM);
        setBounds(0, 0, 83 / com.stukovegor.scs.MainClass.PPM, 155 / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        fdef.isSensor = true;
        EdgeShape frontal = new EdgeShape();
        frontal.set(-50 / com.stukovegor.scs.MainClass.PPM, -70/ com.stukovegor.scs.MainClass.PPM, -50/ com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM);

        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.FRONT_OF_ENEMY_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.OBJECT_BIT;
        fdef.shape = frontal;

        b2body.createFixture(fdef).setUserData(this);

        frontal.set(50 / com.stukovegor.scs.MainClass.PPM, -70/ com.stukovegor.scs.MainClass.PPM, 50/ com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM);
        b2body.createFixture(fdef).setUserData(this);

        fdef.isSensor = true;
        EdgeShape weakPoint = new EdgeShape();
        weakPoint.set(-35/ com.stukovegor.scs.MainClass.PPM, 85/ com.stukovegor.scs.MainClass.PPM, 35 / com.stukovegor.scs.MainClass.PPM, 85/ com.stukovegor.scs.MainClass.PPM);
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.WEAK_POINT_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
        fdef.shape = weakPoint;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void createAnimations() {

        Random random = new Random();
        int randChoice = random.nextInt(3);

        //Текстурпак панка
        if(randChoice == 0)
            region = screen.getManager().getAtlas().findRegion("PunkStatesTexture");
        else if(randChoice == 1)
            region = screen.getManager().getAtlas().findRegion("PunkStatesTexture2");
        else
            region = screen.getManager().getAtlas().findRegion("PunkStatesTexture3");
        int width = 43;
        int height = 63;

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
        shootingLeft = new Animation<TextureRegion>(.5f, frames);
        frames.clear();

        //Инициализация shootingRight
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, width * (2 + i), 2* height, width, height));
        }
        shootingRight = new Animation<TextureRegion>(.5f, frames);
        frames.clear();

        //Инициализация damagedAnimationLeft
        for(int i = 0; i <2; i++){
            frames.add(new TextureRegion(region, width *(2+i), 0, width, height));
        }
        damagedAnimationLeft = new Animation<TextureRegion>(.2f, frames);
        frames.clear();

        //Инициализация deadAnimation
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(region, 4* width, 54*i, 65, 54));
        }
        deadAnimation = new Animation<TextureRegion>(.18f, frames);
        frames.clear();
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        setRegion(getFrame(dt));

        if (!destroyed) {
            //Обновление самого панка
            if (setToDestroy) {
                Filter filter = new Filter();
                filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
                filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                destroyed = true;
                stateTimer = 0;
            } else if (jumping) {
                b2body.setLinearVelocity(-0.3f, b2body.getLinearVelocity().y);
            } else if ((isReachedBattlePosition() || shooting)) {
                b2body.setLinearVelocity(0, 0);
            } else if (isPlayerVisible()) {
                b2body.setLinearVelocity((turnedToRight? 1.4f : -1.4f), b2body.getLinearVelocity().y);
                ableToShoot = false;
            }

            if(shooting || isReachedBattlePosition())
                shoot();
            
            //Обновление состояний флагов
            if (!ableToShoot) {
                shootDeltaTimer += dt;
                if (shootDeltaTimer > 1) {
                    ableToShoot = true;
                    shootDeltaTimer = 0;
                }
            }

            if (shooting) {
                attackTimer += dt;
                if (attackTimer > 2) {
                    shooting = false;
                    attackTimer = 0;
                }
            }

            if(!destroyed) {
                if (b2body.getLinearVelocity().y <= 0 && jumping)
                    jumping = false;
            }
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        }else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - (getHeight() / 2 + 18 / com.stukovegor.scs.MainClass.PPM));
    }



    @Override
    public void getDamage() {
        hp -= (25 + random.nextInt(15));
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().punkDamaged, Sound.class).play(screen.getSoundVolume());
        damaged = true;
        if(hp <= 0){
            setToDestroy = true;
            setBounds(0, 0, 95 / com.stukovegor.scs.MainClass.PPM, 130 / com.stukovegor.scs.MainClass.PPM);
        }
    }

    @Override
    public State getState() {

        if(setToDestroy)
            return State.DEAD;
        else if(damaged)
            return State.DAMAGED_LEFT;
        else if(isReachedBattlePosition() || shooting)
            return(turnedToRight? State.SHOOTING_RIGHT : State.SHOOTING_LEFT);
        else if(isPlayerVisible() && !isReachedBattlePosition() || b2body.getLinearVelocity().x < -0.2)
            return (turnedToRight? State.MOVING_RIGHT : State.MOVING_LEFT);
        else if(b2body.getLinearVelocity().x > .2f)
            return State.MOVING_RIGHT;
        else
            return State.STANDING;

    }

    @Override
    public void shoot() {
        shooting = true;
        if(ableToShoot && !destroyed) {
            if(turnedToRight)
                shells.add(new PunkLaser(this.screen, b2body.getPosition().x + 70 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 45 / com.stukovegor.scs.MainClass.PPM, true));

            else
                shells.add(new PunkLaser(this.screen, b2body.getPosition().x - 70 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 45 / com.stukovegor.scs.MainClass.PPM));

            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().punkShot, Sound.class).play(screen.getSoundVolume());
            ableToShoot = false;
        }
    }

    private boolean isReachedBattlePosition(){

        if(!destroyed && screen.getOscar().getCurrentState() != com.stukovegor.scs.Sprites.Oscar.State.DEAD) {
            return((this.b2body.getPosition().x - getWidth() / 2) - (screen.getOscar().getB2body().getPosition().x - screen.getOscar().getWidth())<= 350 / com.stukovegor.scs.MainClass.PPM
                    && ((this.b2body.getPosition().y - getHeight()) - (screen.getOscar().getB2body().getPosition().y - screen.getOscar().getHeight()) <= 20 / com.stukovegor.scs.MainClass.PPM && (this.b2body.getPosition().y - getHeight()) - (screen.getOscar().getB2body().getPosition().y - screen.getOscar().getHeight()) >= -20 / MainClass.PPM) && b2body.getLinearVelocity().y == 0
                    && screen.getOscar().getCurrentState() != Oscar.State.DEAD);
        }
        else return false;
    }

    @Override
    public void jump(){
        if(!destroyed) {
            b2body.applyLinearImpulse(new Vector2((turnedToRight? .5f : -0.5f), 4.5f), b2body.getWorldCenter(), true);

            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().punkJump, Sound.class).play(screen.getSoundVolume());
            jumping = true;
        }
    }

    @Override
    public void terminated() {
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().punkDamaged, Sound.class).play(screen.getSoundVolume());
        setToDestroy = true;
    }

    @Override
    public void collideWithPlayer() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}

