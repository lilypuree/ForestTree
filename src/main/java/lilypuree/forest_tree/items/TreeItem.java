//package lilypuree.forest_tree.items;
//
//import lilypuree.forest_tree.blocks_old.TreeTile;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.ItemUseContext;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.common.util.Lazy;
//
//import java.util.function.Supplier;
//
//public class TreeItem extends Item {
//    private Lazy<Block> type;
//
//    public TreeItem(Properties properties, Supplier<Block> supplier) {
//        super(properties);
//        this.type = Lazy.of(supplier);
//    }
//
//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
//        World world = context.getWorld();
//        Direction dir = context.getPlacementHorizontalFacing().getOpposite();
//        BlockPos plantingPos = context.getPos().offset(context.getFace());
//        PlayerEntity player = context.getPlayer();
//        ItemStack item = context.getItem();
//
//        if(!item.hasTag()) return ActionResultType.PASS;
//
//        if (canPlantInPos(plantingPos, world)) {
//            if (!world.isRemote) {
//                BlockPos treePos = plantingPos.offset(Direction.UP);
//                world.setBlockState(plantingPos, Blocks.DIRT.getDefaultState());
//                BlockState state = type.get().getDefaultState();
//                world.setBlockState(treePos, state);
//                world.notifyBlockUpdate(treePos, state, state, 1 | 2);
//
//
//                TileEntity tileEntity = world.getTileEntity(treePos);
//                if (tileEntity instanceof TreeTile) {
//                    ((TreeTile) tileEntity).setName(item.getTextComponent());
//                    ((TreeTile) tileEntity).setTreeData(item.getTag());
//                    ((TreeTile) tileEntity).replantTree(dir);
//                }
//                if (player != null) {
//                    if (!player.isCreative()) {
//                        player.addExhaustion(0.1F);
//                        item.shrink(1);
//                    }
//                }
//            }
//
//        }
//
//        return super.onItemUse(context);
//    }
//
//    //tree should be planted in a hole surrounded by 'ground' blocks.
//    private boolean canPlantInPos(BlockPos pos, World worldIn) {
//        Block north = worldIn.getBlockState(pos.offset(Direction.NORTH)).getBlock();
//        Block east = worldIn.getBlockState(pos.offset(Direction.EAST)).getBlock();
//        Block west = worldIn.getBlockState(pos.offset(Direction.WEST)).getBlock();
//        Block south = worldIn.getBlockState(pos.offset(Direction.SOUTH)).getBlock();
//        Block support = worldIn.getBlockState(pos.offset(Direction.DOWN)).getBlock();
//        if (worldIn.getBlockState(pos).getBlock() == Blocks.AIR && isGround(north) && isGround(east) && isGround(west) && isGround(south) && isGround(support)) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isGround(Block block) {
//        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
//    }
//}
//
