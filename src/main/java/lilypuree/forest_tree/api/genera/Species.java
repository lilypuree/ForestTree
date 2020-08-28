package lilypuree.forest_tree.api.genera;

import com.mojang.datafixers.types.DynamicOps;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.api.gen.ITreeModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.IDynamicSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Species extends ForgeRegistryEntry<Species> implements IDynamicSerializable {

    public static final Species NULLSPECIES = new Species(){
        @Override
        public TreeGenus getGenus() {
            return TreeGenus.NULLGENUS;
        }

    };

    public static ForgeRegistry<Species> REGISTRY;

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        REGISTRY = (ForgeRegistry<Species>) new RegistryBuilder<Species>()
                .setName(new ResourceLocation(ForestTree.MODID, "species"))
                .setDefaultKey(new ResourceLocation(ForestTree.MODID, "null"))
                .disableSaving()
                .setType(Species.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();
    }

    protected final TreeGenus genus;
    protected WoodCategory woodCategory;
    protected FoliageCategory foliageCategory;
    protected ITreeModel treeModel;
    protected HashSet<Block> soilList = new HashSet<Block>();

    public Species() {
        this.genus = TreeGenus.NULLGENUS;
    }

    public Species(ResourceLocation name, TreeGenus genus, WoodCategory wood, FoliageCategory foliage) {
        setRegistryName(name);
        this.genus = genus;
        this.woodCategory = wood;
        this.foliageCategory = foliage;
        setStandardSoils();
    }

    public Species(ResourceLocation name, TreeGenus genus) {
        this(name, genus, genus.getWoodCategory(), genus.getFoliageCategory());
    }


    public float getThickness(int age, boolean isEnd, Vec3i sourcePos) {
        return 1.0f / 12.0f * (age);
    }

    public float getStumpThickness(int age) {
        return 1.0f / 12.0f * (age);
    }

    public boolean canLoseLeaves() {
        return false;
    }

    public boolean fruits() {
        return false;
    }

//    public String getName(){
//        return
//    }

    public boolean isValid() {
        return this != NULLSPECIES;
    }

    public ITreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(ITreeModel model) {
        this.treeModel = model;
    }

    public FoliageCategory getFoliage() {
        return foliageCategory;
    }

    public WoodCategory getWood() {
        return woodCategory;
    }

    public TreeGenus getGenus() {
        return genus;
    }

    //Dynamic trees code

    /**
     * This is run by the Species class itself to set the standard
     * blocks available to be used as planting substrate. Developer
     * may override this entirely or just modify the list at a
     * later time.
     */
    protected final void setStandardSoils() {
        addAcceptableSoil(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.MYCELIUM);
    }

    /**
     * Adds blocks to the acceptable soil list.
     *
     * @param soilBlocks
     */
    public Species addAcceptableSoil(Block... soilBlocks) {
        Collections.addAll(soilList, soilBlocks);
        return this;
    }

    /**
     * Removes blocks from the acceptable soil list.
     *
     * @param soilBlocks
     */
    public Species remAcceptableSoil(Block... soilBlocks) {
        for (Block block : soilBlocks) {
            soilList.remove(block);
        }
        return this;
    }

    /**
     * Will clear the acceptable soils list.  Useful for
     * making trees that can only be planted in abnormal
     * substrates.
     */
    public Species clearAcceptableSoils() {
        soilList.clear();
        return this;
    }

    /**
     * Retrieve a clone of the acceptable soils list.
     * Editing this set will not affect the original list.
     * Should only be used for config purposes and is not
     * recommended for realtime gameplay operations.
     *
     * @return A clone of the acceptable soils list.
     */
    public Set<Block> getAcceptableSoils() {
        return (Set<Block>) soilList.clone();
    }

    /**
     * Position sensitive soil acceptability tester.  Mostly to test if the block is dirt but could
     * be overridden to allow gravel, sand, or whatever makes sense for the tree
     * species.
     *
     * @param world
     * @param pos
     * @param soilBlockState
     * @return
     */
    public boolean isAcceptableSoil(IWorld world, BlockPos pos, BlockState soilBlockState) {
        Block soilBlock = soilBlockState.getBlock();
        return soilList.contains(soilBlock);
    }

    /**
     * Version of soil acceptability tester that is only run for worldgen.  This allows for Swamp oaks and stuff.
     *
     * @param world
     * @param pos
     * @param soilBlockState
     * @return
     */
    public boolean isAcceptableSoilForWorldgen(IWorld world, BlockPos pos, BlockState soilBlockState) {
        return isAcceptableSoil(world, pos, soilBlockState);
    }

    @Override
    public <T> T serialize(DynamicOps<T> dynamicOps) {
        return null;
    }
}
