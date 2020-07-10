package lilypuree.forest_tree.trees.world.gen.feature.parametric;

import net.minecraft.nbt.CompoundNBT;

public class MeristemTerminator {
    private float terminalDeathAgeMin = 0.0f;
    private float terminalDeathAgeMax = 0.0f;
    private float axillaryDeathAgeMin = 3.8f;
    private float axillaryDeathAgeMax = 9.2f;
    private float branchDeathAgeMin = 5.4f;
    private float branchDeathAgeMax = 8.3f;

    public MeristemTerminator init(float terminalDeathAgeMaxIn, float terminalDeathAgeMinIn, float axillaryDeathAgeMaxIn, float axillaryDeathAgeMinIn, float branchDeathAgeMaxIn, float branchDeathAgeMinIn) {

        this.terminalDeathAgeMax = terminalDeathAgeMaxIn;
        this.terminalDeathAgeMin = terminalDeathAgeMinIn;
        this.axillaryDeathAgeMax = axillaryDeathAgeMaxIn;
        this.axillaryDeathAgeMin = axillaryDeathAgeMinIn;
        this.branchDeathAgeMax = branchDeathAgeMaxIn;
        this.branchDeathAgeMin = branchDeathAgeMinIn;
        return this;
    }

    public float getMeristemDeathRate(Meristem meristem) {
        if (meristem.isTerminal()) {
            return uniformRandomAlt(terminalDeathAgeMax, terminalDeathAgeMin, meristem.getAge());
        } else {
            if (meristem.getLength() == 0) { //a newly generated axillary.
                return uniformRandomAlt(branchDeathAgeMax, branchDeathAgeMin, meristem.getAge());
            }
            return uniformRandomAlt(axillaryDeathAgeMax, axillaryDeathAgeMin, meristem.getAge());
        }
    }


    private float uniformRandomAlt(float max, float min, float x) {
        if (x <= min) return 0;
        else if (x >= max) return 1;
        else {
            float width = max - min;
            return (x - min) / width;
        }
    }

    public static class Builder {

        private float terminalDeathAgeMin = 0.0f;
        private float terminalDeathAgeMax = 0.0f;
        private float axillaryDeathAgeMin = 3.8f;
        private float axillaryDeathAgeMax = 9.2f;
        private float branchDeathAgeMin = 5.4f;
        private float branchDeathAgeMax = 8.3f;



        public MeristemTerminator.Builder terminalDeathAge(float max, float min) {
            if (min > max) throw new IllegalArgumentException();
            this.terminalDeathAgeMax = max;
            this.terminalDeathAgeMin = min;
            return this;
        }

        public MeristemTerminator.Builder axillaryDeathAge(float max, float min) {
            if (min > max) throw new IllegalArgumentException();
            this.axillaryDeathAgeMax = max;
            this.axillaryDeathAgeMin = min;
            return this;
        }

        public MeristemTerminator.Builder branchDeathAge(float max, float min) {
            if (min > max) throw new IllegalArgumentException();
            this.branchDeathAgeMax = max;
            this.branchDeathAgeMin = min;
            return this;
        }


        public MeristemTerminator build() {
            return new MeristemTerminator().init(terminalDeathAgeMax, terminalDeathAgeMin, axillaryDeathAgeMax, axillaryDeathAgeMin, branchDeathAgeMax, branchDeathAgeMin);
        }
    }

    public void loadFromNbt(CompoundNBT compound) {
        terminalDeathAgeMin = compound.getFloat("terminalDeathAgeMin");
        terminalDeathAgeMax = compound.getFloat("terminalDeathAgeMax");
        axillaryDeathAgeMin = compound.getFloat("axillaryDeathAgeMin");
        axillaryDeathAgeMax = compound.getFloat("axillaryDeathAgeMax");
        branchDeathAgeMin = compound.getFloat("branchDeathAgeMin");
        branchDeathAgeMax = compound.getFloat("branchDeathAgeMax");
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        compound.putFloat("terminalDeathAgeMin", terminalDeathAgeMin);
        compound.putFloat("terminalDeathAgeMax", terminalDeathAgeMax);
        compound.putFloat("axillaryDeathAgeMin", axillaryDeathAgeMin);
        compound.putFloat("axillaryDeathAgeMax", axillaryDeathAgeMax);
        compound.putFloat("terminalDeathAgeMin", terminalDeathAgeMin);
        compound.putFloat("branchDeathAgeMin", branchDeathAgeMin);
        compound.putFloat("branchDeathAgeMax", branchDeathAgeMax);

        return compound;
    }
}
