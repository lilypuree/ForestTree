package lilypuree.forest_tree.api.event;

import com.google.gson.internal.$Gson$Preconditions;
import lilypuree.forest_tree.api.genera.FoliageCategory;
import lilypuree.forest_tree.api.genera.WoodCategory;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryRegisterEvent<S> extends Event {
    protected List<S> entries;

    private CategoryRegisterEvent() {
        this.entries = new ArrayList<>();
    }

    public List<S> getEntries() {
        return entries;
    }

    public void addCategory(Class<S> category) {
        this.entries.addAll(Arrays.asList(category.getEnumConstants()));
    }

    public static class Wood extends CategoryRegisterEvent<WoodCategory> {
    }

    public static class Foliage extends CategoryRegisterEvent<FoliageCategory> {
    }
}
