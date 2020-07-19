package lilypuree.forest_tree.trees.items;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.customization.TreeDesignerTile;
import lilypuree.forest_tree.trees.world.gen.feature.TreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class CustomSaplingItem extends BlockItem {

    public CustomSaplingItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("BlockEntityTag", new TreeGenerator().saveToNbt(new CompoundNBT()));
            ItemStack newStack = new ItemStack(this.getBlock());
            newStack.setTag(nbt);
            items.add(newStack);
        }
    }


    @Nonnull
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        BlockState blockState = world.getBlockState(pos);
        if (world.isRemote) {
            return ActionResultType.PASS;
        } else if (blockState.getBlock() == Registration.TREE_DESIGNER_BLOCK.get()) {
            if (player != null && player.isSneaking()) {
                final TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof TreeDesignerTile) {
                    boolean success = ((TreeDesignerTile) tileEntity).insertSapling(context.getItem());
                    if(success){
                        if(!player.isCreative()){
                            context.getItem().shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUse(context);
    }

}
