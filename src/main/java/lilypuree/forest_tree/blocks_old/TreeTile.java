//package lilypuree.forest_tree.blocks_old;
//
//import com.google.common.primitives.Bytes;
//import lilypuree.forest_tree.blocks_old.properties.BranchType;
//import lilypuree.forest_tree.blocks_old.properties.ForestTreeProperties;
//import lilypuree.forest_tree.datagen.types.ThicknessTypes;
//import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
//import lilypuree.forest_tree.datagen.types.WoodTypes;
//import lilypuree.forest_tree.Registration;
//import net.minecraft.block.*;
//import net.minecraft.entity.item.ItemEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.state.properties.BlockStateProperties;
//import net.minecraft.state.properties.Half;
//import net.minecraft.state.properties.SlabType;
//import net.minecraft.state.properties.StairsShape;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Rotation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.client.model.data.IModelData;
//
//import javax.annotation.Nonnull;
//import java.util.*;
//
//public class TreeTile extends TileEntity {
//    private final static int DEFAULT_HEALTH = 8;
//    private WoodTypes woodType;
//    private ThicknessTypes thicknessType;
//    private TreeData treeData;
//    private ITextComponent name;
//    private int health;
//
//    public TreeTile() {
//        super(Registration.TREE_TILE.get());
//        this.health = DEFAULT_HEALTH;
//    }
//
//    public boolean hasTreeData() {
//        return treeData != null;
//    }
//
//    public void init() {
//        this.treeData = new TreeData(pos, world);
//    }
//
//    public void registerTree() {
//        if (!hasTreeData()) init();
//        this.treeData.register(pos, world);
//        System.out.println("registered tree");
//    }
//
//    public void clearTree() {
//        if (hasTreeData())
//            this.treeData.clear();
//    }
//
//    public boolean attemptChopDown() {
//        markDirty();
//        if (health-- > 0) {
//            return false;
//        }
//        chopDownTree();
//        return true;
//    }
//
//    public boolean attemptUproot(Direction dir) {
//        markDirty();
//        if (health-- > 0) {
//            return false;
//        }
//        uprootTree(dir);
//        return true;
//    }
//
//    public void chopDownTree() {
//        if (this.world.isRemote() || !hasTreeData()) return;
//        for (Vec3i offset : this.treeData.getTreePositions()) {
//            if(offset.equals(Vec3i.NULL_VECTOR)) continue;
//            BlockPos treePos = this.getPos().add(offset);
//            Block block = world.getBlockState(treePos).getBlock();
//            if (isTreeBlock(block) || isLeafBlock(block)) {
//                world.destroyBlock(treePos, true);
//            }
//        }
//        System.out.println("Chopped tree");
//    }
//
//    public void uprootTree(Direction dir) {
//        if (this.world.isRemote() || !hasTreeData()) return;
//        this.treeData.setFacing(dir);
//        world.destroyBlock(pos.down(), false);
//
//        ItemEntity treeItemEntity = new ItemEntity(this.world, pos.getX(), pos.getY(), pos.getZ(), getTreeItem());
//        this.world.addEntity(treeItemEntity);
//
//        for (Vec3i offset : this.treeData.getTreePositions()) {
//            BlockPos treePos = pos.add(offset);
//            Block blockInTreePos = world.getBlockState(treePos).getBlock();
//            if (isTreeBlock(blockInTreePos) || isLeafBlock(blockInTreePos)) {
//                world.setBlockState(treePos, Blocks.AIR.getDefaultState());
//            }
//        }
//    }
//
//    public ItemStack getTreeItem() {
//        ItemStack treeItem = new ItemStack(Registration.getTreeItem(woodType, thicknessType)).setDisplayName(name != null ? name : new TranslationTextComponent("forest_tree:names/tonegawa"));
//        treeItem.setTag(treeData.serialize());
//        return treeItem;
//    }
//
//    public void replantTree(Direction dir) {
//        if (!world.isRemote() && hasTreeData()) {
//            TreeData newTreeData = new TreeData(pos, world);
//            Direction originalFacing = this.treeData.getFacing();
//            for (Vec3i offset : this.treeData.getTreePositions()) {
//                if (offset.compareTo(Vec3i.NULL_VECTOR) == 0) continue; //should keep this stump from resetting.
//
//                Vec3i newOffset = rotateWithDirection(offset, originalFacing, dir);
//                BlockPos treePos = pos.add(newOffset);
//                Rotation rotation = Rotation.NONE;
//                if (dir == originalFacing.getOpposite()) rotation = Rotation.CLOCKWISE_180;
//                else if (dir == originalFacing.rotateY()) rotation = Rotation.CLOCKWISE_90;
//                else if (dir == originalFacing.rotateYCCW()) rotation = Rotation.COUNTERCLOCKWISE_90;
//                if (isLeafBlock(world.getBlockState(treePos).getBlock())) world.destroyBlock(treePos, true);
//                BlockState state = this.treeData.getBlockStateOf(offset).rotate(rotation);
//
//                newTreeData.add(newOffset, state);
//                world.setBlockState(treePos, state);
//                world.notifyBlockUpdate(treePos, state, state, 1 | 2);
//            }
//            this.treeData = newTreeData;
//        }
//    }
//
//    @Nonnull
//    @Override
//    public IModelData getModelData() {
//        return null;
//    }
//
//    @Override
//    public void read(CompoundNBT compound) {
//        super.read(compound);
//        if (!hasTreeData()) init();
//        if (compound.contains("tree_data", 10)) {
//            this.treeData.deserialize(compound.getCompound("tree_data"));
//        }
//        if (compound.contains("health", 3)) {
//            this.health = compound.getInt("health");
//        }
//        if (compound.contains("name", 8)) {
//            this.name = new TranslationTextComponent(compound.getString("name"));
//        }
//        this.woodType = WoodTypes.values()[compound.getInt("wood")];
//        this.thicknessType = ThicknessTypes.values()[compound.getInt("thickness")];
//    }
//
//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        compound.putInt("health", health);
//        if (hasTreeData()) {
//            compound.put("tree_data", this.treeData.serialize());
//        }
//        if (this.name != null) {
//            compound.putString("name", ITextComponent.Serializer.toJson(this.name));
//        }
//        if (thicknessType != null) {
//            compound.putInt("thickness", thicknessType.ordinal());
//        }
//        if (woodType != null) {
//            compound.putInt("wood", woodType.ordinal());
//        }
//        return super.write(compound);
//    }
//
//    public void setWood(WoodTypes wood) {
//        this.woodType = wood;
//    }
//
//    public void setThickness(ThicknessTypes thickness) {
//        this.thicknessType = thickness;
//        health = (int) thickness.getWidth() * 2;
//    }
//
//    public void setName(ITextComponent textComponent) {
//        this.name = textComponent;
//    }
//
//    public void setTreeData(CompoundNBT tag) {
//        if (!hasTreeData()) init();
//        treeData.deserialize(tag);
//    }
//
//    private Vec3i rotateWithDirection(Vec3i in, Direction dirA, Direction dirB) {
//        if (dirA == dirB) return in;
//        else if (dirA.getOpposite() == dirB) return new Vec3i(-in.getX(), in.getY(), -in.getZ());
//        else if (dirA.rotateY() == dirB) return new Vec3i(-in.getZ(), in.getY(), in.getX());
//        else return new Vec3i(in.getZ(), in.getY(), -in.getX());
//    }
//
//
//    public static boolean isLeafBlock(Block blockIn) {
//        return blockIn instanceof ILeafBlock || blockIn instanceof LeavesBlock;
//    }
//
//    public static boolean isTreeBlock(Block blockIn) {
//        return blockIn instanceof ITreeBlock || blockIn instanceof LogBlock;
//    }
//
//
//    //Inner Class
//    public static class TreeData {
//        private static final int MAX_SIZE = 5000;
//        private Map<Vec3i, BlockState> treeBlocks;
//        private Direction facing;
//
//        public TreeData(BlockPos rootPos, World worldIn) {
//            treeBlocks = new HashMap<>();
//            facing = Direction.NORTH;
//        }
//
//        public void add(Vec3i pos, BlockState state){
//            treeBlocks.put(pos, state);
//        }
//
//        public void register(BlockPos rootPos, World worldIn) {
//            treeBlocks = search(rootPos, worldIn);
//        }
//
//        public Set<Vec3i> getTreePositions() {
//            return treeBlocks.keySet();
//        }
//
//        public BlockState getBlockStateOf(Vec3i pos) {
//            return treeBlocks.get(pos);
//        }
//
//        public Direction getFacing() {
//            return this.facing;
//        }
//
//        public void setFacing(Direction dirIn) {
//            this.facing = dirIn;
//        }
//
//        public void clear() {
//            this.treeBlocks.clear();
//        }
//
//        //[vec x][vec y][vec z], [Wood Type/Block Type,State]
//
//        private static int serializeBlockState(BlockState block) {
//            Block blockClass = block.getBlock();
//            String[] name = splitRegistryName(blockClass);
//            int i = 0;
//            if (blockClass instanceof TreeBranch) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(ForestTreeProperties.LEFT_BRANCH) ? 1 : 0) << 1;
//                i = i | (block.get(ForestTreeProperties.RIGHT_BRANCH) ? 1 : 0) << 2;
//                i = i | (block.get(ForestTreeProperties.MAIN_BRANCH) ? 1 : 0) << 3;
//                i = i | (block.get(ForestTreeProperties.CONNECTION)).ordinal() << 4;
//                i = i | (block.get(BlockStateProperties.HORIZONTAL_FACING)).ordinal() << 6;
////                i = i | (block.get(ForestTreeProperties.LEAVES_SLAB_TYPE)).ordinal() << 10;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | (ThicknessTypes.withName(name[1])).ordinal() << 15;
//                i = i | (TreeBlockTypes.withName(name[2])).ordinal() << 17;
//                return i;
//            } else if (blockClass instanceof TreeTrunk) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(BlockStateProperties.UP) ? 1 : 0) << 1;
//                i = i | (block.get(ForestTreeProperties.NORTH_CONNECTION)).ordinal() << 2;
//                i = i | (block.get(ForestTreeProperties.EAST_CONNECTION)).ordinal() << 4;
//                i = i | (block.get(ForestTreeProperties.SOUTH_CONNECTION)).ordinal() << 6;
//                i = i | (block.get(ForestTreeProperties.WEST_CONNECTION)).ordinal() << 8;
////                i = i | (block.get(ForestTreeProperties.LEAVES_SLAB_TYPE)).ordinal() << 10;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | (ThicknessTypes.withName(name[1])).ordinal() << 15;
//                i = i | (TreeBlockTypes.withName(name[2])).ordinal() << 17;
//                return i;
//            } else if (blockClass instanceof TreeStump) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(ForestTreeProperties.STUMP) ? 1 : 0) << 1;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | (ThicknessTypes.withName(name[1])).ordinal() << 15;
//                i = i | (TreeBlockTypes.withName(name[2])).ordinal() << 17;
//                return i;
//            } else if (blockClass instanceof LogBlock) {
////                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(BlockStateProperties.AXIS)).ordinal() << 1;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | 3 << 15;
//                i = i | 3 << 17;
//                return i;
//            } else if (blockClass instanceof LeavesSlabBlock) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(BlockStateProperties.PERSISTENT) ? 1 : 0) << 1;
//                i = i | (block.get(BlockStateProperties.DISTANCE_1_7)).byteValue() << 2;
//                i = i | (block.get(BlockStateProperties.SLAB_TYPE)).ordinal() << 5;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | 1 << 15;
//                i = i | 4 << 17;
//                return i;
//            } else if (blockClass instanceof LeavesStairsBlock) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
////                i = i | (block.get(BlockStateProperties.PERSISTENT) ? 1 : 0) <<1;
////                i = i | (block.get(BlockStateProperties.DISTANCE_1_7)).byteValue() <<2;
//                i = i | (block.get(BlockStateProperties.HALF)).ordinal() << 5;
//                i = i | (block.get(BlockStateProperties.STAIRS_SHAPE)).ordinal() << 6;
//                i = i | (block.get(BlockStateProperties.HORIZONTAL_FACING)).ordinal() << 9;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | 2 << 15;
//                i = i | 4 << 17;
//                return i;
//            } else if (blockClass instanceof LeavesTrapDoorBlock) {
//                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
////                i = i | (block.get(BlockStateProperties.PERSISTENT) ? 1 : 0) <<1;
////                i = i | (block.get(BlockStateProperties.DISTANCE_1_7)).byteValue() <<2;
//                i = i | (block.get(BlockStateProperties.HALF)).ordinal() << 5;
//                i = i | (block.get(BlockStateProperties.OPEN) ? 1 : 0) << 6;
//                i = i | (block.get(BlockStateProperties.POWERED) ? 1 : 0) << 7;
//                i = i | (block.get(BlockStateProperties.HORIZONTAL_FACING)).ordinal() << 8;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | 3 << 15;
//                i = i | 4 << 17;
//                return i;
//            } else if (blockClass instanceof LeavesBlock) {
////                i = i | (block.get(BlockStateProperties.WATERLOGGED) ? 1 : 0);
//                i = i | (block.get(BlockStateProperties.PERSISTENT) ? 1 : 0) << 1;
//                i = i | (block.get(BlockStateProperties.DISTANCE_1_7)).byteValue() << 2;
//                i = i | (WoodTypes.withName(name[0])).ordinal() << 12;
//                i = i | 0 << 15;
//                i = i | 4 << 17;
//                return i;
//            }
//            return -1;
//        }
//
//        private static String[] splitRegistryName(Block block) {
//            String[] name = block.getRegistryName().getPath().split("_");
//            if (name.length > 3) {
//                name[1] = name[2];
//                name[2] = name[3];
//            }
//            return name;
//        }
//
//        private static BlockState deserializeBlockState(final int i) {
//            if (i < 0) return Blocks.AIR.getDefaultState();
//            int type = (i >> 17) & 7;
//            WoodTypes wood = WoodTypes.values()[i >> 12 & 7];
//            if (type < 3) {
//                ThicknessTypes thickness = ThicknessTypes.values()[i >> 15 & 3];
//                if (type == 0) { //trunk
//                    return Registration.getTreeBlock(wood, thickness, TreeBlockTypes.TRUNK).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, ((i & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.UP, ((i >> 1 & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.NORTH_CONNECTION, BranchType.values()[i >> 2 & 3])
//                            .with(ForestTreeProperties.EAST_CONNECTION, BranchType.values()[i >> 4 & 3])
//                            .with(ForestTreeProperties.SOUTH_CONNECTION, BranchType.values()[i >> 6 & 3])
//                            .with(ForestTreeProperties.WEST_CONNECTION, BranchType.values()[i >> 8 & 3]);
////                            .with(ForestTreeProperties.LEAVES_SLAB_TYPE, LeafSlabType.values()[i >> 10 & 3]);
//                }
//                if (type == 1) { //branch
//                    return Registration.getTreeBlock(wood, thickness, TreeBlockTypes.BRANCH).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, ((i & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.LEFT_BRANCH, ((i >> 1 & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.RIGHT_BRANCH, ((i >> 2 & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.MAIN_BRANCH, ((i >> 3 & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.CONNECTION, BranchType.values()[i >> 4 & 3])
//                            .with(BlockStateProperties.HORIZONTAL_FACING, Direction.values()[i >> 6 & 7]);
////                            .with(ForestTreeProperties.LEAVES_SLAB_TYPE, LeafSlabType.values()[i >> 10 & 3]);
//                }
//                if (type == 2) { //stump
//                    return Registration.getTreeBlock(wood, thickness, TreeBlockTypes.STUMP).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, ((i & 1) == 0) ? Boolean.FALSE : Boolean.TRUE)
//                            .with(ForestTreeProperties.STUMP, ((i >> 1 & 1) == 0) ? Boolean.FALSE : Boolean.TRUE);
//                }
//            } else if (type == 3) {
//                Direction.Axis axis = Direction.Axis.values()[i >> 1 & 3];
//                switch (wood) {
//                    case OAK:
//                        return Blocks.OAK_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    case BIRCH:
//                        return Blocks.BIRCH_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    case SPRUCE:
//                        return Blocks.SPRUCE_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    case ACACIA:
//                        return Blocks.ACACIA_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    case JUNGLE:
//                        return Blocks.JUNGLE_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    case DARK_OAK:
//                        return Blocks.DARK_OAK_LOG.getDefaultState().with(BlockStateProperties.AXIS, axis);
//                    default:
//                        return Blocks.AIR.getDefaultState();
//                }
//            } else if (type == 4) {
//                int leafType = i >> 15 & 3;
//                if (leafType == 0) { //Leaves
//                    Boolean persistent = (i >> 1 & 1) == 0 ? Boolean.FALSE : Boolean.TRUE;
//                    Integer distance_1_7 = (i >> 2 & 7);
//                    switch (wood) {
//                        case OAK:
//                            return Blocks.OAK_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        case BIRCH:
//                            return Blocks.BIRCH_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        case SPRUCE:
//                            return Blocks.SPRUCE_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        case ACACIA:
//                            return Blocks.ACACIA_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        case JUNGLE:
//                            return Blocks.JUNGLE_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        case DARK_OAK:
//                            return Blocks.DARK_OAK_LEAVES.getDefaultState().with(BlockStateProperties.PERSISTENT, persistent).with(BlockStateProperties.DISTANCE_1_7, distance_1_7);
//                        default:
//                            return Blocks.AIR.getDefaultState();
//
//                    }
//                } else if (leafType == 1) { //leaf_slab
//                    return Registration.getLeafBlock(wood, leafType).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, (i & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.PERSISTENT, (i >> 1 & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.DISTANCE_1_7, (i >> 2 & 7))
//                            .with(BlockStateProperties.SLAB_TYPE, SlabType.values()[i >> 5 & 3]);
//                } else if (leafType == 2) {//leaf stairs
//                    return Registration.getLeafBlock(wood, leafType).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, (i & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.HALF, Half.values()[i >> 5 & 1])
//                            .with(BlockStateProperties.STAIRS_SHAPE, StairsShape.values()[i >> 6 & 7])
//                            .with(BlockStateProperties.HORIZONTAL_FACING, Direction.values()[i >> 9 & 7]);
//                } else if (leafType == 3) {
//                    return Registration.getLeafBlock(wood, leafType).getDefaultState()
//                            .with(BlockStateProperties.WATERLOGGED, (i & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.HALF, Half.values()[i >> 5 & 1])
//                            .with(BlockStateProperties.OPEN, (i >> 6 & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.POWERED, (i >> 7 & 1) == 0 ? Boolean.FALSE : Boolean.TRUE)
//                            .with(BlockStateProperties.HORIZONTAL_FACING, Direction.values()[i >> 8 & 7]);
//
//                }
//            }
//            return Blocks.AIR.getDefaultState();
//        }
//
//        public CompoundNBT serialize() {
//            List<Byte> vectorList = new ArrayList<>();
//            List<Integer> blockList = new ArrayList<>();
//            for (Map.Entry<Vec3i, BlockState> entry : treeBlocks.entrySet()) {
//                Vec3i key = entry.getKey();
//                vectorList.add((byte) key.getX());
//                vectorList.add((byte) key.getY());
//                vectorList.add((byte) key.getZ());
//                blockList.add(serializeBlockState(entry.getValue()));
//            }
//            CompoundNBT tag = new CompoundNBT();
//            tag.putByteArray("vectors", Bytes.toArray(vectorList));
//            tag.putIntArray("blocks", blockList);
//            tag.putInt("facing", this.facing.getIndex());
//            return tag;
//        }
//
//        public void deserialize(CompoundNBT tag) {
//            Map<Vec3i, BlockState> treeBlocksMap = new HashMap<>();
//            if (tag.contains("vectors") && tag.contains("blocks")) {
//                byte[] vectors = tag.getByteArray("vectors");
//                int[] blocks = tag.getIntArray("blocks");
//                for (int i = 0; i < blocks.length; i++) {
//                    Vec3i vec = new Vec3i(vectors[3 * i], vectors[3 * i + 1], vectors[3 * i + 2]);
//                    treeBlocksMap.put(vec, deserializeBlockState(blocks[i]));
//                }
//            }
//            this.treeBlocks = treeBlocksMap;
//            if (tag.contains("facing")) {
//                this.facing = Direction.byIndex(tag.getInt("facing"));
//            }
//        }
//
//
//        //BFS search for tree blocks.
//        //Vec3i should be less than 255, 255, 255. I hope nobody builds a tree that big.
//        private Map<Vec3i, BlockState> search(BlockPos rootPos, World worldIn) {
//            int MAX_LEAF_DISTANCE = 2;                   //vanilla
//            Set<Long> visited = new HashSet<>();
//            LinkedList<BlockPos> queue = new LinkedList<>();
//            Map<Vec3i, BlockState> treeBlockMap = new HashMap<>();
//            Map<Vec3i, Integer> leafDistanceMap = new HashMap<>();
//
//            visited.add(rootPos.toLong());
//            queue.add(rootPos);
//
//            //first search for trunks and branches
//            while (queue.size() != 0) {
//                BlockPos currentBlockPos = queue.poll();
//                treeBlockMap.put(currentBlockPos.subtract(rootPos), worldIn.getBlockState(currentBlockPos));
//
//                for (Direction dir : Direction.values()) {
//                    BlockPos nextBlockPos = currentBlockPos.offset(dir);
//                    Block nextBlock = worldIn.getBlockState(nextBlockPos).getBlock();
//                    if (!isLeafBlock(nextBlock) && !visited.contains(nextBlockPos.toLong())) {      //leave out leaves. :P
//                        visited.add(nextBlockPos.toLong());
//                        if (isTreeBlock(nextBlock)) {
//                            queue.add(nextBlockPos);                                                //only put tree blocks to map
//                        }
//                    }
//                }
//            }
//            //then add all trunks to queue, and search for leaves with distance factor
//
//            for (Vec3i trunkPos : treeBlockMap.keySet()) {
//                queue.add(rootPos.add(trunkPos));
//            }
//            while (queue.size() != 0) {
//                BlockPos currentBlockPos = queue.poll();
//                Vec3i currentRelativePos = currentBlockPos.subtract(rootPos);
//                if (isLeafBlock(worldIn.getBlockState(currentBlockPos).getBlock())) {             //if the current block is a leaf, put it in map
//                    leafDistanceMap.putIfAbsent(currentRelativePos, 1);                          //this is when the leaf is the first block next to a branch
//                    treeBlockMap.put(currentRelativePos, worldIn.getBlockState(currentBlockPos));
//                }
//                for (Direction dir : Direction.values()) {                                       //for all 6 directions
//                    BlockPos nextBlockPos = currentBlockPos.offset(dir);
//                    if (!visited.contains(nextBlockPos.toLong())) {                               //we already visited treeblocks and non-treeblocks above.
//                        visited.add(nextBlockPos.toLong());
//                        Block nextBlock = worldIn.getBlockState(nextBlockPos).getBlock();
//                        if (isLeafBlock(nextBlock)) {                                             //probably don't have to check this.
//                            int distance = leafDistanceMap.getOrDefault(currentRelativePos, 0) + 1;
//                            if (distance <= MAX_LEAF_DISTANCE) {
//                                leafDistanceMap.put(nextBlockPos.subtract(rootPos), distance);
//                                queue.add(nextBlockPos);                                        //only add to queue if it is a block within a certain distance from a treeblock.
//                            }
//                        }
//                    }
//                }
//            }
//            return treeBlockMap;
//        }
//
//
//    }
//}
