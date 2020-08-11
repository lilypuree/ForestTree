package com.ferreusveritas.dynamictrees.event;

import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDiscProviderUniversal;
import com.ferreusveritas.dynamictrees.worldgen.WorldTreeGenerator;
import lilypuree.forest_tree.ForestTree;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PoissonDiscEventHandler {

    /** This piece of crap event will not fire until after PLENTY of chunks have already generated when creating a new world.  WHY!? */
	/*@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {}*/

    /**
     * We'll use this instead because at least new chunks aren't created after the world is unloaded. I hope. >:(
     */
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        IWorld world = event.getWorld();
        if (!world.isRemote()) {
            WorldTreeGenerator.getWorldTreeGenerator().getCircleProvider().unloadWorld(world);//clears the circles
        }
    }


    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load event) {
        IWorld world = event.getWorld();
        if(world != null && !world.isRemote()){
            byte circleData[] = event.getData().getByteArray("GTCD");
            PoissonDiscProviderUniversal cp = WorldTreeGenerator.getWorldTreeGenerator().getCircleProvider();
            cp.setChunkPoissonData(world, event.getChunk().getPos().x, 0, event.getChunk().getPos().z, circleData);
        }
    }

    @SubscribeEvent
    public static void onChunkDataSave(ChunkDataEvent.Save event) {
        IWorld world = event.getWorld();
        ChunkPos chunkPos = event.getChunk().getPos();
        PoissonDiscProviderUniversal cp = WorldTreeGenerator.getWorldTreeGenerator().getCircleProvider();
        byte circleData[] = cp.getChunkPoissonData(world, chunkPos.x, 0, chunkPos.z);
        ByteArrayNBT circleByteArray = new ByteArrayNBT(circleData);
        event.getData().put("GTCD", circleByteArray);//Growing Trees Circle Data

        // Unload circles here if the chunk is no longer loaded.
        if (!world.getChunkProvider().isChunkLoaded(chunkPos)) {
            cp.unloadChunkPoissonData(world, chunkPos.x, 0, chunkPos.z);
        }
    }
}
