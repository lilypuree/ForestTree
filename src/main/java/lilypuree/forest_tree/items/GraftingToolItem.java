//package lilypuree.forest_tree.items;
//
//import lilypuree.forest_tree.blocks_old.ITreeBlock;
//import lilypuree.forest_tree.blocks_old.ThicknessCycler;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemUseContext;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class GraftingToolItem extends Item {
//    public GraftingToolItem(Item.Properties builder) {
//        super(builder);
//    }
//
//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
//        PlayerEntity player = context.getPlayer();
//        World world = context.getWorld();
//        BlockPos pos = context.getPos();
//        BlockState block = world.getBlockState(pos);
//        if(playerMatchesCycleCondition(player) && block instanceof ITreeBlock){
//            cycle(world, pos, block);
//            return ActionResultType.SUCCESS;
//        }
//        return super.onItemUse(context);
//    }
//
//    private void cycle(World worldIn, BlockPos pos, BlockState blockIn){
//        BlockState cycledBlock = (new ThicknessCycler(blockIn)).cycleThickness();
//        worldIn.setBlockState(pos, cycledBlock);
//    }
//
//    private boolean playerMatchesCycleCondition(PlayerEntity player){
//        return player != null && player.isCreative() && player.isCrouching();
//    }
//}
