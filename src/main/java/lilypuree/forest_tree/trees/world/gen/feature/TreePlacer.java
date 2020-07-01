package lilypuree.forest_tree.trees.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraftforge.common.IPlantable;

import java.util.Optional;
import java.util.Set;


public class TreePlacer {

    IWorldGenerationReader reader;
    Set<BlockPos> branches;
    Set<BlockPos> leaves;
    MutableBoundingBox mBB;

    public Optional<BlockPos> canPlaceTree(int height, BlockPos pos, boolean forcePlacement, boolean ignoreVines, int maxWaterDepth, IPlantable sapling) {
        BlockPos blockpos;

        if (!forcePlacement) {
            int oceanHeight = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int surfaceHeight = reader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            blockpos = new BlockPos(pos.getX(), oceanHeight, pos.getZ());
            if (surfaceHeight - oceanHeight > maxWaterDepth) {
                return Optional.empty();
            }
        } else {
            blockpos = pos;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + height <= reader.getMaxHeight()) {
            for (int treePosY = 0; treePosY <= height; ++treePosY) {
                byte width = getRequiredWidth(blockpos, height, treePosY);

                BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                for (int x = -width; x <= width; ++x) {
                    int z = -width;

                    while (z <= width) {
                        if (treePosY + blockpos.getY() >= 0 && treePosY + blockpos.getY() < reader.getMaxHeight()) {
                            mutablePos.setPos(x + blockpos.getX(), treePosY + blockpos.getY(), z + blockpos.getZ());
                            if (PlacementHelper.canBeReplacedByLogs(reader, mutablePos) && (ignoreVines || !PlacementHelper.isVine(reader, mutablePos))) {
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
            return PlacementHelper.canPlantSapling(reader, blockpos.down(), sapling) && blockpos.getY() < reader.getMaxHeight() - height - 1 ? Optional.of(blockpos) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }


    //TODO
    //fix this to a more relevant method
    protected byte getRequiredWidth(BlockPos pos, int height, int treePosY) {
        byte byte0 = 1;
        if (treePosY == pos.getY()) {
            byte0 = 0;
        }
        if (treePosY >= pos.getY() + height - 1) {
            byte0 = 2;
        }
        return byte0;
    }


    public boolean addBranch(BlockPos pos, BlockState branch) {
        if (!PlacementHelper.isAirOrLeaves(reader, pos) && !PlacementHelper.isTallPlants(reader, pos) && !PlacementHelper.isWater(reader, pos)) {
            return false;
        } else {
            this.setBlockStateAndExpandBounds(reader, pos, branch);
            branches.add(pos.toImmutable());
            return true;
        }
    }

    protected final void setBlockStateAndExpandBounds(IWorldWriter writer, BlockPos pos, BlockState state) {
        setBlockStateWithNoNeighborReaction(writer, pos, state);
        mBB.expandTo(new MutableBoundingBox(pos, pos));
    }

    private static void setBlockStateWithNoNeighborReaction(IWorldWriter writer, BlockPos pos, BlockState state) {
        writer.setBlockState(pos, state, 19);
    }

    public void setParameters(IWorldGenerationReader reader, Set<BlockPos> branches, Set<BlockPos> leaves, MutableBoundingBox mBB) {
        this.reader = reader;
        this.branches = branches;
        this.leaves = leaves;
        this.mBB = mBB;
    }

}
