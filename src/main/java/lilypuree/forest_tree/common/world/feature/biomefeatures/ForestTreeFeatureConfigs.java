package lilypuree.forest_tree.common.world.feature.biomefeatures;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.PlainFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;

public class ForestTreeFeatureConfigs {

    //    public static final AdvancedTreeFeatureConfig OAK_TREE_CONFIG = (new AdvancedTreeFeatureConfig.Builder(new ModSpecies.DefaultSpecies())
//    .height(4).heightRandom(3).age(0).branchYPosRandom(2).extraBranchLength(3)).setSapling(Registration.OAK_SAPLING.get()).build();

//    public static final AdvancedTreeFeatureConfig PINE_CONFIG = (new AdvancedTreeFeatureConfig.Builder(new ModSpecies.Pine())
//            .height(6).heightRandom(7).age(0).extraBranchLength(2)).setSapling(Registration.PLACEBO_SAPLING.get()).build();

    public static final BlockClusterFeatureConfig DENSE_FLOWER_FIELD_CONFIG = (new BlockClusterFeatureConfig.Builder(
            new WeightedBlockStateProvider().addWeightedBlockstate(Blocks.WHITE_TULIP.getDefaultState(), 1).addWeightedBlockstate(Blocks.PINK_TULIP.getDefaultState(),1).addWeightedBlockstate(Blocks.OXEYE_DAISY.getDefaultState(), 1).addWeightedBlockstate(Blocks.ALLIUM.getDefaultState(), 1).addWeightedBlockstate(Blocks.ORANGE_TULIP.getDefaultState(), 1),
            new SimpleBlockPlacer())).tries(800).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).func_227317_b_().build();
}
