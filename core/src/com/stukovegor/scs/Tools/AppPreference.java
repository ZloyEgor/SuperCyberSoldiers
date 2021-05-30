package com.stukovegor.scs.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Класс, отвечающий за сохранение данных.
 * @author Стуков Егор
 * @version 1.1
 */

public final class AppPreference {

    private static final String PREFS_NAME = "settings";

    /** вкл. / выкл. музыки и звуков */
    private static final String PREF_MUSIC_ENABLED = "music_enabled";
    private static final String PREF_SOUND_ENABLED = "sound_enabled";

    /** громкость звуков и музыки */
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOLUME = "sound";

    /** сохранненный уровень */
    private static final String SAVED_LEVEL = "level";

    private Preferences preferences;


    /** Геттеры и сеттеры */
    private Preferences getPrefs(){
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public boolean isSoundEffectsEnabled(){
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public boolean isMusicEnabled(){
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled){
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public void setMusicEnabled(boolean musicEnabled){
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public void setSavedLevel(int newLevel) {
        getPrefs().putInteger(SAVED_LEVEL, newLevel);
        getPrefs().flush();
    }

    public int getSavedLevel(){
        return getPrefs().getInteger(SAVED_LEVEL, 0);
    }
}
