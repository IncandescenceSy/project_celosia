package io.github.celosia.sys.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Lang {
	public static I18NBundle lang;

    // todo use language setting
	public static void createBundle() {
		lang = I18NBundle.createBundle(Gdx.files.internal("i18n/lang"), Locale.getDefault());
	}

	public static void createBundle(Locale locale) {
		lang = I18NBundle.createBundle(Gdx.files.internal("i18n/lang"), locale);
	}
}
