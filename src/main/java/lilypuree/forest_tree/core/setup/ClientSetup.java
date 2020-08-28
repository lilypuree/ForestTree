package lilypuree.forest_tree.core.setup;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.api.genera.FoliageCategory;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.client.models.MultipleFlowerModelLoader;
import lilypuree.forest_tree.client.models.BranchModelLoader;
import lilypuree.forest_tree.client.models.PalmCrownModelLoader;
import lilypuree.forest_tree.client.models.StumpModelLoader;
import lilypuree.forest_tree.client.screen.TreeDesignerScreen;
import lilypuree.forest_tree.client.render.TreeDesignerRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "branchloader"), BranchModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "palmcrownloader"), PalmCrownModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "stumploader"), StumpModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ForestTree.MODID, "multipleflowerloader"), MultipleFlowerModelLoader.INSTANCE);

        TreeBlockRegistry.leavesSlabBlocks.values().forEach(regObject -> {
            RenderTypeLookup.setRenderLayer(regObject.get(), RenderType.getCutoutMipped());
        });
        TreeBlockRegistry.forAllBranchEnds(branchBlock -> {
            if (branchBlock.getWoodCategory().getEndFoliage().isPresent()) {
                RenderTypeLookup.setRenderLayer(branchBlock, RenderType.getCutoutMipped());
            }
        });
        TreeDesignerRenderer.register();
        RenderTypeLookup.setRenderLayer(Registration.CUSTOM_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Registration.PALM_CROWN.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(Registration.MULTIPLE_FLOWER_BLOCK.get(), RenderType.getCutout());
        ScreenManager.registerFactory(Registration.TREE_DESIGNER_CONTAINER.get(), TreeDesignerScreen::new);
    }


    @SubscribeEvent
    public static void onBlockColourHandlerEvent(final ColorHandlerEvent.Block event) {

        for (FoliageCategory category : TreeBlockRegistry.foliageCategories) {
            if (category.hasLeafBlock()) {
                IBlockColor blockColor = (blockstate, iLightReader, blockPos, i) -> {
                    return iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) :
                            category.getFoliageColor();
                };
//                if (category.isForestTreeLeaves()) {
//                    event.getBlockColors().register(blockColor, Registration.LEAVES_SLAB_BLOCKS.get(category).get(), category.getDefaultFoliage());
//                } else {
                event.getBlockColors().register(blockColor, TreeBlockRegistry.leavesSlabBlocks.get(category).get());
//                }
            }
        }

        TreeBlockRegistry.forAllBranchEnds(branchBlock -> {
            Optional<FoliageCategory> foliage = branchBlock.getWoodCategory().getEndFoliage();
            foliage.ifPresent(foliageCategory -> {
                event.getBlockColors().register((blockState, iLightReader, blockPos, i) ->
                                iLightReader != null && blockPos != null ? BiomeColors.getFoliageColor(iLightReader, blockPos) : foliageCategory.getFoliageColor(),
                        branchBlock);
            });
        });
    }

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getMap().getTextureLocation();
        if (!stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            return;
        }
        event.addSprite(TreeDesignerRenderer.HOLOGRAM_RAY);
    }


    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        TreeBlockRegistry.leavesSlabBlocks.values().forEach(regObject -> {
            event.getItemColors().register((itemStack, i) -> {
                BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
                return event.getBlockColors().getColor(blockState, null, null, i);
            }, regObject.get());
        });
    }


}
