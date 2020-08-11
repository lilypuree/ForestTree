package lilypuree.forest_tree.trees.world.gen.feature.parametric;

import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BranchDirectionHelper {

    private Random rand;
    private float baseAxillaryOffset;
    private MeristemFactory factory;
    private MeristemGrower grower;

    //probability of axillary branch being in each state.
    private float secondaryYawParallel = 0.1f;
    private float secondaryYaw45Degree = 0.7f;
    private float secondaryYaw90Degree = 0.2f;
    private float secondaryPitch45Degree = 0.7f;
    private float secondaryPitchHorizontal = 0.3f;
    private float secondaryPitch135Degree = 0.0f;

    //in step sizes
    private float terminalYawVariance = 0.4f;
    private float axillaryYawVariance = 0.4f;

    //m[i][j] : probability of transition from ith pitch -> jth pitch
    private float[][] terminalMarkovMatrix = new float[][]{
            {0.95f, 0.05f, 0f, 0f},
            {0.3f, 0.67f, 0.03f, 0f},
            {0.0f, 0.94f, 0.06f, 0f},
            {0.0f, 0.0f, 0.0f, 1.0f}
    };     //change of terminal meristem
    private float[][] axillaryMarkovMatrix = new float[][]{
            {0.4f, 0.6f, 0f, 0f},
            {0.2f, 0.7f, 0.1f, 0f},
            {0.0f, 0.9f, 0.1f, 0f},
            {0.0f, 0.0f, 0.0f, 1.0f}
    };      //change of axillary meristem
    private float[][] branchingMarkovMatrix = new float[][]{
            {0.0f, 0.8f, 0.2f, 0f},
            {0.0f, 0.8f, 0.2f, 0f},
            {0.0f, 1.0f, 0.0f, 0f},
            {0.0f, 0.0f, 0.0f, 1.0f}
    };     //change of pitch when generating secondary branches

    public BranchDirectionHelper(MeristemFactory factory, MeristemGrower grower) {
        this.factory = factory;
        this.grower = grower;
    }

    public void init(float secondaryYawParallelIn, float secondaryYaw45DegreeIn, float secondaryYaw90DegreeIn, float secondaryPitch45DegreeIn, float secondaryPitchHorizontalIn, float secondaryPitch135DegreeIn, float terminalYawVarianceIn, float axillaryYawVarianceIn, float[][] terminalMarkovMatrixIn, float[][] axillaryMarkovMatrixIn, float[][] branchingMarkovMatrixIn) {
        this.secondaryYawParallel = secondaryYawParallelIn;
        this.secondaryYaw45Degree = secondaryYaw45DegreeIn;
        this.secondaryYaw90Degree = secondaryYaw90DegreeIn;
        this.secondaryPitch45Degree = secondaryPitch45DegreeIn;
        this.secondaryPitchHorizontal = secondaryPitchHorizontalIn;
        this.secondaryPitch135Degree = secondaryPitch135DegreeIn;
        this.terminalYawVariance = terminalYawVarianceIn;
        this.axillaryYawVariance = axillaryYawVarianceIn;
        this.terminalMarkovMatrix = terminalMarkovMatrixIn;
        this.axillaryMarkovMatrix = axillaryMarkovMatrixIn;
        this.branchingMarkovMatrix = branchingMarkovMatrixIn;
    }

    public GrowthVec getRandomTerminalDirection() {
        return new GrowthVec(changeTerminalPitchRandomly(GrowthVec.Pitch.STRAIGHT_UP), GrowthVec.Yaw.values()[rand.nextInt(8)]);
    }

    public Set<GrowthVec> axillaryDirectionsFromVertical(int count, Meristem main) {
        Set<GrowthVec> growthVecs = new HashSet<>();
        int loops = 0;

        int node = (int) (main.getLength() / grower.getNodeInterval(main.isTerminal()));
        GrowthVec existing = main.getDirection();

        GrowthVec.Yaw[] newYaw = branchingYawsFromVertical(node, count);

        int i = 0;
        boolean success = false;
        while (loops < count * 2 && i < count) {

            int loops2 = 0;
            while (loops2++ < 4) {
                GrowthVec.Pitch newPitch = useMarkovMatrix(GrowthVec.Pitch.values(), branchingMarkovMatrix, rand, existing.pitch.ordinal(), 3);
                GrowthVec newDir = new GrowthVec(newPitch, newYaw[i]);
                if (existing.equals(newDir) || growthVecs.contains(newDir)) {
                    continue;
                } else {
                    growthVecs.add(newDir);
                    break;
                }
            }
            i++;
            loops += loops2;
        }
        return growthVecs;
    }

    /**
     * returns Yaw values for the specified node in a leading branch.
     *
     * @param node  position of the node
     * @param count number of axillaries to generate
     * @return an array of size count.
     */
    private GrowthVec.Yaw[] branchingYawsFromVertical(int node, int count) {
        float angleOffset = baseAxillaryOffset + node * factory.getAngleOffsetPerNode();
        float angleDivisions = (float) 360 / factory.getMeristemsPerNode(true);
        GrowthVec.Yaw[] results = new GrowthVec.Yaw[count];
        for (int i = 0; i < count; i++) {
            float angle = angleOffset + angleDivisions * i + factory.getAngleOffsetVariance() * (float) rand.nextGaussian();
            results[i] = GrowthVec.Yaw.fromAngle(angle);
        }
        return results;
    }


    public Set<GrowthVec> axillaryDirectionsFromSideways(int count, GrowthVec direction, boolean excludeInput) {

        GrowthVec.Yaw yaw;
        GrowthVec.Pitch pitch;
        Set<GrowthVec> results = new HashSet<>();
        //TODO
        //this is horrible.
        //It'll run forever
        int loops = 0;
        while (loops++ < count * 2 && results.size() < count) {
            float r = rand.nextFloat();
            if (r < secondaryYawParallel) {
                yaw = direction.yaw;
                r = rand.nextFloat();
                pitch = getRandomPitch(excludeInput ? direction.pitch : null);
            } else {

                if (r - secondaryYawParallel < secondaryYaw45Degree) {
                    yaw = rand.nextBoolean() ? direction.yaw.getCCRotatedYaw(1) : direction.yaw.getCWRotatedYaw(1);
                } else {
                    yaw = rand.nextBoolean() ? direction.yaw.getCCRotatedYaw(2) : direction.yaw.getCWRotatedYaw(2);
                }
                pitch = getRandomPitch(null);
            }
            results.add(new GrowthVec(pitch, yaw));
        }
        return results;
    }

    private GrowthVec.Pitch getRandomPitch(GrowthVec.Pitch exclude) {
        float r = rand.nextFloat();
        if (exclude != null) {
            switch (exclude) {
                case ANGLE_45:
                    if (r < secondaryPitchHorizontal / (secondaryPitch135Degree + secondaryPitchHorizontal)) {
                        return GrowthVec.Pitch.HORIZONTAL;
                    } else return GrowthVec.Pitch.ANGLE_135;
                case HORIZONTAL:
                    if (r < secondaryPitch45Degree / (secondaryPitch135Degree + secondaryPitch45Degree)) {
                        return GrowthVec.Pitch.ANGLE_45;
                    } else return GrowthVec.Pitch.ANGLE_135;
                case ANGLE_135:
                default:
                    if (r < secondaryPitch45Degree / (secondaryPitchHorizontal + secondaryPitch45Degree)) {
                        return GrowthVec.Pitch.ANGLE_45;
                    } else return GrowthVec.Pitch.HORIZONTAL;
            }
        } else {
            if (r < secondaryPitch45Degree) {
                return GrowthVec.Pitch.ANGLE_45;
            } else if (r < secondaryPitch45Degree + secondaryPitchHorizontal) {
                return GrowthVec.Pitch.HORIZONTAL;
            } else return GrowthVec.Pitch.ANGLE_135;
        }
    }


    private static Set<GrowthVec> combinePitchAndYaw(GrowthVec.Pitch[] pitches, GrowthVec.Yaw[] yaws) {
        Set<GrowthVec> result = new HashSet<>();
        for (GrowthVec.Pitch pitch : pitches) {
            for (GrowthVec.Yaw yaw : yaws) {
                result.add(new GrowthVec(pitch, yaw));
            }
        }
        return result;
    }

    public float getTerminalYawVariance() {
        return (float) rand.nextGaussian() * terminalYawVariance;
    }

    public float getAxillaryYawVariance() {
        return (float) rand.nextGaussian() * axillaryYawVariance;
    }

    public GrowthVec.Pitch changeTerminalPitchRandomly(GrowthVec.Pitch pitch) {
        return useMarkovMatrix(GrowthVec.Pitch.values(), terminalMarkovMatrix, rand, pitch.ordinal(), 4);
    }

    public GrowthVec.Pitch changeAxillaryPitchRandomly(GrowthVec.Pitch pitch) {
        return useMarkovMatrix(GrowthVec.Pitch.values(), axillaryMarkovMatrix, rand, pitch.ordinal(), 4);
    }

    private static <T> T useMarkovMatrix(T[] outputMatrix, float[][] markovMatrix, Random rand, int x, int n) {
        float r = rand.nextFloat();
        float cdf = 0;
        for (int i = 0; i < n; i++) {
            cdf += markovMatrix[x][i];
            if (r < cdf) return outputMatrix[i];
        }
        return outputMatrix[n];
    }

    public void setRandom(Random rand) {
        this.rand = rand;
        this.baseAxillaryOffset = rand.nextFloat() * 360;
    }

    public static void setMarkovMatrix(float[][] matrix, float[] probabilities) {
        if (probabilities.length != 16) throw new ArgumentIndexOutOfBoundsException(probabilities.length);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = probabilities[i * 4 + j];
            }
        }
    }

    public static float[] flattenMarkovMatrix(float[][] matrix) {
        float[] flat = new float[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                flat[i * 4 + j] = matrix[i][j];
            }
        }
        return flat;
    }

    public static float[] intArrayToFloatArray(int[] array) {
        float[] floatArray = new float[array.length];
        int index = 0;
        for (int i : array) {
            floatArray[index++] = Float.intBitsToFloat(i);
        }
        return floatArray;
    }

    public static int[] floatArrayToIntArray(float[] array) {
        int[] intArray = new int[array.length];
        int index = 0;
        for (float f : array) {
            intArray[index++] = Float.floatToIntBits(f);
        }
        return intArray;
    }


    public static class Builder {
        private float secondaryYawParallel = 0.1f;
        private float secondaryYaw45Degree = 0.7f;
        private float secondaryYaw90Degree = 0.2f;
        private float secondaryPitch45Degree = 0.7f;
        private float secondaryPitchHorizontal = 0.3f;
        private float secondaryPitch135Degree = 0.0f;

        private float terminalYawVariance = 0.4f;
        private float axillaryYawVariance = 0.4f;

        private float[][] terminalMarkovMatrix = new float[][]{
                {0.95f, 0.05f, 0f, 0f},
                {0.3f, 0.67f, 0.03f, 0f},
                {0.0f, 0.94f, 0.06f, 0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };     //change of terminal meristem
        private float[][] axillaryMarkovMatrix = new float[][]{
                {0.4f, 0.6f, 0f, 0f},
                {0.2f, 0.7f, 0.1f, 0f},
                {0.0f, 0.9f, 0.1f, 0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };      //change of axillary meristem
        private float[][] branchingMarkovMatrix = new float[][]{
                {0.0f, 0.8f, 0.2f, 0f},
                {0.0f, 0.8f, 0.2f, 0f},
                {0.0f, 1.0f, 0.0f, 0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };     //change of pitch when generating secondary branches


        public Builder secondaryYaw(float parallel, float degree45, float degree90) {
            float sum = parallel + degree45 + degree90;
            this.secondaryYawParallel = parallel / sum;
            this.secondaryYaw45Degree = degree45 / sum;
            this.secondaryYaw90Degree = degree90 / sum;
            return this;
        }

        public Builder secondaryPitch(float degree45, float horizontal, float degree135) {
            float sum = horizontal + degree45 + degree135;
            this.secondaryPitch45Degree = degree45 / sum;
            this.secondaryPitchHorizontal = horizontal / sum;
            this.secondaryPitch135Degree = degree135 / sum;
            return this;
        }

        public Builder YawVariance(float terminal, float axillary) {
            this.terminalYawVariance = terminal;
            this.axillaryYawVariance = axillary;
            return this;
        }

        public Builder terminalMarkovMatrix(float[] probabilities) {
            setMarkovMatrix(terminalMarkovMatrix, probabilities);
            return this;
        }

        public Builder axillaryMarkovMatrix(float[] probabilities) {
            setMarkovMatrix(axillaryMarkovMatrix, probabilities);
            return this;
        }

        public Builder branchingMarkovMatrix(float[] probabilities) {
            setMarkovMatrix(branchingMarkovMatrix, probabilities);
            return this;
        }

        public BranchDirectionHelper build(MeristemFactory factory, MeristemGrower grower) {
            BranchDirectionHelper directionHelper = new BranchDirectionHelper(factory, grower);
            directionHelper.init(secondaryYawParallel, secondaryYaw45Degree, secondaryYaw90Degree, secondaryPitch45Degree, secondaryPitchHorizontal, secondaryPitch135Degree, terminalYawVariance, axillaryYawVariance, terminalMarkovMatrix, axillaryMarkovMatrix, branchingMarkovMatrix);
            return directionHelper;
        }
    }

    public void loadFromNbt(CompoundNBT compound) {
        secondaryYawParallel = compound.getFloat("secondaryYawParallel");
        secondaryYaw45Degree = compound.getFloat("secondaryYaw45Degree");
        secondaryYaw90Degree = compound.getFloat("secondaryYaw90Degree");
        secondaryPitch45Degree = compound.getFloat("secondaryPitch45Degree");
        secondaryPitchHorizontal = compound.getFloat("secondaryPitchHorizontal");
        secondaryPitch135Degree = compound.getFloat("secondaryPitch135Degree");
        terminalYawVariance = compound.getFloat("terminalYawVariance");
        axillaryYawVariance = compound.getFloat("axillaryYawVariance");
        setMarkovMatrix(terminalMarkovMatrix, intArrayToFloatArray(compound.getIntArray("terminalMarkovMatrix")));
        setMarkovMatrix(axillaryMarkovMatrix, intArrayToFloatArray(compound.getIntArray("axillaryMarkovMatrix")));
        setMarkovMatrix(branchingMarkovMatrix, intArrayToFloatArray(compound.getIntArray("branchingMarkovMatrix")));
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        compound.putFloat("secondaryYawParallel", secondaryYawParallel);
        compound.putFloat("secondaryYaw45Degree", secondaryYaw45Degree);
        compound.putFloat("secondaryYaw90Degree", secondaryYaw90Degree);
        compound.putFloat("secondaryPitch45Degree", secondaryPitch45Degree);
        compound.putFloat("secondaryPitchHorizontal", secondaryPitchHorizontal);
        compound.putFloat("secondaryPitch135Degree", secondaryPitch135Degree);
        compound.putFloat("terminalYawVariance", terminalYawVariance);
        compound.putFloat("axillaryYawVariance", axillaryYawVariance);
        compound.putIntArray("terminalMarkovMatrix", floatArrayToIntArray(flattenMarkovMatrix(terminalMarkovMatrix)));
        compound.putIntArray("axillaryMarkovMatrix", floatArrayToIntArray(flattenMarkovMatrix(axillaryMarkovMatrix)));
        compound.putIntArray("branchingMarkovMatrix", floatArrayToIntArray(flattenMarkovMatrix(branchingMarkovMatrix)));
        return compound;
    }

}
