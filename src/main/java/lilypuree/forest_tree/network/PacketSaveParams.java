package lilypuree.forest_tree.network;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.common.trees.customization.TreeDesignerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSaveParams {

    private final BlockPos pos;
    private final CompoundNBT generatorTag;

    public PacketSaveParams(BlockPos pos, CompoundNBT generatorTag){
        this.pos = pos;
        this.generatorTag = generatorTag;
    }

    public PacketSaveParams(PacketBuffer buf){
        pos = buf.readBlockPos();
        generatorTag = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf){
        buf.writeBlockPos(pos);
        buf.writeCompoundTag(generatorTag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().world;
            if (world.isBlockLoaded(pos)){
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TreeDesignerTile) {
                    ItemStack newSapling = new ItemStack(Registration.CUSTOM_SAPLING_ITEM.get());
                    newSapling.setTagInfo("BlockEntityTag", generatorTag);
                    ((TreeDesignerTile) te).saplingHandler.setStackInSlot(0, newSapling);
                }
            }
        });
        return true;
    }
}
