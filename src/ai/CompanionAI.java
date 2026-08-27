package extra.ai;

import arc.Core;
import arc.math.geom.Vec2;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import extra.ui.PingBubbleUI;
import mindustry.Vars;
import mindustry.entities.units.AIController;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class CompanionAI extends AIController {
    private static final Vec2 moveVec = new Vec2();

    @Override
    public void updateMovement() {
        Player player = Vars.player;

        // Process 30-second HP decay on active Rift Mites (5 HP per sec)
        Groups.unit.each(u -> u.type == KVRUnits.riftMite, u -> {
            u.health -= 5f * (Time.delta / 60f);
            if (u.health <= 0) u.kill();
        });

        // Update active speech bubble position above Ping
        PingBubbleUI.updatePosition();

        if (player != null && player.unit() != null && player.unit().isValid() && player.team() == unit.team) {
            Unit target = player.unit();
            float distance = unit.dst(target);

            // Warp if distant / respawned
            if (distance > 220f) {
                KVREffects.warpRift.at(unit.x, unit.y);
                unit.set(target.x + 20f, target.y + 20f);
                KVREffects.warpRift.at(unit.x, unit.y);
            } else if (distance > 35f) {
                moveVec.set(target.x - unit.x, target.y - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
            }

            unit.lookAt(target.x, target.y);

            // Tap / Click detection on Ping (PC & Mobile)
            if (Core.input.justTouched()) {
                Vec2 touchWorld = Core.camera.unproject(Core.input.mouse());
                if (unit.dst(touchWorld.x, touchWorld.y) < 24f) {
                    PingBubbleUI.show(unit);
                }
            }
        }
    }
}
