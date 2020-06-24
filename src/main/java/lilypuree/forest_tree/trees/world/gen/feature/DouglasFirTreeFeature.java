package lilypuree.forest_tree.trees.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import lilypuree.forest_tree.trees.TreeBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import org.lwjgl.system.CallbackI;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class DouglasFirTreeFeature extends AdvancedAbstractBaseTreeFeature<AdvancedTreeFeatureConfig> {

    private boolean tall = false;

    public DouglasFirTreeFeature(Function<Dynamic<?>, ? extends AdvancedTreeFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public int setValidDirections(Vec3i currentDirection, boolean[] validDirections) {
        int numValidDirections = 0;

        if (currentDirection.getX() != 1) {
            if (currentDirection.getZ() != 1) {
                validDirections[0] = true;
                numValidDirections++;
            }
            validDirections[1] = true;
            numValidDirections++;
            if (currentDirection.getZ() != -1) {
                validDirections[2] = true;
                numValidDirections++;
            }
        }
        if (currentDirection.getZ() != 1) {
            if (currentDirection.getX() != -1) {
                validDirections[3] = true;
                numValidDirections++;
            }
            validDirections[4] = true;
            numValidDirections++;
        }
        if (currentDirection.getZ() != -1) {
            if (currentDirection.getX() != -1) {
                validDirections[5] = true;
                numValidDirections++;
            }
            validDirections[6] = true;
            numValidDirections++;
        }
        if (currentDirection.getX() != -1) {
            validDirections[7] = true;
            numValidDirections++;
        }
        return numValidDirections;
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
            if (i > 0) {
                int index = rand.nextInt(initialDirections.length);
                while (!validDirections[index]) {
                    index = rand.nextInt(initialDirections.length);
                }
                validDirections[index] = false;
                numValidDirections--;
                curDir = initialDirections[index];
                if (currentRemainingDistance > 1) {
                    currentRemainingDistance = 1;
                }
            }

            currentRemainingDistance = subtractDistance(rand, curDir, currentRemainingDistance);

            Vec3i sourceDir = new Vec3i(-curDir.getX(), -curDir.getY(), -curDir.getZ());

            if (canBeReplacedByLogs(world, pos.add(curDir))) {
                boolean isEnd = currentRemainingDistance <= 3;
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
                        fillInLeaves(world, rand, pos.add(curDir), currentRemainingDistance>1, leaves, mBB, config);
                    } else {
                        addBranch(world, rand, pos.add(curDir), sourceDir, logs, mBB, config);
                    }
                    placedBranch = true;
                }
            }
        }
        return placedBranch;
    }

    @Override
    protected int subtractDistance(Random rand, Vec3i curDir, int currentRemainingDistance) {
        if(curDir.getX() * curDir.getZ() != 0){
            if(shouldSubtractDistance(rand)){
                currentRemainingDistance--;
            }
        }
        return currentRemainingDistance;
    }

    @Override
    protected boolean generate(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedTreeFeatureConfig config) {

        height = config.height + rand.nextInt(config.heightRandom);
        if (rand.nextInt(20) == 0) {
            tall = true;
        }
        if (tall) {
            height += rand.nextInt(10);
        }
        Optional<BlockPos> optionalBlockPos = canPlaceTree(reader, height, pos, config);
        if (!optionalBlockPos.isPresent()) {
            return false;
        } else {
            BlockPos treepos = optionalBlockPos.get();
            for (int treePosY = 0; treePosY < height; treePosY++) {
                addLog(reader, rand, treepos.add(0, treePosY, 0), logs, mBB, config);
            }
            // We want to make branches in a radial pattern going up
            int startHeight = height / 3;
            int branchPattern = rand.nextInt(8);
            for (int treePosY = startHeight; treePosY < height; treePosY++) {
                int numBranchesHere = rand.nextInt(3) + 2;
                int extraBranchLength = treePosY < ((9f * height) / 10f) ? 2 : 1;
                for (int j = 0; j < numBranchesHere; j++) {
                    if (rand.nextInt(4) != 0) {
                        generateRandomBranches(reader, rand, treepos.add(0, treePosY, 0),
                                initialDirections[(branchPattern + j) % 8], 1, rand.nextInt(2) + extraBranchLength, logs, leaves, mBB, config);
                    }
                }
                branchPattern += numBranchesHere;
                branchPattern %= 8;
            }
            for (int treePosY = height; treePosY < height + config.extraHeight; treePosY++) {
                int numBranchesHere = rand.nextInt(3) + 2;
                int extraBranchLength = 1;
                for (int j = 0; j < numBranchesHere; j++) {
                    if (rand.nextInt(4) != 0) {
                        generateRandomBranches(reader, rand, treepos.add(0, treePosY, 0),
                                initialDirections[(branchPattern + j) % 8], 1, rand.nextInt(2) + extraBranchLength, logs, leaves, mBB, config);
                    }
                }
                addBranch(reader, rand, treepos.add(0, treePosY, 0), new Vec3i(0, -1, 0), logs, mBB, config);
                branchPattern += numBranchesHere;
                branchPattern %= 8;
            }
            addBranchEnd(reader, rand, treepos.add(0, height + config.extraHeight, 0), new Vec3i(0, -1, 0), logs, mBB, config);
            fillInLeaves(reader, rand, treepos.add(0, height + config.extraHeight, 0), true, leaves, mBB, config);
            return true;
        }
    }

    protected boolean fillInLeaves(IWorldGenerationReader world, Random rand, BlockPos pos, boolean high, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        for (int x = -1; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (y > 0 && ((x != 0 || z != 0) || !high)) {
                        continue;
                    }
                    addLeaves(world, rand, pos.add(x, y, z), leaves, mBB, config);
                }
            }
        }
        return true;
    }

    @Override
    protected byte getRequiredWidth(BlockPos pos, int height, int treePosY) {
        return (byte) (treePosY > height / 3 ? 3 : 2);
    }
}
