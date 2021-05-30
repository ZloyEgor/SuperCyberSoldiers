package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.stukovegor.scs.Screens.Levels.Level0;
import com.stukovegor.scs.Screens.Levels.Level3;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.Levels.Level1;
import com.stukovegor.scs.Screens.Levels.Level2;
import com.stukovegor.scs.Screens.Levels.Level4;
import com.stukovegor.scs.Screens.Levels.Level5;
import com.stukovegor.scs.Screens.Levels.Level6;
import com.stukovegor.scs.Screens.Levels.Level7;
import com.stukovegor.scs.Tools.AppPreference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Класс экрана главного меню
 * Реализует интерфейс {@link Screen}
 * @see AppPreference
 * @author Стуков Егор
 * @version 1.6
 */

public final class MenuScreen implements Screen {

    /** Главный класс игры */
    private com.stukovegor.scs.MainClass game;

    /**Объект класса {@link Stage} используется как контейнер актёров, т.е таблицы и её содержимого */
    private Stage stage;

    /** Ярлыки, которые будут выступать в качестве кнопок с текстом */
    private Label newGameButton, continueButton, settingsButton, extraScreenButton, exitButton;

    /** Изображение(логотип игры) */
    private Image logoImage;

    /** Музыка */
    private Music titleMusic;

    /** Отрисовщик */
    private SpriteBatch batch;

    /**
     * Конструктор - создание нового экрана
     * @param game - передаём объект главного класса {@link com.stukovegor.scs.MainClass}
     */
    public MenuScreen(com.stukovegor.scs.MainClass game) {
        this.game = game;

        batch = new SpriteBatch();

        stage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera()), batch);

        //Стиль текста
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        //Инициализируем ярлыки
        newGameButton = new Label("NEW GAME", font);
        continueButton = new Label("CONTINUE", font);
        settingsButton = new Label("SETTINGS", font);
        extraScreenButton = new Label("EXTRA", font);
        exitButton = new Label("EXIT", font);

        //Инициализируем изображение и музыку
        logoImage = new Image(game.getManager().getAtlas().findRegion("logo"));
        titleMusic = Gdx.audio.newMusic(Gdx.files.internal(game.getManager().title));
        if(game.getPreferences().isMusicEnabled()) {
            titleMusic.setLooping(false);
            titleMusic.setVolume(game.getPreferences().getMusicVolume());
            titleMusic.play();
        }

    }

    @Override
    public void show() {

        //Создание таблицы
        Table table = new Table();
        table.setFillParent(true);

        //ОТЛАДКА
        table.setDebug(false);

        //Заполняем таблицу
        table.add(logoImage).expandX();
        table.row().padTop(50);
        table.add(newGameButton).expandX();
        table.row().padTop(30);
        table.add(continueButton).expandX();
        table.row().padTop(30);
        table.add(settingsButton).expandX();
        table.row().padTop(30);
        table.add(extraScreenButton).expandX();
        table.row().padTop(30);
        table.add(exitButton).expandX();

        //Добавляем обработчики нажатий на ярлыки
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPreferences().isSoundEffectsEnabled())
                    game.getManager().manager.get(game.getManager().click, Sound.class).play(game.getPreferences().getSoundVolume());
                stage.getRoot().getColor().a = 1;
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(fadeOut(0.5f));
                sequenceAction.addAction(run(new Runnable() {
                    @Override
                    public void run() {
                        game.getPreferences().setSavedLevel(0);
                        game.setScreen(new Level0(game));
                        dispose();
                    }
                }));
                stage.getRoot().addAction(sequenceAction);
                return true;
            }
        });

        continueButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPreferences().isSoundEffectsEnabled())
                    game.getManager().manager.get(game.getManager().click, Sound.class).play(game.getPreferences().getSoundVolume());
                stage.getRoot().getColor().a = 1;
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(fadeOut(0.5f));
                sequenceAction.addAction(run(new Runnable() {
                    @Override
                    public void run() {
                        switch (game.getPreferences().getSavedLevel()) {
                            case 1:
                                game.setScreen(new Level1(game));
                                break;
                            case 2:
                                game.setScreen(new Level2(game));
                                break;
                            case 3:
                                game.setScreen(new Level3(game));
                                break;
                            case 4:
                                game.setScreen(new Level4(game));
                                break;
                            case 5:
                                game.setScreen(new Level5(game));
                                break;
                            case 6:
                                game.setScreen(new Level6(game));
                                break;
                            case 7:
                                game.setScreen(new Level7(game));
                                break;
                            default:
                                game.setScreen(new Level0(game));
                                break;
                        }
                        dispose();
                    }
                }));
                stage.getRoot().addAction(sequenceAction);
                return true;
            }
        });

        settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPreferences().isSoundEffectsEnabled())
                    game.getManager().manager.get(game.getManager().click, Sound.class).play(game.getPreferences().getSoundVolume());
                stage.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new SettingsScreen(game));
                        dispose();
                    }
                })));
                return true;
            }
        });

        extraScreenButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPreferences().isSoundEffectsEnabled())
                    game.getManager().manager.get(game.getManager().click, Sound.class).play(game.getPreferences().getSoundVolume());
                stage.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ExtraScreen(game));
                        dispose();
                    }
                })));
                return true;
            }
        });

        //Добавляем таблицу в curtainStage
        stage.addActor(table);

        //Эффект проявления
        stage.addAction(Actions.fadeIn(1.7f));

        //Устанавливаем процессор ввода
        Gdx.input.setInputProcessor(stage);



    }

    /** Отрисовка экрана */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    /** Изменение размера экрана */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /** Пауза */
    @Override
    public void pause() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /** Возврат с паузы */
    @Override
    public void resume() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /** Игра свёрнута */
    @Override
    public void hide() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /** Уничтожение экрана */
    @Override
    public void dispose() {
        stage.dispose();
        titleMusic.dispose();
        batch.dispose();
    }
}
