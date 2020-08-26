package lilypuree.forest_tree.world.trees.gen_old.feature;

import com.mojang.datafixers.Dynamic;
import lilypuree.forest_tree.trees.TreeBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.IWorldGenerationReader;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class CedarTreeFeature extends DouglasFirTreeFeature {
    public CedarTreeFeature(Function<Dynamic<?>, ? extends AdvancedTreeFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    protected boolean generate(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedTreeFeatureConfig config) {

        height = config.height + rand.nextInt(config.heightRandom);
        Optional<BlockPos> optionalBlockPos = canPlaceTree(reader, height, pos, config);
        if (!optionalBlockPos.isPresent()) {
            return false;
        } else {
            BlockPos treepos = optionalBlockPos.get();
            for (int treePosY = 0; treePosY < height; treePosY++) {
                addLog(reader, rand, treepos.add(0, treePosY, 0), logs, mBB, config);
            }

            int branchPattern = rand.nextInt(8);
            for (int treePosY = 1; treePosY < height; treePosY++) {
                int numBranchesHere = rand.nextInt(3) + 5;
                int extraBranchLength = 1;
//                for (int j = 0; j < numBranchesHere; j++) {
//                    if (rand.nextInt(4) != 0) {
                generateRandomBranches(reader, rand, treepos.add(0, treePosY, 0),
                        new Vec3i(0, 1, 0), numBranchesHere, extraBranchLength, logs, leaves, mBB, config);
//                    }
//                }
                branchPattern += numBranchesHere;
                branchPattern %= 8;
            }

            addBranchEnd(reader, rand, treepos.add(0, height, 0), new Vec3i(0, -1, 0), logs, mBB, config);
            return true;
        }
    }

    @Override
    protected boolean generateRandomBranches(IWorldGenerationReader world, Random rand, BlockPos pos, Vec3i currentDirection, int numBranches, int remainingDistance, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (remainingDistance < 1) {
            return true;
        }
        boolean[] validDirections = new boolean[]{false, false, false, false, false, false, false, false};

        int numValidDirections = setValidDirections(currentDirection, validDirections);
        numBranches = Math.min(numBranches, numValidDirections);

        boolean placedBranch = false;


        for (int i = 0; i < numBranches; i++) {
            int currentRemainingDistance = remainingDistance;
            Vec3i curDir = currentDirection;

            if (numValidDirections == 0) {
                break;
            }

            int index = rand.nextInt(validDirections.length);
            while (!validDirections[index]) {
                index = rand.nextInt(validDirections.length);
            }
            validDirections[index] = false;
            numValidDirections--;
            curDir = initialDirections[index];
            if (currentRemainingDistance > 1) {
                currentRemainingDistance = 1;
            }

            Vec3i sourceDir = new Vec3i(-curDir.getX(), -curDir.getY(), -curDir.getZ());

            if (canBeReplacedByLogs(world, pos.add(curDir))) {
                boolean isEnd = currentRemainingDistance <= 1;
                Block theBranch = TreeBlocks.getBranchBlock(sourceDir, isEnd, config.species);
                // If the branch directly below is the same, it'll look ugly, so
                // skip it. not when it's a conifer
                if (world.hasBlockState(pos.add(curDir.getX(), curDir.getY() - 1, curDir.getZ()), state -> state.getBlock() == theBranch)) {
                    //i is not decreased when it is conifer .
//                    i--;
//                    continue;
                }
                // We only want to place this branch here if this branch can
                // continue.
                if (generateRandomBranches(world, rand, pos.add(curDir), curDir, 3, currentRemainingDistance - 1, logs, leaves, mBB, config)) {
                    if (isEnd) {
                        addBranchEnd(world, rand, pos.add(curDir), sourceDir, logs, mBB, config);
                        fillInLeaves(world, rand, pos.add(curDir), currentRemainingDistance > 1, leaves, mBB, config);
                    } else {
                        addBranch(world, rand, pos.add(curDir), sourceDir, logs, mBB, config);
                    }
                    placedBranch = true;
                }
            }
        }
        return placedBranch;
    }
}
