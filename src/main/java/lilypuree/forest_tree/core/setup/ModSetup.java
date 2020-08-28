package lilypuree.forest_tree.core.setup;

import com.ferreusveritas.dynamictrees.worldgen.WorldTreeGenerator;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.ModTrees;
import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.network.ForestTreePacketHandler;
import lilypuree.forest_tree.core.registry.BiomeRegistry;
import lilypuree.forest_tree.common.world.trees.gen.feature.biomeFeatures.InVanillaFeatures;
import lilypuree.forest_tree.trees.PalmTree;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.Collections;

import static lilypuree.forest_tree.ModTrees.baseFamilies;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public void forestTreeCommonSetup(FMLCommonSetupEvent e) {
        WorldTreeGenerator.preInit();
        InVanillaFeatures.addFeatures();
        ForestTreePacketHandler.registerMessages();
//        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(BiomeRegistry.DENSE_FLOWER_FIELDS, 1));
    }

    @SubscribeEvent
    public static void onServerSetUp(FMLServerStartingEvent event) {
    }



}
