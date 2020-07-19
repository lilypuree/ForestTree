package lilypuree.forest_tree.core.network;

import lilypuree.forest_tree.trees.customization.TreeDesignerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateTreeDesigner {

    private BlockPos pos;
    private ItemStack stack;
    private long lastChangeTime;

    public PacketUpdateTreeDesigner(BlockPos pos, ItemStack stack, long lastChangeTime) {
        this.pos = pos;
        this.stack = stack;
        this.lastChangeTime = lastChangeTime;
    }

    public PacketUpdateTreeDesigner(PacketBuffer buf) {
        pos = buf.readBlockPos();
        stack = buf.readItemStack();
        lastChangeTime = buf.readLong();
    }

    public PacketUpdateTreeDesigner(TreeDesignerTile te){
        pos = te.getPos();
        stack = te.saplingHandler.getStackInSlot(0);
        lastChangeTime = te.lastChangeTime;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);
        buf.writeLong(lastChangeTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            TreeDesignerTile te = (TreeDesignerTile) world.getTileEntity(pos);
            te.saplingHandler.setStackInSlot(0, stack);
            te.lastChangeTime = lastChangeTime;
        });
        return true;
    }
}
