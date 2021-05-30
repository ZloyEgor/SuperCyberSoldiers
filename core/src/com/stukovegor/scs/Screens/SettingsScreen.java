package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.stukovegor.scs.MainClass;

/**
 * Экран настроек
 * @author Стуков Егор
 */

public class SettingsScreen implements Screen {

    /** Объект главного класса */
    private com.stukovegor.scs.MainClass game;

    /** Сцена экрана настроек */
    private Stage stage;

    /** Ярлыки */
    private Label title, volumeMusicLabel, volumeSoundLabel, musicOnOffLabel, soundOnOffLabel;

    /** Отрисовщик */
    private SpriteBatch batch;

    /**
     * Конструктор
     * @param game - главный класс
     */
    SettingsScreen(com.stukovegor.scs.MainClass game){
        this.game = game;

        batch = new SpriteBatch();

        stage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera()), batch);

        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        title = new Label("SETTINGS", style);
        volumeMusicLabel = new Label("MUSIC VOLUME", style);
        volumeSoundLabel = new Label("SOUNDS VOLUME", style);
        musicOnOffLabel = new Label("MUSIC", style);
        soundOnOffLabel = new Label("SOUND EFFECT", style);

    }

    /** Метод, который вызывается при выводе экрана */
    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("skin/skin.json"));

        //Слайдер громкости музыки
        final Slider musicVolumeSlider = new Slider(0f, 1f, .1f, false, skin);
        musicVolumeSlider.setValue(game.getPreferences().getMusicVolume());
        musicVolumeSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(musicVolumeSlider.getValue());
                return false;
            }

        });

        //Слайдер громкости звуков
        final Slider soundVolumeSlider = new Slider(0f, 1f, .1f, false, skin);
        soundVolumeSlider.setValue(game.getPreferences().getSoundVolume());
        soundVolumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(soundVolumeSlider.getValue());
                return false;
            }
        });

        //Чекбокс музыки
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });

        //Чекбокс звуков
        final CheckBox soundCheckbox = new CheckBox(null, skin);
        soundCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        //Кнопка "назад"
        final TextButton backButton = new TextButton("Back", skin); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.getPreferences().isSoundEffectsEnabled())
                    game.getManager().manager.get(game.getManager().click, Sound.class).play(game.getPreferences().getSoundVolume());
                stage.addAction(Actions.sequence(Actions.fadeOut(.6f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                        dispose();
                    }
                })));

            }
        });

        table.add(title).colspan(2).padBottom(27);
        table.row().padBottom(8);
        table.add(volumeMusicLabel);
        table.add(musicVolumeSlider);
        table.row().padBottom(8);
        table.add(musicOnOffLabel);
        table.add(musicCheckbox);
        table.row().padBottom(8);
        table.add(volumeSoundLabel);
        table.add(soundVolumeSlider);
        table.row().padBottom(8);
        table.add(soundOnOffLabel);
        table.add(soundCheckbox);
        table.row();
        table.add(backButton).colspan(2).padTop(23);

        stage.addActor(table);
        stage.addAction(Actions.fadeIn(.6f));
        Gdx.input.setInputProcessor(stage);


    }

    /**
     * Метод отрисовки
     * @param delta - изменение времени
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    //Изменение размера окна
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void resume() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public void hide() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    //Уничтожение экрана
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
