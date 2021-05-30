package com.stukovegor.scs.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stukovegor.scs.Screens.PlayScreen;

/**
 * Класс контроллера. Обрабатывает нажатия пользователя на кнопки
 */

public final class Controller {

    /** Окно просмотра камеры */
    private Viewport viewport;

    /** Пауза */
    private Dialog pause;

    /** Сцена контроллера */
    public Stage stage;

    /** Флаги кнопок */
    private boolean upPressed,
                shotPressed,
                leftPressed,
                rightPressed,
                downPressed,
                pausePressed;

    /**
     * Конструктор
     */
    public Controller(PlayScreen screen){

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Skin skin = new Skin(Gdx.files.internal("skin/skin.json"));

        pause = new Dialog("", skin, "dialog") {

            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            public void result(Object obj) {
                if(obj.equals(true)) {
                    pausePressed = false;
                }
                else
                    Gdx.app.exit();
            }
        };
        pause.text("PAUSE");
        pause.button("CONTINUE", true); //Отправляет true в качестве результата
        pause.button("EXIT", false); //Отправляет false в качестве результата
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        TextureRegion textures = screen.getManager().getAtlas().findRegion("buttons_pack");

        int buttonWidth = (textures.getRegionWidth() - 40) / 5;
        int buttonHeight = textures.getRegionHeight();

        viewport = new StretchViewport(500, 334, new OrthographicCamera());
        stage = new Stage(viewport, screen.getBatch());

        // ТОЛЬКО ДЛЯ ВЕРСИИ ДЛЯ КОМПЬЮТЕРА
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                    case Input.Keys.SPACE:
                        shotPressed = true;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = true;
                        break;
                    case Input.Keys.Q:
                        pausePressed = !pausePressed;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                    case Input.Keys.SPACE:
                        shotPressed = false;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = false;
                        break;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        //КНОПКА ВВЕРХ
        Image upImg = new Image(new TextureRegion(textures, buttonWidth *2, 0, buttonWidth, buttonHeight));
        upImg.setSize(buttonWidth, buttonHeight);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        //КНОПКА ВПРАВО
        Image rightImg = new Image(new TextureRegion(textures, buttonWidth, 0, buttonWidth, buttonHeight));
        rightImg.setSize(buttonWidth, buttonHeight);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        //КНОПКА ВЛЕВО
        Image leftImg = new Image(new TextureRegion(textures,0, 0, buttonWidth, buttonHeight));
        leftImg.setSize(buttonWidth, buttonHeight);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        //КНОПКА ВЫСТРЕЛ
        Image shotImg = new Image(new TextureRegion(textures, buttonWidth *3, 0, buttonWidth, buttonHeight));
        shotImg.setSize(buttonWidth, buttonHeight);
        shotImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shotPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shotPressed = false;
            }
        });

        //КНОПКА ПРИСЕСТЬ
        Image downImg = new Image(new TextureRegion(textures, buttonWidth * 4, 0, buttonWidth, buttonHeight));
        downImg.setSize(buttonWidth, buttonHeight);
        downImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        //КНОПКА ПАУЗЫ
        Image pauseImg = new Image(new TextureRegion(textures, buttonWidth * 5, 36, 40, 24));
        pauseImg.setSize(35, 23);
        pauseImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                pausePressed = !pausePressed;

                if(pausePressed)
                    pause.show(stage);

                return true;
            }


        });

        Table table = new Table();
        table.left().bottom();

        //+++++=====ОТЛАДКА РАСПОЛОЖЕНИЯ КНОПОК=====+++++\\
        table.setDebug(false);
        //+++++=====================================+++++\\


        table.add(pauseImg).size(pauseImg.getWidth(), pauseImg.getHeight()).colspan(4).left().top();
        table.row();
        table.add(shotImg).size(shotImg.getWidth(), shotImg.getHeight()).colspan(4).padLeft(380).padTop(167).spaceLeft(643).right();
        table.row().pad(5, 10, 5, 0);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).spaceLeft(10);
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight()).padLeft(200);
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight()).padLeft(10);
        table.padBottom(10);

        stage.addActor(table);
    }

    /** Отрисовка */
    public void draw(){
        stage.act();
        stage.draw();
    }

    /** Геттеры */
    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isShotPressed() {
        return shotPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isPausePressed() {
        return pausePressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void showPause(){
        pause.show(stage);
        pausePressed = true;
    }
}
