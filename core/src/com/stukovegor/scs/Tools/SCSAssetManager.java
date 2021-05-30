package com.stukovegor.scs.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Класс - обложка для {@link AssetManager}, отвечающий за хранение ресурсов в течение работы программы
 * и за простой к ним доступ
 */
public final class SCSAssetManager extends AssetManager {

    public final AssetManager manager = new AssetManager();

    /** ЗВУКИ */
    public final String shotgunDamaged = "audio/sounds/shotgun_damaged.wav";
    public final String levelCompleted = "audio/sounds/level_completed.mp3";
    public final String rexDamaged = "audio/sounds/rex_damaged.mp3";
    public final String rexShot = "audio/sounds/rex_shot.mp3";
    public final String levelFailed = "audio/sounds/rex_dead.mp3";
    public final String shotgunShot = "audio/sounds/shotgun_shot.mp3";
    public final String shotgunDead = "audio/sounds/shotgun_dead.wav";
    public final String rossShot ="audio/sounds/ross_shot.mp3";
    public final String punkJump ="audio/sounds/punk_jump.wav";
    public final String punkShot ="audio/sounds/punk_shot.wav";
    public final String punkDamaged ="audio/sounds/punk_damaged.wav";
    public final String turretDamaged ="audio/sounds/turret_damaged.mp3";
    public final String turretDead ="audio/sounds/turret_dead.wav";
    public final String turretShot ="audio/sounds/turret_shot.mp3";
    public final String click = "audio/sounds/click.mp3";
    public final String rossDead = "audio/sounds/ross_dead.wav";
    public final String hillSmall = "audio/sounds/hill.wav";
    public final String exploDead = "audio/sounds/explo_dead.wav";
    public final String grenadeExplosion = "audio/sounds/grenade_explosion.wav";

    /** МУЗЫКА */
    public final String musicA = "audio/music/music1.mp3";
    public final String musicB = "audio/music/music2.mp3";
    public final String musicC = "audio/music/music3.mp3";
    public final String musicD = "audio/music/music4.mp3";
    public final String title = "audio/music/title.mp3";
    public final String end = "audio/music/end.mp3";

    /** АТЛАС ТЕКСТУР */
    private String textureAtlas = "atlas.atlas";


    /** Метод, в котором происходит загрузка ресурсов в AssetManager */
    public void loadAssets(){

        //Загрузка звуков
        manager.load(shotgunDamaged, Sound.class);
        manager.load(levelCompleted, Sound.class);
        manager.load(rexDamaged, Sound.class);
        manager.load(rexShot, Sound.class);
        manager.load(levelFailed, Sound.class);
        manager.load(shotgunShot, Sound.class);
        manager.load(shotgunDead, Sound.class);
        manager.load(rossShot, Sound.class);
        manager.load(punkJump, Sound.class);
        manager.load(punkShot, Sound.class);
        manager.load(punkDamaged, Sound.class);
        manager.load(click, Sound.class);
        manager.load(rossDead, Sound.class);
        manager.load(hillSmall, Sound.class);
        manager.load(turretDamaged, Sound.class);
        manager.load(turretDead, Sound.class);
        manager.load(turretShot, Sound.class);
        manager.load(exploDead, Sound.class);
        manager.load(grenadeExplosion, Sound.class);

        //Загрузка атласа текстур
        manager.load(textureAtlas, TextureAtlas.class);

        /*
        ПРИМЕЧАНИЕ:
        Загрузка музыки происходит непосредственно в классе уровня.
        Это объясняется тем, что музыка весит слишком много,
        чтобы постоянно держать её в оперативной памяти
         */
    }

    public TextureAtlas getAtlas(){
        return manager.get(textureAtlas, TextureAtlas.class);
    }
    public void finishLoading(){
        manager.finishLoading();
    }


}
