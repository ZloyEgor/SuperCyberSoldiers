package com.stukovegor.scs.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Shells.Grenade;

import java.util.Random;

/**
 * Класс Экспло, наследника класса {@link com.stukovegor.scs.Sprites.Enemies.Enemy}
 * @see com.stukovegor.scs.Sprites.Enemies.Enemy
 * @author Стуков Егор
 * @version 1.1
 */

public final class Explo extends com.stukovegor.scs.Sprites.Enemies.Enemy {

    /** Дополнительный истино-ложные выражения атаки в данный момент и необходимости остановиться(на краю крыши) */
    private boolean attacking, timeToStop;

    /** Дополнительный таймер атаки */
    private float attackTimer;

    /**
     * Вместо стандартного массива снарядов {@link Enemy},
     * Эскпло использует массив гранат {@link com.stukovegor.scs.Shells.Grenade}
     */
    private Array<com.stukovegor.scs.Shells.Grenade> grenades;

    public Explo(PlayScreen screen, float x, float y){
        super(screen, x, y);
        velocity = new Vector2(-1.8f, 0);
        grenades = new Array<com.stukovegor.scs.Shells.Grenade>();
    }

    @Override
    protected void defineEnemy() {
        super.defineEnemy();

        shape.setAsBox(38 / com.stukovegor.scs.MainClass.PPM, 73 / com.stukovegor.scs.MainClass.PPM);
        setBounds(0, 18 / com.stukovegor.scs.MainClass.PPM, 94f / com.stukovegor.scs.MainClass.PPM, 156f  / com.stukovegor.scs.MainClass.PPM);

        fdef.restitution = 0;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void createAnimations() {

        Random random = new Random();
        int randChoice = random.nextInt(2);

        if(randChoice == 0)
            region = screen.getManager().getAtlas().findRegion("ExploStatesTexture");
        else
            region = screen.getManager().getAtlas().findRegion("ExploStatesTexture2");

        int width = 36;
        int height = 57;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Инициализация standing
        frames.add(new TextureRegion(region, 0, 0, width, height));

        standing = new Animation<TextureRegion>(1, frames);
        frames.clear();

        //Инициализация movingLeft
        for(int i = 2; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, 2 * height, width, height));
        }
        movingLeft = new Animation<TextureRegion>(.4f, frames);
        frames.clear();

        //Инициализация movingRight
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i * width, 3 * height, width, height));
        }
        movingRight = new Animation<TextureRegion>(.4f, frames);
        frames.clear();

        //Инициализация shootingLeft
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(region, i*width, 0, width, height));
        }
        shootingLeft = new Animation<TextureRegion>(.55f, frames);
        frames.clear();

        //Инициализация shootingRight
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(region, i*width, height, width, height));
        }
        shootingRight = new Animation<TextureRegion>(.55f, frames);
        frames.clear();

        //Инициализация damagedAnimationLeft
        frames.add(new TextureRegion(region, 0, 2*height, width, height));
        damagedAnimationLeft = new Animation<TextureRegion>(.25f, frames);
        frames.clear();

        //Инициализация damagedAnimationRight
        frames.add(new TextureRegion(region, width, 2*height, width, height));
        damagedAnimationRight = new Animation<TextureRegion>(.25f, frames);
        frames.clear();

        //Инициализация deadAnimation
        for(int i = 2; i < 4; i++) {
            frames.add(new TextureRegion(region, i*width, 3*height, width, height));
        }
        deadAnimation = new Animation<TextureRegion>(.23f, frames);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if(!destroyed){
            if(setToDestroy) {
                Filter filter = new Filter();
                filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
                filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                destroyed = true;
                stateTimer = 0;
            } else if(isPlayerVisible())
                attackAlgorithm();
        }

        if (b2body.getPosition().x < screen.getOscar().getB2body().getPosition().x)
            turnedToRight = true;

        if(!ableToShoot){
            shootDeltaTimer += dt;
            if(shootDeltaTimer > 1.65f){
                ableToShoot = true;
                shootDeltaTimer = 0;

            }
        }

        if(attacking){
            attackTimer += dt;
            if(attackTimer > 2.8){
                attacking = false;
                attackTimer = 0;
            }
        }

        for(com.stukovegor.scs.Shells.Grenade grenade : grenades){
            grenade.update(dt);
        }

        setRegion(getFrame(dt));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);


    }

    private void attackAlgorithm(){
        if(ableToShoot || attacking)
            shoot();
        else if(b2body.getPosition().x - getWidth() / 2 > screen.getOscar().getB2body().getPosition().x && !timeToStop)
            b2body.setLinearVelocity((turnedToRight? velocity.x : -velocity.x), b2body.getLinearVelocity().y);
    }

    @Override
    public void collideWithPlayer() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void getDamage() {

        damaged = true;
        hp -= (40 + random.nextInt(20));

        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().punkDamaged, Sound.class).play(screen.getSoundVolume());

        if(hp <= 0) {
            setToDestroy = true;
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().exploDead, Sound.class).play(screen.getSoundVolume());
        }
    }

    @Override
    public void shoot() {
        if(ableToShoot && !destroyed){
            grenades.add(new com.stukovegor.scs.Shells.Grenade(screen, b2body.getPosition().x + 55 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y - 10 / MainClass.PPM));
        }
        attacking = true;
        ableToShoot = false;
    }

    //Достиг ли Экспло края крыши?
    public void reachedBorder(){
        timeToStop = true;
    }

    @Override
    public void terminated() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public State getState() {
        if(setToDestroy)
            return State.DEAD;
        else if(damaged)
            return(turnedToRight? State.DAMAGED_RIGHT : State.DAMAGED_LEFT);
        else if(attacking)
            return(turnedToRight? State.SHOOTING_RIGHT : State.SHOOTING_LEFT);
        else if(b2body.getLinearVelocity().x > .2)
            return State.MOVING_RIGHT;
        else if(b2body.getLinearVelocity().x < -.2)
            return State.MOVING_LEFT;
        else
            return State.STANDING;
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed || stateTimer < .46) {
            super.draw(batch);
        }

        for(Grenade grenade: grenades){
            grenade.draw(batch);
        }
    }

    @Override
    public void jump() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
