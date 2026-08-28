package extra.ui;

import arc.Core;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
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

    private static final Seq<MiteSpeechBubble> miteBubbles = new Seq<>();

    public static boolean isVisible() {
        return bubbleTable != null && bubbleTable.visible && targetPing != null;
    }

    public static void showGreeting(Unit pingUnit) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;
        bubbleTable.touchable = Touchable.disabled; // Greetings don't need buttons

        bubbleTable.table(Tex.pane, main -> {
            main.margin(10f);
            main.add("[#c084fc]Ping:[] *Bzz-wip!* Hello there, fellow ally!\nDimensional link calibrated and ready!")
                .style(mindustry.ui.Styles.outlineLabel).left();
        });

        autoDismissTime = Time.time + 300f; // 5s auto-dismiss
        updatePosition();
    }

    public static void show(Unit pingUnit) {
        if (pingUnit == null || !pingUnit.isValid()) return;
        targetPing = pingUnit;
        autoDismissTime = -1f;

        ensureTableCreated();
        bubbleTable.clear();
        bubbleTable.visible = true;
        // CRITICAL FIX: Re-enables button clicks every time the menu opens
        bubbleTable.touchable = Touchable.childrenOnly;

        float remainingTicks = (lastSpawnTime + COOLDOWN) - Time.time;
        int remainingSeconds = (int) Math.ceil(remainingTicks / 60f);

        bubbleTable.table(Tex.pane, main -> {
            main.touchable = Touchable.enabled;
            main.margin(10f);

            // Header
            main.table(header -> {
                header.touchable = Touchable.enabled;
                if (remainingTicks > 0) {
                    header.add("[#c084fc]Ping:[] *Whirr!* Subspace cooling down!\n[#ff79c6]Wait " + remainingSeconds + "s before tearing another rift.[]")
                        .style(mindustry.ui.Styles.outlineLabel).left().padRight(12f);
                } else {
                    header.add("[#c084fc]Ping:[] What is it, ally?\nReady to siphon mining mites?")
                        .style(mindustry.ui.Styles.outlineLabel).left().padRight(12f);
                }

                header.button("✖", PingBubbleUI::hide).size(32f, 32f).right();
            }).growX().padBottom(6f).row();

            // Spawn Button
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

                        UnitType miteType = Vars.content.units().find(u -> u != null && u.name != null && u.name.toLowerCase().contains("rift-mite"));

                        if (miteType != null) {
                            String[] dialogues = {
                                "[#38bdf8]Mite Alpha:[] *Bzz-pip!* Spatial link stable—harvesting ores!",
                                "[#38bdf8]Mite Beta:[] *Whirr!* Target locked, 30 seconds on the clock!",
                                "[#38bdf8]Mite Gamma:[] *Chirp!* Tunneling matrix active, let's dig!"
                            };

                            for (int i = 0; i < 3; i++) {
                                float sx = px + (i - 1) * 16f;
                                float sy = py - 14f;

                                KVREffects.warpRift.at(sx, sy);

                                Unit mite = miteType.spawn(Vars.player.team(), sx, sy);
                                if (mite != null) {
                                    mite.elevation = 1f;
                                    spawnMiteSpeech(mite, dialogues[i % dialogues.length]);
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

    private static void spawnMiteSpeech(Unit mite, String text) {
        Table table = new Table();
        table.touchable = Touchable.disabled;
        table.table(Tex.pane, t -> {
            t.touchable = Touchable.disabled;
            t.margin(6f);
            t.add(text).style(mindustry.ui.Styles.outlineLabel).center();
        });

        Vars.ui.hudGroup.addChild(table);
        miteBubbles.add(new MiteSpeechBubble(mite, table, Time.time + 240f));
    }

    public static void updatePosition() {
        if (bubbleTable != null && bubbleTable.visible) {
            if (autoDismissTime > 0 && Time.time >= autoDismissTime) {
                hide();
            } else if (targetPing == null || !targetPing.isValid()) {
                hide();
            } else {
                Core.camera.project(screenCoords.set(targetPing.x, targetPing.y + 20f));
                bubbleTable.setPosition(screenCoords.x, screenCoords.y, Align.bottom);
            }
        }

        for (int i = miteBubbles.size - 1; i >= 0; i--) {
            MiteSpeechBubble b = miteBubbles.get(i);
            if (Time.time >= b.expiryTime || b.unit == null || !b.unit.isValid()) {
                b.table.remove();
                miteBubbles.remove(i);
            } else {
                Core.camera.project(screenCoords.set(b.unit.x, b.unit.y + 14f));
                b.table.setPosition(screenCoords.x, screenCoords.y, Align.bottom);
            }
        }
    }

    public static void hide() {
        if (bubbleTable != null) {
            bubbleTable.visible = false;
            bubbleTable.touchable = Touchable.disabled;
        }
        targetPing = null;
        autoDismissTime = -1f;
    }

    private static void ensureTableCreated() {
        if (bubbleTable == null) {
            bubbleTable = new Table();
            bubbleTable.touchable = Touchable.childrenOnly;
            Vars.ui.hudGroup.addChild(bubbleTable);
        }
    }

    private static class MiteSpeechBubble {
        public Unit unit;
        public Table table;
        public float expiryTime;

        public MiteSpeechBubble(Unit unit, Table table, float expiryTime) {
            this.unit = unit;
            this.table = table;
            this.expiryTime = expiryTime;
        }
    }
}
