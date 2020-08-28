package lilypuree.forest_tree.common.trees.block;

import lilypuree.forest_tree.api.genera.TreeGenus;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
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

public class BranchVoxelShapes {

    private static boolean isStraight(Vec3i v) {
        return v.getX() * v.getY() == 0 && v.getY() * v.getZ() == 0 && v.getZ() * v.getX() == 0;
    }

    public static VoxelShape getVoxelShapeForBranch(BranchBlock block, int division) {
        Vec3i sp = block.getSourceOffset();
        float radius = (float) division / ModBlockProperties.MAX_DIVISIONS / 2;
        return isStraight(sp) ? createStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius) :
                sp.getX() * sp.getY() * sp.getZ() == 0 ? createPlaneDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius) : createSpaceDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius);
    }

    public static VoxelShape getOppositeVoxelShapeForBranch(BranchBlock block, int division) {
        Vec3i sp = block.getSourceOffset();
        sp = new Vec3i(-sp.getX(), -sp.getY(), -sp.getZ());
        float radius = (float) division / ModBlockProperties.MAX_DIVISIONS / 2;
        return isStraight(sp) ? createStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius) :
                sp.getX() * sp.getY() * sp.getZ() == 0 ? createPlaneDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius) : createSpaceDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius);
    }

    public static VoxelShape getVoxelShapeForStump(StumpBlock block, int division) {
        return createHalfStraightVoxelshape(0, -1, 0, (float) division / ModBlockProperties.MAX_DIVISIONS / 2);
    }

    public static VoxelShape getHalfVoxelShapeForBranch(BranchBlock block, int division) {
        Vec3i sp = block.getSourceOffset();
        float radius = (float) division / ModBlockProperties.MAX_DIVISIONS / 2;
        return (sp.getX() * sp.getY() * sp.getZ() == 0) ? createHalfStraightVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius) :
                createHalfDiagonalVoxelshape(sp.getX(), sp.getY(), sp.getZ(), radius);
    }

    private static VoxelShape createStraightVoxelshape(int x, int y, int z, float t) {
        boolean x0 = x == 0;
        boolean y0 = y == 0;
        boolean z0 = z == 0;
        return VoxelShapes.create(0.5f - (x0 ? t : 0), 0.5f - (y0 ? t : 0), 0.5f - (z0 ? t : 0), 0.5f + (x0 ? t : x), 0.5f + (y0 ? t : y), 0.5f + (z0 ? t : z));
    }

    private static VoxelShape createPlaneDiagonalVoxelshape(int x, int y, int z, float t) {
        float k = 0.5f;
        boolean x0 = x == 0;
        boolean y0 = y == 0;
        boolean z0 = z == 0;
        float cenX = (x0 ? 0 : x * 0.5f) / 2;
        float cenY = (y0 ? 0 : y * 0.5f) / 2;
        float cenZ = (z0 ? 0 : z * 0.5f) / 2;
        float xDepth = (x0 ? t : x * k / 2);
        float yDepth = (y0 ? t : y * k / 2);
        float zDepth = (z0 ? t : z * k / 2);
        VoxelShape voxelShape1 = VoxelShapes.create(0.5f + cenX - xDepth, 0.5f + cenY - yDepth, 0.5f + cenZ - zDepth, 0.5f + cenX + xDepth, 0.5f + cenY + yDepth, 0.5f + cenZ + zDepth);
        VoxelShape voxelShape2 = VoxelShapes.create(0.5f + cenX * 2 - xDepth, 0.5f + cenY * 2 - yDepth, 0.5f + cenZ * 2 - zDepth, 0.5f + cenX * 2 + xDepth, 0.5f + cenY * 2 + yDepth, 0.5f + cenZ * 2 + zDepth);
        VoxelShape voxelShape3 = VoxelShapes.create(0.5f + cenX * 3 - xDepth, 0.5f + cenY * 3 - yDepth, 0.5f + cenZ * 3 - zDepth, 0.5f + cenX * 3 + xDepth, 0.5f + cenY * 3 + yDepth, 0.5f + cenZ * 3 + zDepth);
        return VoxelShapes.or(VoxelShapes.or(voxelShape1, voxelShape2), voxelShape3);
    }

    private static VoxelShape createSpaceDiagonalVoxelshape(int x, int y, int z, float t) {
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
                            shape = ((BranchBlock) currentState.getBlock()).voxelShapes.computeIfAbsent(currentState.get(ModBlockProperties.THICKNESS), age -> BranchVoxelShapes.getVoxelShapeForBranch((BranchBlock) currentState.getBlock(), age));
                        } else {
                            shape = currentState.getShape(blockReader, pos);
                        }
                    } else {
                        BlockPos otherPos = pos.add(i, j, k);
                        BlockState otherState = blockReader.getBlockState(otherPos);
                        Block otherBlock = otherState.getBlock();
                        if (otherBlock instanceof BranchBlock && Util.compareVec3iToInts(((BranchBlock) otherBlock).getSourceOffset(), -i, -j, -k)) {
                            BranchBlock branchBlock = (BranchBlock) otherBlock;
                            shape = TreeBlockRegistry.getBranchBlock(new Vec3i(i, j, k), branchBlock.isEnd(), branchBlock.getWoodCategory())
                                    .voxelShapes.computeIfAbsent(otherState.get(ModBlockProperties.THICKNESS), division -> getOppositeVoxelShapeForBranch(branchBlock, division));
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

