package lilypuree.forest_tree.core.registry;

import com.ferreusveritas.dynamictrees.worldgen.feature.WorldTreeFeature;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.common.world.feature.features.DenseFlowerPatchFeature;
import lilypuree.forest_tree.common.world.trees.gen.feature.AdvancedTreeFeature;
import lilypuree.forest_tree.common.world.trees.gen.feature.TreeConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeatureRegistry {

    //    public static final Feature<TreeFeatureConfig> ROSEWOOD_TREE = new RosewoodTreeFeature(TreeFeatureConfig::func_227338_a_, false);
//    public static final Feature<AdvancedTreeFeatureConfig> DEFAULT_TREE = new CustomShortTreeFeature((ops)-> ForestTreeFeatureConfigs.OAK_TREE_CONFIG);
//    public static final Feature<AdvancedTreeFeatureConfig> PINE = new CedarTreeFeature((ops)-> ForestTreeFeatureConfigs.PINE_CONFIG);
    public static final AdvancedTreeFeature CUSTOM = new AdvancedTreeFeature(TreeConfig::deserialize);
    public static final WorldTreeFeature<NoFeatureConfig> FOREST_TREE = new WorldTreeFeature<>(NoFeatureConfig::deserialize);
    public static final DenseFlowerPatchFeature DENSE_FLOWER_PATCH = new DenseFlowerPatchFeature(BlockClusterFeatureConfig::deserialize);

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(
                FOREST_TREE.setRegistryName("forest_tree"),
                DENSE_FLOWER_PATCH.setRegistryName("dense_flower_patch")
        );
    }
}
