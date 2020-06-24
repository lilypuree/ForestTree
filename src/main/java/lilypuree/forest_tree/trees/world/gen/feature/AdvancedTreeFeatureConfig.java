package lilypuree.forest_tree.trees.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class AdvancedTreeFeatureConfig extends AdvancedBaseTreeFeatureConfig {
    public final int maxWaterDepth;
    public final boolean ignoreVines;
    public final int height;
    public final int heightRandom;
    public final int extraHeight;
    public final int extraBranchLength;
    public final int branchYPosRandom;

    //probably a bunch of variables, don't know what they'll be but I'll abstract it out as much as I can

    public AdvancedTreeFeatureConfig(Species speciesIn, List<TreeDecorator> decoratorsIn, int ageIn, int maxWaterDepthIn, boolean ignoreVinesIn, int heightIn, int heightRandomIn, int  extraHeightIn ,int extraBranchLengthIn, int branchYPosRandomIn) {
        super(speciesIn, decoratorsIn, ageIn);
        this.maxWaterDepth = maxWaterDepthIn;
        this.ignoreVines = ignoreVinesIn;
        this.height = heightIn;
        this.heightRandom = heightRandomIn;
        this.extraHeight = extraHeightIn;
        this.extraBranchLength = extraBranchLengthIn;
        this.branchYPosRandom = branchYPosRandomIn;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
//        builder.put(ops.createString("foliage_placer"), this.foliagePlacer.serialize(ops)).put(ops.createString("height_rand_a"), ops.createInt(this.heightRandA)).put(ops.createString("height_rand_b"), ops.createInt(this.heightRandB)).put(ops.createString("trunk_height"), ops.createInt(this.trunkHeight)).put(ops.createString("trunk_height_random"), ops.createInt(this.trunkHeightRandom)).put(ops.createString("trunk_top_offset"), ops.createInt(this.trunkTopOffset)).put(ops.createString("trunk_top_offset_random"), ops.createInt(this.trunkTopOffsetRandom)).put(ops.createString("foliage_height"), ops.createInt(this.foliageHeight)).put(ops.createString("foliage_height_random"), ops.createInt(this.foliageHeightRandom)).put(ops.createString("max_water_depth"), ops.createInt(this.maxWaterDepth)).put(ops.createString("ignore_vines"), ops.createBoolean(this.ignoreVines));
        Dynamic<T> dynamic = new Dynamic<>(ops, ops.createMap(builder.build()));
        return dynamic.merge(super.serialize(ops));
    }

    //deserialize

    @Override
    protected AdvancedTreeFeatureConfig setSapling(IPlantable value) {
        super.setSapling(value);
        return this;
    }

    public static class Builder extends AdvancedBaseTreeFeatureConfig.Builder{
        private List<TreeDecorator> decorators = ImmutableList.of();
        private int age;
        private int maxWaterDepth;
        private boolean ignoreVines;
        public int height;
        public int heightRandom;
        public int extraHeight;
        public int extraBranchLength;
        public int branchYPosRandom;

        public Builder(Species speciesIn) {
            super(speciesIn);
        }

        public AdvancedTreeFeatureConfig.Builder decorators(List<TreeDecorator> decoratorsIn){
            this.decorators = decoratorsIn;
            return this;
        }

        public AdvancedTreeFeatureConfig.Builder age(int ageIn){
            this.age = ageIn;
            return this;
        }

        public AdvancedTreeFeatureConfig.Builder maxWaterDepth(int maxWaterDepthIn){
            this.maxWaterDepth =maxWaterDepthIn;
            return this;
        }

        public AdvancedTreeFeatureConfig.Builder ignoreVines() {
            this.ignoreVines = true;
            return this;
        }

        public AdvancedTreeFeatureConfig.Builder height(int heightIn) {
            this.height = heightIn;
            return this;
        }
        public AdvancedTreeFeatureConfig.Builder heightRandom(int heightRandomIn) {
            this.heightRandom = heightRandomIn;
            return this;
        }
        public AdvancedTreeFeatureConfig.Builder extraHeight(int extraHeightIn) {
            this.extraHeight = extraHeightIn;
            return this;
        }
        public AdvancedTreeFeatureConfig.Builder extraBranchLength(int extraBranchLengthIn) {
            this.extraBranchLength = extraBranchLengthIn;
            return this;
        }
        public AdvancedTreeFeatureConfig.Builder branchYPosRandom(int branchYPosRandomIn) {
            this.branchYPosRandom = branchYPosRandomIn;
            return this;
        }

        @Override
        public AdvancedTreeFeatureConfig.Builder setSapling(IPlantable value) {
            super.setSapling(value);
            return this;
        }

        public AdvancedTreeFeatureConfig build(){
            return new AdvancedTreeFeatureConfig(this.species, this.decorators, this.age, this.maxWaterDepth, this.ignoreVines, height, heightRandom, extraHeight, extraBranchLength, branchYPosRandom).setSapling(this.sapling);
        }
    }

}
