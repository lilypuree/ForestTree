//package lilypuree.forest_tree.trees.world.gen.feature;
//
//import com.mojang.datafixers.Dynamic;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.gen.IWorldGenerationReader;
//import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
//
//import java.util.Random;
//import java.util.Set;
//
//public class AdvancedPineFoliagePlacer extends AdvancedFoliagePlacer {
//    public AdvancedPineFoliagePlacer(int leavesHeight, int leavesHeightRandom) {
//        super(leavesHeight, leavesHeightRandom, FoliagePlacerType.field_227388_c_);
//    }
//
//    public <T> AdvancedPineFoliagePlacer(Dynamic<T> ops) {
//        this(ops.get("radius").asInt(0), ops.get("radius_random").asInt(0));
//    }
//
//    @Override
//    public void generateLeaves(IWorldGenerationReader reader, Random rand, AdvancedTreeFeatureConfig config, int baseHeight, int trunkHeight, int foliageRadiusIn, BlockPos pos, Set<BlockPos> leaves) {
//        int radius = 0;
//        for(int treeYPos = baseHeight; treeYPos >= trunkHeight; --treeYPos) {
//            this.generateLeavesLayer(reader, rand, config, baseHeight, pos, treeYPos, radius, leaves);
//            if (radius >= 1 && treeYPos == trunkHeight + 1) {
//                --radius;
//            } else if (radius < foliageRadiusIn) {
//                ++radius;
//            }
//        }
//
//    }
//
//    @Override
//    public int getFoliageRadius(Random rand, int trunkHeight, int baseHeight, AdvancedTreeFeatureConfig config) {
//        return this.foliageRadius + rand.nextInt(this.foliageRadiusRandom + 1) + rand.nextInt(baseHeight - trunkHeight + 1);
//    }
//
//    @Override
//    protected boolean noLeaves(Random rand, int baseHeight, int x, int treeYPos, int z, int radius) {
//        return Math.abs(x) == radius && Math.abs(z) == radius && radius > 0;
//    }
//
//    @Override
//    public int requiredWidth(int trunkHeight, int baseHeight, int foliageRadiusIn, int treePosY) {
//        return treePosY <= 1 ? 0 : 2;
//    }
//}
