package lilypuree.forest_tree.core.registry;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.trees.world.biome.ForestTreeFeatureConfigs;
import lilypuree.forest_tree.trees.world.gen.feature.AdvancedTreeFeature;
import lilypuree.forest_tree.trees.world.gen_old.feature.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForestTreeFeatures {

    //    public static final Feature<TreeFeatureConfig> ROSEWOOD_TREE = new RosewoodTreeFeature(TreeFeatureConfig::func_227338_a_, false);
//    public static final Feature<AdvancedTreeFeatureConfig> DEFAULT_TREE = new CustomShortTreeFeature((ops)-> ForestTreeFeatureConfigs.OAK_TREE_CONFIG);
//    public static final Feature<AdvancedTreeFeatureConfig> PINE = new CedarTreeFeature((ops)-> ForestTreeFeatureConfigs.PINE_CONFIG);
    public static final AdvancedTreeFeature<NoFeatureConfig> CUSTOM = new AdvancedTreeFeature<>(NoFeatureConfig::deserialize);


    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(

        );
    }
}
