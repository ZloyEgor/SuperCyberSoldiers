package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.stukovegor.scs.MainClass;

/**
 * Класс финального экрана
 * @author Стуков Егор
 */

public final class ThanksScreen implements Screen {

    /** Объект главного класса игры */
    private com.stukovegor.scs.MainClass game;

    /**Объект класса {@link Stage} используется как контейнер актёров, т.е таблицы и её содержимого */
    private Stage stage;

    /** Ярлыки */
    private Label titleLabel, thanksLabel;

    /** Изображение */
    private Image itSchoolImage;

    /** Музыка */
    private Music endMusic;

    /** Отрисовщик */
    private SpriteBatch batch;

    /**
     * Конструктор
     * @param game - Объект главого класса
     */
    public ThanksScreen(com.stukovegor.scs.MainClass game) {

        this.game = game;

        batch = new SpriteBatch();

        stage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera()), batch);

        //Стандартный стиль текста
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        titleLabel = new Label("THANK YOU FOR PLAYING!", font);

        thanksLabel = new Label("This game was developed as a project\n" +
            "specifically for the Samsung IT School\n" +
            "using the LibGDX library.", font);

        itSchoolImage = new Image(game.getManager().getAtlas().findRegion("samsung"));

        endMusic = Gdx.audio.newMusic(Gdx.files.internal(game.getManager().end));

        if(game.getPreferences().isMusicEnabled()) {
            endMusic.setLooping(false);
            endMusic.setVolume(game.getPreferences().getMusicVolume());
            endMusic.play();
        }
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);

        table.setDebug(false);

        table.add(titleLabel).expandX().colspan(2);
        table.row().padTop(30);
        table.add(thanksLabel).expandX();
        table.row().padTop(50);
        table.add(itSchoolImage).expandX().colspan(2);

        table.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1.2f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                        dispose();
                    }
                })));
                return true;
            }
        });

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        //Эффект проявления
        stage.addAction(Actions.fadeIn(1.3f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

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

    @Override
    public void dispose() {
        stage.dispose();
        endMusic.dispose();
        batch.dispose();
    }
}
