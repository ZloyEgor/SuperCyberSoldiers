package com.stukovegor.scs.Sprites;

import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Shells.PlayerLaser;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Shells.Shell;

import java.util.Random;

/**
 * Класс игрового персонажа
 * @author Стуков Егор
 * @version 1.8
 */

public final class Oscar extends Sprite {

    /** Мир, в который будет помещён игровой персонаж */
    private World world;

    /** Тело игрового персонажа */
    private Body b2body;

    /** Игровой экран */
    private PlayScreen screen;

    /** Очки здоровья */
    private int hp;

    private Random rand = new Random();

    /** Перечисление возвожных состояний игрового персонажа */
    public enum State{  STANDING_LEFT, STANDING_RIGHT,  //Статичное состояние
                        MOVING_LEFT, MOVING_RIGHT,      //Передвижение
                        JUMPING_LEFT, JUMPING_RIGHT,    //Положение прыжка
                        SHOOTING_LEFT, SHOOTING_RIGHT,  //Положение стрельбы
                        SITTING_LEFT, SITTING_RIGHT,    //Положение приседа
                        DAMAGED_LEFT, DAMAGED_RIGHT,    //Получен урон
                        LEVEL_FINISHED, DEAD}           //Уровень пройден, мёртв

    /** Предыдущее и текущее состояние игрового персонажа */
    private State previousState, currentState;

    /** Перечисление возможных статусов игрока на уровне */
    public enum Status{
                        WEAK,       //Слабость - используется на прологе, особая текстура, оружие не доступно
                        COMBAT,     //Боевой статус - используется на большинстве уровней
                        NEUTRAL,    //Нейтральный - оружие не используется на уровне
                        IMMORTAL    //Режим бессмертия - для туториала
    }

    /** Таймеры состояния, полученного урона */
    private float stateTimer, damagedTimer;

    /** Истино-ложные выражения */
    private boolean turnedToRight,  //Повёрнут направо
            shootingFlag,           //Сейчас стреляет
            timeToSitDown,          //Время присесть!
            timeToGetUp,            //Время встать!
            weaponEnable,           //Оружие на уровне разрешено
            ableToShot,             //Способен выстрелить в данный момент
            levelFinished,          //Уровень пройден
            readTime,               //Время прочесть диалог!
            readFlag,               //Читает в данный момент
            sitting,                //Сейчас сидит
            damaged,                //Сейчас ранен
            dead,                   //Мёртв
            immortal;             //Бессмертен


    /**
     * Анимация
     * @see Animation
     */
    private Animation<TextureRegion> oscarStandingLeft;
    private Animation<TextureRegion> oscarStandingRight;
    private Animation<TextureRegion> oscarMovingLeft;
    private Animation<TextureRegion> oscarMovingRight;
    private Animation<TextureRegion> oscarDamagedLeft;
    private Animation<TextureRegion> oscarDamagedRight;
    private Animation<TextureRegion> oscarJumpingLeft;
    private Animation<TextureRegion> oscarJumpingRight;
    private Animation<TextureRegion> oscarShootingLeft;
    private Animation<TextureRegion> oscarShootingRight;
    private Animation<TextureRegion> oscarDead;
    private TextureRegion oscarSittingLeft, oscarSittingRight;

    /** Массив лазеров */
    private Array<PlayerLaser> lasers;

    /** Таймер темпа стерльбы */
    private float fireRateTimer;

    /** Таймер проигрывания анимации стрельбы */
    private float shootAnimationTimer;

    private TextureRegion region;

    /**
     * Основной конструктор
     * @param screen - игровой экран
     * @param x - координата создания по оси X
     * @param y - координата создания по оси Y
     * @param startHp - начальные очки здоровья
     */
    public Oscar(PlayScreen screen, float x, float y, int startHp) {
        super();
        region = screen.getManager().getAtlas().findRegion("RexStatesTextures");
        //Для справки - когда разработка игры только началась, кодовое название класса игрока было "Rex",
        //поэтому я решил оставить в названиях текстур это имя
        this.screen = screen;
        this.hp = startHp;
        this.world = screen.getWorld();
        this.weaponEnable = true;
        defineOscar(x, y);
        createAnimation();
    }

