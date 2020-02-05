package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeTrunk;
import lilypuree.forest_tree.blocks.properties.BranchType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class ThinTreeTrunkBlock extends TreeTrunk {
    public ThinTreeTrunkBlock(Properties properties) {
        super(2.0F, 2.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(UP, Boolean.TRUE).with(NORTH_CONNECTION, BranchType.NONE).with(SOUTH_CONNECTION, BranchType.NONE).with(EAST_CONNECTION, BranchType.NONE).with(WEST_CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));    }
}
