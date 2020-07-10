package lilypuree.forest_tree.core.setup;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.client.BranchModelLoader;
import lilypuree.forest_tree.trees.client.gui.TreeDesignerScreen;
import lilypuree.forest_tree.trees.customization.TreeDesignerTile;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "branchloader"), BranchModelLoader.INSTANCE);
        for (WoodTypes type : WoodTypes.values()) {
//            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), RenderType.getCutoutMipped());
//            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), RenderType.getCutoutMipped());
//            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get(), RenderType.getCutoutMipped());
        }
        Registration.forAllBranchEnds(branchBlock -> {
            if(branchBlock.getSpecies().isConifer()){
                RenderTypeLookup.setRenderLayer(branchBlock, RenderType.getCutoutMipped());
            }
        });
        ScreenManager.registerFactory(Registration.TREE_DESIGNER_CONTAINER.get(), TreeDesignerScreen::new);
    }


    @SubscribeEvent
    public static void onBlockColourHandlerEvent(final ColorHandlerEvent.Block event) {
//        for (WoodTypes type : new WoodTypes[]{WoodTypes.OAK, WoodTypes.ACACIA, WoodTypes.JUNGLE, WoodTypes.DARK_OAK}) {
//            event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
//                return iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : FoliageColors.getDefault();
//            }, Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get());
//        }
//        event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
//            return iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : FoliageColors.getBirch();
//        }, Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_trapdoor").toUpperCase()).get());
//        event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
//            return iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : FoliageColors.getSpruce();
//        }, Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_trapdoor").toUpperCase()).get());

        Registration.forAllBranchEnds(branchBlock -> {
            if(branchBlock.getSpecies().isConifer()){
                event.getBlockColors().register((blockState, iLightReader, blockPos, i) ->
                        iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : FoliageColors.getSpruce(),
                        branchBlock);
            }
        });
    }
//
//    @SubscribeEvent
//    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
//        for (WoodTypes type : WoodTypes.values()) {
//            event.getItemColors().register((itemStack, i) -> {
//                BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
//                return event.getBlockColors().getColor(blockState, null, null, i);
//            }, Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get());
//        }
//    }
//

}
