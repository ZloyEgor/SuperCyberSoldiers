package com.stukovegor.scs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.Viewport;
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

/**
 * Класс "Конец игры" экрана.
 * Реализует интерфейс {@link Screen}
 * @see AppPreference
 * @author Стуков Егор
 * @version 1.4
 */
public final class GameOverScreen implements Screen {

    /**Объект класса {@link Stage} используется как контейнер актёров, т.е таблицы и её содержимого */
    private Stage stage;

    /** Главный класс игры */
    private com.stukovegor.scs.MainClass game;

    /** Ярлыки, которые будут выступать в качестве кнопок с текстом */
    private Label retryLabel;
    private Label menuLabel;

    /** Отрисовщик */
    private SpriteBatch batch;

    GameOverScreen(com.stukovegor.scs.MainClass game){
        this.game = game;

        batch = new SpriteBatch();

        //Окно просмотра камеры
        Viewport viewport = new StretchViewport(com.stukovegor.scs.MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        //Стиль текста
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        //Создаём таблицу
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //+++++=====ОТЛАДКА=====+++++\\
        table.setDebug(false);
        //+++++=================+++++\\

        //Текст на экране
        Label gameOverLabel = new Label("YOU DIED, GAME OVER", new Label.LabelStyle(new BitmapFont(), new Color(0x850707ff)));

        retryLabel = new Label("RETRY", font);
        menuLabel = new Label("RETURN TO MENU", font);

        //Изображение на экране
        Image deadRexImage = new Image(game.getManager().getAtlas().findRegion("deadRex"));

        //Заполняем таблицу
        table.add(gameOverLabel).expandX();
        table.row().padTop(40);
        table.add(deadRexImage);
        table.row().padTop(40);
        table.add(retryLabel).expandX();
        table.row().padTop(30);
        table.add(menuLabel).expandX();


        //Добавляем таблицу в curtainStage
        stage.addActor(table);

        //Устанавливаем InputProcessor, который будет принимать пользовательксий ввод на данном экране
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        //Устанавливаем слушатель на ярлык retryLabel
        retryLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        //Запускаем последний сохранённый уровень...
                        switch(game.getPreferences().getSavedLevel()){
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
                        //...и уничтожаем данный экран
                        dispose();
                    }
                })));
                return true;
            }
        });

        //Устанавливаем слушатель на ярлык menuLabel
        menuLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        //Устанавливаем экран главного меню...
                        game.setScreen(new MenuScreen(game));
                        //...и уничтожаем данный экран
                        dispose();
                    }
                })));

                return true;
            }

        });


    }

    /** Отрисовка экрана */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, .8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    /** Изменение размера экрана */
    @Override
    public void resize(int width, int height) {
        //НЕ ИСПОЛЬЗУЕТСЯ
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
        batch.dispose();
    }

}
