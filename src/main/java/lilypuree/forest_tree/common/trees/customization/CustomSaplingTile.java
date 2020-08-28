package lilypuree.forest_tree.common.trees.customization;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.common.world.trees.gen.feature.TreeGenerator;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomSaplingTile extends TileEntity implements INameable {

    private TreeGenerator treeGenerator;
    private ITextComponent customName;

    public CustomSaplingTile() {
        super(Registration.CUSTOM_SAPLING_TILE.get());
    }

    public TreeGenerator getTreeGenerator() {
        return treeGenerator;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("CustomName", 8)) {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }
//        if (treeGenerator == null) treeGenerator = new TreeGenerator(ModSpecies.PINE);
        treeGenerator.loadFromNbt(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.customName != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }
        return this.saveToNbt(compound);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        if(treeGenerator == null) return compound;
        return treeGenerator.saveToNbt(compound);
    }

    @Override
    public ITextComponent getName() {
        return this.customName != null ? this.customName : new TranslationTextComponent("block.customSapling");
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    public void setCustomName(ITextComponent name) {
        this.customName = name;
    }

}
