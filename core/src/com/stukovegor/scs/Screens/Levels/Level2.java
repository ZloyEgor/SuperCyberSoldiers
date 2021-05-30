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
 * Класс 2-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */

public final class Level2 extends PlayScreen implements Screen {

    Level2(MainClass game, int hp) {
        super(game);
        oscar = new Oscar(this, 12, 19, hp);
        hud.updateHp(hp);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    public Level2(MainClass game){
        super(game);
        oscar = new Oscar(this, 12, 19, 100);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    {
        map = mapLoader.load("maps/Level2.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
    }

    @Override
    public void createDialogues() {
        DialogueNode n1 = new DialogueNode("OSCAR\nWalter, my path was blocked by a wide pit\n" +
                "What's next?", 0);
        DialogueNode n2 = new DialogueNode("LEBOWSKI\nWait a moment...", 1);
        DialogueNode n3 = new DialogueNode("LEBOWSKI\nOscar, your sensors recorded a strange activity here.\n" +
                "You have to go down there and explore the territory.", 2);
        DialogueNode n4 = new DialogueNode("OSCAR\nOkay.\n" +
                "Let's jump!", 3);
        n4.makeFinal();

        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(0, d);
     }

    @Override
    public void show() {
        super.show();
            music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicC));
            if(game.getPreferences().isMusicEnabled()) {
                music.setLooping(true);
                music.setVolume(game.getPreferences().getMusicVolume());
                music.play();
        }
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(3);
        game.setScreen(new Level3(game, oscar.getHp()));
        dispose();
    }
}
