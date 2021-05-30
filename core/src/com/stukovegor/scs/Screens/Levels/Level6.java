package com.stukovegor.scs.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.badlogic.gdx.Screen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс 6-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */

public final class Level6 extends PlayScreen implements Screen {

    public Level6(MainClass game) {
        super(game);
        oscar = new Oscar(this, 10, 10, 100, Oscar.Status.COMBAT);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;

        map = mapLoader.load("maps/Level6.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicD));
        if(game.getPreferences().isMusicEnabled()) {
            music.setLooping(true);
            music.setVolume(game.getPreferences().getMusicVolume());
            music.play();
        }
    }

    @Override
    public void createDialogues() {
        DialogueNode n1 = new DialogueNode("OSCAR\n" +
                "Interesting, climbed right into the den to these assholes\n" +
                "and arrived in the paradise-like place.", 0);
        DialogueNode n2 = new DialogueNode("OSCAR\n" +
                "Walter, did you say that the war destroyed\n" +
                "almost all major settlements didn't you? ", 1);
        DialogueNode n3 = new DialogueNode("OSCAR\n" +
                "And what about these islands?", 2);
        DialogueNode n4 = new DialogueNode("LEBOWSKI\n" +
                "Evidently our data was wrong.\n" +
                "It's one more reason to check this place, Oscar.", 3);
        n4.makeFinal();

        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(0, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(7);
        game.setScreen(new Level7(game, oscar.getHp()));
        dispose();
    }

    @Override
    protected void setBackground() {
        backgroundStage = new Stage(new StretchViewport(MainClass.WIDTH, MainClass.HEIGHT), batch);
        Image backgroundImg = new Image(getManager().getAtlas().findRegion("background1"));
        backgroundImg.setSize(MainClass.WIDTH, MainClass.HEIGHT);
        backgroundStage.addActor(backgroundImg);
    }
}
