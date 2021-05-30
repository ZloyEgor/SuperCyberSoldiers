package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.stukovegor.scs.MainClass;

/**
 * Класс экрана с авторами игровой музыки
 * @author Стуков Егор
 */
public class MusicCreditsScreen implements Screen {

    /** Объект главного класса игры */
    private com.stukovegor.scs.MainClass game;

    /**Объект класса {@link Stage} используется как контейнер актёров, т.е таблицы и её содержимого */
    private Stage stage;

    /** Ярлыки */
    private Label creditsLabel, backButtonLabel;

    /** Изображение */
    private Image gameArtImage;

    /** Отрисовщик */
    private SpriteBatch batch;

    /**
     * Конструктор
     * @param game - Объект главного класса
     */
    MusicCreditsScreen(com.stukovegor.scs.MainClass game){

        this.game = game;

        batch = new SpriteBatch();

        stage = new Stage(new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera()), batch);

        //Стандартный стиль текста
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        creditsLabel = new Label("MAIN THEME MUSIC - PIXEL RIVER (BY TREVOR LENTZ)\n\n" +
                                            "PROLOGUE & LEVEL 5 - 8 BIT TITLE OR THEME (BY SHWIGGITY SHWAG)\n\n" +
                                            "LEVEL 1 & LEVEL 4 - OGRE (BY EG)\n\n" +
                                            "LEVEL 2 & LEVEL 3 - SUSPENSE (BY LALANL)\n\n" +
                                            "LEVEL 6 & LEVEL 7 - LONG AWAY HOME (BY NENE)", font);

        backButtonLabel = new Label("BACK", font);

        gameArtImage = new Image(game.getManager().getAtlas().findRegion("openGameArt"));
    }

    @Override
    public void show() {

        Table table = new Table();
        table.setFillParent(true);

        table.setDebug(false);

        table.add(gameArtImage).colspan(2).expandX();
        table.row().padTop(50);
        table.add(creditsLabel).expandX();
        table.row().padTop(50);
        table.add(backButtonLabel).colspan(2).expandX();

        backButtonLabel.addListener(new InputListener(){
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

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        //Эффект проявления
        stage.addAction(Actions.fadeIn(.5f));
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
        batch.dispose();
    }
}
