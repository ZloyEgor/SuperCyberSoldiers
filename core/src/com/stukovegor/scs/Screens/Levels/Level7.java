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
import com.stukovegor.scs.Screens.ThanksScreen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс 7-го уровня
 * @see PlayScreen
 * @author Стуков Егор
 */

public final class Level7 extends PlayScreen implements Screen {

    public Level7(MainClass game) {
        super(game);
        oscar = new Oscar(this, 6, 8, 100, Oscar.Status.COMBAT);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    Level7(MainClass game, int hp) {
        super(game);
        oscar = new Oscar(this, 6, 8, hp, Oscar.Status.COMBAT);
        hud.updateHp(hp);
        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / MainClass.PPM;
        gameCamera.position.y = oscar.getB2body().getPosition().y - oscar.getHeight() / 2 + 128 / MainClass.PPM;
    }

    {
        map = mapLoader.load("maps/Level7.tmx");
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
                "Damn...\n" +
                "It seems that there's no one except bandits.\n" +
                "But I discovered something like a lab here.", 0);
        DialogueNode n2 = new DialogueNode("OSCAR\n" +
                "Apparently those who worked here\n" +
                "have already been evacuated.", 1);

        DialogueNode n3 = new DialogueNode("OSCAR\n" +
                "Walter?", 2);
        DialogueNode n4 = new DialogueNode("RADIO LINK\n" +
                "...", 3);
        DialogueNode n5 = new DialogueNode("OSCAR\n" +
                "Lebowski?\n" +
                "..." +
                "Anyone?", 4);
        DialogueNode n6 = new DialogueNode("OSCAR\n" +
                "Oh, no...." +
                "I... .\n" +
                "I should go to the base.", 5);
        n6.makeFinal();

        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        d.addNode(n5);
        d.addNode(n6);

        dialogues.put(0, d);
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(0);
        game.setScreen(new ThanksScreen(game));
        dispose();

    }

    @Override
    public void playFinishSound() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    protected void setBackground() {
        backgroundStage = new Stage(new StretchViewport(MainClass.WIDTH, MainClass.HEIGHT), batch);
        Image backgroundImg = new Image(getManager().getAtlas().findRegion("background1"));
        backgroundImg.setSize(MainClass.WIDTH, MainClass.HEIGHT);
        backgroundStage.addActor(backgroundImg);
    }

}
