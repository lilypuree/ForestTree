package lilypuree.forest_tree.trees;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class TreeBlocks {

    public static Block getBranchBlock(Vec3i sourcePos, boolean isEnd, Species species){
        if (isEnd){
            return getBranchEndBlock(sourcePos, species);
        }else
            return getBranchBlock(sourcePos,species);
    }

    public static Block getBranchBlock(Vec3i sourcePos, Species species){
       return Registration.BRANCH_BLOCKS.get(ImmutablePair.of(species.getID(), sourcePos)).get();
    }
    public static Block getBranchEndBlock(Vec3i sourcePos, Species species){
        return Registration.BRANCH_END_BLOCKS.get(ImmutablePair.of(species.getID(), sourcePos)).get();
    }
}
