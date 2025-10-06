package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public class Accessories {

    public static final Accessory FIREBORN_RING = new Accessory.Builder(lang.get("accessory.fireborn_ring"),
            "blank", "[ORANGE][+fire-ring]").passives(Passives.IGNIS_AFF_UP).build();
}
