package io.github.celosia.sys.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Lang {
    public static I18NBundle lang;

    public static void createBundle() {
        lang = I18NBundle.createBundle(Gdx.files.internal("i18n/lang"), new Locale("en", "US"));
    }

    public static void createBundle(Locale locale) {
        lang = I18NBundle.createBundle(Gdx.files.internal("i18n/lang"), locale);
    }
}
