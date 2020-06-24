//package lilypuree.forest_tree.datagen;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import lilypuree.forest_tree.datagen.types.ThicknessTypes;
//import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
//import lilypuree.forest_tree.datagen.types.WoodTypes;
//import lilypuree.forest_tree.Registration;
//import net.minecraft.advancements.criterion.EnchantmentPredicate;
//import net.minecraft.advancements.criterion.ItemPredicate;
//import net.minecraft.advancements.criterion.MinMaxBounds;
//import net.minecraft.advancements.criterion.StatePropertiesPredicate;
//import net.minecraft.block.Block;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.DirectoryCache;
//import net.minecraft.data.IDataProvider;
//import net.minecraft.data.LootTableProvider;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.item.Items;
//import net.minecraft.state.properties.BlockStateProperties;
//import net.minecraft.state.properties.SlabType;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.storage.loot.*;
//import net.minecraft.world.storage.loot.conditions.*;
//import net.minecraft.world.storage.loot.functions.ExplosionDecay;
//import net.minecraft.world.storage.loot.functions.SetCount;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.HashMap;
//import java.util.Map;
//
//public class LootTables extends LootTableProvider {
//    private final DataGenerator generator;
//    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//
//    public LootTables(DataGenerator dataGeneratorIn) {
//        super(dataGeneratorIn);
//        this.generator = dataGeneratorIn;
//    }
//
//    @Override
//    public void act(DirectoryCache cache) {
//        for (WoodTypes wood : WoodTypes.values()) {
//            for (ThicknessTypes thickness : ThicknessTypes.values()) {
//                for (TreeBlockTypes type : TreeBlockTypes.values()) {
//                    lootTables.put(Registration.getTreeBlock(wood, thickness, type), createTreeBlockTable(wood, thickness, type));
//                }
//            }
//            lootTables.put(Registration.getLeafBlock(wood, "slab"),createLeavesSlab(wood));
//            lootTables.put(Registration.getLeafBlock(wood, "stairs"),createLeavesStairs(wood));
//            lootTables.put(Registration.getLeafBlock(wood, "trapdoor"),createLeavesTrapdoor(wood));
//        }
//
//        Map<ResourceLocation, LootTable> tables = new HashMap<>();
//        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
//            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
//        }
//        writeTables(cache, tables);
//    }
//
//    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
//        Path outputFolder = this.generator.getOutputFolder();
//        tables.forEach((key, lootTable) -> {
//            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
//            try {
//                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
//            } catch (IOException e) {
//                e.printStackTrace();
////                LOGGER.error("Couldn't write loot table {}", path, e);
//            }
//        });
//    }
//
//    private LootTable.Builder createTreeBlockTable(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
//        LootPool.Builder builder = LootPool.builder()
//                .name(wood + "_" + thickness + "_" + type)
//                .rolls(ConstantRange.of(1))
//                .addEntry(AlternativesLootEntry.builder(
//                        ItemLootEntry.builder(Registration.getTreeBlock(wood, thickness, type))
//                                .acceptCondition(MatchTool.builder(ItemPredicate.Builder.create()
//                                        .enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))),
//                        ItemLootEntry.builder(wood.getLog())
//                                .acceptFunction(SetCount.builder(timberDrops(thickness)))
//                                .acceptFunction(ExplosionDecay.builder()))
//                );
//        LootPool.Builder bonus = LootPool.builder()
//                .name(wood + "_" + thickness + "_" + type + "_bonus")
//                .rolls(ConstantRange.of(1))
//                .addEntry(ItemLootEntry.builder(wood.getLog()))
//                .acceptCondition(Inverted.builder(MatchTool.builder(ItemPredicate.Builder.create()
//                        .enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))))
//                .acceptFunction(SetCount.builder(timberBonusDrops(thickness)))
//                .acceptFunction(ExplosionDecay.builder());
//        return LootTable.builder().addLootPool(builder).addLootPool(bonus);
//    }
//
//    private LootPool.Builder sticks = LootPool.builder()
//            .rolls(ConstantRange.of(1))
//            .addEntry(ItemLootEntry.builder(Items.STICK)
//                    .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))
//                    .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F)))
//                    .acceptFunction(ExplosionDecay.builder())
//            )
//            .acceptCondition(Inverted.builder(
//                    Alternative.builder(
//                            MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)),
//                            MatchTool.builder(ItemPredicate.Builder.create().enchantment(
//                                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))
//                                    )
//                            )
//                    )));
//
//    private LootTable.Builder createLeavesSlab(WoodTypes wood) {
//        LootPool.Builder leavesOrSapling = LootPool.builder()
//                .rolls(ConstantRange.of(1))
//                .addEntry(AlternativesLootEntry.builder(
//                        ItemLootEntry.builder(Registration.getLeafBlock(wood, "slab"))
//                                .acceptFunction(SetCount.builder(ConstantRange.of(2))
//                                        .acceptCondition(BlockStateProperty.builder(Registration.getLeafBlock(wood, "slab"))
//                                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE))))
//                                .acceptFunction(ExplosionDecay.builder())
//                                .acceptCondition(Alternative.builder(
//                                        MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)),
//                                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(
//                                                new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))
//                                )),
//                        ItemLootEntry.builder(wood.getSapling())
//                                .acceptCondition(SurvivesExplosion.builder())
//                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, (float) 0.05, (float) 0.0625, (float) 0.083333336, (float) 0.1))
//                ));
//        return LootTable.builder().addLootPool(leavesOrSapling).addLootPool(sticks);
//    }
//
//    private LootTable.Builder createLeavesStairs(WoodTypes wood) {
//        LootPool.Builder leavesOrSapling = LootPool.builder()
//                .rolls(ConstantRange.of(1))
//                .addEntry(AlternativesLootEntry.builder(
//                        ItemLootEntry.builder(Registration.getLeafBlock(wood, "stairs"))
//                                .acceptCondition(SurvivesExplosion.builder())
//                                .acceptCondition(Alternative.builder(
//                                        MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)),
//                                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(
//                                                new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))
//                                )),
//                        ItemLootEntry.builder(wood.getSapling())
//                                .acceptCondition(SurvivesExplosion.builder())
//                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, (float) 0.05, (float) 0.0625, (float) 0.083333336, (float) 0.1))
//                ));
//        return LootTable.builder().addLootPool(leavesOrSapling).addLootPool(sticks);
//    }
//
//    private LootTable.Builder createLeavesTrapdoor(WoodTypes wood) {
//        LootPool.Builder leavesOrSapling = LootPool.builder()
//                .rolls(ConstantRange.of(1))
//                .addEntry(AlternativesLootEntry.builder(
//                        ItemLootEntry.builder(Registration.getLeafBlock(wood, "trapdoor"))
//                                .acceptCondition(SurvivesExplosion.builder())
//                                .acceptCondition(Alternative.builder(
//                                        MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)),
//                                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(
//                                                new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))
//                                )),
//                        ItemLootEntry.builder(wood.getSapling())
//                                .acceptCondition(SurvivesExplosion.builder())
//                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, (float) 0.05, (float) 0.0625, (float) 0.083333336, (float) 0.1))
//                ));
//        return LootTable.builder().addLootPool(leavesOrSapling).addLootPool(sticks);
//    }
//
//
//    private ConstantRange timberDrops(ThicknessTypes thickness) {
//        switch (thickness) {
//            case THIN:
//                return new ConstantRange(0);
//            case THICK:
//                return new ConstantRange(1);
//            case THICKEST:
//                return new ConstantRange(2);
//        }
//        return new ConstantRange(0);
//    }
//
//    private BinomialRange timberBonusDrops(ThicknessTypes thickness) {
//        switch (thickness) {
//
//            case THICK:
//                return new BinomialRange(1, (float) 1 / 3);
//            case THICKEST:
//            case THIN:
//                return new BinomialRange(1, (float) 2 / 3);
//        }
//        return new BinomialRange(0, 0.0F);
//    }
//
//
//}
