//package lilypuree.forest_tree.blocks_old;
//
//import lilypuree.forest_tree.blocks_old.properties.ForestTreeProperties;
//import lilypuree.forest_tree.datagen.types.ThicknessTypes;
//import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
//import lilypuree.forest_tree.datagen.types.WoodTypes;
//import lilypuree.forest_tree.Registration;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.state.properties.BlockStateProperties;
//
//public class ThicknessCycler {
//
//    private BlockState blockState;
//    private String path;
//
//    public ThicknessCycler(BlockState state){
//        this.blockState = state;
//        path = blockState.getBlock().getRegistryName().getPath();
//    }
//
//    public BlockState cycleThickness() {
//        Block block = blockState.getBlock();
//        if (block instanceof TreeStump) {
//            return cycleStump();
//        } else if (block instanceof TreeTrunk) {
//            return cycleTrunk();
//        } else if (block instanceof TreeBranch) {
//            return cycleBranch();
//        }
//        return blockState;
//    }
//
//    private BlockState cycleStump() {
//        Block block = Registration.getTreeBlock(getWoodType(), getThicknessType(), TreeBlockTypes.STUMP);
//        return block.getDefaultState().with(ForestTreeProperties.STUMP, blockState.get(ForestTreeProperties.STUMP)).with(BlockStateProperties.WATERLOGGED, blockState.get(BlockStateProperties.WATERLOGGED));
//    }
//
//    private BlockState cycleTrunk(){
//        Block block = Registration.getTreeBlock(getWoodType(), getThicknessType(), TreeBlockTypes.TRUNK);
//        return block.getDefaultState()
//                .with(ForestTreeProperties.NORTH_CONNECTION, blockState.get(ForestTreeProperties.NORTH_CONNECTION))
//                .with(ForestTreeProperties.SOUTH_CONNECTION, blockState.get(ForestTreeProperties.SOUTH_CONNECTION))
//                .with(ForestTreeProperties.EAST_CONNECTION, blockState.get(ForestTreeProperties.EAST_CONNECTION))
//                .with(ForestTreeProperties.WEST_CONNECTION, blockState.get(ForestTreeProperties.WEST_CONNECTION))
//                .with(BlockStateProperties.UP, blockState.get(BlockStateProperties.UP))
//                .with(BlockStateProperties.WATERLOGGED, blockState.get(BlockStateProperties.WATERLOGGED));
//    }
//
//    private BlockState cycleBranch(){
//        Block block = Registration.getTreeBlock(getWoodType(), getThicknessType(), TreeBlockTypes.BRANCH);
//        return block.getDefaultState()
//                .with(ForestTreeProperties.MAIN_BRANCH, blockState.get(ForestTreeProperties.MAIN_BRANCH))
//                .with(ForestTreeProperties.LEFT_BRANCH, blockState.get(ForestTreeProperties.LEFT_BRANCH))
//                .with(ForestTreeProperties.RIGHT_BRANCH, blockState.get(ForestTreeProperties.RIGHT_BRANCH))
//                .with(ForestTreeProperties.CONNECTION, blockState.get(ForestTreeProperties.CONNECTION))
//                .with(BlockStateProperties.FACING, blockState.get(BlockStateProperties.HORIZONTAL_FACING))
//                .with(BlockStateProperties.WATERLOGGED, blockState.get(BlockStateProperties.WATERLOGGED));
//    }
//
//    private ThicknessTypes getThicknessType() {
//        return ThicknessTypes.fromPath(path);
//    }
//
//    private WoodTypes getWoodType() {
//        return WoodTypes.fromPath(path);
//    }
//
//}
//
