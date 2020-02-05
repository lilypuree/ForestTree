package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeBranch;
import lilypuree.forest_tree.blocks.properties.BranchType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class ThickestTreeBranchBlock extends TreeBranch {
    public ThickestTreeBranchBlock(Properties properties){
        super(6.0F, 4.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(MAIN_BRANCH, Boolean.TRUE).with(FACING, Direction.NORTH).with(RIGHT_BRANCH, Boolean.FALSE).with(LEFT_BRANCH, Boolean.FALSE).with(CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));
    }


}
