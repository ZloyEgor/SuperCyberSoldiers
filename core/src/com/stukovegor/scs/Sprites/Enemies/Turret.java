package com.stukovegor.scs.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Shells.TurretLaser;
import com.stukovegor.scs.MainClass;

import java.util.Random;

/**
 * Класс Турели, наследника класса {@link Enemy}
 * @see Enemy
 * @author Стуков Егор
 * @version 2.1
 */

public final class Turret extends Enemy {

    public Turret(PlayScreen screen, float x, float y){
        super(screen, x, y);
    }

    @Override
    protected void defineEnemy() {

        super.defineEnemy();

        shape.setAsBox(65 / com.stukovegor.scs.MainClass.PPM, 60 / com.stukovegor.scs.MainClass.PPM);
        setBounds(0, 0, 152 / com.stukovegor.scs.MainClass.PPM, 120 / com.stukovegor.scs.MainClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        fdef.isSensor = true;
        EdgeShape weakPoint = new EdgeShape();
        weakPoint.set(-50/ com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM, 50 / com.stukovegor.scs.MainClass.PPM, 70/ com.stukovegor.scs.MainClass.PPM);
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.WEAK_POINT_BIT;
        fdef.filter.maskBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
        fdef.shape = weakPoint;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void createAnimations() {

        int width = 38;
        int height = 25;

        Random random = new Random();
        int randChoice = random.nextInt(2);

        if(randChoice == 0)
            region = screen.getManager().getAtlas().findRegion("TurretStatesTexture");
        else
            region = screen.getManager().getAtlas().findRegion("TurretStatesTexture2");

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Инициализация shootingLeft
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i*width, 0, width, height));
        }
        shootingLeft = new Animation<TextureRegion>(.9f, frames);
        frames.clear();

        //shootingRight
        for(int i = 2; i < 4; i++){
            frames.add(new TextureRegion(region, i*width, 0, width, height));
        }
        shootingRight = new Animation<TextureRegion>(.9f, frames);
        frames.clear();

        //standing
        frames.add(new TextureRegion(region, 0, 0, width, height));
        standing = new Animation<TextureRegion>(1, frames);
        frames.clear();

        //damagedLeft
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(region, i * width, 2 * height, width, height));
        }
        damagedAnimationLeft = new Animation<TextureRegion>(.15f, frames);
        frames.clear();

        //damagedRight
        for(int i = 2; i < 4; i++){
            frames.add(new TextureRegion(region, i * width, 2 * height, width, height));
        }
        damagedAnimationRight = new Animation<TextureRegion>(.2f, frames);
        frames.clear();

        //deadAnimation
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(region, i * width, height, width, height));
        }
        deadAnimation = new Animation<TextureRegion>(.25f, frames);
        frames.clear();
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        if(!destroyed){
            if(setToDestroy){
                Filter filter = new Filter();
                filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
                filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
                for(Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                destroyed = true;
            }
            if(isPlayerVisible()){
                shoot();
            }
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(!ableToShoot) {
            shootDeltaTimer += dt;
            if (shootDeltaTimer > 1.8) {
                ableToShoot = true;
                shootDeltaTimer = 0;
            }
        }

    }

    @Override
    public void getDamage() {
        hp -= (15 + random.nextInt(20));
        if(screen.isSoundEnabled()){
            screen.getManager().manager.get(screen.getManager().turretDamaged, Sound.class).play(screen.getSoundVolume());
        }
        damaged = true;
        if(hp <= 0){
            setToDestroy = true;
            if(screen.isSoundEnabled()){
                screen.getManager().manager.get(screen.getManager().turretDead, Sound.class).play(screen.getSoundVolume());
            }
        }
    }

    @Override
    public void shoot() {
        if(ableToShoot && !destroyed){
            if(turnedToRight)
                shells.add(new TurretLaser(this.screen, b2body.getPosition().x + 70 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 45 / com.stukovegor.scs.MainClass.PPM, true));
            else
                shells.add(new TurretLaser(this.screen, b2body.getPosition().x - 78 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 52 / MainClass.PPM, false));
            if(screen.isSoundEnabled()){
                screen.getManager().manager.get(screen.getManager().turretShot, Sound.class).play(screen.getSoundVolume());
            }
            ableToShoot = false;
        }
    }

    @Override
    public State getState() {
        if(setToDestroy)
            return State.DEAD;
        else if(damaged)
            return(turnedToRight ? State.DAMAGED_RIGHT : State.DAMAGED_LEFT);
        else if(isPlayerVisible())
            return(turnedToRight ? State.SHOOTING_RIGHT : State.SHOOTING_LEFT);
        else
            return State.STANDING;
    }

    @Override
    public void terminated() {
        setToDestroy = true;
        if(screen.isSoundEnabled()){
            screen.getManager().manager.get(screen.getManager().turretDead, Sound.class).play(screen.getSoundVolume());
        }
    }

    @Override
    public void collideWithPlayer() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void jump() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
