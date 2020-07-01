package lilypuree.forest_tree.trees.world.gen.feature;

import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeristemFactory {

    private Random rand;
    private BranchDirectionHelper directionHelper;

    private Species species = new ModSpecies.Pine();
    private int maxHeight = 30;
    private int minimumBranchAge = 1;

    private float meristemsPerNode = 4.5f;
    private float meristemsPerNodeVariance = 0.8f;
    private float meristemsPerAxillaryNode = 2.1f;
    private float meristemsPerAxillaryNodeVariance = 0.8f;

    private float angleOffsetPerNode = 69;
    private float angleOffsetVariance = 9;

    private float axillaryAgeOffset = 1.4f;
    private float axillaryAgeOffsetVariance = 0.5f;

    //in meters
    private float nodeInterval = 1.3f;
    private float nodeIntervalVariance = 0.5f;

    private float axillaryNodeInterval = 1.1f;
    private float axillaryNodeIntervalVariance = 0.5f;

    private float terminalGrowthRate = 2.4f;
    private float terminalGrowthRateVariance = 0.5f;

    private float axillaryGrowthRate = 1.2f;
    private float axillaryGrowthRateVariance = 0.5f;

    private Function<Integer, Float> terminalDeathRateGetter = (age -> 0.1f / age);
    private Function<Integer, Float> axillaryDeathRateGetter = (age -> {
        if (age > 6) return 0.9f;
        else return (float) age * age / 144;
    });
    private Function<Integer, Float> terminalRegenRateGetter = (age -> 0.6f);

    public MeristemFactory() {
        this.directionHelper = new BranchDirectionHelper(this);
    }

    public Meristem createAxillaryMeristem(int age, BlockPos pos, GrowthVec dir) {
        Meristem.Axillary meristem = new Meristem.Axillary();
        meristem.init(age, pos, dir, this);
        return meristem;
    }

    public Meristem createTerminalMeristem(int age, BlockPos pos, GrowthVec dir) {
        Meristem.Terminal meristem = new Meristem.Terminal();
        meristem.init(age, pos, dir, this);
        return meristem;
    }

    public Collection<Meristem> createMeristemsOnDeadNode(int count, int age, BlockPos source, Meristem main) {
        Collection<Meristem> results;
        boolean shouldRegenerateTerminal = rand.nextFloat() < getTerminalRegenRate(age);

        if (shouldRegenerateTerminal) {
            GrowthVec terminalGrowthVec = directionHelper.getRandomTerminalDirection();
            Meristem newTerminal = createTerminalMeristem(age, source, terminalGrowthVec);
            results = createMeristemsOnNode(Math.max(count - 1, 0), age, source, newTerminal);
            results.add(newTerminal);
        } else {
            Collection<GrowthVec> directions;
            if (main.getDirection().pitch.isVertical()) {
                directions = directionHelper.axillaryDirectionsFromVertical(count, main);
            } else {
                directions = directionHelper.axillaryDirectionsFromSideways(count, main.getDirection(), false);
            }
            results = directions.stream().flatMap(dir -> Stream.of(createAxillaryMeristem(age, source, dir))).collect(Collectors.toSet());

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
        return directions.stream().flatMap(dir -> Stream.of(createAxillaryMeristem(age, source, dir))).collect(Collectors.toSet());
    }

    public BranchDirectionHelper getDirectionHelper() {
        return directionHelper;
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

    public float getMeristemDeathRate(Meristem meristem) {
        if (meristem.isTerminal())
            return terminalDeathRateGetter.apply(meristem.getAge());
        else
            return axillaryDeathRateGetter.apply(meristem.getAge());
    }


    public float getTerminalRegenRate(int age) {
        return terminalRegenRateGetter.apply(age);
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

    public float getNodeInterval() {
        return nodeInterval;
    }
    public float getAxillaryNodeInterval() {
        return axillaryNodeInterval;
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
        this.directionHelper.setRandom(rand);
    }


}
