package lilypuree.forest_tree.core.setup;

import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;

import java.io.File;
import java.util.Map;

public class ForestTreePackFinder implements IPackFinder {

    private File folder;

    public ForestTreePackFinder(File file) {
        this.folder = file;
    }

    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        if(folder.exists() && folder.isDirectory()) {
            T t = ResourcePackInfo.createResourcePack("Forest Tree Generated Assets", true, () -> new FolderPack(folder), packInfoFactory, ResourcePackInfo.Priority.TOP);
            if (t == null) return;
            nameToPackMap.put("forest_tree:generated", t);
        }
    }
}