    /**
     * Дополнительный конструктор
     * @param screen - игровой экран
     * @param x - координата создания по оси X
     * @param y - координата создания по оси Y
     * @param startHp - начальные очки здоровья
     * @param status - статус игрового персонажа на уровне
     * @see Status
     */
    public Oscar(PlayScreen screen, float x, float y, int startHp, Status status) {
        super();
        region = screen.getManager().getAtlas().findRegion(status == Status.WEAK? "RexStatesTextures(alter)" : "RexStatesTextures");
        this.screen = screen;
        hp = startHp;
        this.world = screen.getWorld();

        immortal = (status == Status.IMMORTAL);

        //Оружие доступно только в боевом или бессмертном режимах
        this.weaponEnable = ((status == Status.COMBAT) || (status == Status.IMMORTAL));
        defineOscar(x, y);  //Создаём тело игрового персонажа
        createAnimation();  //Создаём анимации
    }


    {
        //Инициализация текущего и предыдущего состояний
        currentState = State.STANDING_RIGHT;
        previousState = State.STANDING_RIGHT;
        stateTimer = 0;

        turnedToRight = true;
        shootingFlag = false;
        sitting = false;
        timeToSitDown = false;
        timeToGetUp = false;
        damaged = false;
        levelFinished = false;
        readTime = false;

        setBounds(0, 0, 86/ com.stukovegor.scs.MainClass.PPM, 150/ com.stukovegor.scs.MainClass.PPM);
        lasers = new Array<PlayerLaser>();
        ableToShot = true;
        fireRateTimer = 0;
    }

