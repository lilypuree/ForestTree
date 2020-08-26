package lilypuree.forest_tree.world.trees.gen.feature.parametric;

import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeristemFactory {

    private Random rand;
    private MeristemGrower grower;
    private MeristemTerminator terminator;
    private BranchDirectionHelper directionHelper;
    private int startingAge;

    private Species species;
    private int maxHeight = 30;
    private int minimumBranchAge = 1;
    private int maximumBranchOrder = 4;

    private float meristemsPerNode = 4.5f;
    private float meristemsPerNodeVariance = 0.8f;
    private float meristemsPerAxillaryNode = 2.1f;
    private float meristemsPerAxillaryNodeVariance = 0.8f;

    private float angleOffsetPerNode = 69;
    private float angleOffsetVariance = 9;

    private float axillaryAgeOffset = 1.4f;
    private float axillaryAgeOffsetVariance = 0.5f;

    private float terminalRegenRate = 0.6f;

    public MeristemFactory(Species speciesIn) {
        this.species = speciesIn;
        this.grower = new MeristemGrower();
        this.terminator = new MeristemTerminator();
        this.directionHelper = new BranchDirectionHelper(this, grower);
    }

    public MeristemFactory init(int maxHeightIn, int minimumBranchAgeIn, int maximumBranchOrderIn, float meristemsPerNodeIn, float meristemsPerNodeVarianceIn, float meristemsPerAxillaryNodeIn, float meristemsPerAxillaryNodeVarianceIn, float angleOffsetPerNodeIn, float angleOffsetVarianceIn, float axillaryAgeOffsetIn, float axillaryAgeOffsetVarianceIn, float terminalRegenRateIn) {
        this.maxHeight = maxHeightIn;
        this.minimumBranchAge = minimumBranchAgeIn;
        this.maximumBranchOrder = maximumBranchOrderIn;
        this.meristemsPerNode = meristemsPerNodeIn;
        this.meristemsPerNodeVariance = meristemsPerNodeVarianceIn;
        this.meristemsPerAxillaryNode = meristemsPerAxillaryNodeIn;
        this.meristemsPerAxillaryNodeVariance = meristemsPerAxillaryNodeVarianceIn;
        this.angleOffsetPerNode = angleOffsetPerNodeIn;
        this.angleOffsetVariance = angleOffsetVarianceIn;
        this.axillaryAgeOffset = axillaryAgeOffsetIn;
        this.axillaryAgeOffsetVariance = axillaryAgeOffsetVarianceIn;
        this.terminalRegenRate = terminalRegenRateIn;
        return this;
    }

    public Meristem createAxillaryMeristem(int age, int order, BlockPos pos, GrowthVec dir) {
        Meristem.Axillary meristem = new Meristem.Axillary();
        meristem.init(age, order, pos, dir, this, grower, terminator);
        return meristem;
    }

    public Meristem createTerminalMeristem(int age, int order, BlockPos pos, GrowthVec dir) {
        Meristem.Terminal meristem = new Meristem.Terminal();
        meristem.init(age, order, pos, dir, this, grower, terminator);
        return meristem;
    }

    public Collection<Meristem> createMeristemsOnDeadNode(int count, int age, BlockPos source, Meristem main) {
        Collection<Meristem> results;
        boolean shouldRegenerateTerminal = rand.nextFloat() < getTerminalRegenRate();

        if (shouldRegenerateTerminal) {
            GrowthVec terminalGrowthVec = directionHelper.getRandomTerminalDirection();
            Meristem newTerminal = createTerminalMeristem(age, main.getOrder(), source, terminalGrowthVec);
            results = createMeristemsOnNode(Math.max(count - 1, 0), age, source, newTerminal);
            results.add(newTerminal);
        } else {
            Collection<GrowthVec> directions;
            if (main.getDirection().pitch.isVertical()) {
                directions = directionHelper.axillaryDirectionsFromVertical(count, main);
            } else {
                directions = directionHelper.axillaryDirectionsFromSideways(count, main.getDirection(), false);
            }
            results = directions.stream().flatMap(dir -> Stream.of(createAxillaryMeristem(age, main.getNextOrder(), source, dir))).collect(Collectors.toSet());

        }
        return results;
    }

    public Collection<Meristem> createMeristemsOnNode(int count, int age, BlockPos source, Meristem main) {
        Collection<GrowthVec> directions;
        if (main instanceof Meristem.Terminal || main.getDirection().pitch.isVertical()) {
            directions = directionHelper.axillaryDirectionsFromVertical(count, main);
        } else {
            directions = directionHelper.axillaryDirectionsFromSideways(count, main.getDirection(), true);
        }
        directions.remove(main.getDirection());
        return directions.stream().flatMap(dir -> Stream.of(createAxillaryMeristem(age, main.getNextOrder(), source, dir))).collect(Collectors.toSet());
    }

    public BranchDirectionHelper getDirectionHelper() {
        return directionHelper;
    }

    public MeristemGrower getGrower() {
        return grower;
    }

    public MeristemTerminator getTerminator() {
        return terminator;
    }

    public Species getSpecies() {
        return species;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinimumBranchAge() {
        return minimumBranchAge;
    }

    public int getMaximumBranchOrder() {
        return maximumBranchOrder;
    }

    public float getMeristemsPerNode(boolean isTerminal) {
        return isTerminal ? meristemsPerNode : meristemsPerAxillaryNode;
    }

    public float randomMeristemsPerNode(boolean isTerminal) {
        return isTerminal ? (float) rand.nextGaussian() * meristemsPerNodeVariance + meristemsPerNode : (float) rand.nextGaussian() * meristemsPerAxillaryNodeVariance + meristemsPerAxillaryNode;
    }

    /**
     * in degrees
     */
    public float getAngleOffsetPerNode() {
        return angleOffsetPerNode;
    }

    public float getAngleOffsetVariance() {
        return angleOffsetVariance;
    }

    public float randomAxillaryAgeOffset() {
        return (float) Math.max(rand.nextGaussian() * axillaryAgeOffsetVariance + axillaryAgeOffset, 0);
    }

    public float getTerminalRegenRate() {
        return terminalRegenRate;
    }

    public void setRandom(Random rand) {
        this.rand = rand;
        grower.setRandom(rand);
        this.directionHelper.setRandom(rand);
    }

    public void setStartingAge(int startingAge) {
        this.startingAge = startingAge;
    }

    public int getStartingAge() {
        return startingAge;
    }

    public static class Builder {
        private int maxHeight = 30;
        private int minimumBranchAge = 1;
        private int maximumBranchOrder = 4;

        private float meristemsPerNode = 4.5f;
        private float meristemsPerNodeVariance = 0.8f;
        private float meristemsPerAxillaryNode = 2.1f;
        private float meristemsPerAxillaryNodeVariance = 0.8f;

        private float angleOffsetPerNode = 69;
        private float angleOffsetVariance = 9;

        private float axillaryAgeOffset = 1.4f;
        private float axillaryAgeOffsetVariance = 0.5f;

        private float terminalRegenRate = 0.6f;

        public Builder maxHeight(int maxHeightIn) {
            this.maxHeight = maxHeightIn;
            return this;
        }

        public Builder minimumBranchAge(int minimumBranchAgeIn) {
            this.minimumBranchAge = minimumBranchAgeIn;
            return this;
        }

        public Builder maximumBranchOrder(int maximumBranchOrderIn) {
            this.maximumBranchOrder = maximumBranchOrderIn;
            return this;
        }

        public Builder meristemsPerNode(float meristemsPerNodeIn, float variance) {
            this.meristemsPerNode = meristemsPerNodeIn;
            this.meristemsPerNodeVariance = variance;
            return this;
        }

        public Builder meristemsPerAxillaryNode(float meristemsPerAxillaryNodeIn, float variance) {
            this.meristemsPerAxillaryNode = meristemsPerAxillaryNodeIn;
            this.meristemsPerAxillaryNodeVariance = variance;
            return this;
        }

        public Builder angleOffsetPerNode(float angleOffsetPerNodeIn, float variance) {
            this.angleOffsetPerNode = angleOffsetPerNodeIn;
            this.angleOffsetVariance = variance;
            return this;
        }

        public Builder axillaryAgeOffset(float axillaryAgeOffsetIn, float variance) {
            this.axillaryAgeOffset = axillaryAgeOffsetIn;
            this.axillaryAgeOffsetVariance = variance;
            return this;
        }

        public Builder terminalRegenRate(int terminalRegenRateIn) {
            this.terminalRegenRate = terminalRegenRateIn;
            return this;
        }

        public MeristemFactory build(Species species) {
            return new MeristemFactory(species).init(maxHeight, minimumBranchAge, maximumBranchOrder, meristemsPerNode, meristemsPerNodeVariance, meristemsPerAxillaryNode, meristemsPerAxillaryNodeVariance, angleOffsetPerNode, angleOffsetVariance, axillaryAgeOffset, axillaryAgeOffsetVariance, terminalRegenRate);
        }
    }

    public void loadFromNbt(CompoundNBT compound) {
        directionHelper.loadFromNbt(compound);
        grower.loadFromNbt(compound);
        terminator.loadFromNbt(compound);

        maxHeight = compound.getInt("maxHeight");
        minimumBranchAge = compound.getInt("minimumBranchAge");
        maximumBranchOrder = compound.getInt("maximumBranchOrder");
        meristemsPerNode = compound.getFloat("meristemsPerNode");
        meristemsPerNodeVariance = compound.getFloat("meristemsPerNodeVariance");
        meristemsPerAxillaryNode = compound.getFloat("meristemsPerAxillaryNode");
        meristemsPerAxillaryNodeVariance = compound.getFloat("meristemsPerAxillaryNodeVariance");
        angleOffsetPerNode = compound.getFloat("angleOffsetPerNode");
        angleOffsetVariance = compound.getFloat("angleOffsetVariance");
        axillaryAgeOffset = compound.getFloat("axillaryAgeOffset");
        axillaryAgeOffsetVariance = compound.getFloat("axillaryAgeOffsetVariance");
        terminalRegenRate = compound.getFloat("terminalRegenRate");
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        directionHelper.saveToNbt(compound);
        grower.saveToNbt(compound);
        terminator.saveToNbt(compound);

        compound.putInt("maxHeight", maxHeight);
        compound.putInt("minimumBranchAge", minimumBranchAge);
        compound.putInt("maximumBranchOrder", maximumBranchOrder);
        compound.putFloat("meristemsPerNode", meristemsPerNode);
        compound.putFloat("meristemsPerNodeVariance", meristemsPerNodeVariance);
        compound.putFloat("meristemsPerAxillaryNode", meristemsPerAxillaryNode);
        compound.putFloat("meristemsPerAxillaryNodeVariance", meristemsPerAxillaryNodeVariance);
        compound.putFloat("angleOffsetPerNode", angleOffsetPerNode);
        compound.putFloat("angleOffsetVariance", angleOffsetVariance);
        compound.putFloat("axillaryAgeOffset", axillaryAgeOffset);
        compound.putFloat("axillaryAgeOffsetVariance", axillaryAgeOffsetVariance);
        compound.putFloat("terminalRegenRate", terminalRegenRate);
        return compound;
    }
}
