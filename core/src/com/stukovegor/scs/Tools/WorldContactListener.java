package com.stukovegor.scs.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Sprites.Enemies.Enemy;
import com.stukovegor.scs.InteractiveObjects.Hill;
import com.stukovegor.scs.InteractiveObjects.DialogueZone;
import com.stukovegor.scs.Sprites.Enemies.Explo;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Shells.Shell;

/**
 * Класс обработчика столкновений, реализующий интерфейс {@link ContactListener}.
 * Используется для обработки соприкосновений различных тел
 * и применения методов в зависимости от характеристики этих тел
 * @author Стуков Егор
 * @version 1.3
 */
public class WorldContactListener implements ContactListener {

    /**
     * Начало контакта
     * @param contact - Объект класса {@link Contact}, содержащий информацию о соприкоснувшихся телах
     */
    @Override
    public void beginContact(Contact contact) {

        //Получаем информацию о контактирующих телах
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //В работе определения, какие тела контактируют, лежит принцип побитового "ИЛИ"

        int contactDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (contactDef) {

                //ИГРОК + ВРАГ
            case MainClass.PLAYER_BIT | MainClass.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == MainClass.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).collideWithPlayer();
                    ((Oscar)fixB.getUserData()).getDamage();
                }
                else {
                    ((Enemy) fixB.getUserData()).collideWithPlayer();
                    ((Oscar) fixA.getUserData()).getDamage();
                }
                break;

                //ВРАГ + ОБЪЕКТ НА КАРТЕ
            case MainClass.ENEMY_BIT | MainClass.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainClass.ENEMY_BIT){
                    if (((Enemy) fixA.getUserData()).getId() == MainClass.SHOTGUN_ID) {
                        ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                    }
                }
                else if (((Enemy) fixB.getUserData()).getId() == MainClass.SHOTGUN_ID) {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

                //ВРАГ + ЛАЗЕР ИГРОКА
            case MainClass.ENEMY_BIT | MainClass.LASER_OF_PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.LASER_OF_PLAYER_BIT){
                    ((com.stukovegor.scs.Shells.Shell)fixA.getUserData()).collideWithTarget();
                    ((Enemy)fixB.getUserData()).getDamage();
                }
                else{
                    ((com.stukovegor.scs.Shells.Shell)fixB.getUserData()).collideWithTarget();
                    ((Enemy)fixA.getUserData()).getDamage();
                }
                break;

                //ИГРОК + ЛАЗЕР ВРАГА
            case MainClass.PLAYER_BIT | MainClass.LASER_OF_ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.LASER_OF_ENEMY_BIT){
                    ((com.stukovegor.scs.Shells.Shell)fixA.getUserData()).collideWithTarget();
                    ((Oscar)fixB.getUserData()).getDamage();
                }else{
                    ((com.stukovegor.scs.Shells.Shell)fixB.getUserData()).collideWithTarget();
                    ((Oscar) fixA.getUserData()).getDamage();
                }
                break;

                //ОБЪЕКТ НА КАРТЕ + ПЕРЕД ВРАГА
            case MainClass.OBJECT_BIT | MainClass.FRONT_OF_ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.FRONT_OF_ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).jump();
                else
                    ((Enemy)fixB.getUserData()).jump();
                break;

                //ИГРОК + ФИНИШ УРОВНЯ
            case MainClass.PLAYER_BIT | MainClass.LEVEL_FINISH_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.PLAYER_BIT)
                    ((Oscar)fixA.getUserData()).setLevelFinished(true);
                else
                    ((Oscar)fixB.getUserData()).setLevelFinished(true);
                break;

                //ИГРОК + ЗОНА ДИАЛОГА
            case MainClass.PLAYER_BIT | MainClass.MESSAGE_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.MESSAGE_BIT) {
                    ((DialogueZone) fixA.getUserData()).collideWithPlayer();
                    ((Oscar)fixB.getUserData()).getScreen().getHud().setDialogue(((DialogueZone) fixA.getUserData()).getDialogue());
                    ((Oscar)fixB.getUserData()).setReadTime(true);

                }
                else {
                    ((DialogueZone) fixB.getUserData()).collideWithPlayer();
                    ((Oscar)fixA.getUserData()).getScreen().getHud().setDialogue(((DialogueZone) fixB.getUserData()).getDialogue());
                    ((Oscar)fixA.getUserData()).setReadTime(true);
                }
                break;

                //ИГРОК + АПТЕЧКА
            case MainClass.PLAYER_BIT | MainClass.HILL_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.HILL_BIT){
                    if(((Oscar)fixB.getUserData()).getHp() < 100) {
                        ((Oscar) fixB.getUserData()).getHill();
                        ((Hill)fixA.getUserData()).collideWithPlayer();
                    }
                }
                else {
                    if(((Oscar)fixA.getUserData()).getHp() < 100) {
                        ((Oscar)fixA.getUserData()).getHill();
                        ((Hill)fixB.getUserData()).collideWithPlayer();
                    }
                }
                break;

                //ИГРОК + СЛАБОЕ МЕСТО ВРАГА
            case MainClass.PLAYER_BIT | MainClass.WEAK_POINT_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.PLAYER_BIT)
                    ((Enemy)fixB.getUserData()).terminated();
                else
                    ((Enemy)fixA.getUserData()).terminated();
            break;

                //ИГРОК + ЗОНА СМЕРТИ
            case MainClass.PLAYER_BIT | MainClass.DEATH_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.PLAYER_BIT)
                    ((Oscar)fixA.getUserData()).die();
                else
                    ((Oscar)fixB.getUserData()).die();
            break;

                //ВРАГ(ЭКСПЛО) + БОРДЮР
            case MainClass.ENEMY_BIT | MainClass.BORDER_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.ENEMY_BIT)
                    ((Explo)fixA.getUserData()).reachedBorder();
                else
                    ((Explo)fixB.getUserData()).reachedBorder();
            break;

                //СНАРЯД ВРАГА + ЗЕМЛЯ
            case MainClass.LASER_OF_ENEMY_BIT | MainClass.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.LASER_OF_ENEMY_BIT)
                    ((com.stukovegor.scs.Shells.Shell)fixA.getUserData()).collideWithTarget();
                else
                    ((com.stukovegor.scs.Shells.Shell)fixB.getUserData()).collideWithTarget();
            break;

                //ЛАЗЕР ИГРОКА + ЗЕМЛЯ
            case MainClass.LASER_OF_PLAYER_BIT | MainClass.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == MainClass.LASER_OF_PLAYER_BIT)
                    ((com.stukovegor.scs.Shells.Shell)fixA.getUserData()).collideWithTarget();
                else
                    ((Shell)fixB.getUserData()).collideWithTarget();
            break;

        }
    }

    /**
     * Конец контакта
     * @param contact - Объект класса {@link Contact}, содержащий информацию о соприкоснувшихся телах
     */
    @Override
    public void endContact(Contact contact) {

    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
