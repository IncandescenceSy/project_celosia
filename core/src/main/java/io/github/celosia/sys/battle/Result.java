package io.github.celosia.sys.battle;

// Result of the previous SkillEffect
public enum Result {
    FAIL,
    HIT_BARRIER, // Only damaged Barrier or Protect
    SUCCESS;
}
