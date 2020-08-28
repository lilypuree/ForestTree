/**
 * Code based on https://github.com/JTK222/DRP-MARG
 */
package lilypuree.forest_tree.core.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;

import java.util.concurrent.CompletableFuture;

public class ClientConstruction {

    public static void run() {
        if (Minecraft.getInstance() == null) return;
        Minecraft.getInstance().getResourcePackList().addPackFinder(new ForestTreePackFinder(BranchTextureStitcher.RESOURCE_PACK_FOLDER));
        if (Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
                    (stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
                        CompletableFuture branchStitcher = CompletableFuture.supplyAsync(BranchTextureStitcher::new)
                                .thenApplyAsync(BranchTextureStitcher::prepare)
                                .thenAcceptAsync(BranchTextureStitcher::generate, backgroundExecutor);
                        return branchStitcher.thenCompose(stage::markCompleteAwaitingOthers);
                    });
        }
    }
}
