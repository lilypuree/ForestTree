package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeBranch;
import lilypuree.forest_tree.blocks.properties.BranchType;
import net.minecraft.util.Direction;

public class VanillaTreeBranchBlock extends TreeBranch {
    public VanillaTreeBranchBlock(Properties properties){
        super(8.0F, 6.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(MAIN_BRANCH, Boolean.TRUE).with(FACING, Direction.NORTH).with(RIGHT_BRANCH, Boolean.FALSE).with(LEFT_BRANCH, Boolean.FALSE).with(CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));
    }

}
