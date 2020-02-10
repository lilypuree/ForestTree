package lilypuree.forest_tree.setup;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ForestTree.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        for (WoodTypes type : WoodTypes.values()) {
            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get(), RenderType.cutoutMipped());
        }
    }


    @SubscribeEvent
    public static void onBlockColourHandlerEvent(final ColorHandlerEvent.Block event) {
        for (WoodTypes type : new WoodTypes[]{WoodTypes.OAK, WoodTypes.ACACIA, WoodTypes.JUNGLE, WoodTypes.DARK_OAK}) {
            event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
                return iLightReader != null && blockPos != null ? BiomeColors.func_228361_b_(iLightReader, blockPos) : FoliageColors.getDefault();
            }, Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get());
        }
        event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
            return iLightReader != null && blockPos != null ? BiomeColors.func_228361_b_(iLightReader, blockPos) : FoliageColors.getBirch();
        }, Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.BIRCH + "_leaves_trapdoor").toUpperCase()).get());
        event.getBlockColors().register((blockState, iLightReader, blockPos, i) -> {
            return iLightReader != null && blockPos != null ? BiomeColors.func_228361_b_(iLightReader, blockPos) : FoliageColors.getSpruce();
        }, Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((WoodTypes.SPRUCE + "_leaves_trapdoor").toUpperCase()).get());
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        for (WoodTypes type : WoodTypes.values()) {
            event.getItemColors().register((itemStack, i) -> {
                BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
                return event.getBlockColors().getColor(blockState, null, null, i);
            }, Registration.TREE_BLOCKS.get((type + "_leaves_slab").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_stairs").toUpperCase()).get(), Registration.TREE_BLOCKS.get((type + "_leaves_trapdoor").toUpperCase()).get());
        }
    }


}
