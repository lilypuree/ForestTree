package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.util.ContainerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TreeDesignerContainer extends Container {

    private TreeDesignerTile treeDesigner;

    @OnlyIn(Dist.CLIENT)
    public static TreeDesignerContainer create(final int windowId, final PlayerInventory playerInventory, final PacketBuffer extraData) {
        TreeDesignerTile treeDesignerTile = (TreeDesignerTile) Minecraft.getInstance().world.getTileEntity(extraData.readBlockPos());
        return new TreeDesignerContainer(windowId, treeDesignerTile, playerInventory, Minecraft.getInstance().player);
    }

    public TreeDesignerContainer(final int windowId, TreeDesignerTile treeDesignerIn, final PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.TREE_DESIGNER_CONTAINER.get(), windowId);
        this.treeDesigner = treeDesignerIn;

        treeDesigner.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 50, 58));
        });
//        ContainerHelper.addPlayerInventoryToContainer(this, playerInventory);
    }


    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        return ContainerHelper.transferStackInSlot(this, player, index);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        BlockPos pos = treeDesigner.getPos();

        // based on Container.isWithinUsableDistance but with more generic blockcheck
        if (treeDesigner.getWorld().getBlockState(treeDesigner.getPos()).getBlock() instanceof TreeDesignerBlock) {
            return player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }

        return false;
    }
}
