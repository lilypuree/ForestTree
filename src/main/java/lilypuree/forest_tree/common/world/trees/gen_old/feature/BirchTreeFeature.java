//package lilypuree.forest_tree.common.world.trees.gen_old.feature;
//
//import com.mojang.datafixers.Dynamic;
//import net.minecraft.block.Block;
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MutableBoundingBox;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.world.gen.IWorldGenerationReader;
//
//import java.util.Optional;
//import java.util.Random;
//import java.util.Set;
//import java.util.function.Function;
//
//public class BirchTreeFeature extends AdvancedAbstractBaseTreeFeature<AdvancedTreeFeatureConfig> {
//    public BirchTreeFeature(Function<Dynamic<?>, ? extends AdvancedTreeFeatureConfig> configFactoryIn) {
//        super(configFactoryIn);
//    }
//
//    protected final Vec3i[] birchDirections = new Vec3i[]{v(-1, 1, -1), v(-1, 1, 0), v(-1, 1, 1),
//            v(1, 1, -1), v(0, 1, -1), v(1, 1, 1), v(0, 1, 1), v(1, 1, 0), v(0, 1, 0)};
//
//    @Override
//    protected boolean generate(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedTreeFeatureConfig config) {
//
//        //don't have to do the chunk boundary test
//
//        //change height to be dependent on species and age
//        height = rand.nextInt(6) + 7;
//
//        Optional<BlockPos> optionalBlockPos = canPlaceTree(reader, height, pos, config);
//
//        if (!optionalBlockPos.isPresent()) {
//            return false;
//        } else {
//            BlockPos treepos = optionalBlockPos.get();
//            conversionMatrix = new boolean[32][32];
//            boolean branches = false;
//
//            for (int treePosY = 0; treePosY < height; treePosY++) {
//                addBranch(reader, rand, treepos.add(0, treePosY, 0), new Vec3i(0, -1, 0), logs, mBB, config);
//                if (treePosY >= height * 0.4) {
//                    for (int i = 0; i < 8; i++) {
//                        if (rand.nextInt(3) == 0) {
//                            branches |= generateRandomBranches(reader, rand, treepos.add(0, treePosY, 0), directions[i], 1, 1, logs, leaves, mBB, config);
//                        }
//                    }
//                }
//            }
//            if (branches && isAirOrLeaves(reader, treepos.add(0, height, 0))) {
//                addBranchEnd(reader, rand, treepos.add(0, height, 0), new Vec3i(0, -1, 0), logs, mBB, config);
//                placeLeaves(reader, rand, treepos.add(0, height, 0), 2, leaves, mBB, config);
//            }
//            if (!branches) {
//                for (int treePosY = 0; treePosY < height; treePosY++) {
//                    this.setBlockStateAndExpandBounds(reader, treepos.add(0, treePosY, 0), Blocks.AIR.getDefaultState(), mBB);
//                    //tryDoMoss;
//                }
//            } else {
//                for (int treePosY = 0; treePosY < height; treePosY++) {
//                    //tryDoMoss;
//                }
//            }
//            return branches;
//        }
//    }
//
//    @Override
//    protected boolean generateRandomBranches(IWorldGenerationReader world, Random rand, BlockPos pos, Vec3i currentDirection, int numBranches, int remainingDistance, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
//        if (remainingDistance < 1) {
//            return true;
//        }
//        boolean[] validDirections = new boolean[]{false, false, false, false, false, false, false, false, false,
//                false, false, false, false, false, false, false};
//
//        int numValidDirections = setValidDirections(currentDirection, validDirections);
//        numBranches = Math.min(numBranches, numValidDirections);
//
//        boolean placedBranch = false;
//        Vec3i curDir = null;
//
//        for (int i = 0; i < numBranches; i++) {
//            int currentRemainingDistance = remainingDistance;
//            if (numValidDirections == 0) {
//                break;
//            }
//
//            boolean ignoreBranching = shouldIgnoreBranching(rand, numBranches, currentRemainingDistance);
//
//            curDir = currentDirection;
//            if (!ignoreBranching) {
//                int index = rand.nextInt(directions.length);
//                while (!validDirections[index]) {
//                    index = rand.nextInt(directions.length);
//                }
//                validDirections[index] = false;
//                numValidDirections--;
//                curDir = directions[index];
//            }
//
//            currentRemainingDistance = subtractDistance(rand, curDir, currentRemainingDistance);
//
//            Vec3i sourceDir = new Vec3i(-curDir.getX(), -curDir.getY(), -curDir.getZ());
//
//            if (canBeReplacedByLogs(world, pos.add(curDir))) {
//                boolean isEnd = currentRemainingDistance <= 1;
//                Block theBranch = TreeBlocks.getBranchBlock(sourceDir, isEnd, config.species);
//                // If the branch directly below is the same, it'll look ugly, so
//                // skip it.
//                if (world.hasBlockState(pos.add(curDir.getX(), curDir.getY() - 1, curDir.getZ()), state -> state.getBlock() == theBranch)) {
//                    i--;
//                    continue;
//                }
//                // We only want to place this branch here if this branch can
//                // continue.
//                if (generateRandomBranches(world, rand, pos.add(curDir), curDir, 3, currentRemainingDistance - 1, logs, leaves, mBB, config)) {
//                    if (isEnd) {
//                        addBranchEnd(world, rand, pos.add(curDir), sourceDir, logs, mBB, config);
//                        placeLeaves(world, rand, pos.add(curDir), 2, leaves, mBB, config);
//                    } else {
//                        addBranch(world, rand, pos.add(curDir), sourceDir, logs, mBB, config);
//                    }
//                    placedBranch = true;
//                }
//            }
//        }
//        return placedBranch;
//    }
//
//    protected boolean shouldIgnoreBranching(Random rand, int numBranches, int currentRemainingDistance){
//        boolean ignoreBranching = numBranches == 1;
//
//        if (currentRemainingDistance > 1) {
//            // 50/50 chance whether we keep going straight or start
//            // branching.
//            ignoreBranching = rand.nextBoolean();
//        }
//        return ignoreBranching;
//    }
//
//    protected int subtractDistance(Random rand, Vec3i curDir, int currentRemainingDistance){
//        if (curDir.getX() * curDir.getZ() != 0 || curDir.getX() * curDir.getY() != 0 || curDir.getY() * curDir.getZ() != 0) {
//            if (shouldSubtractDistance(rand) && currentRemainingDistance > 1) {
//                currentRemainingDistance--;
//            }
//        }
//        return currentRemainingDistance;
//    }
//
//
//}
