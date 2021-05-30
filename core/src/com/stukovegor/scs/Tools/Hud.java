package com.stukovegor.scs.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;

/**
 * Класс пользовательского игрового интерфейса, на котором отображаются диалоги и количество очков здоровья.
 * @see Dialogue
 * @see DialogueNode
 * @author Стуков Егор
 * @version 2.1
 */
public final class Hud {

    /** Сцена */
    private Stage stage;

    /** Окно просмотра камеры */
    private Viewport viewport;

    /** Окно диалогов */
    private com.stukovegor.scs.Tools.DialogueBox dialogueBox;

    /** Таблица */
    private Table table;

    /** Объект, содержащий графическую часть пользовательского интерфейса */
    private Skin skin;

    /** Ярлык очков здоровья */
    private Label hpLabel;

    /** Отображается ли диалог? */
    private boolean dialogueDisplayed;

    /** Диалог */
    private Dialogue dialogue;

    /** Текущий узел диалога */
    private DialogueNode currentNode;

    /**
     * Конструктор
     * @param sb - отрисовщик
     */
    public Hud(SpriteBatch sb){
        viewport = new StretchViewport(MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        hpLabel = new Label(String.valueOf(100), new Label.LabelStyle(new BitmapFont(), new Color(0x0c725aff)));

        dialogueDisplayed = false;
        dialogue = new Dialogue();

        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        NinePatch background = new NinePatch(new Texture("skin/background.9.png"), 24, 16, 24, 16);
        skin.add("background", background);

        table = new Table();
        table.setFillParent(true);
        table.right().top();

        //+++++=====ОТЛАДКА ПОЛЬЗОВАТЕЛЬСКОГО ИНТЕРФЕЙСА=====+++++\\
        table.setDebug(false);
        //+++++==============================================+++++\\

        Label healthTextLabel = new Label("HEALTH", new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));

        table.add().size(515, 8);
        table.add(healthTextLabel).pad(10, 0, 8, 20);
        table.row();
        table.add();
        table.add(hpLabel).spaceRight(8);
        table.row();

        stage.addActor(table);

        // multiplexer позволяет делить обработку пользовательского ввода между несколькими процессорами ввода
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(Gdx.input.getInputProcessor());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /** Метод обновления очков здоровья */
    public void updateHp(int newHp){
        if(newHp <30)
            hpLabel.getStyle().fontColor = new Color(0x850707ff);
        else if(newHp < 60)
            hpLabel.getStyle().fontColor = new Color(0xb6af09ff);
        else if(newHp <= 100)
            hpLabel.getStyle().fontColor = new Color(0x0c725aff);
        hpLabel.setText(String.valueOf(newHp));
    }

    /** Показать диалог */
    public void showDialog(){
        dialogueBox = new DialogueBox(skin);
        dialogueBox.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (currentNode.isFinalNode()) {
                        hideDialog();
                    } else {
                        currentNode = dialogue.getNode(currentNode.getPointer());
                        if (dialogueDisplayed)
                            dialogueBox.animateText(currentNode.getText());
                    }
                return false;
            }
        });
        dialogueBox.animateText(currentNode.getText());
        dialogueDisplayed = true;

        table.row();
        table.add(dialogueBox)
                .expandX()
                .center()
                .padTop(210f)
                .colspan(2);
    }

    /** Спрятать диалог */
    private void hideDialog(){
        Cell cell = table.getCell(dialogueBox);
        table.removeActor(dialogueBox);
        table.removeActor(dialogueBox);
        table.getCells().removeValue(cell, true);
        table.invalidate();
        dialogueDisplayed = false;
    }

    /** Метод, отвечающий за отрисовку интерфейса */
    public void draw(){
        stage.act();
        stage.draw();
    }

    /** Изменить размер окна */
    public void resize(int width, int height){
        viewport.update(width, height);
    }

    /** Не видно ли диалогового окна? */
    public boolean isDialogueNotDisplayed() {
        return !dialogueDisplayed;
    }

    /** Устанавливаем диалог */
    public void setDialogue(Dialogue dialogue){
        this.dialogue = dialogue;
        currentNode = dialogue.getNode(dialogue.getStart());
    }

    /** Получить сцену */
    public Stage getStage() {
        return stage;
    }
}
