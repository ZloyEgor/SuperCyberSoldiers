package com.stukovegor.scs.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.MenuScreen;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс обучающего уровня
 * @see PlayScreen
 * @author Стуков Егор
 */
public final class Tutorial extends PlayScreen {

    public Tutorial(MainClass game) {
        super(game);
        map = mapLoader.load("maps/Tutorial.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
        oscar = new Oscar(this, 7, 5, 100, Oscar.Status.IMMORTAL);
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicA));

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

        DialogueNode n1 = new DialogueNode("SCIENTIST\n" +
                "Welcome to the 'Peacemaker' project`s\n" +
                "training course!\n" +
                "[Tap the text to continue]", 0);
        DialogueNode n2 = new DialogueNode("SCIENTIST\n" +
                "To get the best experience of this course,\n" +
                "please follow all our instructions", 1);
        DialogueNode n3 = new DialogueNode("SCIENTIST\n" +
                "Since we 'cybernated' your body,\n" +
                "walking can be unusual.", 2);
        DialogueNode n4 = new DialogueNode("SCIENTIST\n" +
                "Try to go forward please\n" +
                "[Move left and right using arrow buttons\b" +
                "in the lower left corner of the screen]", 3);
        n4.makeFinal();
        Dialogue d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(0, d);

        n1 = new DialogueNode("SCIENTIST\n" +
                "Excellent,\n" +
                "you are doing well...", 0);
        n2 = new DialogueNode("SCIENTIST\n" +
                "...but in order to move through the ruins of cities,\n" +
                "you need to learn how to jump as well.\n", 1);
        n3 = new DialogueNode("SCIENTIST\n" +
                "Try to get through these boxes.\n" +
                "[Press the arrow button pointing upwards to jump]", 2);
        n3.makeFinal();
        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        dialogues.put(1, d);

        n1 = new DialogueNode("SCIENTIST\n" +
                "Taking part in the 'Peacemaker' project implies\n" +
                "confrontation with dangerous inhabitants...", 0);
        n2 = new DialogueNode("SCIENTIST\n" +
                "...that`s why you must learn to use your laser gun.\n" +
                "And learn how not to be killed, of course.", 1);
        n3 = new DialogueNode("SCIENTIST\n" +
                "Don`t forget to dodge shells.", 2);
        n4 = new DialogueNode("[Click on the gun button to shoot,\n" +
                "jump or crouch to dodge lasers]", 3);
        n4.makeFinal();
        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        dialogues.put(2, d);

        n1 = new DialogueNode("SCIENTIST\n" +
                "If you get hurt in a fight, you can use first-aid kits\n" +
                "left over from the distribution of medicine.", 0);
        n2 = new DialogueNode("[If your health state is not in normal,\n" +
                "the first-aid kit near you will be used automatically]", 1);
        n3 = new DialogueNode("SCIENTIST\n" +
                "Congratulations!\n" +
                "Training course is completed successfully!\n" +
                "Now, go ahead for further orders.", 2);
        n3.makeFinal();
        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        dialogues.put(3, d);
    }

    @Override
    public void setNextLevel() {
        super.setNextLevel();
        game.setScreen(new MenuScreen(game));
        dispose();
    }
}
