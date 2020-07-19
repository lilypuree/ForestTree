package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.util.ContainerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.*;

import javax.annotation.Nonnull;

public class TreeDesignerContainer extends Container {

    private final IWorldPosCallable usabilityTest;
    private final BlockPos pos;
    private final PlayerEntity player;

    public static TreeDesignerContainer getClientContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer extraData) {
        return new TreeDesignerContainer(windowId, playerInventory, extraData.readBlockPos(), new ItemStackHandler(1));
    }

    public static IContainerProvider getServerContainerProvider(TreeDesignerTile te, BlockPos activationPos){
        return (id, playerInventory, serverPlayer) -> new TreeDesignerContainer(id, playerInventory, activationPos, te.saplingHandler);
    }

    private TreeDesignerContainer(final int id, final PlayerInventory playerInventory, BlockPos pos, IItemHandler itemHandler) {
        super(Registration.TREE_DESIGNER_CONTAINER.get(), id);
        this.pos = pos;
        this.player = playerInventory.player;
        this.usabilityTest = IWorldPosCallable.of(this.player.world, pos);
        addSlot(new SlotItemHandler(itemHandler, 0, 15, 135));

//        ContainerHelper.addPlayerInventoryToContainer(this, playerInventory);
    }


    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        return ContainerHelper.transferStackInSlot(this, player, index);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(this.usabilityTest, player, Registration.TREE_DESIGNER_BLOCK.get());
    }

    public BlockPos getPos(){
        return pos;
    }
}
