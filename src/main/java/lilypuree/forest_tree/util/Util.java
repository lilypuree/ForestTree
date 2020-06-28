package lilypuree.forest_tree.util;

import net.minecraft.util.math.Vec3i;

public class Util {
    public static boolean compareVec3iToInts(Vec3i vec, int x, int y, int z) {
        return vec.getX() == x && vec.getY() == y && vec.getZ() == z;
    }
}
