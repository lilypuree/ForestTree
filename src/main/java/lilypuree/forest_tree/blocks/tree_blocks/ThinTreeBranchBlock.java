package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeBranch;
import lilypuree.forest_tree.blocks.properties.BranchType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class ThinTreeBranchBlock extends TreeBranch {
    public ThinTreeBranchBlock(Properties properties){
        super(2.0F, 2.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(MAIN_BRANCH, Boolean.TRUE).with(FACING, Direction.NORTH).with(RIGHT_BRANCH, Boolean.FALSE).with(LEFT_BRANCH, Boolean.FALSE).with(CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));
    }


  }
