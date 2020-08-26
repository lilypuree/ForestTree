package lilypuree.forest_tree.world.trees.gen.feature.biomeFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class InVanillaFeatures {
    public static void addFeatures(){
        for (Biome biome : ForgeRegistries.BIOMES){
            ForestTreeTreeFeatures.addTree(biome);
        }
    }
}
