package io.github.celosia.sys.battle;

public class Accessory extends EquippableEntity {

    private final Skill[] skills;
    private final Passive[] passives;

    public Accessory(String name, String desc, String icon, Skill[] skills, Passive[] passives) {
        super(name, desc, icon);
        this.skills = skills;
        this.passives = passives;
    }

    public Accessory(String name, String desc, String icon, Passive... passives) {
        this(name, desc, icon, null, passives);
    }

    public Accessory(String name, String desc, String icon, Skill... skills) {
        this(name, desc, icon, skills, null);
    }

    public Skill[] getSkills() {
        return skills;
    }

    public Passive[] getPassives() {
        return passives;
    }

    @Override
    public void apply(Unit unit, boolean give) {
        if (give) {
            if (skills != null) unit.addSkills(skills);
            if (passives != null) unit.addPassives(passives);
        } else {
            if (skills != null) unit.removeSkills(skills);
            if (passives != null) unit.removePassives(passives);
        }
    }
}
