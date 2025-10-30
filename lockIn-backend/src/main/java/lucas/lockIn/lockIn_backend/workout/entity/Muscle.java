package lucas.lockIn.lockIn_backend.workout.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Muscle {
    //CHEST
    UPPER_CHEST,
    LOWER_CHEST,
    MIDDLE_CHEST,

    //BACK
    TRAPEZIUS_UPPER_BACK,
    RHOMBOIDS_UPPER_BACK,
    TERES_MAJOR_UPPER_BACK,
    LATS,

    //SHOULDER
    FRONT_DELTS,
    LATERAL_DELTS,
    REAR_DELTS,

    //ARMS
    LONG_HEAD_BICEPS,
    SHORT_HEAD_BICEPS,
    BRACHIALIS,
    FOREARMS,

    LONG_HEAD_TRICEPS,
    SHORT_HEAD_TRICEPS,
    MEDIAL_HEAD_TRICEPS,

    //CORE
    ABS,

    //LOWER BODY
    //GLUTES
    MAXIMUS_GLUTES,
    MEDIUS_GLUTES,
    MINIMUS_GLUTES,

    QUADS,
    HAMSTRING,
    ADDUCTORS,
    ABDUCTORS,
    CALVES;

    @JsonCreator
    public static Muscle fromValue(String value) {
        for (Muscle m : values()) {
            if (m.name().equalsIgnoreCase(value)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Unknown muscle: " + value);
    }
}
