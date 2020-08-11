package lilypuree.forest_tree.mixin;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.block.BranchBlock;
import lilypuree.forest_tree.trees.block.BranchVoxelShapes;
import lilypuree.forest_tree.trees.block.ModBlockProperties;
import lilypuree.forest_tree.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public World world;

    @Shadow
    public abstract Vec3d getEyePosition(float partialTicks);

    @Shadow
    public abstract Vec3d getLook(float partialTicks);

    @Shadow
    @Final
    protected static Logger LOGGER;


    //raytrace inside block to return the correct blockraytrace result with respective blockpos
    @Inject(method = "pick", at = @At("RETURN"), cancellable = true)
    protected void onPick(double distance, float partialTicks, boolean raytraceFluids, CallbackInfoReturnable<RayTraceResult> info) {
        RayTraceResult result = info.getReturnValue();
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
            Block block = this.world.getBlockState(blockResult.getPos()).getBlock();
            if (block instanceof BranchBlock) {
                Vec3d eyePos = this.getEyePosition(partialTicks);
                Vec3d look = this.getLook(partialTicks);
                Vec3d target = eyePos.add(look.scale(distance));
                BlockRayTraceResult newResult = raytraceInBranch(eyePos, target, blockResult.getPos(), world, ((BranchBlock) block).getSourceOffset());
                info.setReturnValue(newResult);
            }
        }
    }

    private BlockRayTraceResult raytraceInBranch(Vec3d start, Vec3d end, BlockPos pos, IBlockReader blockReader, Vec3i sourceOffset) {
        BlockRayTraceResult temp = null;
        double tempDistance = Double.MAX_VALUE;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    VoxelShape shape;
                    BlockRayTraceResult result;
                    BlockPos newPos = pos.add(i, j, k);
                    BlockState otherState = blockReader.getBlockState(newPos);
                    Block otherBlock = otherState.getBlock();

                    if (i == 0 && j == 0 && k == 0) {
                        if (otherBlock instanceof BranchBlock) {
                            shape = otherState.getShape(world, newPos);
                            result = shape.rayTrace(start, end, pos);
                        } else {
                            result = null;
                        }
                    } else {
                        if (otherBlock instanceof BranchBlock && Util.compareVec3iToInts(((BranchBlock) otherBlock).getSourceOffset(), -i, -j, -k)) {
                            shape = otherState.getShape(world, newPos);
                            result = shape.rayTrace(start, end, newPos);
                        } else {
                            continue;
                        }
                    }
                    double distance = result == null ? Double.MAX_VALUE : start.squareDistanceTo(result.getHitVec());
                    if (distance < tempDistance) {
                        temp = result;
                        tempDistance = distance;
                    }
                }
            }
        }
        return temp;
    }
}
