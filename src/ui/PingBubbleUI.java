package extra.ui;

import arc.Core;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Time;
import extra.content.KVREffects;
import mindustry.Vars;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class PingBubbleUI {
    private static Table bubbleTable;
    private static Unit targetPing;
    private static float lastSpawnTime = -3600f;
    private static final float COOLDOWN = 3600f; // 60s
    private static final Vec2 screenCoords = new Vec2();
    private static float autoDismissTime = -1f;

    public static boolean isVisible() {
        return bubbleTable != null && bubbleTable.visible && targetPing != null;
    }

    public static void showGreeting(Unit pingUnit) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;

        bubbleTable.table(Tex.pane, main -> {
            main.margin(10f);
            main.add("[#c084fc]Ping:[] *Bzz-wip!* Hello there, fellow ally!\nDimensional link calibrated and ready!")
                .style(mindustry.ui.Styles.outlineLabel).left();
        });

        autoDismissTime = Time.time + 300f;
        updatePosition();
    }

    public static void show(Unit pingUnit) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;
        autoDismissTime = -1f;

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;

        float remainingTicks = (lastSpawnTime + COOLDOWN) - Time.time;
        int remainingSeconds = (int) Math.ceil(remainingTicks / 60f);

        bubbleTable.table(Tex.pane, main -> {
            main.touchable = Touchable.enabled;
            main.margin(10f);

            // Header
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

            // Spawn Mites Button
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

                        // Grab the JSON-defined Rift Mite
                        UnitType miteType = Vars.content.unit("krv-rift-mite");
                        if (miteType == null) miteType = Vars.content.unit("rift-mite");

                        if (miteType != null) {
                            for (int i = 0; i < 3; i++) {
                                float sx = px + (i - 1) * 16f;
                                float sy = py - 14f;

                                KVREffects.warpRift.at(sx, sy);

                                Unit mite = miteType.create(Vars.player.team());
                                if (mite != null) {
                                    mite.set(sx, sy);
                                    mite.elevation = 1f;
                                    mite.add();
                                }
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
