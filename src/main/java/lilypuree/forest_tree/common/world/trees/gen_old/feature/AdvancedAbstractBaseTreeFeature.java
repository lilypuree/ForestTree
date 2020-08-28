package lilypuree.forest_tree.common.world.trees.gen_old.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationReader;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class AdvancedAbstractBaseTreeFeature<T extends AdvancedTreeFeatureConfig> extends AdvancedAbstractTreeFeature<T> {

    protected int height;
    protected boolean[][] conversionMatrix = new boolean[32][32];

    protected final Vec3i[] initialDirections = new Vec3i[] {v( -1, 0, -1 ), v(0,0,-1),
           v(1,0,-1), v(1,0,0), v(1,0,1), v(0,0,1),
           v(-1,0,1),v(-1,0,0) };


    protected final Vec3i[] directions = new Vec3i[]{v(-1, 0, -1), v(-1, 0, 0), v(-1, 0, 1),
            v(1, 0, -1), v(0, 0, -1), v(1, 0, 1), v(0, 0, 1),
            v(1, 0, 0), v(-1, 1, -1), v(-1, 1, 0), v(-1, 1, 1),
            v(1, 1, -1), v(0, 1, -1), v(1, 1, 1), v(0, 1, 1),
            v(1, 1, 0)
    };

    protected Vec3i v(int x, int y, int z) {
        return new Vec3i(x, y, z);
    }

    public AdvancedAbstractBaseTreeFeature(Function<Dynamic<?>, ? extends T> configFactoryIn) {
        super(configFactoryIn);
    }

    protected void generateLogs(IWorldGenerationReader reader, Random rand, int baseHeight, BlockPos pos, int trunkTopOffset, Set<BlockPos> logs, MutableBoundingBox mBB, AdvancedTreeFeatureConfig config) {
        for (int i = 0; i < baseHeight - trunkTopOffset; ++i) {
            this.addLog(reader, rand, pos.up(i), logs, mBB, config);
        }
    }

    abstract protected boolean generateRandomBranches(IWorldGenerationReader world, Random rand, BlockPos pos, Vec3i currentDirection, int numBranches, int remainingDistance, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config);

    public boolean placeLeaves(IWorldGenerationReader world, Random rand, BlockPos pos, int radiusIn, Set<BlockPos> leaves, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
//        int radius = height > 4 ? 3 : 2;
        int radius = radiusIn;
//        boolean lost = BlockBranch.shouldLoseLeaf(world, xCoord , yCoord, zCoord , random,block2);
//        boolean defLost = BlockBranch.shouldDefinitelyLoseLeaf(world, xCoord, yCoord, zCoord, block2);
        boolean lost = false;
        boolean defLost = false;
        for (int x = -(radius + 1); x <= (radius + 1); x++) {
            for (int y = 0; y <= (radius + 1); y++) {
                for (int z = -(radius + 1); z <= radius + 1; z++) {
                    //blockExists?
                    boolean canPlaceLeaves = x * x + y * y + z * z <= radius * radius && !defLost && (!lost || rand.nextInt(4) == 0);
                    if (canPlaceLeaves) {
                        addLeaves(world, rand, pos.add(x,y,z), leaves, mBB, config);
//                        if (!fromSapling && !conversionMatrix[16+(xCoord + i)-tempSourceX][16+(zCoord+k)-tempSourceZ])
//                        {
//                            convertGrassToDirt(world, xCoord + i, yCoord + j, zCoord + k, height);
//                            conversionMatrix[16+(xCoord + i)-tempSourceX][16+(zCoord+k)-tempSourceZ] = true;
//                        }
                    }
                }
            }
        }
        return true;
    }

    public Optional<BlockPos> canPlaceTree(IWorldGenerationReader reader, int height, BlockPos pos, AdvancedTreeFeatureConfig config) {
        BlockPos blockpos;
        if (!config.forcePlacement) {
            int oceanHeight = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int surfaceHeight = reader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            blockpos = new BlockPos(pos.getX(), oceanHeight, pos.getZ());
            if (surfaceHeight - oceanHeight > config.maxWaterDepth) {
                return Optional.empty();
            }
        } else {
            blockpos = pos;
        }

        int extraHeight = config.extraHeight;
        if (blockpos.getY() >= 1 && blockpos.getY() + height + extraHeight <= reader.getMaxHeight()) {
            for (int treePosY = 0; treePosY <= height + extraHeight; ++treePosY) {
                byte width = getRequiredWidth(blockpos, height, treePosY);

                BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                for (int x = -width; x <= width; ++x) {
                    int z = -width;

                    while (z <= width) {
                        if (treePosY + blockpos.getY() >= 0 && treePosY + blockpos.getY() < reader.getMaxHeight()) {
                            mutablePos.setPos(x + blockpos.getX(), treePosY + blockpos.getY(), z + blockpos.getZ());
                            if (canBeReplacedByLogs(reader, mutablePos) && (config.ignoreVines || !isVine(reader, mutablePos))) {
//                          if (!j3.isAir(world, i2, i1, l2) && !j3.canBeReplacedByLeaves(world, i2, i1, l2))
                                ++z;
                                continue;
                            }
                            return Optional.empty();
                        }
                        return Optional.empty();
                    }
                }
            }

            return isSoilOrFarm(reader, blockpos.down(), config.getSapling()) && blockpos.getY() < reader.getMaxHeight() - height - 1 ? Optional.of(blockpos) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    protected byte getRequiredWidth(BlockPos pos, int height, int treePosY){
        byte byte0 = 1;
        if(treePosY == pos.getY()){
            byte0 = 0;
        }
        if(treePosY>= pos.getY()+height-1){
            byte0 =2;
        }
        return byte0;
    }

    protected int subtractDistance(Random rand, Vec3i curDir, int currentRemainingDistance){
        if (curDir.getX() * curDir.getZ() != 0 || curDir.getX() * curDir.getY() != 0 || curDir.getY() * curDir.getZ() != 0) {
            if (shouldSubtractDistance(rand)) {
                currentRemainingDistance--;
            }
        }
        return currentRemainingDistance;
    }

    protected boolean shouldSubtractDistance(Random rand) {
        //This is math. Basically, the trees look funky on daigonals because the diagonal length is greater than
        //the straight length. It is longer by sqrt(2) = 1.414
        //So in order to account for this, we must subtract from our overall length
        //How often we subtract should be such that the chance of subtracing = 1/(sqrt(2))
        //in the situation rand.nextInt(x) > y, we have y + a = x
        //where a is the number of values > y.
        //Thus, the chance of succeeding is a/x = 0.707...
        //given x - y = a, we divide everything by x, giving 1 - (y/x) = 0.707
        //Move the integers to one side and we get the ratio (y/x) = 0.29289
        //The inverse of 0.29298 is 3.414
        if (rand.nextInt(3414) > 1000) {
            return true;
        }
        return false;
    }

    public int setValidDirections(Vec3i currentDirection, boolean[]validDirections){
        int numValidDirections = 0;

        if (currentDirection.getX() != 1) {
            if (currentDirection.getZ() != 1) {
                validDirections[0] = true;
                numValidDirections++;
                validDirections[0 + 8] = true;
                numValidDirections++;
            }
            validDirections[1] = true;
            numValidDirections++;
            validDirections[1 + 8] = true;
            numValidDirections++;
            if (currentDirection.getZ() != -1) {
                validDirections[2] = true;
                numValidDirections++;
                validDirections[2 + 8] = true;
                numValidDirections++;
            }
        }
        if (currentDirection.getZ() != 1) {
            if (currentDirection.getX() != -1) {
                validDirections[3] = true;
                numValidDirections++;
                validDirections[3 + 8] = true;
                numValidDirections++;
            }
            validDirections[4] = true;
            numValidDirections++;
            validDirections[4 + 8] = true;
            numValidDirections++;
        }
        if (currentDirection.getZ() != -1) {
            if (currentDirection.getX() != -1) {
                validDirections[5] = true;
                numValidDirections++;
                validDirections[5 + 8] = true;
                numValidDirections++;
            }
            validDirections[6] = true;
            numValidDirections++;
            validDirections[6 + 8] = true;
            numValidDirections++;
        }
        if (currentDirection.getX() != -1) {
            validDirections[7] = true;
            numValidDirections++;
            validDirections[7 + 8] = true;
            numValidDirections++;
        }
        return numValidDirections;
    }

}
