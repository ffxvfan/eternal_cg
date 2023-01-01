package com.gotenks.eternal_cg.types;

public enum Type {
    SUN(0),
    MOON(1),
    WATER(2),
    FIRE(4),
    ICE(8),
    FAUNA(16),
    FLORA(32),
    METAL(64),
    LAVA(128),
    ROCK(256);

    private final int ID;
    Type(int ID) {
        this.ID = ID;
    }

    // weaknessChart[attacking type] contains(defending type) == attacker is strong against defender
    public final static int[] strengths = {
            MOON.ID | WATER.ID | ICE.ID,
            WATER.ID | FAUNA.ID | LAVA.ID,
            FIRE.ID | METAL.ID | ROCK.ID,
            ICE.ID | FAUNA.ID | FLORA.ID,
            WATER.ID | FLORA.ID | LAVA.ID,
            SUN.ID | WATER.ID | FLORA.ID,
            SUN.ID | WATER.ID | ROCK.ID,
            FIRE.ID | FAUNA.ID | ROCK.ID,
            WATER.ID | METAL.ID | ROCK.ID,
            FIRE.ID | ICE.ID | FLORA.ID,
    };

    public static int typeDamageMod(Type atkType, Type defType, boolean isMajor) {
        return (strengths[atkType.ordinal()] & defType.ID) > 0 ? (isMajor ? 7 : 2) : 0;
    }
}
