package com.stukovegor.scs.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Enemies.Enemy;
import com.stukovegor.scs.Sprites.Enemies.Explo;
import com.stukovegor.scs.Sprites.Enemies.Turret;
import com.stukovegor.scs.InteractiveObjects.Hill;
import com.stukovegor.scs.InteractiveObjects.InteractiveObject;
import com.stukovegor.scs.InteractiveObjects.DialogueZone;
import com.stukovegor.scs.Sprites.Enemies.Punk;
import com.stukovegor.scs.Sprites.Enemies.Ross;
import com.stukovegor.scs.Sprites.Enemies.Shotgun;

/**
 * Класс, генерирующий в мир тела, информация о которых заложена в карту уровня
 * @see Enemy
 * @see InteractiveObject
 * @see PlayScreen
 * @author Стуков Егор
 * @version 1.1
 */

public class WorldCreator {

    /** Массив с врагами */
    private Array<Enemy> enemies;

    /** Массив с интерактивными объектами */
    private Array<InteractiveObject> objects;

    /**
     * Конструктор
     * @param screen - игровой экран
     */
    public WorldCreator(PlayScreen screen) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //СТАТИЧЕСКИЕ ОБЪЕКТЫ: ПОЛЫ, СТЕНЫ, ПОТОЛКИ, ЗОНЫ ОКОНЧАНИЯ УРОВНЕЙ(0 слой)
        for (MapObject object : screen.getMap().getLayers().get(0).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainClass.PPM, (rect.getY() + rect.getHeight() / 2) / MainClass.PPM);
            body = screen.getWorld().createBody(bdef);
            if(object.getProperties().containsKey("finish")){
                shape.setAsBox(rect.getWidth() / 2 / MainClass.PPM, rect.getHeight() / 2 / MainClass.PPM);
                fdef.filter.categoryBits = MainClass.LEVEL_FINISH_BIT;
                fdef.isSensor = true;
            }
            else if(object.getProperties().containsKey("object")){
                shape.setAsBox(rect.getWidth() / 2 / MainClass.PPM, rect.getHeight() / 2 / MainClass.PPM);
                fdef.filter.categoryBits = MainClass.OBJECT_BIT;
                fdef.isSensor = false;
            }
            else if(object.getProperties().containsKey("death")){
                shape.setAsBox(rect.getWidth() / 2 / MainClass.PPM, rect.getHeight() / 2 / MainClass.PPM);
                fdef.filter.categoryBits = MainClass.DEATH_BIT;
                fdef.isSensor = false;
            }
            else if(object.getProperties().containsKey("border")){
                shape.setAsBox(rect.getWidth() / 2 / MainClass.PPM, rect.getHeight() / 2 / MainClass.PPM);
                fdef.filter.categoryBits = MainClass.BORDER_BIT;
                fdef.isSensor = false;
            }
            else {
                shape.setAsBox(rect.getWidth() / 2 / MainClass.PPM, rect.getHeight() / 2 / MainClass.PPM);
                fdef.filter.categoryBits = MainClass.GROUND_BIT;
                fdef.isSensor = false;

            }
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //МЕСТА ГЕНЕРАЦИИ ВРАГОВ(1 слой)
        enemies = new Array<Enemy>();
        for(MapObject object : screen.getMap().getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(object.getProperties().containsKey("turret"))
                enemies.add(new Turret(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM));
            if(object.getProperties().containsKey("shotgun"))
                enemies.add(new Shotgun(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM));
            if(object.getProperties().containsKey("punk"))
                enemies.add(new Punk(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM));
            if(object.getProperties().containsKey("ross"))
                enemies.add(new Ross(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM));
            if(object.getProperties().containsKey("explo"))
                enemies.add(new Explo(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM));
        }

        //ИНТЕРАКТИВНЫЕ ОБЪЕКТЫ(2 слой)
        objects = new Array<InteractiveObject>();
        int c = 0;
        for(MapObject object : screen.getMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(object.getProperties().containsKey("message")) {
                objects.add(new DialogueZone(screen, rect.getX() / MainClass.PPM, rect.getY() / MainClass.PPM, c));
                c++;
            }
            if(object.getProperties().containsKey("hill"))
                objects.add(new Hill(screen, (rect.getX() - rect.getWidth()/2)/ MainClass.PPM, (rect.getY() - rect.getHeight() / 2) / MainClass.PPM));
        }

    }

    /** Геттеры */

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Array<InteractiveObject> getObjects() {
        return objects;
    }
}
