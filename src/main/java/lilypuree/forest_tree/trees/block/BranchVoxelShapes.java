package lilypuree.forest_tree.trees.block;

import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class BranchVoxelShapes {

    public static VoxelShape getVoxelShapeForBranch(BranchBlock block, int age) {
        Vec3i sp = block.getSourceOffset();
        float thickness = block.getSpecies().getThickness(0, age, block.isEnd(), sp);
        return (sp.getX() * sp.getY() * sp.getZ() == 0) ? createStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness) :
                createDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness);
    }

    public static VoxelShape getOppositeVoxelShapeForBranch(BranchBlock block, int age) {
        Vec3i sp = block.getSourceOffset();
        sp = new Vec3i(-sp.getX(), -sp.getY(), -sp.getZ());
        float thickness = block.getSpecies().getThickness(0, age, block.isEnd(), sp);
        return (sp.getX() * sp.getY() * sp.getZ() == 0) ? createStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness) :
                createDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness);
    }

    public static VoxelShape getVoxelShapeForStump(StumpBlock block, int age) {
        return createHalfStraightVoxelshape(0, -1, 0, block.getSpecies().getThickness(0, age, false, new Vec3i(0, -1, 0)));
    }

    public static VoxelShape getHalfVoxelShapeForBranch(BranchBlock block, int age) {
        Vec3i sp = block.getSourceOffset();
        float thickness = block.getSpecies().getThickness(0, age, block.isEnd(), sp);
        return (sp.getX() * sp.getY() * sp.getZ() == 0) ? createHalfStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness) :
                createHalfDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), thickness);
    }

    private static VoxelShape createStraightVoxelshape(int x, int y, int z, float thickness) {
        boolean x0 = x == 0;
        boolean y0 = y == 0;
        boolean z0 = z == 0;
        return VoxelShapes.create(0.5f - (x0 ? thickness : 0), 0.5f - (y0 ? thickness : 0), 0.5f - (z0 ? thickness : 0), 0.5f + (x0 ? thickness : x), 0.5f + (y0 ? thickness : y), 0.5f + (z0 ? thickness : z));
    }


    private static VoxelShape createDiagonalVoxelshape(int x, int y, int z, float t) {
        float k = 0.3f;
        VoxelShape voxelShape1 = VoxelShapes.create(0.5f - x * t * k, 0.5f - y * t * k, 0.5f - z * t * k, 0.5f + 0.5f * x, 0.5f + 0.5f * y, 0.5f + 0.5f * z);
        VoxelShape voxelShape3 = VoxelShapes.create(0.5f + 0.25f * x - x * t * k / 2, 0.5f + 0.25f * y - y * t * k / 2, 0.5f + 0.25f * z - z * t * k / 2, 0.5f + 0.75f * x + x * t * k / 2, 0.5f + 0.75f * y + y * t * k / 2, 0.5f + 0.75f * z + z * t * k / 2);
        VoxelShape voxelShape2 = VoxelShapes.create(0.5f + 0.5f * x, 0.5f + 0.5f * y, 0.5f + 0.5f * z, 0.5f + x + x * t * k, 0.5f + y + y * t * k, 0.5f + z + z * t * k);
        return VoxelShapes.or(VoxelShapes.or(voxelShape1, voxelShape2), voxelShape3);
    }

    private static VoxelShape createHalfStraightVoxelshape(int x, int y, int z, float t) {
        boolean x0 = x == 0;
        boolean y0 = y == 0;
        boolean z0 = z == 0;
        return VoxelShapes.create(0.5f - (x0 ? t : 0), 0.5f - (y0 ? t : 0), 0.5f - (z0 ? t : 0), 0.5f + (x0 ? t : x / 2.0f), 0.5f + (y0 ? t : y / 2.0f), 0.5f + (z0 ? t : z / 2.0f));
    }

    private static VoxelShape createHalfDiagonalVoxelshape(int x, int y, int z, float t) {
        float k = 0.3f;
        VoxelShape voxelShape = VoxelShapes.create(0.5f - x * t * k, 0.5f - y * t * k, 0.5f - z * t * k, 0.5f + 0.5f * x, 0.5f + 0.5f * y, 0.5f + 0.5f * z);
        return voxelShape;
    }


    public static VoxelShape getClosestShape(Vec3d start, Vec3d end, BlockPos pos, BlockState currentState, IBlockReader blockReader) {

        VoxelShape temp = VoxelShapes.empty();
        double tempDistance = Double.MAX_VALUE;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    VoxelShape shape;
                    if (i == 0 && j == 0 && k == 0) {
                        if (currentState.getBlock() instanceof BranchBlock) {
                            shape = ((BranchBlock) currentState.getBlock()).ageToVoxelShapes.computeIfAbsent(currentState.get(ModBlockProperties.TREE_AGE), age -> BranchVoxelShapes.getVoxelShapeForBranch((BranchBlock) currentState.getBlock(), age));
                        } else {
                            shape = currentState.getShape(blockReader, pos);
                        }
                    } else {
                        BlockPos otherPos = pos.add(i, j, k);
                        BlockState otherState = blockReader.getBlockState(otherPos);
                        Block otherBlock = otherState.getBlock();
                        if (otherBlock instanceof BranchBlock && Util.compareVec3iToInts(((BranchBlock) otherBlock).getSourceOffset(), -i, -j, -k)) {
                            BranchBlock branchBlock = (BranchBlock) otherBlock;
                            shape = TreeBlocks.getBranchBlock(new Vec3i(i, j, k), branchBlock.isEnd(), branchBlock.getSpecies())
                                    .ageToVoxelShapes.computeIfAbsent(otherState.get(ModBlockProperties.TREE_AGE), age -> getOppositeVoxelShapeForBranch(branchBlock, age));
                        } else {
                            continue;
                        }
                    }
                    BlockRayTraceResult result = shape.rayTrace(start, end, pos);
                    double distance = result == null ? Double.MAX_VALUE : start.squareDistanceTo(result.getHitVec());
                    if (distance < tempDistance) {
                        temp = shape;
                        tempDistance = distance;
                    }
                }
            }
        }

        return temp;
    }
}

