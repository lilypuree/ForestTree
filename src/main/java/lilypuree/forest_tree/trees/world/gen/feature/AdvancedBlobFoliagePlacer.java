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
//public class AdvancedBlobFoliagePlacer extends AdvancedFoliagePlacer {
//
//    public AdvancedBlobFoliagePlacer(int foliageRadiusIn, int foliageRadiusRandomIn) {
//        super(foliageRadiusIn, foliageRadiusRandomIn, FoliagePlacerType.field_227386_a_);
//    }
//
//    public <T> AdvancedBlobFoliagePlacer(Dynamic<T> ops) {
//        this(ops.get("radius").asInt(0), ops.get("radius_random").asInt(0));
//    }
//
//    public void generateLeaves(IWorldGenerationReader reader, Random rand, AdvancedTreeFeatureConfig config, int baseHeight, int trunkHeight, int foliageRadiusIn, BlockPos pos, Set<BlockPos> leaves) {
//        for(int treeYPos = baseHeight; treeYPos >= trunkHeight; --treeYPos) {
//            int radius = Math.max(foliageRadiusIn - 1 - (treeYPos - baseHeight) / 2, 0);
//            this.generateLeavesLayer(reader, rand, config, baseHeight, pos, treeYPos, radius, leaves);
//        }
//
//    }
//
//    public int getFoliageRadius(Random rand, int trunkHeight, int baseHeight, AdvancedTreeFeatureConfig config) {
//        return this.foliageRadius + rand.nextInt(this.foliageRadiusRandom + 1);
//    }
//
//    protected boolean noLeaves(Random rand, int baseHeight, int x, int treeYPos, int z, int radius) {
//        return Math.abs(x) == radius && Math.abs(z) == radius && (rand.nextInt(2) == 0 || treeYPos == baseHeight);
//    }
//
//    public int requiredWidth(int trunkHeight, int baseHeight, int foliageRadiusIn, int treePosY) {
//        return treePosY == 0 ? 0 : 1;
//    }
//}
