package extra.ui;

import arc.Core;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.gen.Tex;
import mindustry.gen.Unit;

public class PingBubbleUI {
    private static Table bubbleTable;
    private static Unit targetPing;
    private static float lastSpawnTime = -3600f;
    private static final float COOLDOWN = 3600f; // 60 seconds
    private static final Vec2 screenCoords = new Vec2();
    private static float autoDismissTime = -1f;

    public static boolean isVisible() {
        return bubbleTable != null && bubbleTable.visible && targetPing != null;
    }

    public static void showGreeting(Unit pingUnit, boolean isReturn) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;

        String msg = isReturn
            ? "[#c084fc]Ping:[] *Warp-flux stabilized!* Welcome back, ally!\nDid the timeline behave while away?"
            : "[#c084fc]Ping:[] *Bzz-wip!* Hello there, fellow ally!\nDimensional link calibrated and ready!";

        bubbleTable.table(Tex.pane, main -> {
            main.margin(10f);
            main.add(msg).style(mindustry.ui.Styles.outlineLabel).left();
        });

        // Automatically dismiss greeting after 5 seconds (300 ticks)
        autoDismissTime = Time.time + 300f;
        updatePosition();
    }

    public static void show(Unit pingUnit) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;
        autoDismissTime = -1f; // Manual interaction does not auto-dismiss

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;

        float remainingTicks = (lastSpawnTime + COOLDOWN) - Time.time;
        int remainingSeconds = (int) Math.ceil(remainingTicks / 60f);

        bubbleTable.table(Tex.pane, main -> {
            main.touchable = Touchable.enabled;
            main.margin(10f);

            // Header (Dialogue + [✖] Close button)
            main.table(header -> {
                if (remainingTicks > 0) {
                    header.add("[#c084fc]Ping:[] *Whirr!* Subspace cooling down!\n[#ff79c6]Wait " + remainingSeconds + "s before tearing another rift.[]")
                        .style(mindustry.ui.Styles.outlineLabel).left().padRight(12f);
                } else {
                    header.add("[#c084fc]Ping:[] What is it, ally?\nReady to siphon mining mites?")
                        .style(mindustry.ui.Styles.outlineLabel).left().padRight(12f);
                }

                header.button("✖", PingBubbleUI::hide).size(32f, 32f).right();
            }).growX().padBottom(6f).row();

            // Action Button
            if (remainingTicks > 0) {
                TextButton btn = main.button("⏳ Recharging (" + remainingSeconds + "s)", () -> {}).size(220f, 38f).get();
                btn.setDisabled(true);
            } else {
                main.button("⚡ Spawn Mites", () -> {
                    if (targetPing != null && targetPing.isValid()) {
                        float px = targetPing.x;
                        float py = targetPing.y;

                        lastSpawnTime = Time.time;
                        hide();

                        for (int i = 0; i < 3; i++) {
                            float offsetX = (i - 1) * 16f;
                            float sx = px + offsetX;
                            float sy = py - 14f;

                            KVREffects.warpRift.at(sx, sy);

                            Unit mite = KVRUnits.riftMite.create(Vars.player.team());
                            if (mite != null) {
                                mite.set(sx, sy);
                                mite.health = KVRUnits.riftMite.health;
                                mite.elevation = 1f;
                                mite.add();
                            }
                        }
                    } else {
                        hide();
                    }
                }).size(220f, 38f);
            }
        });

        updatePosition();
    }

    public static void updatePosition() {
        if (bubbleTable == null || !bubbleTable.visible) return;

        // Check auto-dismiss timer
        if (autoDismissTime > 0 && Time.time >= autoDismissTime) {
            hide();
            return;
        }

        if (targetPing == null || !targetPing.isValid()) {
            hide();
            return;
        }

        Core.camera.project(screenCoords.set(targetPing.x, targetPing.y + 20f));
        bubbleTable.setPosition(screenCoords.x, screenCoords.y, Align.bottom);
    }

    public static void hide() {
        if (bubbleTable != null) {
            bubbleTable.visible = false;
        }
        targetPing = null;
        autoDismissTime = -1f;
    }

    private static void ensureTableCreated() {
        if (bubbleTable == null) {
            bubbleTable = new Table();
            bubbleTable.touchable = Touchable.enabled;
            Vars.ui.hudGroup.addChild(bubbleTable);
        }
    }
}
