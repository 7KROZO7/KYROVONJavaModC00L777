package extra.ui;

import arc.Core;
import arc.math.geom.Vec2;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.gen.Tex;
import mindustry.gen.Unit;

public class PingBubbleUI {
    private static Table bubbleTable;
    private static Unit targetPing;
    private static float lastSpawnTime = -3600f;
    private static final float COOLDOWN = 3600f; // 60 seconds (3600 ticks)
    private static final Vec2 screenCoords = new Vec2();

    public static void showGreeting(boolean isReturn, float x, float y) {
        String msg = isReturn
            ? "[#c084fc]Ping:[] *Warp-flux stabilized!* Welcome back, ally! Did the timeline behave while away?"
            : "[#c084fc]Ping:[] *Bzz-wip!* Hello there, fellow ally! Dimensional link calibrated and ready!";
        
        Call.label(msg, 5f, x, y + 16f);
    }

    public static void show(Unit pingUnit) {
        targetPing = pingUnit;
        if (bubbleTable == null) {
            bubbleTable = new Table();
            Vars.ui.hudGroup.addChild(bubbleTable);
        }

        bubbleTable.clear();
        bubbleTable.visible = true;

        float remainingTicks = (lastSpawnTime + COOLDOWN) - Time.time;
        int remainingSeconds = (int) Math.ceil(remainingTicks / 60f);

        bubbleTable.table(Tex.pane, t -> {
            t.margin(10f);

            if (remainingTicks > 0) {
                t.add("[#c084fc]Ping:[] *Whirr!* Subspace siphon cooling down!\n[#ff79c6]Wait " + remainingSeconds + "s before tearing another rift.[]")
                    .style(mindustry.ui.Styles.outlineLabel).padBottom(6f).row();

                TextButton btn = t.button("⏳ Recharging (" + remainingSeconds + "s)", () -> {}).size(180f, 36f).get();
                btn.setDisabled(true);
            } else {
                t.add("[#c084fc]Ping:[] What is it, ally? Ready to siphon some mining mites?")
                    .style(mindustry.ui.Styles.outlineLabel).padBottom(6f).row();

                t.button("⚡ Spawn Mites", () -> {
                    lastSpawnTime = Time.time;
                    hide();

                    for (int i = 0; i < 3; i++) {
                        float offsetX = (i - 1) * 16f;
                        float sx = targetPing.x + offsetX;
                        float sy = targetPing.y - 12f;

                        KVREffects.warpRift.at(sx, sy);
                        KVRUnits.riftMite.spawn(Vars.player.team(), sx, sy);
                    }
                }).size(180f, 36f).row();
            }

            t.button("✖", PingBubbleUI::hide).size(28f, 28f).padTop(4f);
        });

        updatePosition();
    }

    public static void updatePosition() {
        if (bubbleTable == null || !bubbleTable.visible || targetPing == null || !targetPing.isValid()) {
            hide();
            return;
        }

        // Convert world coords to 2D screen pixels
        Core.camera.project(screenCoords.set(targetPing.x, targetPing.y + 18f));
        bubbleTable.setPosition(screenCoords.x, screenCoords.y, Align.bottom);
    }

    public static void hide() {
        if (bubbleTable != null) {
            bubbleTable.visible = false;
        }
        targetPing = null;
    }
}
