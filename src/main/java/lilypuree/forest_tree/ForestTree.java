package lilypuree.forest_tree;

import lilypuree.forest_tree.core.setup.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ForestTree.MODID)
public class ForestTree
{
    public static final String MODID = "forest_tree";
    public static final Logger LOGGER = LogManager.getLogger();

//    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = new ModSetup();
    public static ForestTree instance;

    // Directly reference a log4j logger.

    public ForestTree() {

        instance = this;

        Registration.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> setup.forestTreeCommonSetup(e));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    private static String prefix(String path) {
        return ForestTree.MODID + "." + path;
    }

}
