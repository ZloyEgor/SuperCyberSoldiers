package com.stukovegor.scs.Tools;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;

/**
 * Диалоговая рамка, позволяющая выводить анимированный текст диалогов.
 * @see Dialogue
 * @see DialogueNode
 * @author Стуков Егор
 * @version 2.1
 */

public final class DialogueBox extends Table {

    /** Поле начального текста. При создании - пустая строка*/
    private String targetText = "";

    /** Поле таймера анимации */
    private float animTimer = 0f;

    /** Поле, обозначающее полное время анимирования текста*/
    private float animTotalTime = 0f;

    /** Текущее состояние диалог. окна*/
    private State currentState = State.STATIC;

    /**
     * Объект класса {@link Label}, позволяющий выводить текст
     * внутри диалог. окна
     */
    private Label textLabel;

    /** Перечисление состояний диалог. окна*/
    private enum State {
        ANIMATING, STATIC
    }

    /**
     * Конструктор - создание нового диалог. окна
     * @param skin - передаваемый объект класса {@link Skin},
     *             содержащий графическую составляющую пользовательского интерфейса
     */
    DialogueBox(Skin skin){
        super(skin);
        this.setBackground("background");
        textLabel = new Label("\n", skin, "my");
        this.add(textLabel).expandX().bottom().padBottom(8);
    }

    /**
     * @param text - строка, которую нужно будет отрисовать в конечном счете
     */
    void animateText(String text){
        targetText = text;
        float TIME_PER_CHAR = .045f;
        animTotalTime = text.length() * TIME_PER_CHAR;
        currentState = State.ANIMATING;
        animTimer = 0f;
    }

    /**
     * Технический метод, необходимый для отображения текста на {@link DialogueBox#textLabel}
     * @param text - устанавливаемый текст
     */
    private void setText(String text){
        if(!text.contains("\n"))
            text += "\n";
        this.textLabel.setText(text);
    }

    /**
     * Метод обновления диалог. окна
     * @param delta - изменение времени
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if(currentState == State.ANIMATING){
            animTimer += delta;
            if(animTimer >= animTotalTime){
                currentState = State.STATIC;
                animTimer = animTotalTime;
            }
            StringBuilder displayedText = new StringBuilder();

            //Подсчет количества символов, которые нужно вывести на экран
            int charactersToDisplay = (int)((animTimer / animTotalTime)*targetText.length());

            //Построение текста для отображения
            for(int i = 0; i < charactersToDisplay; i++){
                displayedText.append(targetText.charAt(i));
            }

            //Если текст, выведенный на textLabel не равен только что построенному тексту, мы обновляем текст
            if(!displayedText.toString().equals(textLabel.getText().toString())){
                setText(displayedText.toString());
            }
        }
    }


    /**
     * Метод, возвращаемый предпочтительную ширину диалог. окна
     * @return - ширина диалог. окна, которая на 300 пикселей меньше ширины экрана
     */
    @Override
    public float getPrefWidth() {
        return MainClass.WIDTH - 300f;
    }

}
