package net.oredebug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

public class BlockFinder {

    private static final Set<BlockPos> foundBlocks = new HashSet<>();

    public static void scanNearbyBlocks(int range) {
        foundBlocks.clear();

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        ClientWorld world = client.world;
        BlockPos playerPos = client.player.getBlockPos();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = playerPos.add(x, y, z);

                    String blockId = world.getBlockState(checkPos).getBlock().getTranslationKey();
                    for (String allowedId : OreDebugClient.getVisibleBlockIds()) {
                        if (blockId.contains(allowedId.replace("minecraft:", ""))) {
                            foundBlocks.add(checkPos.toImmutable());
                        }
                    }
                }
            }
        }
    }

    public static Set<BlockPos> getFoundBlocks() {
        return foundBlocks;
    }
}

