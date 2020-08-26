package lilypuree.forest_tree.core.setup;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.shrubs.client.MultipleFlowerModelLoader;
import lilypuree.forest_tree.trees.client.BranchModelLoader;
import lilypuree.forest_tree.trees.client.PalmCrownModelLoader;
import lilypuree.forest_tree.trees.client.StumpModelLoader;
import lilypuree.forest_tree.trees.client.gui.TreeDesignerScreen;
import lilypuree.forest_tree.trees.client.render.TreeDesignerRenderer;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "branchloader"), BranchModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "palmcrownloader"), PalmCrownModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "stumploader"), StumpModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "multipleflowerloader"), MultipleFlowerModelLoader.INSTANCE);
        for (Species species : ModSpecies.allSpecies()) {
            if(species == ModSpecies.PALM) continue;
            RenderTypeLookup.setRenderLayer(Registration.LEAVES_SLAB_BLOCKS.get(species).get(), RenderType.getCutoutMipped());
        }
        Registration.forAllBranchEnds(branchBlock -> {
            if (branchBlock.getSpecies().isConifer()) {
                RenderTypeLookup.setRenderLayer(branchBlock, RenderType.getCutoutMipped());
            }
        });
        TreeDesignerRenderer.register();
//        MultipleFlowerRenderer.register();
        RenderTypeLookup.setRenderLayer(Registration.CUSTOM_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Registration.PALM_CROWN.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Registration.MULTIPLE_FLOWER_BLOCK.get(), RenderType.getCutout());
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
        for (Species species : ModSpecies.allSpecies()) {
            if(species == ModSpecies.PALM) continue;
            event.getBlockColors().register((blockstate, iLightReader, blockPos, i) -> {
                return iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) :
                        species.isConifer() ? FoliageColors.getSpruce() : FoliageColors.getDefault();
            }, Registration.LEAVES_SLAB_BLOCKS.get(species).get());
        }
        Registration.forAllBranchEnds(branchBlock -> {
            if (branchBlock.getSpecies().isConifer()) {
                event.getBlockColors().register((blockState, iLightReader, blockPos, i) ->
                                iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : FoliageColors.getSpruce(),
                        branchBlock);
            }
        });
    }

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getMap().getTextureLocation();
        if (!stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            return;
        }
        event.addSprite(TreeDesignerRenderer.HOLOGRAM_RAY);
        event.addSprite(new ResourceLocation("block/oak_log_large"));
    }


    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        for (Species species : ModSpecies.allSpecies()) {
            if(species == ModSpecies.PALM) continue;
            event.getItemColors().register((itemStack, i) -> {
                BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
                return event.getBlockColors().getColor(blockState, null, null, i);
            }, Registration.LEAVES_SLAB_BLOCKS.get(species).get());
        }
    }


}
