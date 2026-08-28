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

public class MiteAI extends AIController {
    private Tile targetTile;
    private static final Vec2 moveVec = new Vec2();
    private float scanTimer = 0f;

    @Override
    public void updateMovement() {
        unit.elevation = 1f; // Force flight elevation

        // Scan for nearest ore every 30 ticks using world indexer
        scanTimer += Time.delta;
        if (targetTile == null || targetTile.drop() == null || scanTimer >= 30f) {
            scanTimer = 0f;
            targetTile = findClosestOreTile();
        }

        if (targetTile != null) {
            // Convert to true world pixel coordinates
            float targetWorldX = targetTile.worldx();
            float targetWorldY = targetTile.worldy();
            float distance = unit.dst(targetWorldX, targetWorldY);

            // Flying bobbing physics
            float bob = Mathf.sin(Time.time + unit.id * 10f, 6f, 1.2f);

            if (distance > 32f) {
                // Fly to ore deposit using direct world vector
                moveVec.set(targetWorldX - unit.x, (targetWorldY + bob) - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(targetWorldX, targetWorldY);
            } else {
                // In range: Face tile and activate mining laser
                unit.lookAt(targetWorldX, targetWorldY);
                unit.mineTile = targetTile;

                // Deliver payload to Core or Player inventory
                if (unit.stack.amount > 0 && targetTile.drop() != null) {
                    Building core = unit.closestCore();
                    Player player = Vars.player;

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

    private Tile findClosestOreTile() {
        Item[] ores = {Items.copper, Items.lead, Items.coal, Items.titanium, Items.scrap, Items.sand};
        Tile closest = null;
        float minDst = Float.MAX_VALUE;

        for (Item item : ores) {
            Tile tile = Vars.indexer.findClosestOre(unit.x, unit.y, item);
            if (tile != null) {
                float dst = unit.dst(tile.worldx(), tile.worldy());
                if (dst < minDst) {
                    minDst = dst;
                    closest = tile;
                }
            }
        }
        return closest;
    }
}
