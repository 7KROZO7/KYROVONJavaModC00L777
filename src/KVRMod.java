package extra;

import arc.Events;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    public KVRMod() {
        // Constructor
    }

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        // Triggers the exact moment the player enters/controls their unit
        Events.on(EventType.UnitControlEvent.class, event -> {
            if (event.player == null || event.unit == null) return;

            Time.runTask(45f, () -> { // Brief delay after controlling unit
                if (event.unit != null && event.unit.isValid()) {
                    // Check if Ping is already on this team
                    boolean pingExists = Groups.unit.contains(u -> u.type == KVRUnits.ping && u.team == event.player.team());

                    if (!pingExists) {
                        float spawnX = event.unit.x + 24f;
                        float spawnY = event.unit.y + 24f;

                        // Play Warp Rift animation
                        KVREffects.warpRift.at(spawnX, spawnY);

                        // Spawn Ping
                        KVRUnits.ping.spawn(event.player.team(), spawnX, spawnY);
                    }
                }
            });
        });
    }
}
