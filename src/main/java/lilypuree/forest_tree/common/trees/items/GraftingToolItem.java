package lilypuree.forest_tree.common.trees.items;

import lilypuree.forest_tree.api.genera.WoodCategory;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.common.trees.block.BranchBlock;
import lilypuree.forest_tree.api.genera.Species;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class GraftingToolItem extends Item {
    public GraftingToolItem(Item.Properties builder) {
        super(builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();

        if (world.isRemote) return ActionResultType.SUCCESS;

        BlockPos pos = context.getPos();
        BlockState block = world.getBlockState(pos);
        Vec3d hitvec = context.getHitVec();

        if (playerMatchesCycleCondition(player) && block.getBlock() instanceof BranchBlock) {
            Vec3i offset = getOffset(player.getLookVec());
            BlockPos newPos = pos.add(offset);
            Vec3i dir = new Vec3i(-offset.getX(), -offset.getY(), -offset.getZ());
            WoodCategory wood = ((BranchBlock) block.getBlock()).getWoodCategory();
            Block branch = player.isSneaking() ? TreeBlockRegistry.getBranchEndBlock(dir, wood) : TreeBlockRegistry.getBranchBlock(dir, wood);
            BlockState branchState = branch.getDefaultState();
            world.setBlockState(newPos, branchState);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }

    private Vec3i getOffset(Vec3d lookVec) {
//        System.out.println(hitVec);
//        System.out.println(pos);
//        Vec3d blockOffset = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ()).normalize();
//        System.out.println(blockOffset);
        int x = Math.abs(lookVec.x) < 0.4 ? 0 : ((lookVec.x > 0) ? 1 : -1);
        int y = Math.abs(lookVec.y) < 0.4 ? 0 : ((lookVec.y > 0) ? 1 : -1);
        int z = Math.abs(lookVec.z) < 0.4 ? 0 : ((lookVec.z > 0) ? 1 : -1);
        if (x == 0 && y == 0 && z == 0) y += 1;
        return new Vec3i(-x, -y, -z);
    }

    private void cycle(World worldIn, BlockPos pos, BlockState blockIn) {


    }

    private boolean playerMatchesCycleCondition(PlayerEntity player) {
        return player != null && player.isCreative();
    }
}
