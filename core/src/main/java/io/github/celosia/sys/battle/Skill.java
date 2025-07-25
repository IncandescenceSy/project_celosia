package io.github.celosia.sys.battle;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang, functional targeting, display type even for non-damaging skills, explicitly define which are "for allies" and which are "for opponents" for autotargeting and AI
public class Skill {
    private final String name;
    private final String desc;
    private final Element element;
    private final Targeting targeting;
    private final int cost;

    // todo: NYI
    // Skills happen in order of prio, and in order of user Agi within each prio
    // Prio brackets:
    // -9: Always happens last
    // -1: Happens late
    // 0: Normal
    // +1: Happens early (most prio skills)
    // +2: Happens very early (certain special prio skills?)
    // +3: Happens before attacks (Defend, Protect, Ally Switch)
    // +9: Always happens immediately (Follow Ups)
    private final int prio;

    private final boolean isBloom;
    private final SkillEffect[] skillEffects;

    Skill(String name, String desc, Element element, Targeting targeting, int cost, int prio, boolean isBloom, SkillEffect... skillEffects) {
        this.name = name;
        this.desc = desc;
        this.element = element;
        this.targeting = targeting;
        this.cost = cost;
        this.prio = prio;
        this.isBloom = isBloom;
        this.skillEffects = skillEffects;
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, int prio, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, prio, false, skillEffects);
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, boolean isBloom, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, 0, isBloom, skillEffects);
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, 0, false, skillEffects);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Element getElement() {
        return element;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public int getCost() {
        return cost;
    }

    public int getPrio() {
        return prio;
    }

    public boolean isBloom() {
        return isBloom;
    }

    public SkillEffect[] getSkillEffects() {
        return skillEffects;
    }
}
