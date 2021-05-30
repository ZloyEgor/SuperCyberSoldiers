package com.stukovegor.scs.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс 3-го уровня
 * @see com.stukovegor.scs.Screens.PlayScreen
 * @author Стуков Егор
 */

public final class Level3 extends PlayScreen implements Screen {

    public Level3(com.stukovegor.scs.MainClass game) {
        super(game);
        oscar = new com.stukovegor.scs.Sprites.Oscar(this, 6, 5, 100);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / com.stukovegor.scs.MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / com.stukovegor.scs.MainClass.PPM;
    }

    Level3(com.stukovegor.scs.MainClass game, int hp) {
        super(game);
        oscar = new Oscar(this, 6, 5, hp);
        hud.updateHp(hp);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / com.stukovegor.scs.MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    {
        map = mapLoader.load("maps/Level3.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
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
    public void createDialogues() {
        com.stukovegor.scs.Dialogue.DialogueNode d1 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nWhat the hell?\n" +
                "Lebowski, there are damn cyborgs!", 0);
        com.stukovegor.scs.Dialogue.DialogueNode d2 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nOh no, it's too bad...", 1);
        com.stukovegor.scs.Dialogue.DialogueNode d3 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\nYou said the cyborgs?\n" +
                "I thought they were all destroyed after the war!", 2);
        com.stukovegor.scs.Dialogue.DialogueNode d4 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nWhere did these scums got it?", 3);
        com.stukovegor.scs.Dialogue.DialogueNode d5 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nI don't know, Rundle\n" +
                "But you have to find it out", 4);
        com.stukovegor.scs.Dialogue.DialogueNode d6 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\nModern weapons, cyborgs,\n" +
                "a whole base under the ground.\n" +
                "It all looks like a conspiracy, doesn't it", 5);
        d6.makeFinal();

        com.stukovegor.scs.Dialogue.Dialogue d = new com.stukovegor.scs.Dialogue.Dialogue();
        d.addNode(d1);
        d.addNode(d2);
        d.addNode(d3);
        d.addNode(d4);
        d.addNode(d5);
        d.addNode(d6);
        dialogues.put(0, d);

        d1 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nWalter, I found working computer.", 0);
        d2 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nPerfect.\n" +
                "Can you download information from it?", 1);
        d3 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nEah, give me a moment...", 2);
        d4 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\n...", 3);
        d5 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nDone", 4);
        d6 = new DialogueNode("LEBOWSKI\nGood...\n" +
                "Now get back to the base as soon as possible.", 5);
        d6.makeFinal();

        d = new Dialogue();
        d.addNode(d1);
        d.addNode(d2);
        d.addNode(d3);
        d.addNode(d4);
        d.addNode(d5);
        d.addNode(d6);
        dialogues.put(1, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(4);
        game.setScreen(new Level4(game, oscar.getHp()));
        dispose();
    }
}
