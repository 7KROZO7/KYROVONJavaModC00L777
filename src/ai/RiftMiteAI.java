package extra.ai;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.world.Tile;

public class RiftMiteAI extends AIController {
    private Tile targetTile;
    private static final Vec2 moveVec = new Vec2();

    @Override
    public void updateMovement() {
        unit.elevation = 1f; // Enables idle hovering bobbing

        // Locate closest mineable ore nearby
        if (targetTile == null || targetTile.drop() == null || unit.dst(targetTile.worldx(), targetTile.worldy()) > 450f) {
            targetTile = Vars.indexer.findClosestOre(unit.x, unit.y, Items.copper);
            if (targetTile == null) {
                targetTile = Vars.indexer.findClosestOre(unit.x, unit.y, Items.lead);
            }
        }

        if (targetTile != null) {
            float tx = targetTile.worldx();
            float ty = targetTile.worldy();
            float dist = unit.dst(tx, ty);

            // Natural hover bobbing
            float bob = Mathf.sin(Time.time + unit.id * 10f, 8f, 1.2f);

            if (dist > 32f) {
                // Fly to ore tile
                moveVec.set(tx - unit.x, (ty + bob) - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(tx, ty);
            } else {
                // In range: Mine ore
                unit.lookAt(tx, ty);
                unit.mineTile = targetTile;

                // Transfer mined items directly to Core or Player
                if (unit.stack.amount > 0 && targetTile.drop() != null) {
                    Building core = unit.closestCore();
                    Player player = Vars.player;

                    // Fixed > 0 check for int return type
                    if (core != null && core.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0) {
                        int accepted = core.acceptStack(unit.stack.item, unit.stack.amount, unit);
                        core.handleStack(unit.stack.item, accepted, unit);
                        unit.stack.amount -= accepted;
                    } else if (player != null && player.unit() != null) {
                        player.unit().addItem(unit.stack.item, unit.stack.amount);
                        unit.stack.amount = 0;
                    }
                }
            }
        }
    }
}
