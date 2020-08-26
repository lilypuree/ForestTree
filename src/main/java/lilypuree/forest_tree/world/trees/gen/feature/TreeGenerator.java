package lilypuree.forest_tree.world.trees.gen.feature;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.block.ModBlockProperties;
import lilypuree.forest_tree.trees.species.Species;
import lilypuree.forest_tree.world.trees.gen.feature.parametric.Meristem;
import lilypuree.forest_tree.world.trees.gen.feature.parametric.MeristemFactory;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;

import java.util.*;

public class TreeGenerator {

    private MeristemFactory factory;
    private TreePlacer placer;
    private Random rand;

    public TreeGenerator(Species species) {
        factory = new MeristemFactory(species);
        placer = new TreePlacer();
    }

    public boolean generate(BlockPos pos, int age) {
        ForestTree.LOGGER.info("starting generation");
        //TODO
        //temporary values here
        Optional<BlockPos> optionalBlockPos = placer.canPlaceTree(factory.getMaxHeight(), pos, true, true, 0, Registration.CUSTOM_SAPLING.get());
        if (!optionalBlockPos.isPresent()) {
            return false;
        } else {
            BlockPos startPos = optionalBlockPos.get();
            createTree(pos, age);
            return true;
        }
    }

//TODO
    //check if meristems can extend all the way out first. if not cancel branch generation?

    private void createTree(BlockPos pos, int age) {
        ForestTree.LOGGER.info("starting tree creation");
        Queue<Meristem> meristems = new ArrayDeque<>();
        factory.setStartingAge(age);
        meristems.add(factory.createTerminalMeristem(age, 0, pos, factory.getDirectionHelper().getRandomTerminalDirection()));

        while (!meristems.isEmpty()) {
            Meristem meristem = meristems.poll();

            if (meristem.getAge() < factory.getMinimumBranchAge() || meristem.getOrder() > factory.getMaximumBranchOrder()) continue;

            meristem.grow(rand);
            if (meristem.isAlive()) {
                makeBranch(meristem);
                meristems.add(meristem);
            }
            Collection<Meristem> axillaries = meristem.generateAxillaries(rand);

            axillaries.removeIf(axillary -> axillary.getAge() < factory.getMinimumBranchAge());

            axillaries.forEach(this::makeBranch);
            meristems.addAll(axillaries);
        }
    }

    private void makeBranch(Meristem meristem) {
        Block branch = TreeBlocks.getBranchBlock(meristem.getSourceDirection(), factory.getSpecies());
        placer.addBranch(meristem.getPos(), branch.getDefaultState().with(ModBlockProperties.TREE_AGE, meristem.getAge()));
    }

    public void setTreegenParameters(IWorldGenerationReader reader, Random rand, Set<BlockPos> branches, Set<BlockPos> leaves, MutableBoundingBox mBB) {
        this.rand = rand;
        factory.setRandom(rand);
        placer.setParameters(reader, branches, leaves, mBB);
    }

    public void loadFromNbt(CompoundNBT compound) {
        if (compound.contains("TreeGenerator")) {
            factory.loadFromNbt(compound.getCompound("TreeGenerator"));
        }
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        compound.put("TreeGenerator", factory.saveToNbt(new CompoundNBT()));
        return compound;
    }
}