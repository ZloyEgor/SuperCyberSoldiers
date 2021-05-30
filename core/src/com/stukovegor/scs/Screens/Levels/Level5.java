package com.stukovegor.scs.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.badlogic.gdx.Screen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс 5-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */

public final class Level5 extends PlayScreen implements Screen {

    public Level5(MainClass game) {
        super(game);
        oscar = new Oscar(this, 8, 9, 100, Oscar.Status.NEUTRAL);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;

        map = mapLoader.load("maps/Level5.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicA));
        if(game.getPreferences().isMusicEnabled()) {
            music.setLooping(true);
            music.setVolume(game.getPreferences().getMusicVolume());
            music.play();
        }
    }

    @Override
    public void createDialogues() {
        super.createDialogues();

        DialogueNode n1 = new DialogueNode("LEBOWSKI\n" +
                "Welcome back, Oscar.\n" +
                "You have done very important job.", 0);
        DialogueNode n2 = new DialogueNode("LEBOWSKI\n" +
                "We assumed that these scums were led\n" +
                "by the madness of the apocalypse.", 1);
        DialogueNode n3 = new DialogueNode("LEBOWSKI\n" +
                "But it turned out that a group\n" +
                "from the Caribbean islands is in response\n" +
                "of their actions.", 2);
        DialogueNode n4 = new DialogueNode("LEBOWSKI\n" +
                "We also assume that they may have a nuclear weapon.\n" +
                "Therefore, to conduct a full-scale military operation\n" +
                "would be too risky.", 3);
        DialogueNode n5 = new DialogueNode("OSCAR\n" +
                "Damn...", 4);
        DialogueNode n6 = new DialogueNode("OSCAR\n" +
                "Well then...\n" +
                "How can I help?", 5);
        DialogueNode n7 = new DialogueNode("LEBOWSKI\n" +
                "You must take part in a special operation.\n" +
                "We will land you on the territory of the Caribbean islands.", 6);
        n7.makeFinal();

        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        d.addNode(n5);
        d.addNode(n6);
        d.addNode(n7);
        dialogues.put(0, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(6);
        game.setScreen(new Level6(game));
        dispose();
    }
}
