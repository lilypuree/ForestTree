package lilypuree.forest_tree.trees.treemodels;

import lilypuree.forest_tree.api.gen.ForestContext;
import lilypuree.forest_tree.api.gen.GenerationTarget;
import lilypuree.forest_tree.api.gen.ITreeModel;
import lilypuree.forest_tree.api.gen.ITreePlacer;
import lilypuree.forest_tree.api.genera.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class PalmTreeModel implements ITreeModel {

    private Species species;

    public PalmTreeModel(Species species) {
        this.species = species;
    }

    @Override
    public boolean createTree(ForestContext context, GenerationTarget target, BlockPos pos, ITreePlacer placer, Random rand) {
        int heightRange = 9 + rand.nextInt(5);

        placer.placeStump(pos, species.getWood(), 0.55f);
        BlockPos.Mutable tempPos = new BlockPos.Mutable(pos);
        Vec3i source = new Vec3i(0, -1, 0);
        for (int i = 0; i < heightRange; i++) {
            tempPos.move(Direction.UP);
            placer.placeBranch(tempPos, species.getWood(), source, 0.55f - i * 0.015f);
        }
        tempPos.move(Direction.UP);

        placer.placeFoliage(tempPos, species.getFoliage());
        return true;
    }
}
