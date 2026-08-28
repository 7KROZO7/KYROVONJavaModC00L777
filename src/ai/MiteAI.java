package extra.ai;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.world.Tile;

public class MiteAI extends AIController {
    private Tile targetTile;
    private boolean delivering = false;
    private float scanTimer = 0f;

    @Override
    public void updateMovement() {
        unit.elevation = 1f;

        // Switch to Delivery mode when inventory is full
        if (unit.stack.amount >= unit.type.itemCapacity) {
            delivering = true;
            unit.mineTile = null;
        }

        // Switch back to Mining mode when inventory is empty
        if (unit.stack.amount <= 0) {
            delivering = false;
        }

        // --- DELIVERY STATE ---
        if (delivering) {
            Building core = unit.closestCore();
            Player player = Vars.player;

            if (core != null) {
                // Fly to Core
                moveTo(core, 30f);
                unit.lookAt(core);

                if (unit.within(core, 45f)) {
                    if (core.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0) {
                        int accepted = core.acceptStack(unit.stack.item, unit.stack.amount, unit);
                        core.handleStack(unit.stack.item, accepted, unit);
                        unit.stack.amount -= accepted;
                    } else {
                        unit.stack.amount = 0; // Storage fallback
                    }
                }
            } else if (player != null && player.unit() != null) {
                // Sandbox Fallback: Deliver directly to player
                moveTo(player.unit(), 24f);
                if (unit.within(player.unit(), 36f)) {
                    player.unit().addItem(unit.stack.item, unit.stack.amount);
                    unit.stack.amount = 0;
                }
            }
            return;
        }

        // --- MINING STATE ---
        // Scan for nearest ore once every 45 ticks (Prevents scanning lag)
        scanTimer += Time.delta;
        if (targetTile == null || targetTile.drop() == null || scanTimer >= 45f) {
            scanTimer = 0f;
            targetTile = findNearestOre();
        }

        if (targetTile != null) {
            moveTo(targetTile, unit.type.mineRange * 0.6f);
            unit.lookAt(targetTile.worldx(), targetTile.worldy());

            // Within laser range: Activate extraction
            if (unit.within(targetTile.worldx(), targetTile.worldy(), unit.type.mineRange)) {
                unit.mineTile = targetTile;
            }
        }
    }

    private Tile findNearestOre() {
        Item[] searchOres = {Items.copper, Items.lead, Items.coal, Items.titanium, Items.scrap, Items.sand};
        for (Item item : searchOres) {
            Tile ore = Vars.indexer.findClosestOre(unit.x, unit.y, item);
            if (ore != null) return ore;
        }
        return null;
    }
  }
