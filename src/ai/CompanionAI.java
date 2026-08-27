package extra.ai;

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

        // 30-Second Lifespan: 150 HP / 1800 ticks
        Groups.unit.each(u -> u.type == KVRUnits.riftMite, u -> {
            u.health -= (150f / (30f * 60f)) * Time.delta;

            // Clean warp return to home dimension
            if (u.health <= 0) {
                KVREffects.warpRift.at(u.x, u.y);
                u.remove(); // Vanishes cleanly without wreckage
            }
        });

        // Update speech bubble position
        PingBubbleUI.updatePosition();

        if (player != null && player.unit() != null && player.unit().isValid() && player.team() == unit.team) {
            Unit target = player.unit();
            float distance = unit.dst(target);

            // Warp if distant or respawned
            if (distance > 220f) {
                KVREffects.warpRift.at(unit.x, unit.y);
                unit.set(target.x + 20f, target.y + 20f);
                KVREffects.warpRift.at(unit.x, unit.y);
            } else if (distance > 35f) {
                moveVec.set(target.x - unit.x, target.y - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
            }

            unit.lookAt(target.x, target.y);
        }
    }
}
