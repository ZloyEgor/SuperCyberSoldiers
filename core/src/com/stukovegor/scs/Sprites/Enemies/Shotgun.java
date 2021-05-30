package com.stukovegor.scs.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Shells.ShotgunShell;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Shells.Shell;

import java.util.Random;

/**
 * Класс Дробовика, наследника класса {@link Enemy}
 * @see Enemy
 * @author Стуков Егор
 * @version 2.1
 */

public final class Shotgun extends Enemy {

    //Специальные для Дробовика флаги на проверку состояния
    private boolean warned, playerVisible;


    public Shotgun(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        id = com.stukovegor.scs.MainClass.SHOTGUN_ID;

        //Состояние шотгана
        warned = false;
        playerVisible = false;

        shootDeltaTimer = 0;

        velocity = new Vector2(1, 0);
    }

    @Override
    public void defineEnemy() {

        super.defineEnemy();

        shape.setAsBox(58f / 2 / com.stukovegor.scs.MainClass.PPM, 135f / 2 / com.stukovegor.scs.MainClass.PPM);
        setBounds(0, 0, 78 / com.stukovegor.scs.MainClass.PPM, 148 / com.stukovegor.scs.MainClass.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this );

        fdef.isSensor = true;
        EdgeShape weakPoint = new EdgeShape();
        weakPoint.set(-23/ com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM, 23 / com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM);
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.WEAK_POINT_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
        fdef.shape = weakPoint;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void createAnimations() {

        Random random = new Random();
        int randChoice = random.nextInt(2);

        if (randChoice == 0)
            region = screen.getManager().getAtlas().findRegion("ShotgunStatesTexture");
        else
            region = screen.getManager().getAtlas().findRegion("ShotgunStatesTexture2");

        int width = 30;
        int height = 57;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //Инициализация shotStanding
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, 0, width, height));
        }
        standing = new Animation<TextureRegion>(0.5f, frames);
        frames.clear();

        //Инициализация shotMovingLeft
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i * width, height, width, height));
        }
        movingLeft = new Animation<TextureRegion>(0.32f, frames);
        frames.clear();

        //Инициализация shotMovingRight
        for(int i = 2; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, height, width, height));
        }
        movingRight = new Animation<TextureRegion>(0.32f, frames);
        frames.clear();

        //Инициализфция shotShooting
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, height * 2, width, height));
        }
        shootingLeft = new Animation<TextureRegion>(.5f, frames);
        frames.clear();

        //Инициализация shotDead
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, height * 3, width, height));
        }
        deadAnimation = new Animation<TextureRegion>(.15f, frames);
        frames.clear();
    }

    @Override
    public State getState(){
        if(warned && !playerVisible)
            return State.STANDING;
        else if(b2body.getLinearVelocity().x > 0.2 && !setToDestroy)
            return State.MOVING_RIGHT;
        else if(b2body.getLinearVelocity().x < -0.2  && !setToDestroy)
            return State.MOVING_LEFT;
        else if(setToDestroy)
            return State.DEAD;
        else if(isPlayerVisible())
            return State.SHOOTING_LEFT;
        else
            return State.STANDING;
    }



    @Override
    public void getDamage(){
        hp -= (25 + random.nextInt(15));
        if(hp <= 0) {
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().shotgunDead, Sound.class).play(screen.getSoundVolume());
            setToDestroy = true;
        }else {
            if (screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().shotgunDamaged, Sound.class).play(screen.getSoundVolume());
        }


    }

    @Override
    public void update(float dt){

        super.update(dt);

        if(setToDestroy){
            if(!destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
            }
        }
        else if(isPlayerVisible()) {
            shoot();
        }

        else if(isWarned()){
            b2body.setLinearVelocity(0, 0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }

        else {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        setRegion(getFrame(dt));


        //Может ли выстрелить шотган?
        if(!ableToShoot){
            shootDeltaTimer += dt;
            if(shootDeltaTimer > 2f){
                ableToShoot = true;
                shootDeltaTimer = 0;
            }
        }


    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed || stateTimer < 1) {
            super.draw(batch);
        }
        for(Shell shell: shells) {
            shell.draw(batch);
        }
    }

    @Override
    public boolean isPlayerVisible(){
        return (this.b2body.getPosition().x - screen.getOscar().getB2body().getPosition().x <= 380 / com.stukovegor.scs.MainClass.PPM);
    }

    //Дробовик насторожен
    private boolean isWarned(){
        return (this.b2body.getPosition().x - screen.getOscar().getB2body().getPosition().x <= 420 / com.stukovegor.scs.MainClass.PPM);
    }

    public void shoot(){
        if(ableToShoot) {
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().shotgunShot, Sound.class).play(screen.getSoundVolume());
            shells.add(new ShotgunShell(this.screen, b2body.getPosition().x - 55 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 12 / MainClass.PPM));
            ableToShoot = false;
        }
    }

    @Override
    public void terminated() {
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().shotgunDead, Sound.class).play(screen.getSoundVolume());
        setToDestroy = true;
    }

    @Override
    public void jump() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void collideWithPlayer() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
