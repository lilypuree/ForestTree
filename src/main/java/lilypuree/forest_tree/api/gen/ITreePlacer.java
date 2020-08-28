package lilypuree.forest_tree.api.gen;

import lilypuree.forest_tree.api.genera.FoliageCategory;
import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.api.genera.WoodCategory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public interface ITreePlacer {

    boolean placeStump(BlockPos pos, WoodCategory category, float thickness);

    boolean placeBranch(BlockPos pos, WoodCategory category ,Vec3i dir, float thickness);

    boolean placeBranchEnd(BlockPos pos, WoodCategory category ,Vec3i dir, float thickness);

    boolean placeFoliage(BlockPos pos, FoliageCategory foliage);

    boolean addBlock(BlockPos pos, BlockState state);

}
