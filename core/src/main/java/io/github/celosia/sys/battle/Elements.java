package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_LUX;

public class Elements {

    // todo icons
    public static final Element VIS = new Element(lang.get("element.vis"), lang.get("element.vis.desc"),
            "[LIGHT GRAY][+rolling-energy]");
    public static final Element IGNIS = new Element(lang.get("element.ignis"), lang.get("element.ignis.desc"),
            "[ORANGE][+fire]");
    public static final Element GLACIES = new Element(lang.get("element.glacies"), lang.get("element.glacies.desc"),
            "[light azure][+snowflake-2]");
    public static final Element FULGUR = new Element(lang.get("element.fulgur"), lang.get("element.fulgur.desc"),
            "[YELLOW][+electric]");
    public static final Element VENTUS = new Element(lang.get("element.ventus"), lang.get("element.ventus.desc"),
            "[GREEN][+wind-slap]");
    public static final Element TERRA = new Element(lang.get("element.terra"), lang.get("element.terra.desc"),
            "[light BROWN][+rock]");
    public static final Element LUX = new Element(lang.get("element.lux"), lang.get("element.lux.desc"),
            C_LUX + "[+todo]");
    public static final Element MALUM = new Element(lang.get("element.malum"), lang.get("element.malum.desc"),
            "[PURPLE][+evil-wings]");
}
