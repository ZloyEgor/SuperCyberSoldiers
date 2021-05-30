package com.stukovegor.scs.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.stukovegor.scs.Dialogue.Dialogue;
import com.stukovegor.scs.Dialogue.DialogueNode;
import com.stukovegor.scs.MainClass;
import com.stukovegor.scs.Screens.PlayScreen;
import com.stukovegor.scs.Sprites.Oscar;
import com.stukovegor.scs.Tools.WorldCreator;

/**
 * Класс уровня-пролога
 * @see com.stukovegor.scs.Screens.PlayScreen
 * @author Стуков Егор
 */

public final class Level0 extends PlayScreen {


    public Level0(com.stukovegor.scs.MainClass game) {
        super(game);
        map = mapLoader.load("maps/Level0.tmx");
        renderer.setMap(map);
        creator = new WorldCreator(this);
        oscar = new com.stukovegor.scs.Sprites.Oscar(this, 24, 8, 100, Oscar.Status.WEAK);
        music = Gdx.audio.newMusic(Gdx.files.internal(manager.musicA));

        gameCamera.position.x = oscar.getB2body().getPosition().x - oscar.getWidth() / 2 + 156 / com.stukovegor.scs.MainClass.PPM;
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


        com.stukovegor.scs.Dialogue.DialogueNode n1 = new com.stukovegor.scs.Dialogue.DialogueNode("YOU\nOh... What, where am I? Hey, you!\n" +
                "Who are you and what the hell I am doing here?!\n" +
                "[tap the text to continue]", 0);
        com.stukovegor.scs.Dialogue.DialogueNode n2 = new com.stukovegor.scs.Dialogue.DialogueNode("SCIENTIST\nLieutenant Oscar Rundle, please calm down.\n" +
                "Short-term memory loss is a side effect after cryogenic freezing", 1);
        com.stukovegor.scs.Dialogue.DialogueNode n3 = new com.stukovegor.scs.Dialogue.DialogueNode("SCIENTIST\nCan you tell the last date that you remember\n" +
                "from your life and the event associated with this day, please", 2);
        com.stukovegor.scs.Dialogue.DialogueNode n4 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nYeah, November 23, 1997\n" +
                "The invasion of Ottawa... I was in a special squad...", 3);
        com.stukovegor.scs.Dialogue.DialogueNode n5 = new com.stukovegor.scs.Dialogue.DialogueNode("SCIENTIST\nLieutenant, the invasion failed.\n" +
                "Formally, you and your whole group are dead", 4);
        com.stukovegor.scs.Dialogue.DialogueNode n6 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nIt...\nIt's impossible...", 5);
        com.stukovegor.scs.Dialogue.DialogueNode n7 = new com.stukovegor.scs.Dialogue.DialogueNode("SCIENTIST\nBut we saved your life by the latest achievements of science....\n" +
                "And now you are the CYBER-SOLDIER - half man, half machine!\n" +
                "Please go ahead for further instructions.", 6);
        n7.makeFinal();

        com.stukovegor.scs.Dialogue.Dialogue d = new com.stukovegor.scs.Dialogue.Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        d.addNode(n5);
        d.addNode(n6);
        d.addNode(n7);
        dialogues.put(0, d);

        n1 = new com.stukovegor.scs.Dialogue.DialogueNode("SCIENTIST\nWelcome back, Lieutenant Rundle!\n" +
                "Please allow me to bring you up to date.", 0);
        n2 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nOh, I'm sorry. I forget to introduce myself.\n" +
                "My name is Walter Lebowski and I will become your assistant in the 'Peacemaker' project", 1);
        n3 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nAs you can see on the map, nuclear wars have destroyed\n" +
                "almost all the major settlements on the planet", 2);
        n4 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nHoly crap...\nWhat a madness is going on this planet...",3);
        n5 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nEah, it is the real madness!\n" +
                "But let me continue.",4);
        n6 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nAccording to the 'Peacemaker' project, your task will be to support public order\n" +
                "and to maintain the reduction of crime", 5);
        n7 = new com.stukovegor.scs.Dialogue.DialogueNode("LEBOWSKI\nAnd now, please go on to get the equipment and next instructions", 6);
        n7.makeFinal();

        d = new com.stukovegor.scs.Dialogue.Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        d.addNode(n5);
        d.addNode(n6);
        d.addNode(n7);
        dialogues.put(1, d);


        n1 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\nI can not believe my eyes!\nIs it Oscar Rundle himself?!", 0);
        n2 = new com.stukovegor.scs.Dialogue.DialogueNode("OSCAR\nColonel Brander, I am very glad to meet you again.\n" +
                "What are you doing here?\n" +
                "And what... what year is it now?", 1);
        n3 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\n" +
                "Wow, your brain has been frozen well, yeah?\n" +
                "It has been about 10 years after the end of nuclear war.", 2);
        n4 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\n" +
                "As you see, the war brought only devastation and horror.\n" +
                "In addition, our planet was badly damaged.", 3);
        n5 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\n" +
                "But there is nothing irreparable, yeah?\n" +
                "Therefore, these scientists have developed the project 'Peacemaker'", 4);
        n6 = new com.stukovegor.scs.Dialogue.DialogueNode("BRANDER\nNow change your equipment \n" +
                "and get ready for your first task", 5);
        n7 = new DialogueNode("BRANDER\nIt is time to serve your country, cyber-soldier!", 6);
        n7.makeFinal();

        d = new Dialogue();
        d.addNode(n1);
        d.addNode(n2);
        d.addNode(n3);
        d.addNode(n4);
        d.addNode(n5);
        d.addNode(n6);
        d.addNode(n7);
        dialogues.put(2, d);
    }

    @Override
    public void playFinishSound() {
        //Здесь музыка не нужна
    }

    @Override
    public void setNextLevel() {
        game.getPreferences().setSavedLevel(1);
        game.setScreen(new Level1(game));
        dispose();
    }
}
