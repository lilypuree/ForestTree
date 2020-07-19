package lilypuree.forest_tree.trees.world.gen.feature.parametric;

public class Parameter {

    public static final Parameter[][] parameters = new Parameter[][]{
            {p("maxHeight", 0), p("minimumBranchAge", 0), pf("meristemsPerNode"), pf("meristemsPerNodeVariance"), pf("meristemsPerAxillaryNode"), pf("meristemsPerAxillaryNodeVariance"), pf("angleOffsetPerNode"), pf("angleOffsetVariance"), pf("axillaryAgeOffset"), pf("axillaryAgeOffsetVariance"), pf("terminalRegenRate")},
            {pf("nodeInterval"), pf("nodeIntervalVariance"), pf("axillaryNodeInterval"), pf("axillaryNodeIntervalVariance"), pf("terminalGrowthRate"), pf("terminalGrowthRateVariance"), pf("axillaryGrowthRate"), pf("axillaryGrowthRateVariance")},
            {pf("terminalDeathAgeMin"), pf("terminalDeathAgeMax"), pf("axillaryDeathAgeMin"), pf("axillaryDeathAgeMax"), pf("branchDeathAgeMin"), pf("branchDeathAgeMax")},
            {pf("secondaryYawParallel"), pf("secondaryYaw45Degree"), pf("secondaryYaw90Degree"), pf("secondaryPitch45Degree"), pf("secondaryPitchHorizontal"), pf("secondaryPitch135Degree"), pf("terminalYawVariance"), pf("axillaryYawVariance"), p("terminalMarkovMatrix", 2), p("axillaryMarkovMatrix", 2), p("branchingMarkovMatrix", 2)}
    };


    public String name;

    /**
     * 0 : int, 1 : float, 2 : float array
     */
    public byte type;

    private Parameter(String name, byte type) {
        this.name = name;
        this.type = type;
    }

    private static Parameter pf(String name) {
        return new Parameter(name, (byte) 1);
    }

    private static Parameter p(String name, int type) {
        return new Parameter(name, (byte) type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Parameter) {
            return ((Parameter) obj).name.equals(this.name);
        }
        return false;
    }
}
