package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.InteractiveObjects.InteractiveObject;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Sprites.Enemies.Enemy;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.AntiBleedingOrthogonalTiledMapRenderer;
import com.stukovegor.scs.Tools.Controller;
import com.stukovegor.scs.Tools.Hud;
import com.stukovegor.scs.Tools.SCSAssetManager;
import com.stukovegor.scs.Tools.WorldContactListener;
import com.stukovegor.scs.Tools.WorldCreator;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс игрового экрана, реализующий интерфейс {@link Screen}.
 * От данного класса наследуются все уровни в игре.
 * @author Стуков Егор
 */

public class PlayScreen implements Screen {

    /** Объект главного класса игры */
    protected com.stukovegor.scs.MainClass game;

    /** Менеджер игровых ресурсов */
    protected com.stukovegor.scs.Tools.SCSAssetManager manager;

    /** Фоновая музыка */
    protected Music music;

    /** Игрова камера */
    protected OrthographicCamera gameCamera;

    /** Окно просмотра игровой камеры */
    private Viewport viewport;

    /** Загрузчик карты уровня */
    protected TmxMapLoader mapLoader;

    /** Карта уровня */
    protected TiledMap map;

    /** Визуализатор карты */
    protected com.stukovegor.scs.Tools.AntiBleedingOrthogonalTiledMapRenderer renderer;

    /** Слои карты, которые необходимо отрисовывать за персонажами */
    private int[] firstLayersToDraw;

    /** Массив диалогов */
    protected Map<Integer, com.stukovegor.scs.Dialogue.Dialogue> dialogues;

    /** Строитель мира */
    protected WorldCreator creator;

    /** Мир */
    private World world;

    /** Отладчик мира(используется по желанию для отладки отдельных участков мира) */
    private Box2DDebugRenderer b2dr;

    /** Игровой персонаж */
    protected com.stukovegor.scs.Sprites.Oscar oscar;

    /** Сцена "черной шторки, необходимой для плавных переходов" */
    private Stage curtainStage;

    /** Нужно ли показывать "чёрную шторку?" */
    private boolean needToDrawCurtain;

    /** Сцена фона */
    protected Stage backgroundStage;

    /** Интерфейс игрока */
    //Контроллер
    private com.stukovegor.scs.Tools.Controller controller;
    //Индикаторы
    protected com.stukovegor.scs.Tools.Hud hud;

    /** Поставлена ли игра на паузу? */
    private boolean isPaused;

    /** Отрисовщик экрана */
    protected SpriteBatch batch;