    /** Метод, отвечающий за инициализацию всех анимаций игрового персонажа */
    private void createAnimation(){

        //Размер стандартного кадра анимации
        int width = 44;
        int height = 58;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Стоим и смотрим налево
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(region, i * width, height * 4, width, height));
        }
        oscarStandingLeft = new Animation<TextureRegion>(0.36f, frames);
        frames.clear();

        //Стоим и смотрим направо
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(region, i * width, 0, width, height));
        }
        oscarStandingRight = new Animation<TextureRegion>(.26f, frames);
        frames.clear();

        //Идем налево
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(region, i * width, 2 * height, width, height));
        }
        oscarMovingLeft = new Animation<TextureRegion>(.16f, frames);
        frames.clear();

        //Идем напрво
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(region, i * width, height, width, height));
        }
        oscarMovingRight = new Animation<TextureRegion>(.16f, frames);
        frames.clear();

        //Прыгаем вправо
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(region, width * (3 + i), 3 * height, width, height));
        }
        oscarJumpingRight = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        //Прыгаем влево
        for (int i = 1; i >= 0; i--) {
            frames.add(new TextureRegion(region, width * (3 + i), 4 * height, width, height));
        }
        oscarJumpingLeft = new Animation<TextureRegion>(.3f, frames);
        frames.clear();

        //Стреляем вправо
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(region, i * width, 3 * height, width, height));
        }
        oscarShootingRight = new Animation<TextureRegion>(.26f, frames);
        frames.clear();

        //Среляем влево
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(region, i * width, 5 * height, width, height));
        }
        oscarShootingLeft = new Animation<TextureRegion>(.23f, frames);
        frames.clear();

        //Получили урон, будучи направленным вправо
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(region, width * (2 + i), 6 * height, width, height));
        }
        oscarDamagedRight = new Animation<TextureRegion>(.14f, frames);
        frames.clear();

        //Получили урон, будучи направленным влево
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(region, width * i, 6 * height, width, height));
        }
        oscarDamagedLeft = new Animation<TextureRegion>(.14f, frames);
        frames.clear();

        //Сыграли в ящик
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(region, 5 * width, i * 35, 52, 35));
        }
        oscarDead = new Animation<TextureRegion>(.4f, frames);
        frames.clear();


        //Сидим, смотрим налево
        oscarSittingLeft = new TextureRegion(region, 3 * width, 5 * height + 17, width, 40);
        //Сидим, смотрим направо
        oscarSittingRight = new TextureRegion(region, 4 * width, 5 * height + 17, width, 40);
    }

    /** Метод, вычисляющий состояние игрового персонажа */
    private State getState(){

        //Уровень пройден
        if(levelFinished)
            return State.LEVEL_FINISHED;
        //Сыграли в ящик
        if(dead)
            return State.DEAD;
        //Получили урон
        else if(damaged)
            return(turnedToRight? State.DAMAGED_RIGHT : State.DAMAGED_LEFT);
        //Сидим
        else if(sitting)
            return (turnedToRight ? State.SITTING_RIGHT : State.SITTING_LEFT);
        //Либо прыгаем, либо падаем(движемся по оси 0Y)
        else if(b2body.getLinearVelocity().y != 0)
            return (turnedToRight? State.JUMPING_RIGHT : State.JUMPING_LEFT);
        //Движемся вправо
        else if(b2body.getLinearVelocity().x > 0.2)
            return State.MOVING_RIGHT;
        //Движемся влево
        else if(b2body.getLinearVelocity().x < -0.2)
            return State.MOVING_LEFT;
        //Стреляем
        else if(shootingFlag)
            return (turnedToRight? State.SHOOTING_RIGHT : State.SHOOTING_LEFT);
        //Иначе, просто стоим и смотрим влево...
        else if(!turnedToRight)
            return State.STANDING_LEFT;
        //...либо смотрим право
        else
            return State.STANDING_RIGHT;
    }

    /**
     * Метод, необходимый для работы анимации игрового персонажа
     * @param dt - изменение времени
     * @return - возвращаемый кадр из анимации, который надо отобразить в момент времени dt
     */
    private TextureRegion getFrame(float dt){

        //Получаем текущее состояние
        currentState = getState();

        TextureRegion region;

        //В зависимости от этого состояния выбираем кадр из анимации, соответствующий моменту времени
        switch(currentState){
            case DEAD:
                region = oscarDead.getKeyFrame(stateTimer);
                break;
            case LEVEL_FINISHED:
                region = oscarMovingRight.getKeyFrame(stateTimer, true);
                break;
                case STANDING_LEFT:
                region = oscarStandingLeft.getKeyFrame(stateTimer, true);
                break;
            case STANDING_RIGHT:
                region = oscarStandingRight.getKeyFrame(stateTimer, true);
                break;
            case MOVING_RIGHT:
                region = oscarMovingRight.getKeyFrame(stateTimer, true);
                break;
            case MOVING_LEFT:
                region = oscarMovingLeft.getKeyFrame(stateTimer, true);
                break;
            case JUMPING_LEFT:
                region = oscarJumpingLeft.getKeyFrame(stateTimer, false);
                break;
            case JUMPING_RIGHT:
                region = oscarJumpingRight.getKeyFrame(stateTimer, false);
                break;
            case SHOOTING_LEFT:
                region = oscarShootingLeft.getKeyFrame(stateTimer, true);
                break;
            case SHOOTING_RIGHT:
                region = oscarShootingRight.getKeyFrame(stateTimer, true);
                break;
            case SITTING_LEFT:
                region = oscarSittingLeft;
                break;
            case SITTING_RIGHT:
                region = oscarSittingRight;
                break;
            case DAMAGED_LEFT:
                region = oscarDamagedLeft.getKeyFrame(stateTimer);
                break;
            case DAMAGED_RIGHT:
                region = oscarDamagedRight.getKeyFrame(stateTimer);
                break;
            default:
                region = oscarStandingRight.getKeyFrame(stateTimer, true);
                break;
        }

        //Если предыдущее состояние является текущим, то таймер анимации возрастает, если
        //наступило другое состояние, таймер анимации обнуляется
        stateTimer = currentState == previousState? stateTimer + dt : 0;
        previousState = currentState;

        //Возвращаем полученный ранее кадр
        return region;
    }

    /**
     * Инициализация и создание тела игрового персонажа
     * @param x - координата создания по оси X
     * @param y - координата создания по оси Y
     */
    private void defineOscar(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x*32/ com.stukovegor.scs.MainClass.PPM, y*32/ com.stukovegor.scs.MainClass.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        //Создаём прямоугольник
        PolygonShape shape = new PolygonShape();
        //Устанавливаем его размер
        shape.setAsBox(68f / 2 / com.stukovegor.scs.MainClass.PPM, 150f / 2 / com.stukovegor.scs.MainClass.PPM);

        //Определение категории игрока и категорий, с которыми игрок сможет сталкиваться
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.PLAYER_BIT;

        fdef.filter.maskBits =  MainClass.GROUND_BIT         |
                                com.stukovegor.scs.MainClass.ENEMY_BIT          |
                                com.stukovegor.scs.MainClass.OBJECT_BIT         |
                                com.stukovegor.scs.MainClass.HILL_BIT           |
                                com.stukovegor.scs.MainClass.LEVEL_FINISH_BIT   |
                                com.stukovegor.scs.MainClass.MESSAGE_BIT        |
                                com.stukovegor.scs.MainClass.WEAK_POINT_BIT     |
                                com.stukovegor.scs.MainClass.DEATH_BIT          |
                                com.stukovegor.scs.MainClass.LASER_OF_ENEMY_BIT ;

        //Устанавливаем фигурой тела ранее созданный прямоугольник
        fdef.shape = shape;

        //Устанавливаем трение
        fdef.friction = 0.5f;
        b2body.createFixture(fdef).setUserData(this);
    }

    /** Обновление игрового персонажа */
    public void update(float dt){
        if(!dead)
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        else
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 - 45f / com.stukovegor.scs.MainClass.PPM);
        setRegion(getFrame(dt));

        if(readFlag && screen.getHud().isDialogueNotDisplayed()){
            readFlag = false;
            readTime = false;
        }
        if(readTime && screen.getHud().isDialogueNotDisplayed()) {
            screen.getHud().showDialog();
            readFlag = true;
        }

        //Если нужно сесть, садимся
        if(timeToSitDown){
            sitDown();
        }

        //Если нужно встать, встаём
        if(timeToGetUp){
            getUp();
        }

        //В какую сторону смотрит игровой персонаж?
        if(b2body.getLinearVelocity().x > 0)
            turnedToRight = true;
        else if(b2body.getLinearVelocity().x < 0)
            turnedToRight = false;

        //Обновление массива лазеров
        for(com.stukovegor.scs.Shells.Shell shell: lasers) {
            shell.update();
            if(shell.isDestroyed())
                lasers.pop();
        }

        //Может ли выстрелить игрок?
        if(!ableToShot){
            fireRateTimer += dt;
            if(fireRateTimer > .8f){
                ableToShot = true;
                fireRateTimer = 0;
            }
        }

        //Проигрывание анимации стрельбы
        if(shootingFlag){
            shootAnimationTimer +=dt;
            if(shootAnimationTimer > .3f){
                shootingFlag = false;
                shootAnimationTimer = 0;
            }
        }

        //Таймер полученного урона
        if(damaged){
            damagedTimer +=dt;
            if(damagedTimer > .3f){
                damaged = false;
                damagedTimer = 0;
            }
        }
    }

    /** Отрисовка игрового персонажа */
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for(Shell shell: lasers) {
            if(!shell.isSetToDestroy())
                shell.draw(batch);
        }
    }

    /** Метод, позволяющий игровому персонажу выстрелить */
    public void shoot() {
        if (weaponEnable) {
            if (!sitting && !damaged && !readTime) {
                if (ableToShot && (b2body.getLinearVelocity().x == 0 || b2body.getLinearVelocity().y != 0)) {
                    if (turnedToRight)
                        lasers.add(new PlayerLaser(this.screen, b2body.getPosition().x + 55 / com.stukovegor.scs.MainClass.PPM, b2body.getPosition().y + 45 / com.stukovegor.scs.MainClass.PPM));
                    else
                        lasers.add(new PlayerLaser(this.screen, b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 30 / com.stukovegor.scs.MainClass.PPM, true));
                    ableToShot = false;
                    if (screen.isSoundEnabled())
                        screen.getManager().manager.get(screen.getManager().rexShot, Sound.class).play(screen.getSoundVolume());
                }
                shootingFlag = true;
            }
        }
    }

    /** Метод, отвечающий за движение вправо */
    public void moveRight(){
        if(b2body.getLinearVelocity().x <= 2 && !sitting && !damaged && !readTime)
            b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
    }

    /** Метод, отвечающий за движение влево */
    public void moveLeft(){
        if(b2body.getLinearVelocity().x >= -2 && !sitting && !damaged && !readTime)
            b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    }

    /** Метод, отвечающий за прыжок */
    public void jump(){
        if(b2body.getLinearVelocity().y == 0 && !sitting && !damaged && !readTime){
            b2body.applyLinearImpulse(new Vector2(0, 6f), b2body.getWorldCenter(), true);
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().punkJump, Sound.class).play(screen.getSoundVolume());
        }
    }

    /** Метод, позволяющий игровому персонажу присесть */
    private void sitDown(){

        //Игровой персонаж может сесть только тогда, когда не сидит и не ранен(только что получил урон)
        if(!sitting && !damaged) {

            //Получаем координату левого нижнего угла тела игрового персонажа...
            Vector2 currentPosition = b2body.getPosition().add(-getWidth() / 2, -getHeight() / 2);
            //...и уничтожаем текущее тело
            world.destroyBody(b2body);

            //Создаём новое, с такими же характеристиками, отличающееся размером и формой
            BodyDef bdef = new BodyDef();
            bdef.position.set(currentPosition.add(46 / com.stukovegor.scs.MainClass.PPM, 42.5f / com.stukovegor.scs.MainClass.PPM));
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);
            //Увеличиваем гравитацию, чтобы игрок быстрее падал в таком положении
            b2body.setGravityScale(12);
            setBounds(0, 0, 92/ com.stukovegor.scs.MainClass.PPM, 85 / com.stukovegor.scs.MainClass.PPM);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            FixtureDef fdef = new FixtureDef();

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(85f / 2 / com.stukovegor.scs.MainClass.PPM, 85f / 2 / com.stukovegor.scs.MainClass.PPM);


            fdef.filter.categoryBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
            fdef.filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT |
                    com.stukovegor.scs.MainClass.ENEMY_BIT |
                    com.stukovegor.scs.MainClass.OBJECT_BIT |
                    com.stukovegor.scs.MainClass.MESSAGE_BIT |
                    com.stukovegor.scs.MainClass.HILL_BIT |
                    com.stukovegor.scs.MainClass.DEATH_BIT |
                    com.stukovegor.scs.MainClass.LASER_OF_ENEMY_BIT;

            fdef.shape = shape;

            //В приседе трение больше
            fdef.friction = 0.8f;
            b2body.createFixture(fdef).setUserData(this);
            //Устанавливаем флаг, что теперь игровой персонаж сидит
            sitting = true;
        }
        timeToSitDown = false;
    }

    /** Метод, позволяющий игровому персонажу встать */
    private void getUp(){

        //Игровой персонаж может встать только тогда, когда он сидит и не мёртв
        if(sitting && !dead) {

            //Получаем координату левого нижнего угла тела...
            Vector2 currentPosition = b2body.getPosition().add(-getWidth() / 2, -getHeight() / 2);
            //И уничтожаем его
            world.destroyBody(b2body);

            //Далее создаём тело, идентичное тому, какое создаётся в методе defineOscar()
            BodyDef bdef = new BodyDef();
            bdef.position.set(currentPosition.add(43 / com.stukovegor.scs.MainClass.PPM, 75/ com.stukovegor.scs.MainClass.PPM));
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);
            setBounds(0, 0, 86/ com.stukovegor.scs.MainClass.PPM, 150/ com.stukovegor.scs.MainClass.PPM);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            FixtureDef fdef = new FixtureDef();

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(68f / 2 / com.stukovegor.scs.MainClass.PPM, 150f / 2 / com.stukovegor.scs.MainClass.PPM);

            //Определение категории игрока и категорий, с которыми игрок сможет сталкиваться
            fdef.filter.categoryBits = com.stukovegor.scs.MainClass.PLAYER_BIT;
            fdef.filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT |
                    com.stukovegor.scs.MainClass.ENEMY_BIT |
                    com.stukovegor.scs.MainClass.LEVEL_FINISH_BIT |
                    com.stukovegor.scs.MainClass.OBJECT_BIT |
                    com.stukovegor.scs.MainClass.MESSAGE_BIT |
                    com.stukovegor.scs.MainClass.HILL_BIT |
                    com.stukovegor.scs.MainClass.DEATH_BIT |
                    com.stukovegor.scs.MainClass.LASER_OF_ENEMY_BIT;

            fdef.shape = shape;
            fdef.friction = 0.5f;
            b2body.createFixture(fdef).setUserData(this);

            //Ставим флаг, что больше игровой персонаж не сидит
            sitting = false;
        }
        timeToGetUp = false;
    }

    /** Получение урона */
    public void getDamage(){

        if(!immortal)
            hp -= (12 + rand.nextInt(11));

        //Если доступны звуки, проигрываем характерный звуковой эффект
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().rexDamaged, Sound.class).play(screen.getSoundVolume());

        //Если количество hp стало отрицаиельным(что не естественно для мертвеца), то делаем значение hp равным нулю
        if(hp < 0)
            hp = 0;

        //Обновляем hp на экране
        screen.getHud().updateHp(hp);

        this.damaged = true;

        //Если Оскар сидел, то от боли он подскочил
        if(sitting)
            timeToGetUp = true;

        //Если количество очков здоровья стало равням нулю, то игровой персонаж умер
        if(hp == 0) {
            dead = true;
            if(screen.isSoundEnabled())
                screen.getManager().manager.get(screen.getManager().levelFailed, Sound.class).play(screen.getSoundVolume());

            //Ставим категорию тела - "мертвец",
            //это значит, что игровой персонаж упадёд на землю
            Filter filter = new Filter();
            filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
            filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
            for(Fixture fixture: b2body.getFixtureList())
                fixture.setFilterData(filter);
            setBounds(0, 0, 132f / com.stukovegor.scs.MainClass.PPM , 56 / com.stukovegor.scs.MainClass.PPM);
        }
    }

    /** Получение дополнительных очков здоровья */
    public void getHill(){

        //Эффективность аптечки тоже зависит от рандома
        hp += 18 + rand.nextInt(7);
        hp = (hp > 100? 100 : hp);
        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().hillSmall, Sound.class).play(screen.getSoundVolume());
        screen.getHud().updateHp(hp);
    }

    /** При вызове данного метода игровой персонаж сразу умирает */
    public void die(){
        hp = 0;
        screen.getHud().updateHp(hp);

        if(sitting)
            timeToGetUp = true;

        dead = true;

        if(screen.isSoundEnabled())
            screen.getManager().manager.get(screen.getManager().levelFailed, Sound.class).play(screen.getSoundVolume());

        Filter filter = new Filter();
        filter.categoryBits = com.stukovegor.scs.MainClass.DEAD_BIT;
        filter.maskBits = com.stukovegor.scs.MainClass.GROUND_BIT;
        for(Fixture fixture: b2body.getFixtureList())
            fixture.setFilterData(filter);
        setBounds(0, 0, 132f / com.stukovegor.scs.MainClass.PPM , 56 / MainClass.PPM);
    }

    /**
     * Метод, устанавливающий, пройден уровень или нет
     * @param levelFinished - пройден ли уровень
     */
    public void setLevelFinished(boolean levelFinished) {
        screen.playFinishSound();
        this.levelFinished = levelFinished;
    }

    /** С помощью этого метода устанавливаем время прочтения диалогов */
    public void setReadTime(boolean readTime){
        this.readTime = readTime;
    }

    /** Пора присесть */
    public void setTimeToSitDown() {
        if(!readTime)
            timeToSitDown = true;

    }

    /** Пора вставать */
    public void setTimeToGetUp() {
        if(!readTime)
            timeToGetUp = true;
    }

    /**
     * @return - текущее состояние игрового персонажа
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * @return - таймер состояния
     */
    public float getStateTimer() {
        return stateTimer;
    }

    /** Закончен ли уровеь? */
    public boolean isLevelFinished() {
        return levelFinished;
    }

    /**
     * @return - очки здоровья игрока
     */
    public int getHp() {
        return hp;
    }

    /**
     * @return - игровой экран
     */
    public PlayScreen getScreen() {
        return screen;
    }

    /**
     * @return - тело игрового персонажа
     */
    public Body getB2body() {
        return b2body;
    }
}
