//package lilypuree.forest_tree.trees.world.gen.feature;
//
//import com.google.common.collect.ImmutableMap;
//import com.mojang.datafixers.Dynamic;
//import com.mojang.datafixers.types.DynamicOps;
//import net.minecraft.util.IDynamicSerializable;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.gen.IWorldGenerationReader;
//import net.minecraft.world.gen.feature.TreeFeatureConfig;
//import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
//
//import java.util.Random;
//import java.util.Set;
//
//public abstract class AdvancedFoliagePlacer implements IDynamicSerializable {
//    protected final int foliageRadius;
//    protected final int foliageRadiusRandom;
//    protected final FoliagePlacerType<?> foliagePlacerType;
//
//    public AdvancedFoliagePlacer(int foliageRadiusIn, int foliageRadiusRandomIn, FoliagePlacerType<?> foliagePlacerType) {
//        this.foliageRadius = foliageRadiusIn;
//        this.foliageRadiusRandom = foliageRadiusRandomIn;
//        this.foliagePlacerType = foliagePlacerType;
//    }
//
//    public abstract void generateLeaves(IWorldGenerationReader reader, Random rand, AdvancedTreeFeatureConfig config, int baseHeight, int trunkHeight, int foliageRadiusIn, BlockPos pos, Set<BlockPos> leaves);
//
//    public abstract int getFoliageRadius(Random rand, int trunkHeight, int baseHeight, AdvancedTreeFeatureConfig config);
//
//    protected abstract boolean noLeaves(Random rand, int baseHeight, int x, int treeYPos, int z, int radius);
//
//    public abstract int requiredWidth(int trunkHeight, int baseHeight, int foliageRadiusIn, int treePosY);
//
//    protected void generateLeavesLayer(IWorldGenerationReader reader, Random rand, AdvancedTreeFeatureConfig config, int baseHeight, BlockPos pos, int treeYPos, int radius, Set<BlockPos> leaves) {
//        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
//
//        for(int x = -radius; x <= radius; ++x) {
//            for(int z = -radius; z <= radius; ++z) {
//                if (!this.noLeaves(rand, baseHeight, x, treeYPos, z, radius)) {
//                    mutablePos.setPos(x + pos.getX(), treeYPos + pos.getY(), z + pos.getZ());
//                    this.setLeaves(reader, rand, mutablePos, config, leaves);
//                }
//            }
//        }
//
//    }
//
//    protected void setLeaves(IWorldGenerationReader reader, Random rand, BlockPos pos, AdvancedTreeFeatureConfig config, Set<BlockPos> leaves) {
//        if (AdvancedAbstractTreeFeature.isAirOrLeaves(reader, pos) || AdvancedAbstractTreeFeature.isTallPlants(reader, pos) || AdvancedAbstractTreeFeature.isWater(reader, pos)) {
//            reader.setBlockState(pos, config.leavesProvider.getBlockState(rand, pos), 19);
//            leaves.add(pos.toImmutable());
//        }
//
//    }
//
//    public <T> T serialize(DynamicOps<T> p_218175_1_) {
//        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
//        builder.put(p_218175_1_.createString("type"), p_218175_1_.createString(Registry.FOLIAGE_PLACER_TYPE.getKey(this.foliagePlacerType).toString())).put(p_218175_1_.createString("radius"), p_218175_1_.createInt(this.foliageRadius)).put(p_218175_1_.createString("radius_random"), p_218175_1_.createInt(this.foliageRadiusRandom));
//        return (new Dynamic<>(p_218175_1_, p_218175_1_.createMap(builder.build()))).getValue();
//    }
//}
