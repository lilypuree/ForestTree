package lilypuree.forest_tree.core.setup;

import lilypuree.forest_tree.ForestTree;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {



    public void init(FMLCommonSetupEvent e) {
    }

    @SubscribeEvent
    public static void onServerSetUp(FMLServerStartingEvent event) {
    }



}
