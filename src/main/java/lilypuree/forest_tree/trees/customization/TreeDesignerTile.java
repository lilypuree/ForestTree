package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TreeDesignerTile extends TileEntity implements INamedContainerProvider {

    private LazyOptional<ItemStackHandler> handler = LazyOptional.of(this::createHandler);
    private ItemStackHandler saplingHandler;

    private static final String inventoryKey = "inv";


    public TreeDesignerTile() {
        super(Registration.TREE_DESIGNER_TILE.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        handler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound(inventoryKey)));

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        handler.ifPresent(handler -> compound.put(inventoryKey, handler.serializeNBT()));
        return compound;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.treeDesigner");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TreeDesignerContainer(windowId, this, playerInventory, playerEntity);
    }
}
