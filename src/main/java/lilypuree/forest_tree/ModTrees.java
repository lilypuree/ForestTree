package lilypuree.forest_tree;

import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.api.genera.TreeGenus;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.trees.PalmTree;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTrees {

    public static final String NULL = "null";
    public static final String OAK = "oak";
    public static final String BIRCH = "birch";
    public static final String SPRUCE = "spruce";
    public static final String JUNGLE = "jungle";
    public static final String DARKOAK = "darkoak";
    public static final String ACACIA = "acacia";

    public static ArrayList<TreeGenus> baseFamilies = new ArrayList<>();


    @SubscribeEvent
    public static void onSpeciesRegistry(RegistryEvent.Register<Species> event) {
        Collections.addAll(baseFamilies, new PalmTree());
        baseFamilies.forEach(tree -> tree.registerSpecies(event.getRegistry()));
        event.getRegistry().register(Species.NULLSPECIES.setRegistryName(new ResourceLocation(ForestTree.MODID, NULL)));
    }

    @SubscribeEvent
    public static void newRegistry(RegistryEvent.NewRegistry event) {
        Species.newRegistry(event);
    }


}
