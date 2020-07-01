package lilypuree.forest_tree.trees.world.gen.feature;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.block.ModBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.IWorldGenerationReader;

import java.util.*;

public class TreeGenerator {

    private MeristemFactory factory;
    private TreePlacer placer;
    private Random rand;

    public TreeGenerator() {
        factory = new MeristemFactory();
        placer = new TreePlacer();
    }

    public boolean generate(BlockPos pos, int age) {
        ForestTree.LOGGER.info("starting generation");
        //TODO
        //temporary values here
        Optional<BlockPos> optionalBlockPos = placer.canPlaceTree(factory.getMaxHeight(), pos, true, true, 0, Registration.PLACEBO_SAPLING.get());
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
        meristems.add(factory.createTerminalMeristem(age, pos, factory.getDirectionHelper().getRandomTerminalDirection()));

        while (!meristems.isEmpty()) {
            Meristem meristem = meristems.poll();

            if (meristem.getAge() < factory.getMinimumBranchAge()) continue;

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

}