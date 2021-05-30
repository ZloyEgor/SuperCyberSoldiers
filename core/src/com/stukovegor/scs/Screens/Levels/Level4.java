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
 * Класс 4-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */

public final class Level4 extends PlayScreen implements Screen {

    public Level4(MainClass game) {
        super(game);
        oscar = new Oscar(this, 8, 39, 100);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    Level4(MainClass game, int hp) {
        super(game);
        oscar = new Oscar(this, 8, 39, hp);
        hud.updateHp(hp);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    {
        map = mapLoader.load("maps/Level4.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicB));
        if(game.getPreferences().isMusicEnabled()) {
            music.setLooping(true);
            music.setVolume(game.getPreferences().getMusicVolume());
            music.play();
        }
    }

    @Override
    public void createDialogues() {

        DialogueNode n1 = new DialogueNode("OSCAR\n" +
                "Walter, I got out.\n" +
                "It looks like I'm on the roof of some building.", 0);
        DialogueNode n2 = new DialogueNode("LEBOWSKI\n" +
                "Got it. We sent a helicopter.\n" +
                "Try to move to a safer place, we are tracking you.", 1);
        DialogueNode n3 = new DialogueNode("OSCAR\n" +
                "Understood.", 2);
        n3.makeFinal();
        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        dialogues.put(0, d);

        n1 = new DialogueNode("BRANDER\n" +
                "I`m very glad to see you alive, Rundle!", 0);
        n2 = new DialogueNode("BRANDER\n" +
                "One of the scientists said that data you\n" +
                "transmitted contains a serious evidence.", 1);
        n3 = new DialogueNode("BRANDER\n" +
                "Hurry up, soldier!\n" +
                "We have to go to the base right now.\n" +
                "Get into the helicopter.", 2);
        n3.makeFinal();

        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        dialogues.put(1, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(5);
        game.setScreen(new Level5(game));
        dispose();
    }
}
