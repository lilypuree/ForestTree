package lilypuree.forest_tree.shrubs.block;

import com.ferreusveritas.dynamictrees.systems.poissondisc.Vec2i;
import lilypuree.forest_tree.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleFlowerTile extends TileEntity {

    public static ModelProperty<List<BlockState>> FLOWERS = new ModelProperty<>();
//    public List<Vec2f> offsets;
    public List<BlockState> flowerList;
//    public final ItemStackHandler flowerHandler = new ItemStackHandler(9) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            offsets.clear();
//        }
//
//        @Override
//        public int getSlotLimit(int slot) {
//            return 1;
//        }
//
//        @Override
//        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//            return stack.getItem().isIn(ItemTags.FLOWERS);
//        }
//    };

    private static final String inventoryKey = "inv";

//    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> flowerHandler);

    public MultipleFlowerTile() {
        super(Registration.MULTIPLE_FLOWER_TILE.get());
//        offsets = new ArrayList<>();
        flowerList = new ArrayList<>();
    }

//    public boolean insertFlower(ItemStack stack, boolean consumeFlower) {
//        if (flowers < flowerHandler.getSlots() && !world.isRemote()) {
//            if (flowerHandler.isItemValid(flowers, stack)) {
//                if (consumeFlower) {
//                    flowerHandler.insertItem(flowers++, stack.split(1), false);
//                } else {
//                    flowerHandler.insertItem(flowers++, new ItemStack(stack.getItem(), 1), false);
//                }
//                world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
//                this.markDirty();
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean insertFlower(BlockState flower) {

        if (flowerList.size() < 9 && (world == null || !world.isRemote()) && flower.isIn(BlockTags.FLOWERS)) {
            flowerList.add(flower);
            if(world != null){
                world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
                this.markDirty();
            }
            return true;
        }
        return false;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
//        int[] list = compound.getIntArray("offsets");
//        List<Vec2f> offset = new ArrayList<>();
//        for (int i = 0; i < list.length / 2; i++) {
//            offset.add(new Vec2f(Float.intBitsToFloat(list[2 * i]), Float.intBitsToFloat(list[2 * i + 1])));
//        }

        this.flowerList.clear();
        ListNBT flowerNBTs = compound.getList("list", Constants.NBT.TAG_COMPOUND);
        flowerNBTs.forEach(inbt -> {
            flowerList.add(NBTUtil.readBlockState((CompoundNBT) inbt));
        });
//        this.offsets = offset;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
//        List<Integer> list = new ArrayList<>();
//        for (Vec2f v : offsets) {
//            list.add(Float.floatToIntBits(v.x));
//            list.add(Float.floatToIntBits(v.y));
//        }
        ListNBT flowerNBTs = new ListNBT();
        for (BlockState state : flowerList) {
            flowerNBTs.add(NBTUtil.writeBlockState(state));
        }
        compound.put("list", flowerNBTs);
//        compound.putIntArray("offsets", list);
        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//        offsets.clear();
        this.read(pkt.getNbtCompound());
        ModelDataManager.requestModelDataRefresh(this);
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(FLOWERS, flowerList)
                .build();
    }
}
