package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.core.network.ForestTreePacketHandler;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TreeDesignerTile extends TileEntity implements INamedContainerProvider {

    public final ItemStackHandler saplingHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!world.isRemote) {
                lastChangeTime = world.getGameTime();
            }
            markDirty();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return super.isItemValid(slot, stack);
        }
    };

    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> saplingHandler);

    private static final String inventoryKey = "inv";

    public long lastChangeTime;

    public TreeDesignerTile() {
        super(Registration.TREE_DESIGNER_TILE.get());
    }

    public void extractSapling() {
        if (world.isRemote) return;
        ItemStack extractStack = saplingHandler.extractItem(0, 1, false);
        if (!extractStack.isEmpty()) {
            InventoryHelper.spawnItemStack(this.world, pos.getX(), pos.getY() + 0.4, pos.getZ(), extractStack);
        }
        this.markDirty();
    }

    public boolean insertSapling(ItemStack stack) {
        if (world.isRemote || stack.getItem() != Registration.CUSTOM_SAPLING_ITEM.get() || !saplingHandler.getStackInSlot(0).isEmpty()) {
            return false;
        } else {
            ItemStack returnStack = saplingHandler.insertItem(0, stack, false);
            world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
            this.markDirty();
            return true;
        }
    }

    @Override
    public void remove() {
        this.extractSapling();
        super.remove();
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
        lastChangeTime = compound.getLong("lastChangeTime");
        saplingHandler.deserializeNBT(compound.getCompound(inventoryKey));
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putLong("lastChangeTime", lastChangeTime);
        compound.put(inventoryKey, saplingHandler.serializeNBT());
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(pkt.getNbtCompound());
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.treeDesigner");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
        return TreeDesignerContainer.getServerContainerProvider(this, pos).createMenu(windowId, playerInventory, playerEntity);
    }


    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }

}
