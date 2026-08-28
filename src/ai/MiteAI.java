package extra.ai;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.world.Tile;

public class MiteAI extends AIController {
    private Tile targetTile;
    private boolean delivering = false;
    private static final Vec2 moveVec = new Vec2();
    private float scanTimer = 0f;

    @Override
    public void updateMovement() {
        unit.elevation = 1f; // Force flight elevation to keep engine glow active

        // Check if inventory is full
        if (unit.stack.amount >= unit.type.itemCapacity) {
            delivering = true;
            unit.mineTile = null;
        }

        // Check if inventory is empty
        if (unit.stack.amount <= 0) {
            delivering = false;
        }

        // --- 1. DELIVERY STATE ---
        if (delivering) {
            Building core = unit.closestCore();
            Player player = Vars.player;

            if (core != null) {
                float dist = unit.dst(core.x, core.y);
                moveVec.set(core.x - unit.x, core.y - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(core.x, core.y);

                if (dist < 40f) {
                    if (core.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0) {
                        int accepted = core.acceptStack(unit.stack.item, unit.stack.amount, unit);
                        core.handleStack(unit.stack.item, accepted, unit);
                        unit.stack.amount -= accepted;
                    } else {
                        unit.stack.amount = 0; // Storage overflow fallback
                    }
                }
            } else if (player != null && player.unit() != null) {
                // Sandbox fallback: deliver to player
                moveVec.set(player.x - unit.x, player.y - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(player.x, player.y);

                if (unit.dst(player.x, player.y) < 36f) {
                    player.unit().addItem(unit.stack.item, unit.stack.amount);
                    unit.stack.amount = 0;
                }
            }
            return;
        }

        // --- 2. MINING STATE ---
        scanTimer += Time.delta;
        if (targetTile == null || targetTile.drop() == null || scanTimer >= 30f) {
            scanTimer = 0f;
            targetTile = scanWorldForOre();
        }

        if (targetTile != null) {
            float wx = targetTile.worldx();
            float wy = targetTile.worldy();
            float dist = unit.dst(wx, wy);

            if (dist > 28f) {
                // Fly to ore deposit
                moveVec.set(wx - unit.x, wy - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(wx, wy);
            } else {
                // In range: Face tile and activate mining beam
                unit.lookAt(wx, wy);
                unit.mineTile = targetTile;
            }
        } else {
            // Idle Orbit (Keeps unit moving and thruster lit if no ores are on screen)
            Player player = Vars.player;
            if (player != null && player.unit() != null) {
                float angle = Time.time * 2.5f + unit.id * 120f;
                float tx = player.x + Mathf.cosDeg(angle) * 40f;
                float ty = player.y + Mathf.sinDeg(angle) * 40f;

                moveVec.set(tx - unit.x, ty - unit.y).setLength(unit.speed());
                unit.movePref(moveVec);
                unit.lookAt(tx, ty);
            }
        }
    }

    // Direct grid scanner: Finds any mineable floor or wall ore within 35 tiles
    private Tile scanWorldForOre() {
        int uTileX = (int)(unit.x / Vars.tilesize);
        int uTileY = (int)(unit.y / Vars.tilesize);
        int radius = 35;

        Tile bestTile = null;
        float bestDst = Float.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int tx = uTileX + dx;
                int ty = uTileY + dy;

                if (tx >= 0 && ty >= 0 && tx < Vars.world.width() && ty < Vars.world.height()) {
                    Tile tile = Vars.world.tile(tx, ty);
                    if (tile != null && tile.drop() != null && tile.drop().hardness <= unit.type.mineTier) {
                        float d = unit.dst(tile.worldx(), tile.worldy());
                        if (d < bestDst) {
                            bestDst = d;
                            bestTile = tile;
                        }
                    }
                }
            }
        }
        return bestTile;
    }
}
