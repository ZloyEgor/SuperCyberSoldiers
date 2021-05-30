package com.stukovegor.scs.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Shells.Shell;
import com.stukovegor.scs.Sprites.Oscar;

import java.util.Random;

/**
 * Абстрактный класс, от которого наследуются все враги игры.
 * Содержит в себе все общие для них поля и методы.
 * @author Стуков Егор
 * @version 2.4
 */

public abstract class Enemy extends Sprite {

    /** Идентификатор врага */
    protected int id;

    /**
     * Мир, в который будет помещён спрайт
     * @see World
     */
    World world;

    /** Игровой экран, содержащий мир */
    protected PlayScreen screen;

    /** Тело врага */
    protected Body b2body;

    /**
     * Объект класса {@link FixtureDef}, определяющий свойства {@link Enemy#b2body},
     * такие как масса, трение, упругость
     */
    FixtureDef fdef;

    /**
     * Объект класса {@link PolygonShape}, определяющий фигуру тела {@link Enemy#b2body}
     */
    PolygonShape shape;

    /** Скорость врага */
    protected Vector2 velocity;

    /** Очки здоровья */
    float hp;

    /** Таймер стрельбы */
    float shootDeltaTimer;

    /** Таймер получения урона */
    private float getDamageTimer;

    /**
     * Регион текстур спрайта
     * @see TextureRegion
     */
    TextureRegion region;

    /** Перечисление возможных состояний врага */
    protected enum State{STANDING, MOVING_LEFT, MOVING_RIGHT, SHOOTING_LEFT, SHOOTING_RIGHT, DAMAGED_LEFT, DAMAGED_RIGHT, DEAD}

    /** Предыдущее и текущее состояния врага */
    private State previousState, currentState;

    /** Таймер состояния */
    float stateTimer;

    /**
     * Анимации
     * @see Animation
     */
    Animation standing,
            movingLeft,
            movingRight,
            shootingLeft,
            shootingRight,
            damagedAnimationLeft,
            damagedAnimationRight,
            deadAnimation;

    /**
     * Массив снарядов
     * @see  com.stukovegor.scs.Shells.Shell
     */
    protected Array<com.stukovegor.scs.Shells.Shell> shells;

    /** Флаги не проверку состояния */
    boolean ableToShoot,     //способность стрелять в данный момент
            setToDestroy,   //"приговорен к уничтожению"
            damaged,        //ранен
            turnedToRight;  //повёрнут вправо

    protected boolean destroyed;    //уничтожен

    Random random;


    /**
     * Конструктор - создание нового врага
     * @param screen - игровой экран в мир (World) которого будет помещён враг
     * @param x - координата создания врага по оси X
     * @param y - координата создания врага по оси Y
     */
    Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);

        //Создание массива снарядов
        shells = new Array<com.stukovegor.scs.Shells.Shell>();

        random = new Random();

        //Объявление состояния
        previousState = State.STANDING;
        currentState = State.STANDING;
        stateTimer = 0;

        //Объявление очков здоровья. У всех врагов их количество одинаково, однако наносимый им урон различается
        hp = 100;

        ableToShoot = true;
        setToDestroy = false;
        damaged = false;
        turnedToRight = false;
        destroyed = false;

        defineEnemy();
        createAnimations();

        //по умолчанию враг не активен
        b2body.setActive(false);
    }

    /**
     * Технический метод, необходимый для работы анимации спрайтов
     * @param dt - изменение времени
     * @return - возвращаемый кадр из анимации, который надо отобразить в момент времени dt
     */
    TextureRegion getFrame(float dt){
        currentState = getState();
        stateTimer = (currentState == previousState? stateTimer + dt : 0);
        previousState = currentState;
        TextureRegion region;
        switch(currentState){
            case STANDING:
                region = (TextureRegion)standing.getKeyFrame(stateTimer, true);
                break;
            case MOVING_RIGHT:
                region = (TextureRegion)movingRight.getKeyFrame(stateTimer, true);
                break;
            case MOVING_LEFT:
                region = (TextureRegion)movingLeft.getKeyFrame(stateTimer, true);
                break;
            case SHOOTING_LEFT:
                region = (TextureRegion) shootingLeft.getKeyFrame(stateTimer, true);
                break;
            case SHOOTING_RIGHT:
                region = (TextureRegion) shootingRight.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(stateTimer, false);
                break;
            case DAMAGED_LEFT:
                region = (TextureRegion) damagedAnimationLeft.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion)standing.getKeyFrame(stateTimer, true);
                break;
        }
        return region;
    }

    /**
     * Метод, выполняющий проверку на то, находится ли игрок в поле зрения врага
     * @return - истино-ложное выражение, характеризующее нахождение игрока в поле зрения
     */
    protected boolean isPlayerVisible(){
        if(!destroyed && screen.getOscar().getCurrentState() != Oscar.State.DEAD){
            if ((this.b2body.getPosition().x) - (screen.getOscar().getB2body().getPosition().x)<= 460 / com.stukovegor.scs.MainClass.PPM && (this.b2body.getPosition().x) - (screen.getOscar().getB2body().getPosition().x) > 0 ){
                turnedToRight = false;
                return true;
            }
            else if(screen.getOscar().getB2body().getPosition().x > this.b2body.getPosition().x) {
                turnedToRight = true;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    /** Инвентирование скорости спрайта */
    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    /** Инициализация и создание тела врага */
    protected void defineEnemy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        fdef.friction = 0.8f;
        fdef.restitution = 0.15f;
        fdef.filter.categoryBits = com.stukovegor.scs.MainClass.ENEMY_BIT;
        fdef.filter.maskBits =  com.stukovegor.scs.MainClass.GROUND_BIT |
                                com.stukovegor.scs.MainClass.PLAYER_BIT |
                                com.stukovegor.scs.MainClass.OBJECT_BIT |
                                com.stukovegor.scs.MainClass.BORDER_BIT |
                                MainClass.LASER_OF_PLAYER_BIT;

        shape = new PolygonShape();
    }

    /**
     * Обновление врага
     * @param dt - изменение игрока
     */
    public void update(float dt){
        if (damaged) {
            getDamageTimer += dt;
            if (getDamageTimer > 0.4f) {
                damaged = false;
                getDamageTimer = 0;
            }
        }

        for(com.stukovegor.scs.Shells.Shell shell: shells)
            shell.update();
    }

    /**
     * Отрисовка врага
     * @param batch - отрисовщик
     */
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for (Shell shell : shells) {
            if (!shell.isSetToDestroy())
                    shell.draw(batch);
        }
    }

    /** Абстрактные методы, необходимые реализовать*/

    //Соприкосновение с игроком
    public  abstract void collideWithPlayer();

    //Получение урона
    public abstract void getDamage();

    //Выстрел
    public abstract void shoot();

    //Принудительное уничтожение
    public abstract void terminated();

    //Инициализация анимаций
    public abstract void createAnimations();

    //Метод, позволяющий узнать состояние врага(перечень возможных состояний см. выше)
    public abstract State getState();

    //Прыжок
    public abstract void jump();

    /** "Геттеры" */

    //Возвращает идентификатор врага
    public int getId(){
        return id;
    }

    //Возвращает тело спрайта
    public Body getB2body() {
        return b2body;
    }

    //Уничтожен ли враг?
    public boolean isDestroyed() {
        return destroyed;
    }

}
