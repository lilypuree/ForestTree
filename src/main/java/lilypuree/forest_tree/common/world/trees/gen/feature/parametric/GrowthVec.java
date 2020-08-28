package lilypuree.forest_tree.common.world.trees.gen.feature.parametric;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class GrowthVec extends Vec3i {


    public Pitch pitch;
    public Yaw yaw;

    public GrowthVec(Vec3i parent) {
        this(parent.getX(), parent.getY(), parent.getZ());
    }

    public GrowthVec(int xIn, int yIn, int zIn) {
        super(xIn, yIn, zIn);
    }

    public GrowthVec(Pitch pitch, Yaw yaw) {
        super(pitch.isVertical() ? 0 : yaw.x, pitch.y, pitch.isVertical() ? 0 : yaw.z);
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Vec3i getVec3i() {
        if (pitch.isVertical()) {
            return new Vec3i(0, pitch.y, 0);
        } else {
            return new Vec3i(yaw.x, pitch.y, yaw.z);
        }
    }

    @Override
    public int getX() {
        return pitch.isVertical() ? 0 : yaw.x;
    }

    @Override
    public int getY() {
        return pitch.y;
    }

    @Override
    public int getZ() {
        return pitch.isVertical() ? 0 : yaw.z;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof GrowthVec)) {
            return false;
        } else {
            GrowthVec growthVec = (GrowthVec) other;
            if (this.pitch != growthVec.pitch) {
                return false;
            } else {
                return this.yaw == growthVec.yaw;
            }
        }
    }

    public enum Pitch {
        STRAIGHT_UP(1), ANGLE_45(1), HORIZONTAL(0), ANGLE_135(-1), STRAIGHT_DOWN(-1);

        private byte y;

        Pitch(int y) {
            this.y = (byte) y;
        }

        public boolean isVertical() {
            return this == STRAIGHT_UP || this == STRAIGHT_DOWN;
        }

        public Pitch lower(int steps) {
            steps = Math.max(steps, 4 - this.ordinal());
            return Pitch.values()[this.ordinal() + steps];
        }

        public Pitch raise(int steps) {
            steps = Math.max(steps, this.ordinal());
            return Pitch.values()[this.ordinal() - steps];
        }
    }

    public enum Yaw {
        N(0, -1), NW(-1, -1), W(-1, 0), SW(-1, 1), S(0, 1), SE(1, 1), E(1, 0), NE(1, -1);

        private byte x;
        private byte z;

        Yaw(int xIn, int zIn) {
            x = (byte) xIn;
            z = (byte) zIn;
        }

        public Yaw getCCRotatedYaw(int steps) {
            int newIndex = this.ordinal() + steps;
            return Yaw.values()[Math.floorMod(newIndex, 8)];
        }

        public Yaw getCWRotatedYaw(int steps) {
            return getCCRotatedYaw(-steps);
        }

        /**
         * Get the Yaw corresponding to the given angle in degrees (0-360). Out of bounds values are wrapped around.
         * An angle of 0 is NORTH, an angle of 90 would be WEST.
         */
        public static Yaw fromAngle(double angle) {
            return Yaw.values()[MathHelper.floor(angle / 45.0D + 0.5D) & 7];
        }
    }
}
