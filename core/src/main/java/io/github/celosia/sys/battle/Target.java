package io.github.celosia.sys.battle;

// Targets that Ranges are built from
public enum Target {
    SELF, // Self
    SELF_UP, // Ally above self
    SELF_DOWN, // Ally below self
    SELF_ACROSS, // Opponent across from self
    SELF_ACROSS_UP, // Opponent across from and above self
    SELF_ACROSS_DOWN, // Opponent across from and below self
    SELF_TEAM, // Self's team (not including self)
    TARGET, // Target
    TARGET_UP, // Unit above target
    TARGET_DOWN, // Unit below target
    TARGET_TEAM; // Target's team (not including target)
}
