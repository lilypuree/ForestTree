package lilypuree.forest_tree.trees.client.gui;

public enum Module {
    CREATION(0), GROWTH(1), TERMINATION(2), DIRECTIONS(3);

    private int moduleIndex;
    private static final String[][] parameterNames = new String[][]{
            {"maxHeight", "minimumBranchAge", "meristemsPerNode", "meristemsPerNodeVariance", "meristemsPerAxillaryNode", "meristemsPerAxillaryNodeVariance","angleOffsetPerNode", "angleOffsetVariance", "axillaryAgeOffset", "axillaryAgeOffsetVariance", "terminalRegenRate"},
            {"nodeInterval", "nodeIntervalVariance", "axillaryNodeInterval", "axillaryNodeIntervalVariance", "terminalGrowthRate", "terminalGrowthRateVariance","axillaryGrowthRate", "axillaryGrowthRateVariance"},
            {"terminalDeathAgeMin", "terminalDeathAgeMax", "axillaryDeathAgeMin", "axillaryDeathAgeMax", "terminalDeathAgeMin", "branchDeathAgeMin","branchDeathAgeMax"},
            {"secondaryYawParallel", "secondaryYaw45Degree", "secondaryYaw90Degree", "secondaryPitch45Degree", "secondaryPitchHorizontal", "secondaryPitch135Degree","terminalYawVariance", "axillaryYawVariance", "terminalMarkovMatrix", "axillaryMarkovMatrix", "branchingMarkovMatrix"}
    };

    Module(int i){
        this.moduleIndex = i;
    }

    public int getParameterCount() {
        return parameterNames[moduleIndex].length;
    }

    public String[] getParameterNames() {
        return parameterNames[moduleIndex];
    }
}
