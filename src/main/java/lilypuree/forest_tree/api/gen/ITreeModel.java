package lilypuree.forest_tree.api.gen;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public interface ITreeModel {

    boolean createTree(ForestContext context, GenerationTarget target, BlockPos pos, ITreePlacer placer, Random rand);
}