    /**
     * Конструктор
     * @param game - объект главного класса
     */
    public PlayScreen(com.stukovegor.scs.MainClass game){

        //Инициализация отрисовщика
        batch = new SpriteBatch();

        this.game = game;
        this.manager = game.getManager();

        //Создаём диалоги
        dialogues = new HashMap<Integer, com.stukovegor.scs.Dialogue.Dialogue>();
        createDialogues();

        //Создание и инициализация игровой камеры
        gameCamera = new OrthographicCamera();
        viewport = new StretchViewport(com.stukovegor.scs.MainClass.WIDTH / com.stukovegor.scs.MainClass.PPM, com.stukovegor.scs.MainClass.HEIGHT / com.stukovegor.scs.MainClass.PPM, gameCamera);
        gameCamera.setToOrtho(false);

        //Инициализация загрузчика  и визуализатора карты
        mapLoader = new TmxMapLoader();

        //Сама карта инициализируется в конструкторе уровня, поэтому используем null в качестве параметра
        renderer = new AntiBleedingOrthogonalTiledMapRenderer(null, 1 / com.stukovegor.scs.MainClass.PPM, batch);

        //Создание мира и его отладчика
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //+++++=====ОТЛАДКА=====+++++\\
        b2dr.setDrawBodies(false);
        //+++++=================+++++\\

        //Устанавливаем обработчик столкновений тел
        world.setContactListener(new WorldContactListener());

        //Пользовательский интерфейс
        controller = new Controller(this);
        hud = new com.stukovegor.scs.Tools.Hud(batch);

        //При создании игра не на паузе
        isPaused = false;


        //Устанавливаем фон уровня
        setBackground();

        //При создании экрана чёрную шторку отрисовывать надо, ставим true
        needToDrawCurtain = true;

        //Инициализация сцены "чёрной шторки", нужной для плавного перехода по уровням
        curtainStage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, com.stukovegor.scs.MainClass.HEIGHT), batch);
        Image img = new Image(manager.getAtlas().findRegion("curtain"));
        img.setSize(com.stukovegor.scs.MainClass.WIDTH, com.stukovegor.scs.MainClass.HEIGHT);
        curtainStage.addActor(img);

        //На всех картах слои заднего плана распологаются под следующими номерами
        firstLayersToDraw = new int[]{3, 4, 5};

    }

    /** Метод, который вызывается при выводе экрана */
    @Override
    public void show() {
        //При показе экрана "чёрная шторка" выцвечивается в течение 1,5 секунд, далее её отрисовывать не надо
        curtainStage.addAction(Actions.sequence(Actions.fadeOut(1.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                needToDrawCurtain = false;
            }
        })));
    }

    /**
     * Метод, отвечающий за обновление игрового экрана
     * @param dt - изменение времени
     */
    public void update(float dt){

        //Обрабатываем нажатия
        handleInput();

        //Основное обновление происходит только тогда, когда игра не на паузе
        if(!isPaused) {

            world.step(1 / 60f, 6, 2);

            //Если игровой персонаж не мёртв и уровень не пройден, то камера по оси X следит за игровым персонажем
            if (oscar.getCurrentState() != com.stukovegor.scs.Sprites.Oscar.State.DEAD && !oscar.isLevelFinished()) {
                gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / com.stukovegor.scs.MainClass.PPM;

                //Если по оси Y игровой персонаж отклоняется от камеры, то её положение по оси Y изменяется
                if(oscar.getB2body().getPosition().y - oscar.getHeight()/2 - 96 / com.stukovegor.scs.MainClass.PPM < gameCamera.position.y - gameCamera.viewportHeight /2)
                    gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 96 / com.stukovegor.scs.MainClass.PPM;
                else if(oscar.getB2body().getPosition().y - oscar.getHeight() / 2 > gameCamera.position.y - gameCamera.viewportHeight / 2 + 270 / com.stukovegor.scs.MainClass.PPM)
                    gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() /2;

                if(game.getPreferences().isMusicEnabled())
                    music.play();
            }

            //Если уровень пройден, то музыка ставится на паузу, а игровой персонаж движется вправо
            else if (oscar.isLevelFinished()) {
                music.pause();
                oscar.moveRight();
            }

            //Обновляем игровую камеру
            gameCamera.update();

            //Обновление игрового персонажа
            oscar.update(dt);

            //Устанавливаем, что мы хотим отрисовывать ту часть карты, которую мы видим с помощью камеры
            renderer.setView(gameCamera);

            for (com.stukovegor.scs.Sprites.Enemies.Enemy enemy : creator.getEnemies()) {
                //Если враг находится на достаточно близком расстоянии от того, что мы видим на экране, то мы его обновляем
                if(enemy.getB2body().getPosition().x - enemy.getWidth()/2 <= gameCamera.position.x + gameCamera.viewportWidth / 2 + 200 / com.stukovegor.scs.MainClass.PPM
                        && enemy.getB2body().getPosition().x + enemy.getWidth()/2 + 64 / com.stukovegor.scs.MainClass.PPM >= gameCamera.position.x - gameCamera.viewportWidth / 2)
                    enemy.update(dt);
                //При создании тела всех врагов находятся в неактивном состоянии, когда игрок приближается к врагу на достаточное
                //расстояние, то мы активируем его тело
                if (!enemy.isDestroyed() && enemy.getX() < oscar.getX() + 16 * 32 / com.stukovegor.scs.MainClass.PPM && !enemy.getB2body().isActive())
                    enemy.getB2body().setActive(true);
            }


            for(com.stukovegor.scs.InteractiveObjects.InteractiveObject object : creator.getObjects()){
                //Обновляем только те интерактивные объекты, которые расположены на достаточно близком к экрану расстоянии
                if(!object.isDestroyed() && object.getBody().getPosition().x - object.getWidth()/2 <= gameCamera.position.x + gameCamera.viewportWidth / 2)
                object.update();
            }
        }
        //...иначе, если игра на паузе, то и музыку мы ставим на паузу
        else {
            music.pause();
        }

        //Если игровой персонаж умер, то мы также ставим игру на паузу
        if(oscar.getCurrentState() == com.stukovegor.scs.Sprites.Oscar.State.DEAD)
            music.pause();
    }

    /**
     * Метод отрисовки
     * @param delta - изменение времени
     */
    @Override
    public void render(float delta) {

        //Перед отрисовкой обновляем игровой экран
        update(delta);

        //Отчищаем экран, т.к. это необходимо при отрисовке
        Gdx.gl.glClearColor(0.07f, 0.09f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Отрисовываем задний фон
        batch.setProjectionMatrix(backgroundStage.getCamera().combined);
        backgroundStage.draw();

        //Отрисовываем задний план карты
        renderer.setView(gameCamera);
        renderer.render(firstLayersToDraw);

        //Отрисовывать физические тела можно для отладки
        //b2dr.render(world, gameCamera.combined);

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        for(InteractiveObject object : creator.getObjects()){
            //Отрисовываем только те интерактивные объекты, которы мы видим на экране
            if(!object.isDestroyed() && object.getBody().getPosition().x - object.getWidth()/2 <= gameCamera.position.x + gameCamera.viewportWidth / 2) {
                    object.draw(batch);
            }
        }

        for(Enemy enemy: creator.getEnemies()){
            //Отрисовываем только тех врагов, которые находятся на достаточно близко к полю зрения камеры
            if(enemy.getB2body().getPosition().x - enemy.getWidth()/2 <= gameCamera.position.x + gameCamera.viewportWidth / 2 + 200 / com.stukovegor.scs.MainClass.PPM
            && enemy.getB2body().getPosition().x + enemy.getWidth()/2 + 64 / com.stukovegor.scs.MainClass.PPM >= gameCamera.position.x - gameCamera.viewportWidth / 2 )
                enemy.draw(batch);
        }

        //Отрисовываем Оскара(игрового персонажа)
        oscar.draw(batch);

        //Отрисовываем передний план карты
        renderer.setView(gameCamera);
        renderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("forward"));
        batch.end();

        //Отрисовываем пользовательский интерфейс
        batch.setProjectionMatrix(controller.stage.getCamera().combined);
        controller.draw();

        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.draw();

        //Если нужно отрисовать "черную шторку", то мы её отрисовываем
        if(needToDrawCurtain) {
            batch.setProjectionMatrix(curtainStage.getCamera().combined);
            curtainStage.act();
            curtainStage.draw();
        }

        //Если уровень пройден, ставим следующий
        if(levelFinished()){
            setNextLevel();
        }

        //Если игровой персонаж погиб, ставим соответствующий экран
        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    /** Изменение размера окна */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        controller.resize(width, height);
        hud.resize(width, height);
    }

    /** Пауза */
    @Override
    public void pause() {
        controller.showPause();
    }

    /** Возвращение с паузы */
    @Override
    public void resume() {

    }

    /** Метод, вызывающийся когда окно игры свернуто */
    @Override
    public void hide() {
        controller.showPause();
    }

    /** Уничтожение игрового экрана
     * ПРИМЕЧАНИЕ:
     * Все перечисленные в методе объекты реализуют интерфейс {@link com.badlogic.gdx.utils.Disposable}.
     * это значит, что данные объекты необходимо уничтожать вручную во избежание затрат памяти
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        b2dr.dispose();
        world.dispose();
        music.dispose();
        curtainStage.dispose();
        backgroundStage.dispose();
        batch.dispose();
    }

    /** Инициализация сцены фона */
    protected void setBackground(){
        backgroundStage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, com.stukovegor.scs.MainClass.HEIGHT), batch);
        Image backgroundImg = new Image(manager.getAtlas().findRegion("background"));
        backgroundImg.setSize(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT);
        backgroundStage.addActor(backgroundImg);
    }

    /** Метод, отвечающий за обработку пользовательского ввода */
    private void handleInput(){

        //Обрабатываем пользовательский ввод при условии, что игровой персонаж жив-здоров и уровень не пройден
        if(oscar.getCurrentState() != com.stukovegor.scs.Sprites.Oscar.State.DEAD && !oscar.isLevelFinished()) {

            isPaused = controller.isPausePressed();

            //Если игра на паузе, то действия ходьбы, прыжка, стрельбы и приседа игроку не доступны
            if(!isPaused) {
                if (controller.isRightPressed())
                    oscar.moveRight();
                if (controller.isLeftPressed())
                    oscar.moveLeft();
                if (controller.isUpPressed())
                    oscar.jump();
                if (controller.isShotPressed())
                    oscar.shoot();
                if (controller.isDownPressed())
                    oscar.setTimeToSitDown();
                else
                    oscar.setTimeToGetUp();
            }
        }
    }

    /** В этом методе создаются внутриигровые диалоги и добавляются в dialogues */
    public void createDialogues(){ }

    /** Метод, устанавливающий следующий уровень */
    public void setNextLevel(){}

    /** Метод, отвечающий за проигрыш музыки при прохождении уровня */
    public void playFinishSound(){
        if(isSoundEnabled())
            getManager().manager.get(getManager().levelCompleted, Sound.class).play(getSoundVolume());
    }

    /** Геттеры */

    //Игровая камера
    public OrthographicCamera getGameCamera() { return gameCamera; }

    //Игровой персонаж
    public com.stukovegor.scs.Sprites.Oscar getOscar(){ return oscar;}

    //Карта
    public TiledMap getMap(){
        return map;
    }

    //Мир(с физическими объектами)
    public World getWorld(){
        return world;
    }

    //Интерфейс(здоровье + диалоги)
    public Hud getHud() {
        return hud;
    }

    //Менеджер ресурсов
    public SCSAssetManager getManager() {
        return manager;
    }

    //Игра окончена?
    private boolean gameOver(){
        return(oscar.getCurrentState() == com.stukovegor.scs.Sprites.Oscar.State.DEAD && oscar.getStateTimer() > 3);
    }

    //Уровент пройден?
    private boolean levelFinished(){
        return(oscar.getCurrentState() == Oscar.State.LEVEL_FINISHED && oscar.getStateTimer() > 3.5f);
    }

    //Доступны ли звуки?
    public boolean isSoundEnabled(){
        return game.getPreferences().isSoundEffectsEnabled();
    }

    //Громкость звуков
    public float getSoundVolume(){
        return game.getPreferences().getSoundVolume();
    }

    //Возвращаем диалог по его номеру
    public Dialogue getDialogue(int id) {
        return dialogues.get(id);
    }

    //Отрисовщик
    public SpriteBatch getBatch() {
        return batch;
    }
}
