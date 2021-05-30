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
 * Класс 1-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */
public final class Level1 extends PlayScreen implements Screen {

    public Level1(MainClass game) {
        super(game);
        map = mapLoader.load("maps/Level1.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
        oscar = new Oscar(this, 11, 5, 100);
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicB));

        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    @Override
    public void show() {
        super.show();
        if(game.getPreferences().isMusicEnabled()) {
            music.setLooping(true);
            music.setVolume(game.getPreferences().getMusicVolume());
            music.play();
        }

    }

    @Override
    public void createDialogues() {
        super.createDialogues();
        DialogueNode n1 = new DialogueNode("OSCAR\nWalter, I'm on the spot.\n" +
                "Horrible...How could we allow such madness...", 0);
        DialogueNode n2 = new DialogueNode("LEBOWSKI\nSee you on the screen.\n" +
                "Here the crime rate is off the scale. You gotta\n" +
                "figure it out.", 1);
        DialogueNode n3 = new DialogueNode("LEBOWSKI\nAnd Rundle,\n" +
                "Use force on armed ones only.", 2);
        DialogueNode n4 = new DialogueNode("OSCAR\nUnderstood", 3);
        n4.makeFinal();

        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(1, d);

        n1 = new DialogueNode("CIGARETTE GUY\n" +
                "Hey you, stranger!\n" +
                "I haven't seen you here before, but I should warn you...", 0);
        n2 = new DialogueNode("CIGARETTE GUY\n" +
                "This place is controlled by a dangerous gang.\n" +
                "I do not know where they got it from,\n" +
                "but they have a similar to your weapon.", 1);
        n3 = new DialogueNode("OSCAR\n" +
                "I'm here to figure it out, thank you\n" +
                "for your cooperation, citizen", 2);
        n4 = new DialogueNode("CIGARETTE GUY\n" +
                "Who?", 3);
        n4.makeFinal();

        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(0, d);

        n1 = new DialogueNode("OSCAR\nAre you okay, mister?",0);
        n2 = new DialogueNode("DRUNKARD\n" +
                "Brr... Wht..\n" +
                "Did you come for my pants? Huh...", 1);
        n3 = new DialogueNode("DRUNKARD\n" +
                "Get off, sannuva...", 2);
        n3.makeFinal();

        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        dialogues.put(2, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(2);
        game.setScreen(new Level2(game, oscar.getHp()));
        dispose();
    }


}
