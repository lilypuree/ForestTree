package lilypuree.forest_tree.trees.client.util;

import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class QuaternionHelper {
    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final float SQRT_3 = (float) Math.sqrt(3);


    //TODO
    //add some documentation
    //refactor this stuff
    //use the enums in treegen?
    //search what Quaternions do..? do they handle the normalization?
    public static Quaternion getQuaternionFromSourceOffset(Vec3i sourceOffset) {
        int x = sourceOffset.getX();
        int y = sourceOffset.getY();
        int z = sourceOffset.getZ();
        Vector3f axis = new Vector3f(0, 1, 0);

        float len = (float) Math.sqrt(x * x + y * y + z * z);
        Vector3f normal = new Vector3f(x / len, y / len, z / len);
        axis.cross(normal);
        axis.normalize();
        if (len > 1.6) {
            float angle = (float) ((y > 0) ? Math.asin(SQRT_2 / SQRT_3) : Math.PI - Math.asin(SQRT_2 / SQRT_3));
            Quaternion planeRotation = new Quaternion(axis, angle, false);
            Quaternion rotation = new Quaternion(new Vector3f(0, 1, 0), 45, true);
            planeRotation.multiply(rotation);
            return planeRotation;
        } else {
            float angle = (90 - y * 45) * (float) Math.PI / 180;
            return new Quaternion(axis, angle, false);
        }
    }




}
