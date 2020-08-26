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

public class PineTreeFeature extends DouglasFirTreeFeature {
    final Vec3i[] pineDirections = new Vec3i[]{v(-1, 0, -1), v(-1, 0, 0),
            v(-1, 0, 1), v(1, 0, -1), v(0, 0, -1), v(1, 0, 1),
            v(0, 0, 1), v(1, 0, 0)};

    public PineTreeFeature(Function<Dynamic<?>, ? extends AdvancedTreeFeatureConfig> configFactoryIn) {
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

            int lim = 2 + rand.nextInt(height / 4);
            for (int treePosY = 0; treePosY < height; treePosY++) {
                addBranch(reader, rand, pos.add(0, treePosY, 0), new Vec3i(0, -1, 0), logs, mBB, config);
                //trydomoss
                if (treePosY > lim) {
                    BlockPos newPos = pos.add(0, treePosY, 0);
                    if (treePosY < height / 3) {
                        int numBranches = 4 - rand.nextInt(3) + 1;
                        generateRandomBranches(reader, rand, newPos, new Vec3i(0, 1, 0), numBranches, config.extraBranchLength + 2, logs, leaves, mBB, config);
                    } else if (treePosY < (2 * height) / 3) {
                        int numBranches = 6 - rand.nextInt(rand.nextInt(5) + 1);
                        generateRandomBranches(reader, rand, newPos, new Vec3i(0, 1, 0), numBranches, rand.nextInt(config.extraBranchLength + 2) + 1, logs, leaves, mBB, config);
                        fillInLeaves(reader, rand, newPos, false, leaves, mBB, config);
                    } else if (treePosY >= 2 * height / 3) {
                        int numBranches = 4 - rand.nextInt(rand.nextInt(3) + 1);
                        generateRandomBranches(reader, rand, newPos, new Vec3i(0, 1, 0), numBranches, 1, logs, leaves, mBB, config);
                    }
                }
            }
            BlockPos endPos = pos.add(0, height, 0);
            addBranchEnd(reader, rand, endPos, new Vec3i(0, -1, 0), logs, mBB, config);
            return true;
        }
    }


    @Override
    protected boolean generateRandomBranches(IWorldGenerationReader world, Random rand, BlockPos pos, Vec3i currentDirection, int numBranches, int remainingDistance, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (remainingDistance < 1) {
            return true;
        }
        boolean[] validDirections = new boolean[] { false, false, false, false, false, false, false, false };
        int numValidDirections = setValidDirections(currentDirection, validDirections);
        numBranches = Math.min(numBranches, numValidDirections);

        boolean placedBranch = false;


        for (int i = 0; i < numBranches; i++) {
            int currentRemainingDistance = remainingDistance;
            Vec3i curDir = currentDirection;

            if (numValidDirections == 0) {
                break;
            }

            int index = rand.nextInt(pineDirections.length);
            while (!validDirections[index]) {
                index = rand.nextInt(pineDirections.length);
            }
            validDirections[index] = false;
            numValidDirections--;
            curDir = initialDirections[index];

            currentRemainingDistance = subtractDistance(rand, curDir, currentRemainingDistance);

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
