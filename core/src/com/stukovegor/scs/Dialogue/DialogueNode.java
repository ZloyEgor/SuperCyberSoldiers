package com.stukovegor.scs.Dialogue;

/**
 * Узел диалога.
 * Содержит непосредственно текст сообщения, id и указатель на следующий узел(если данный узел не является конечным)
 * @see Dialogue
 * @author Стуков Егор
 * @version 1.1
 */

public final class DialogueNode {

    /** Строка текста */
    private String text;

    /** Значение id */
    private int id;

    /** Значение указателя на следующий узел*/
    private int pointer;

    /** Истино-ложное выражение конечного узла*/
    private boolean finalNode;

    /**
     * Конструктор - создание нового узла с заданными полями
     * @param id - заданный id
     * @param text - заданный текст узла диалога
     */
    public DialogueNode(String text, int id){
        this.text = text;
        this.id = id;
        this.pointer = id+1;
        this.finalNode = false;
    }

    /**
     * Метод, делающий данный узел конечным
     */
    public void makeFinal(){
        finalNode = true;
        pointer = 0;
    }

    /**
     * Метод получения значения поля {@link DialogueNode#finalNode}
     * @return возвращает boolean в зависимости от того, конечный узел или нет
     */
    public boolean isFinalNode(){
        return finalNode;
    }

    /**
     * Метод получения значения поля {@link DialogueNode#text}
     * @return возвращает текст сообщения
     */
    public String getText() {
        return text;
    }

    /**
     * Метод получения значения поля {@link DialogueNode#id}
     * @return возвращает id узла
     */
    public int getId() {
        return id;
    }

    /**
     * Метод получения значения поля {@link DialogueNode#pointer}
     * @return возвращает указатель на следующий узел
     */
    public int getPointer() {
        return pointer;
    }
}
