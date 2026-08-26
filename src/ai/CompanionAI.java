package extra.ai;

import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.entities.units.AIController;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class CompanionAI extends AIController {
    private static final Vec2 moveVec = new Vec2();

    @Override
    public void updateMovement() {
        Player player = Vars.player;

        // Verify player and unit are valid and on the same team
        if (player != null && player.unit() != null && player.unit().isValid() && player.team() == unit.team) {
            Unit target = player.unit();
            float distance = unit.dst(target);
            float followRadius = 35f; // Hover distance from player

            if (distance > 500f) {
                // Warp close if player moves too far (e.g., respawn or teleport)
                unit.set(target.x, target.y);
            } else if (distance > followRadius) {
                // Smoothly accelerate toward player
                moveVec.set(target.x - unit.x, target.y - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
            }

            // Always face the player
            unit.lookAt(target.x, target.y);
        }
    }
}
