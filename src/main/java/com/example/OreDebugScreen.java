package net.oredebug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OreDebugScreen extends Screen {

    private TextFieldWidget blockInput;
    private SliderWidget rangeSlider;
    private final List<ButtonWidget> removeButtons = new ArrayList<>();

    protected OreDebugScreen() {
        super(Text.of("Ore Debug Settings"));
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int y = 40;

        blockInput = new TextFieldWidget(textRenderer, centerX - 100, y, 200, 20, Text.of("Add block"));
        blockInput.setMaxLength(100);
        addDrawableChild(blockInput);

        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.of("Add Block"), btn -> {
            String blockId = blockInput.getText().trim();
            if (!blockId.isEmpty()) {
                OreDebugClient.getVisibleBlockIds().add(blockId.toLowerCase());
                blockInput.setText("");
                recreateRemoveButtons();
                ConfigManager.save();

            }
        }).position(centerX - 50, y).size(100, 20).build());

        y += 40;
        rangeSlider = new SliderWidget(centerX - 100, y, 200, 20, Text.of("Range: 24"), 24 / 64.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.of("Range: " + getValue()));
            }

            @Override
            protected void applyValue() {
                OreDebugClient.scanRange = (int) (this.value * 64);
                updateMessage();
            }

            public double getValue() {
                return Math.max(8, (int) (this.value * 64));
            }
        };
        addDrawableChild(rangeSlider);

        y += 40;
        recreateRemoveButtons();
    }

    private void recreateRemoveButtons() {
        for (ClickableWidget btn : removeButtons) {
            remove(btn);
        }
        removeButtons.clear();

        int centerX = width / 2;
        int y = 160;
        int i = 0;

        for (String blockId : OreDebugClient.getVisibleBlockIds()) {
            ButtonWidget btn = ButtonWidget.builder(Text.of("Remove: " + blockId), b -> {
                OreDebugClient.getVisibleBlockIds().remove(blockId);
                recreateRemoveButtons();
            }).position(centerX - 100, y + i * 25).size(200, 20).build();

            removeButtons.add(btn);
            addDrawableChild(btn);
            i++;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, "Ore Debug Settings", width / 2, 10, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
