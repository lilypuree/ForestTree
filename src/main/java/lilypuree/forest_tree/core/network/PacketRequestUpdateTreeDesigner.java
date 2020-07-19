package lilypuree.forest_tree.core.network;

import lilypuree.forest_tree.trees.customization.TreeDesignerTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRequestUpdateTreeDesigner {

    private BlockPos pos;

    public PacketRequestUpdateTreeDesigner(BlockPos pos, int dimensionID){
        this.pos = pos;
    }

    public PacketRequestUpdateTreeDesigner(PacketBuffer buf){
        pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf){
        buf.writeBlockPos(pos);
    }


    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            World world = ctx.get().getSender().world;
            TreeDesignerTile te = (TreeDesignerTile)world.getTileEntity(pos);
            ForestTreePacketHandler.sendToClient(new PacketUpdateTreeDesigner(te), ctx.get().getSender());
        });
        return true;
    }
}
