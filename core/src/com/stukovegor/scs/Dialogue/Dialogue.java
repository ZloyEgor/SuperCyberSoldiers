package com.stukovegor.scs.Dialogue;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс диалога.
 * Сам диалог - ассоциативный массив, содержащий в себе объекты класса {@link DialogueNode}.
 * @see DialogueNode
 * @author Стуков Егор
 * @version 1.1
 */

public final class Dialogue {

    /** Ассоциативный массив, содержащий объекты класса {@link DialogueNode}*/
    private Map<Integer, DialogueNode> nodes = new HashMap<Integer, DialogueNode>();

    /**
     * Метод добавления узла диалога непосредственно в диалог.
     * Ключом узла диалога в nodes является id, получаемое методом узла {@link DialogueNode#getId()}
     * @param node - узел, который необходимо добавить
     */
    public void addNode(DialogueNode node){
        this.nodes.put(node.getId(), node);
    }

    /**
     * Метод получения узла диалога по получаемому параметру
     * @param id - ключ, по которому в массиве {@link Dialogue#nodes} берётся узел
     * @return возвращаем найденный по ключу узел диалога
     */
    public DialogueNode getNode(int id){
        return nodes.get(id);
    }

    /**
     * @return возвращаем ключ массива {@link Dialogue#nodes}, обозначающий начало диалога
     */
    public int getStart(){
        return 0;
    }

}
