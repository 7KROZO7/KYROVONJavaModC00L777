package extra.ui;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextButton;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.gen.Unit;

public class PingDialog {
    private static float lastSpawnTime = -3600f; // 60s cooldown tracker in ticks (60s * 60 ticks = 3600)
    private static final float COOLDOWN = 3600f;

    public static void showGreeting(boolean isReturn, float x, float y) {
        String msg = isReturn
            ? "[#c084fc]Ping:[] *Warp-flux stabilized!* Welcome back, ally! Did the timeline behave while away?"
            : "[#c084fc]Ping:[] *Bzz-wip!* Hello there, fellow ally! Dimensional link calibrated and ready!";
        
        Call.label(msg, 6f, x, y + 16f);
    }

    public static void openInteraction(Unit pingUnit) {
        float remainingTicks = (lastSpawnTime + COOLDOWN) - Time.time;
        int remainingSeconds = (int) Math.ceil(remainingTicks / 60f);

        Dialog dialog = new Dialog("");
        dialog.setBackground(null);

        dialog.cont.table(t -> {
            t.background(mindustry.gen.Tex.pane);
            t.margin(14f);

            if (remainingTicks > 0) {
                t.add("[#c084fc]Ping:[] *Whirr...* Subspace siphon is recharging!\n[#ff79c6]Wait " + remainingSeconds + "s before tearing another rift.[]").padBottom(10f).row();
                TextButton btn = t.button("⏳ Recharging (" + remainingSeconds + "s)", () -> dialog.hide()).size(210f, 44f).get();
                btn.setDisabled(true);
            } else {
                t.add("[#c084fc]Ping:[] What is it, ally? Ready to siphon some mining mites?").padBottom(10f).row();
                t.button("⚡ Spawn Mites", () -> {
                    lastSpawnTime = Time.time;
                    dialog.hide();

                    // Spawn 3 Mining Mites with Warp Animations
                    for (int i = 0; i < 3; i++) {
                        float offsetX = (i - 1) * 16f;
                        float sx = pingUnit.x + offsetX;
                        float sy = pingUnit.y - 12f;

                        KVREffects.warpRift.at(sx, sy);
                        KVRUnits.riftMite.spawn(Vars.player.team(), sx, sy);
                    }

                    Call.label("[#c084fc]Ping:[] *Mites deployed! 30 seconds on the clock!*", 4f, pingUnit.x, pingUnit.y + 16f);
                }).size(210f, 44f).row();
            }

            t.button("Close", dialog::hide).size(120f, 36f).padTop(6f);
        });

        dialog.show();
    }
}
