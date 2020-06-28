package lilypuree.forest_tree.trees.block;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import javax.annotation.Nonnull;

public class BranchVoxelShapes {

    private static VoxelShape[][][] voxelShapes = new VoxelShape[3][3][3];
    private static VoxelShape[][][] halfVoxelShapes = new VoxelShape[3][3][3];

    static {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0) continue;
                    voxelShapes[i + 1][j + 1][k + 1] = (i * j * k == 0) ? createStraightVoxelshape(i, j, k, 0.2f) : createDiagonalVoxelshape(i, j, k, 0.2f);
                    halfVoxelShapes[i + 1][j + 1][k + 1] = (i * j * k == 0) ? createHalfStraightVoxelshape(i, j, k, 0.2f) : createHalfDiagonalVoxelshape(i, j, k, 0.2f);
                }
            }
        }
    }

    public static VoxelShape getVoxelShapeForDirection(int x, int y, int z) {
        return voxelShapes[x + 1][y + 1][z + 1];
    }

    public static VoxelShape getVoxelShapeForDirection(Vec3i dir) {
        return getVoxelShapeForDirection(dir.getX(), dir.getY(), dir.getZ());
    }

    public static VoxelShape getHalfShapeForDirection(Vec3i dir) {
        return getHalfShapeForDirection(dir.getX(), dir.getY(), dir.getZ());
    }

    private static VoxelShape getHalfShapeForDirection(int x, int y, int z) {
        return halfVoxelShapes[x+1][y+1][z+1];
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
        VoxelShape voxelShape1 = VoxelShapes.create(0.5f - x * t * k, 0.5f - y * t * k, 0.5f - z * t * k, 0.5f + 0.5f * x, 0.5f + 0.5f * y, 0.5f + 0.5f * z);
        return voxelShape1;
    }


}

