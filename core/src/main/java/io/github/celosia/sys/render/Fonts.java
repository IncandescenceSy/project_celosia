package io.github.celosia.sys.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.github.tommyettinger.textra.Font;

import java.util.HashMap;
import java.util.Map;

import static io.github.celosia.Main.atlasIcons;
import static io.github.celosia.Main.atlasPrompts;

// todo refactor
// todo save fonts to file after generation without it being hideous looking maybe??
public class Fonts {
	// Battle log
	static FontSize koruri20;

	// Most normal text
	static FontSize koruri30;

	// Main menu, popup title, turn label
	static FontSize koruri80;

	// Input guide
	// todo is the outline actually needed? just dont have light backgrounds,
	static FontSize koruriBorder20;

	public static void createFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("koruri.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		StringBuilder builder = new StringBuilder();

		// Hiragana
		// todo fix (doesnt work)
		// for(char c = 0x3041; c <= 0x309f; c++) builder.append(c);

		// Katakana
		// for(char c = 0x30a0; c <= 0x30ff; c++) builder.append(c);

		// Additional symbols
		builder.append("∞←→↑↓");

		parameter.characters += builder;

		parameter.magFilter = Texture.TextureFilter.Linear;
		parameter.minFilter = Texture.TextureFilter.Linear;

		parameter.color = Color.WHITE;

		// parameter.gamma = 2f;
		// parameter.renderCount = 3;

		// parameter.packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2,
		// false, new PixmapPacker.SkylineStrategy());
		// BitmapFontWriter.FontInfo info = new BitmapFontWriter.FontInfo();
		// info.padding = new BitmapFontWriter.Padding(1, 1, 1, 1);

		// long t1 = System.currentTimeMillis();
		parameter.size = 20;
		koruri20 = new FontSize(new Font(generator.generateFont(parameter)), 20); // ~68-80ms
		// long t2 = System.currentTimeMillis();
		// Gdx.app.log("koruri20 gen in ", (t2 - t1) + "ms");
		parameter.borderWidth = 2;
		koruriBorder20 = new FontSize(new Font(generator.generateFont(parameter)), 20); // ~20-25ms
		// t1 = System.currentTimeMillis();
		// Gdx.app.log("koruriBorder20 gen in ", (t1 - t2) + "ms");
		parameter.borderWidth = 0;
		parameter.size = 30;
		koruri30 = new FontSize(new Font(generator.generateFont(parameter)), 30); // ~10-12ms
		// t2 = System.currentTimeMillis();
		// Gdx.app.log("koruri30 gen in ", (t2 - t1) + "ms");
		parameter.size = 80;
		koruri80 = new FontSize(new Font(generator.generateFont(parameter)), 80); // ~18-23ms
		// t1 = System.currentTimeMillis();
		// Gdx.app.log("koruri80 gen in ", (t1 - t2) + "ms");

		// Save to file

		/*
		 * parameter.size = 20; FreeTypeFontGenerator.FreeTypeBitmapFontData data =
		 * generator.generateData(parameter); BitmapFontWriter.writeFont(data, new
		 * String[] {"fnt/koruri_20.png"}, Gdx.files.absolute("fnt/koruri_20.fnt"),
		 * info, 512, 512); BitmapFontWriter.writePixmaps(parameter.packer.getPages(),
		 * Gdx.files.absolute("fnt"), "koruri_20");
		 */

		/*
		 * parameter.size = 20; parameter.borderWidth = 2;
		 * FreeTypeFontGenerator.FreeTypeBitmapFontData data =
		 * generator.generateData(parameter); BitmapFontWriter.writeFont(data, new
		 * String[] {"fnt/koruri_border_20.png"},
		 * Gdx.files.absolute("fnt/koruri_border_20.fnt"), info, 512, 512);
		 * BitmapFontWriter.writePixmaps(parameter.packer.getPages(),
		 * Gdx.files.absolute("fnt"), "koruri_border_20");
		 */

		/*
		 * parameter.size = 30; FreeTypeFontGenerator.FreeTypeBitmapFontData data =
		 * generator.generateData(parameter); BitmapFontWriter.writeFont(data, new
		 * String[] {"fnt/koruri_30.png"}, Gdx.files.absolute("fnt/koruri_30.fnt"),
		 * info, 1024, 1024); BitmapFontWriter.writePixmaps(parameter.packer.getPages(),
		 * Gdx.files.absolute("fnt"), "koruri_30");
		 */

		/*
		 * parameter.size = 80; FreeTypeFontGenerator.FreeTypeBitmapFontData data =
		 * generator.generateData(parameter); BitmapFontWriter.writeFont(data, new
		 * String[] {"fnt/koruri_80.png"}, Gdx.files.absolute("fnt/koruri_80.fnt"),
		 * info, 2048, 2048); BitmapFontWriter.writePixmaps(parameter.packer.getPages(),
		 * Gdx.files.absolute("fnt"), "koruri_80");
		 */

		// data.dispose();
		generator.dispose();
		// parameter.packer.dispose();

		// loadFonts();
	}

	public static void loadFonts() {
		koruriBorder20 = new FontSize(new Font("fnt/koruri_border_20.fnt"), 20);

		koruri20 = new FontSize(new Font("fnt/koruri_20.fnt"), 20);
		koruri30 = new FontSize(new Font("fnt/koruri_30.fnt"), 30);
		koruri80 = new FontSize(new Font("fnt/koruri_80.fnt"), 80);
	}

	public static class FontSize {
		private final Font font;
		private final int size;

		FontSize(Font font, int size) {
			this.font = font;
			this.size = size;
		}

		public Font getFont() {
			return font;
		}

		public int getSize() {
			return size;
		}
	}

	public enum FontType {
		KORURI(koruri20, koruri30, koruri80), KORURI_BORDER(koruriBorder20);

		private final Map<Integer, Font> sizes = new HashMap<>();
		// private final Int2ObjectMap<Font> sizes = new Int2ObjectOpenHashMap<>();

		FontType(FontSize... fonts) {
			for (FontSize font : fonts) {
				font.getFont().addAtlas(atlasPrompts);
				font.getFont().addAtlas(atlasIcons);
				sizes.put(font.getSize(), font.getFont());
			}
		}

		public Font get(int size) {
			return sizes.get(size);
		}
	}
}
