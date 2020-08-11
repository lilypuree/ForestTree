package lilypuree.forest_tree.trees.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class InvisibleBranchSource extends Block {
    public InvisibleBranchSource(){
        super(Block.Properties.create(Material.AIR).notSolid().doesNotBlockMovement());
    }

    @Override
    public boolean isAir(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Entity looker = context.getEntity();
        if (looker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) looker;
            Vec3d start = player.getEyePosition(0);
            Vec3d look = player.getLook(0);
            Vec3d end = start.add(look.scale(6));
            return BranchVoxelShapes.getClosestShape(start, end, pos, state, worldIn);
        }
        return VoxelShapes.empty();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

}
