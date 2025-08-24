package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.List;

// Utilities for dealing with Unit positions
public class PosLib {
    // Returns the pos off spaces below this one, or -1 if it's invalid
    public static int getUpDown(int pos, int off) {
        int posNew = pos + off;
        if(pos < 4 && (posNew < 0 || posNew > 3)) return -1;
        else if(pos >= 4 && (posNew < 4 || posNew > 7)) return -1;
        else return posNew;
    }

    // Returns the pos directly across from this one
    public static int getAcross(int pos) {
        return pos + (4 * ((pos < 4) ? 1 : -1));
    }

    // Returns the poses of the Units on the team except the provided one
    public static List<Integer> getTeamWithout(int pos) {
        List<Integer> result = new ArrayList<>();

        int lower = (pos < 4) ? 0 : 4;
        int upper = (pos < 4) ? 3 : 7;

        for(int i = lower; i < upper; i++) {
            if(i != pos) result.add(i);
        }

        return result;
    }

    // Returns the height 0-3 of pos
    public static int getHeight(int pos) {
        return (pos < 4) ? pos : pos - 4;
    }

    // Returns the Side of pos
    public static Side getSide(int pos) {
        return (pos < 4) ? Side.ALLY : Side.OPPONENT;
    }

    // Returns the Side of pos2 relative to the Side of pos1
    public static Side getRelativeSide(int pos1, int pos2) {
        return (pos2 < 4) ?
            (pos1 < 4) ? Side.ALLY : Side.OPPONENT
            : (pos1 >= 4) ? Side.ALLY : Side.OPPONENT;
    }
}
