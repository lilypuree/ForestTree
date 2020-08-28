package lilypuree.forest_tree.common.trees.block;

import lilypuree.forest_tree.api.genera.WoodCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class StumpBlock extends Block {
    public static final IntegerProperty THICKNESS = ModBlockProperties.THICKNESS;
    public static final BooleanProperty STUMP = ModBlockProperties.STUMP;
    private WoodCategory woodCategory;
    private Map<Integer, VoxelShape> voxelShapes = new HashMap<>();

    public StumpBlock(Properties properties, WoodCategory categoryIn) {
        super(properties);
        this.woodCategory = categoryIn;
        this.setDefaultState(this.getStateContainer().getBaseState().with(THICKNESS, 1).with(STUMP, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(THICKNESS, STUMP);
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
        return voxelShapes.computeIfAbsent(state.get(THICKNESS), division -> BranchVoxelShapes.getVoxelShapeForStump(this, state.get(THICKNESS)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getCollisionShape(state, worldIn, pos, context);
    }

    public WoodCategory getWoodCategory() {
        return woodCategory;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
