//package lilypuree.forest_tree.datagen;
//
//import lilypuree.forest_tree.datagen.types.ThicknessTypes;
//import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
//import lilypuree.forest_tree.datagen.types.WoodTypes;
//import lilypuree.forest_tree.Registration;
//import net.minecraft.advancements.criterion.InventoryChangeTrigger;
//import net.minecraft.advancements.criterion.ItemPredicate;
//import net.minecraft.block.Blocks;
//import net.minecraft.data.*;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.tags.Tag;
//import net.minecraftforge.common.Tags;
//
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class Recipes extends RecipeProvider {
//    public Recipes(DataGenerator generatorIn){
//        super(generatorIn);
//    }
//
//    @Override
//    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
//        for(WoodTypes wood : WoodTypes.values()){
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THIN, TreeBlockTypes.BRANCH), 4)
//                    .patternLine("xx ")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THIN, TreeBlockTypes.TRUNK), 4)
//                    .patternLine(" x ")
//                    .patternLine(" x ")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THIN, TreeBlockTypes.STUMP), 3)
//                    .patternLine(" x ")
//                    .patternLine("x x")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICK, TreeBlockTypes.BRANCH), 3)
//                    .patternLine("xxx")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICK, TreeBlockTypes.TRUNK), 3)
//                    .patternLine(" x ")
//                    .patternLine(" x ")
//                    .patternLine(" x ")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICK, TreeBlockTypes.STUMP), 2)
//                    .patternLine(" x ")
//                    .patternLine(" x ")
//                    .patternLine("x x")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICKEST, TreeBlockTypes.BRANCH), 2)
//                    .patternLine(" x ")
//                    .patternLine("xxx")
//                    .patternLine("   ")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICKEST, TreeBlockTypes.TRUNK), 2)
//                    .patternLine(" x ")
//                    .patternLine(" xx")
//                    .patternLine(" x ")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ItemTags.LOGS).build()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getTreeBlock(wood, ThicknessTypes.THICKEST, TreeBlockTypes.STUMP), 2)
//                    .patternLine(" xx")
//                    .patternLine(" xx")
//                    .patternLine("xxx")
//                    .key('x', wood.getWood())
//                    .addCriterion("wood", InventoryChangeTrigger.Instance.forItems(wood.getLog()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getLeafBlock(wood, "slab"), 6)
//                    .patternLine("xxx")
//                    .key('x', wood.getLeaves())
//                    .addCriterion("has_leaves", InventoryChangeTrigger.Instance.forItems(wood.getLeaves()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getLeafBlock(wood, "stairs"), 4)
//                    .patternLine("x  ")
//                    .patternLine("xx ")
//                    .patternLine("xxx")
//                    .key('x', wood.getLeaves())
//                    .addCriterion("has_leaves", InventoryChangeTrigger.Instance.forItems(wood.getLeaves()))
//                    .build(consumer);
//
//            ShapedRecipeBuilder.shapedRecipe(Registration.getLeafBlock(wood, "trapdoor"), 2)
//                    .patternLine("xxx")
//                    .patternLine("xxx")
//                    .key('x', wood.getLeaves())
//                    .addCriterion("has_leaves", InventoryChangeTrigger.Instance.forItems(wood.getLeaves()))
//                    .build(consumer);
//
//            timberToWood(wood,consumer);
//        }
//        super.registerRecipes(consumer);
//    }
//
//
//
//    private void timberToWood(WoodTypes wood, Consumer<IFinishedRecipe> consumer){
//        ShapelessRecipeBuilder.shapelessRecipe(wood.getLog(), 2)
//                .addIngredient(Registration.getTimber(wood), 2)
//                .setGroup("forest tree")
//                .addCriterion("has_timber", InventoryChangeTrigger.Instance.forItems(Registration.getTimber(wood)))
//                .build(consumer);
//    }
//}
