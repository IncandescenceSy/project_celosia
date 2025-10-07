package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;
import io.github.celosia.sys.entity.IconEntity;
import io.github.celosia.sys.util.ArrayLib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.celosia.Main.SKILLS;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.BoolLib.booleanToInt;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_ELEMENT;
import static io.github.celosia.sys.util.TextLib.C_NUM;
import static io.github.celosia.sys.util.TextLib.C_STAT;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getColor;
import static io.github.celosia.sys.util.TextLib.getSign;

public class Skill extends ComplexDescriptionEntity {

    private final Element element;
    private final Range range;
    private final int cost;
    private final int cooldown;

    // Skills happen in order of prio, and in order of user Agi within each prio
    // Prio brackets:
    // -9: Always happens last (Slow Follow-Ups)
    // -1: Happens late
    // 0: Normal
    // +1: Happens early (most prio skills)
    // +2: Happens very early (certain special prio skills?)
    // +3: Happens before attacks (Defend, Protect, Ally Switch)
    // +9: Always happens immediately (Follow-Ups)
    private final int prio;

    private final boolean isBloom;

    private final SkillRole[] skillRoles;
    private final SkillEffect[] skillEffects;

    public Skill(Builder builder) {
        super(builder);
        element = builder.element;
        range = builder.range;
        cost = builder.cost;
        cooldown = builder.cooldown;
        prio = builder.prio;
        isBloom = builder.isBloom;
        skillRoles = builder.skillRoles;
        skillEffects = builder.skillEffects;
        SKILLS.add(this);
    }

    public static class Builder extends ComplexDescriptionEntity.Builder {

        private final Element element;
        private final Range range;
        private final int cost;
        private int cooldown = 0;
        private int prio = 0;
        private boolean isBloom = false;

        private SkillRole[] skillRoles = new SkillRole[] {};
        private SkillEffect[] skillEffects = new SkillEffect[] {};

        public Builder(String name, String desc, Element element, Range range, int cost) {
            super(name, desc, element.getIcon());
            this.element = element;
            this.range = range;
            this.cost = cost;
        }

        // Override so they can be used without casting
        @Override
        public Builder descArgs(String... descArgs) {
            super.descArgs(descArgs);
            return this;
        }

        @Override
        public Builder descInclusions(IconEntity... descInclusions) {
            super.descInclusions(descInclusions);
            return this;
        }

        public Builder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public Builder prio(int prio) {
            this.prio = prio;
            return this;
        }

        public Builder bloom() {
            isBloom = true;
            return this;
        }

        public Builder roles(SkillRole... skillRoles) {
            this.skillRoles = skillRoles;
            return this;
        }

        public Builder effects(SkillEffect... skillEffects) {
            this.skillEffects = skillEffects;
            return this;
        }

        public Skill build() {
            return new Skill(this);
        }
    }

    public Element getElement() {
        return element;
    }

    public Range getRange() {
        return range;
    }

    public int getCost() {
        return cost;
    }

    public String getCostFormatted() {
        return lang.format("skill_cost", formatNum(cost), booleanToInt(isBloom));
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getPrio() {
        return prio;
    }

    public boolean isBloom() {
        return isBloom;
    }

    public SkillRole[] getSkillRoles() {
        return skillRoles;
    }

    public boolean hasRole(SkillRole skillRole) {
        return ArrayLib.contains(skillRoles, skillRole);
    }

    public SkillEffect[] getSkillEffects() {
        return skillEffects;
    }

    public boolean isRangeSelf() {
        return range == Ranges.SELF || range == Ranges.SELF_UP_DOWN;
    }

    public boolean shouldTargetOpponent() {
        return range.getSide() == Side.OPPONENT || this.hasRole(SkillRole.ATTACK) ||
                this.hasRole(SkillRole.DEBUFF_DEFENSIVE) || this.hasRole(SkillRole.DEBUFF_OFFENSIVE);
    }

    public SkillInstance toSkillInstance() {
        return new SkillInstance(this);
    }

    @Override
    public String getPartialDesc() {
        StringBuilder partialDesc = new StringBuilder(super.getPartialDesc());

        if (this.getDescInclusions().length == 0) {
            partialDesc.append("\n");
        }

        Set<IconEntity> inclusions = HashSet.newHashSet(8);
        for (SkillEffect effect : skillEffects) {
            inclusions.add(effect.getDescInclusion());
        }

        for (IconEntity inclusion : inclusions) {
            if (inclusion != null) {
                partialDesc.append("\n[WHITE](").append(inclusion.getNameWithIcon(C_BUFF)).append("[WHITE]: ")
                        .append(inclusion.getDesc().replace("\n", ". ")).append("[WHITE])");
            }
        }

        return partialDesc.toString();
    }

    @Override
    public String getDesc() {
        int pow = 0;
        List<String> skillTypes = new ArrayList<>(3);

        for (SkillEffect skillEffect : skillEffects) {
            // todo better pow logic
            // multihit should output eg 60+20*2
            int effectPow = skillEffect.getPow();
            if (effectPow > pow) {
                pow = effectPow;
            }

            SkillType effectType = skillEffect.getSkillType();
            String str = "C_STAT" + effectType.getName() + "WHITE";
            if (effectType != SkillType.STAT && !skillTypes.contains(str)) {
                skillTypes.add(str);
            }
        }

        String skillTypesStr;
        if (!skillTypes.isEmpty()) {
            skillTypesStr = skillTypes.toString().replace("[", "").replace("]", "")
                    .replace("WHITE", "[WHITE]")
                    .replace("C_STAT", C_STAT).replace(", ", "[WHITE]/");
        } else {
            skillTypesStr = C_STAT + SkillType.STAT.getName() + "[WHITE]";
        }

        return lang.format("skill_desc", skillTypesStr, element.getNameWithIcon(C_ELEMENT), range.getName(),
                (pow == 0) ? "" : ", " + C_NUM + pow + " [WHITE]" + lang.get("pow"),
                (prio == 0) ? "" : ", " + getColor(prio) + getSign(prio) + prio + " [WHITE]" + lang.get("prio"),
                this.getPartialDesc());
    }
}
