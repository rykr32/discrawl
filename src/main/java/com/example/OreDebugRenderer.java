package net.oredebug;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import org.joml.Matrix4f;

public class OreDebugRenderer implements WorldRenderer.WorldRendererDebugRenderer {

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double camX, double camY, double camZ) {
        if (!OreDebugClient.isXrayEnabled()) return;

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());

        for (BlockPos pos : BlockFinder.getFoundBlocks()) {
            Box box = new Box(pos).offset(-camX, -camY, -camZ);
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            drawBox(matrix, consumer, box);
        }
    }

    private void drawBox(Matrix4f matrix, VertexConsumer vertices, Box box) {
        float r = 0.0f, g = 1.0f, b = 1.0f;

        // 12 linii obrysu
        drawLine(vertices, matrix, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.maxY, box.minZ, box.minX, box.maxY, box.minZ, r, g, b);
        drawLine(vertices, matrix, box.minX, box.maxY, box.minZ, box.minX, box.minY, box.minZ, r, g, b);

        drawLine(vertices, matrix, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ, r, g, b);

        drawLine(vertices, matrix, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b);
        drawLine(vertices, matrix, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, r, g, b);
    }

    private void drawLine(VertexConsumer consumer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b) {
        consumer.vertex(matrix, (float) x1, (float) y1, (float) z1).color(r, g, b, 1.0f).next();
        consumer.vertex(matrix, (float) x2, (float) y2, (float) z2).color(r, g, b, 1.0f).next();
    }
}
