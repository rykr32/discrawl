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
        KeyBindingHelper.registerKeyBinding(toggleKey);

        ores.add(new BlockHighlight(Blocks.DIAMOND_ORE, 0x00FFFF));
        ores.add(new BlockHighlight(Blocks.IRON_ORE, 0xFFA500));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
        xrayEnabled = !xrayEnabled;
        System.out.println("X-Ray " + (xrayEnabled ? "enabled" : "disabled"));
    }

    // Skanuj tylko, gdy X-Ray aktywny
    if (xrayEnabled && client.player != null && client.world != null) {
        BlockFinder.scanNearbyBlocks(24); // bezpieczny zasięg 24 bloki
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
