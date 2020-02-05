package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeTrunk;
import lilypuree.forest_tree.blocks.properties.BranchType;

public class VanillaTreeTrunkBlock extends TreeTrunk {
    public VanillaTreeTrunkBlock(Properties properties) {
        super(8.0F, 6.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(UP, Boolean.TRUE).with(NORTH_CONNECTION, BranchType.NONE).with(SOUTH_CONNECTION, BranchType.NONE).with(EAST_CONNECTION, BranchType.NONE).with(WEST_CONNECTION, BranchType.NONE).with(WATERLOGGED, Boolean.FALSE));    }
}
