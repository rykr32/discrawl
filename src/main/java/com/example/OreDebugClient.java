package net.oredebug;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class OreDebugClient implements ClientModInitializer {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Set<BlockHighlight> ores = new HashSet<>();
    private static boolean xrayEnabled = false;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = new KeyBinding("key.oredebug.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "key.categories.misc");
        private static KeyBinding openGuiKey;
        openGuiKey = KeyBindingHelper.registerKeyBinding(
    new KeyBinding("key.oredebug.opengui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_INSERT, "category.oredebug")
   ConfigManager.load();
   Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save));
   );

        ores.add(new BlockHighlight(Blocks.DIAMOND_ORE, 0x00FFFF));
        ores.add(new BlockHighlight(Blocks.IRON_ORE, 0xFFA500));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
        xrayEnabled = !xrayEnabled;
        System.out.println("X-Ray " + (xrayEnabled ? "enabled" : "disabled"));
        while (openGuiKey.wasPressed()) {
           client.setScreen(new OreDebugScreen());
    }

    }

    // Skanuj tylko, gdy X-Ray aktywny
    if (xrayEnabled && client.player != null && client.world != null) {
        BlockFinder.scanNearbyBlocks(scanRange); // bezpieczny zasięg 24 bloki
    }
});
    }

    public static boolean isXrayEnabled() {
        return xrayEnabled;
    }

    public static Set<BlockHighlight> getOres() {
        return ores;
    }
}
    public static int scanRange = 24; // domyślny zasięg
