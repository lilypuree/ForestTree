package lilypuree.forest_tree.trees;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.block.BranchBlock;
import lilypuree.forest_tree.trees.block.StumpBlock;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class TreeBlocks {

    public static BranchBlock getBranchBlock(Vec3i sourcePos, boolean isEnd, Species species){
        if(sourcePos.equals(Vec3i.NULL_VECTOR)){
            return null;
        }
        if (isEnd){
            return getBranchEndBlock(sourcePos, species);
        }else
            return getBranchBlock(sourcePos,species);
    }

    public static BranchBlock getBranchBlock(Vec3i sourcePos, Species species){
       return Registration.BRANCH_BLOCKS.get(ImmutablePair.of(species.getID(), sourcePos)).get();
    }
    public static BranchBlock getBranchEndBlock(Vec3i sourcePos, Species species){
        return Registration.BRANCH_END_BLOCKS.get(ImmutablePair.of(species.getID(), sourcePos)).get();
    }
    public static StumpBlock getStumpBlock(Species species){
        return Registration.STUMP_BLOCKS.get(species).get();
    }
}
