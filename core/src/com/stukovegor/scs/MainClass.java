package com.stukovegor.scs;

import com.badlogic.gdx.Game;
import com.stukovegor.scs.Screens.MenuScreen;
import com.stukovegor.scs.Tools.AppPreference;
import com.stukovegor.scs.Tools.SCSAssetManager;
import com.stukovegor.scs.Tools.WorldContactListener;

/**
 * Главный игровой класс.
 * {@link Game} - родителький класс.
 * @author Стуков Егор
 */
public class MainClass extends Game {

	/** Ширина экрана */
	public static final int WIDTH = 600;

	/** Высота экрана */
	public static final int HEIGHT = 384;

	/** Количество пикселей на метр(для Box2D) */
	public static final float PPM = 85;

	/** Идентификаторы врагов */
	public static final int SHOTGUN_ID = 7;
	public static final int PUNK_ID = 14;

	/** Степени двойки для идентификации тел
	 * @see WorldContactListener */
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short ENEMY_BIT = 4;
	public static final short OBJECT_BIT = 8;
	public static final short LASER_OF_PLAYER_BIT = 16;
	public static final short LASER_OF_ENEMY_BIT = 32;
	public static final short FRONT_OF_ENEMY_BIT = 64;
	public static final short DEAD_BIT = 128;
	public static final short LEVEL_FINISH_BIT = 256;
	public static final short MESSAGE_BIT = 512;
	public static final short HILL_BIT = 1024;
	public static final short WEAK_POINT_BIT = 2048;
	public static final short DEATH_BIT = 4096;
	public static final short BORDER_BIT = 8192;

	private com.stukovegor.scs.Tools.SCSAssetManager manager;
	private com.stukovegor.scs.Tools.AppPreference preferences;

	/** Создание класса */
	@Override
	public void create () {
	    manager = new com.stukovegor.scs.Tools.SCSAssetManager();
	    preferences = new com.stukovegor.scs.Tools.AppPreference();

	    manager.loadAssets();
	    manager.finishLoading();

	    //При создании устанавливаем экран меню
		setScreen(new MenuScreen(this));
	}

	/** Отрисовка */
	@Override
	public void render () {
	    super.render();
	}

	/** Уничтожение */
	@Override
	public void dispose () {
	}

	/** Получить менеджер ресурсов */
	public SCSAssetManager getManager() {
	    return manager;
	}

	/** Получить сохранения */
    public AppPreference getPreferences() {
        return preferences;
    }
}

