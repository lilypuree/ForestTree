package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.properties.BranchType;
import lilypuree.forest_tree.blocks.TreeBranch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ThickTreeBranchBlock extends TreeBranch {
    public ThickTreeBranchBlock(Block.Properties properties){
        super(4.0F, 2.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(MAIN_BRANCH, Boolean.TRUE).with(FACING, Direction.NORTH).with(RIGHT_BRANCH, Boolean.FALSE).with(LEFT_BRANCH, Boolean.FALSE).with(CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));
    }

}
