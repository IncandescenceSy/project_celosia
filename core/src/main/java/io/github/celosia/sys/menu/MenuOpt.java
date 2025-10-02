package io.github.celosia.sys.menu;

import io.github.celosia.sys.menu.MenuLib.MenuOptType;

// A menu option type with text and position for it
public class MenuOpt {

    MenuOptType optType;
    String text;
    int posX;
    int posY;

    MenuOpt(MenuOptType optType, String text, int posX, int posY) {
        this.optType = optType;
        this.text = text;
        this.posX = posX;
        this.posY = posY;
    }

    public MenuOptType getType() {
        return optType;
    }

    public String getText() {
        return text;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
