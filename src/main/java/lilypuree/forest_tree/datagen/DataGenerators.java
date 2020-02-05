package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.ForestTree;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
//            generator.addProvider(new Recipes(generator));
//            generator.addProvider(new LootTables(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new Items(generator, ForestTree.MODID, event.getExistingFileHelper()));
//            generator.addProvider(new Languages(generator, "en_us"));
        }
    }

    public static String connectWithUnderscore(Object firstString, Object... strings){
        StringBuilder builder = new StringBuilder(firstString.toString());
        for (Object s : strings){
            builder.append("_");
            builder.append(s.toString());
        }
        return builder.toString();
    }
}
