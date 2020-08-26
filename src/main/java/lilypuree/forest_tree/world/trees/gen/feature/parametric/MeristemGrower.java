package lilypuree.forest_tree.world.trees.gen.feature.parametric;

import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class MeristemGrower {

    private Random rand;

    //the length between nodes (in meters)
    private float nodeInterval = 1.3f;
    private float nodeIntervalVariance = 0.5f;

    private float axillaryNodeInterval = 1.1f;
    private float axillaryNodeIntervalVariance = 0.5f;

    //the length meristems in one age
    private float terminalGrowthRate = 2.4f;
    private float terminalGrowthRateVariance = 0.5f;

    private float axillaryGrowthRate = 1.2f;
    private float axillaryGrowthRateVariance = 0.5f;

    public MeristemGrower init(float nodeIntervalIn, float nodeIntervalVarianceIn, float axillaryNodeIntervalIn, float axillaryNodeIntervalVarianceIn, float terminalGrowthRateIn, float terminalGrowthRateVarianceIn, float axillaryGrowthRateIn, float axillaryGrowthRateVarianceIn) {
        this.nodeInterval = nodeIntervalIn;
        this.nodeIntervalVariance = nodeIntervalVarianceIn;
        this.axillaryNodeInterval = axillaryNodeIntervalIn;
        this.axillaryNodeIntervalVariance = axillaryNodeIntervalVarianceIn;
        this.terminalGrowthRate = terminalGrowthRateIn;
        this.terminalGrowthRateVariance = terminalGrowthRateVarianceIn;
        this.axillaryGrowthRate = axillaryGrowthRateIn;
        this.axillaryGrowthRateVariance = axillaryGrowthRateVarianceIn;
        return this;
    }

    public float getNodeInterval(boolean isTerminal) {
        return isTerminal ? nodeInterval : axillaryNodeInterval;
    }

    public boolean shouldGenerateNode(float interval, boolean isTerminal) {
        return isTerminal ? uniformRandom(nodeInterval, nodeIntervalVariance, interval) : uniformRandom(axillaryNodeInterval, axillaryNodeIntervalVariance, interval);
    }

    public boolean shouldBranchAge(float interval, boolean isTerminal) {
        return isTerminal ? uniformRandom(terminalGrowthRate, terminalGrowthRateVariance, interval) : uniformRandom(axillaryGrowthRate, axillaryGrowthRateVariance, interval);
    }

    /**
     * evaluates x based on a uniform distribution made from the parameters
     *
     * @param midpoint  the point where probability of getting selected is 50%
     * @param widthHalf widht/2 of the region where the uniform distribution cdf increases.
     * @return whether x is selected
     */
    private boolean uniformRandom(float midpoint, float widthHalf, float x) {
        boolean aboveLimit = x > midpoint + widthHalf;
        boolean uniformRandom = rand.nextFloat() < 0.5f + (0.5f / widthHalf) * (x - midpoint);
        return aboveLimit || uniformRandom;
    }

    public void setRandom(Random rand) {
        this.rand = rand;
    }

    public static class Builder {

        //in meters
        private float nodeInterval = 1.3f;
        private float nodeIntervalVariance = 0.5f;

        private float axillaryNodeInterval = 1.1f;
        private float axillaryNodeIntervalVariance = 0.5f;

        private float terminalGrowthRate = 2.4f;
        private float terminalGrowthRateVariance = 0.5f;

        private float axillaryGrowthRate = 1.2f;
        private float axillaryGrowthRateVariance = 0.5f;


        public Builder nodeInterval(float nodeIntervalIn, float variance) {
            this.nodeInterval = nodeIntervalIn;
            this.nodeIntervalVariance = variance;
            return this;
        }

        public Builder axillaryNodeInterval(float axillaryNodeIntervalIn, float variance) {
            this.axillaryNodeInterval = axillaryNodeIntervalIn;
            this.axillaryNodeIntervalVariance = variance;
            return this;
        }

        public Builder terminalGrowthRate(float terminalGrowthRateIn, float variance) {
            this.terminalGrowthRate = terminalGrowthRateIn;
            this.terminalGrowthRateVariance = variance;
            return this;
        }

        public Builder axillaryGrowthRate(float axillaryGrowthRateIn, float variance) {
            this.axillaryGrowthRate = axillaryGrowthRateIn;
            this.axillaryGrowthRateVariance = variance;
            return this;
        }

        public MeristemGrower build(Species species) {
            return new MeristemGrower().init(nodeInterval, nodeIntervalVariance, axillaryNodeInterval, axillaryNodeIntervalVariance, terminalGrowthRate, terminalGrowthRateVariance, axillaryGrowthRate, axillaryGrowthRateVariance);
        }
    }

    public void loadFromNbt(CompoundNBT compound) {
        nodeInterval = compound.getFloat("nodeInterval");
        nodeIntervalVariance = compound.getFloat("nodeIntervalVariance");
        axillaryNodeInterval = compound.getFloat("axillaryNodeInterval");
        axillaryNodeIntervalVariance = compound.getFloat("axillaryNodeIntervalVariance");
        terminalGrowthRate = compound.getFloat("terminalGrowthRate");
        terminalGrowthRateVariance = compound.getFloat("terminalGrowthRateVariance");
        axillaryGrowthRate = compound.getFloat("axillaryGrowthRate");
        axillaryGrowthRateVariance = compound.getFloat("axillaryGrowthRateVariance");
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        compound.putFloat("nodeInterval", nodeInterval);
        compound.putFloat("nodeIntervalVariance", nodeIntervalVariance);
        compound.putFloat("axillaryNodeInterval", axillaryNodeInterval);
        compound.putFloat("axillaryNodeIntervalVariance", axillaryNodeIntervalVariance);
        compound.putFloat("terminalGrowthRate", terminalGrowthRate);
        compound.putFloat("terminalGrowthRateVariance", terminalGrowthRateVariance);
        compound.putFloat("axillaryGrowthRate", axillaryGrowthRate);
        compound.putFloat("axillaryGrowthRateVariance", axillaryGrowthRateVariance);
        return compound;
    }
}
