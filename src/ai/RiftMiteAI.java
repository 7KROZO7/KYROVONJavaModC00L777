package extra.ai;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.world.Tile;

public class RiftMiteAI extends AIController {
    private Tile targetTile;
    private static final Vec2 moveVec = new Vec2();

    @Override
    public void updateMovement() {
        unit.elevation = 1f; // Enables idle hovering & flying bobbing

        // Find closest ore tile if none targeted
        if (targetTile == null || targetTile.drop() == null || unit.dst(targetTile.worldx(), targetTile.worldy()) > 400f) {
            targetTile = Vars.indexer.findClosestOre(unit.x, unit.y, Vars.content.item(0)); // Finds nearest mineable ore
        }

        if (targetTile != null) {
            float tx = targetTile.worldx();
            float ty = targetTile.worldy();
            float dist = unit.dst(tx, ty);

            // Natural hover bobbing
            float bob = Mathf.sin(Time.time + unit.id * 10f, 8f, 1.2f);

            if (dist > 30f) {
                // Fly to ore deposit
                moveVec.set(tx - unit.x, (ty + bob) - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(tx, ty);
            } else {
                // In range: Mine ore
                unit.lookAt(tx, ty);
                unit.mineTile = targetTile;

                // Transfer mined ore directly to Core or Player
                if (unit.stack.amount > 0 && targetTile.drop() != null) {
                    Building core = unit.closestCore();
                    Player player = Vars.player;

                    if (core != null && core.acceptStack(unit.stack.item, unit.stack.amount, unit)) {
                        core.handleStack(unit.stack.item, unit.stack.amount, unit);
                        unit.stack.amount = 0;
                    } else if (player != null && player.unit() != null) {
                        player.unit().addItem(unit.stack.item, unit.stack.amount);
                        unit.stack.amount = 0;
                    }
                }
            }
        }
    }
}
