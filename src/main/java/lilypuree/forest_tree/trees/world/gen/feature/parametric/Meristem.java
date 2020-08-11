package lilypuree.forest_tree.trees.world.gen.feature.parametric;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public abstract class Meristem {

    protected MeristemFactory factory;
    protected MeristemTerminator terminator;
    protected MeristemGrower grower;
    protected BranchDirectionHelper directionHelper;

    protected boolean alive = true;
    protected int age;
    protected float lastNodeLength = 0;
    protected float lastAgeLength = 0;
    protected float length = 0;
    protected BlockPos.Mutable pos;
    protected GrowthVec dir;


    public void init(int age, BlockPos pos, GrowthVec dir, MeristemFactory factory, MeristemGrower grower, MeristemTerminator killer) {
        this.age = age;
        this.pos = new BlockPos.Mutable(pos);
        this.dir = dir;
        this.factory = factory;
        this.grower = grower;
        this.terminator = killer;
        this.directionHelper = factory.getDirectionHelper();
    }


    /**
     * <p>checks if meristem should die. If dead, returns.
     * <p>jitters the direction slightly
     * <p>grow by 1 block along direction
     * <p>ages if needed
     */
    public void grow(Random rand) {
        if (rand.nextFloat() < terminator.getMeristemDeathRate(this)) {
            alive = false;
            return;
        }
        shiftDirection();

        pos.move(dir.getX(), dir.getY(), dir.getZ());
        length += distanceFactor();
        decreaseAge();
    }

    public boolean isAlive() {
        return alive;
    }

    public float getLength() {
        return length;
    }

    public GrowthVec getDirection() {
        return dir;
    }

    public Vec3i getSourceDirection() {
        return new Vec3i(-dir.getX(), -dir.getY(), -dir.getZ());
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getAge() {
        return age;
    }

    /**
     * nodes will generate based on distance.
     * If the meristem is dead, it will generate more on the current position of the meristem.
     *
     * @param rand
     * @return secondary meristems generated on the source of the current meristem.
     */
    public Collection<Meristem> generateAxillaries(Random rand) {
        List<Meristem> newMeristems = new ArrayList<>();

        float interval = length - lastNodeLength;

        if (grower.shouldGenerateNode(interval, isTerminal()) || !this.isAlive() && isTerminal()) {
            lastNodeLength = length;
            BlockPos sourcePos = alive ? pos.add(getSourceDirection()) : pos;
            int count = (int) factory.randomMeristemsPerNode(isTerminal());
            int newAge = age - (int) factory.randomAxillaryAgeOffset();

            if (alive) {
                newMeristems.addAll(factory.createMeristemsOnNode(count, newAge, sourcePos, this));
            } else {
                newMeristems.addAll(factory.createMeristemsOnDeadNode(count, newAge, sourcePos, this));
            }

            newMeristems.forEach(meristem -> meristem.grow(rand));
            newMeristems.removeIf(meristem -> !meristem.isAlive());
        }
        return newMeristems;
    }

    public abstract boolean isTerminal();

    public abstract void shiftDirection();

    public void decreaseAge() {
        float interval = length - lastAgeLength;
        if (grower.shouldBranchAge(interval, isTerminal())) {
            age -= 1;
            lastAgeLength = length;
        }
    }

    protected float distanceFactor() {
        return (float) dir.distanceSq(0, 0, 0, false);
    }


    public static class Terminal extends Meristem {

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public void shiftDirection() {
            if (!dir.pitch.isVertical()) {
                dir.yaw = dir.yaw.getCCRotatedYaw((int) (directionHelper.getTerminalYawVariance()));
            }
            dir.pitch = directionHelper.changeTerminalPitchRandomly(dir.pitch);
        }
    }

    public static class Axillary extends Meristem {

        @Override
        public boolean isTerminal() {
            return false;
        }

        @Override
        public void shiftDirection() {
            if (!dir.pitch.isVertical()) {
                dir.yaw = dir.yaw.getCCRotatedYaw((int) (directionHelper.getAxillaryYawVariance()));
            }
            dir.pitch = directionHelper.changeAxillaryPitchRandomly(dir.pitch);
        }
    }
}
