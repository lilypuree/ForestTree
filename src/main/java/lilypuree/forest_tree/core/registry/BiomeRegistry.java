package lilypuree.forest_tree.core.registry;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.world.shrubs.biome.DenseFlowerFields;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiomeRegistry {
    public static List<String> biomeList = new ArrayList<>();

    public static Biome DENSE_FLOWER_FIELDS = new DenseFlowerFields();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event){
        IForgeRegistry<Biome> registry = event.getRegistry();
        registerBiome(registry, DENSE_FLOWER_FIELDS, "dense_flower_fields", true, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.OVERWORLD);
    }

    private static void registerBiome(IForgeRegistry<Biome> registry, Biome biome, String name, boolean spawn, BiomeDictionary.Type... types) {
        registry.register(biome.setRegistryName(ForestTree.MODID, name));
        if (spawn) {
            BiomeManager.addSpawnBiome(biome);
        }
        BiomeDictionary.addTypes(biome, types);
        biomeList.add(name);
    }
}
