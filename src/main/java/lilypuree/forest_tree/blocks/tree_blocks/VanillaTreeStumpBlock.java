package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeStump;

public class VanillaTreeStumpBlock extends TreeStump {
        public VanillaTreeStumpBlock(Properties properties) {
        super(8.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(STUMP, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
    }

}
