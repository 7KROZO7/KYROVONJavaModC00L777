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
        // Triggers the exact moment the player touches down
        Events.on(EventType.PlayerSpawnEvent.class, event -> {
            if (event.player == null || event.player.unit() == null) return;

            Time.runTask(30f, () -> { // Brief 0.5s pause after touchdown
                // Check if Ping already exists for this team
                boolean pingExists = Groups.unit.contains(u -> u.type == KVRUnits.ping && u.team == event.player.team());

                if (!pingExists && event.player.unit() != null) {
                    float spawnX = event.player.x + 24f;
                    float spawnY = event.player.y + 24f;

                    // Play Warp-in Effect
                    KVREffects.warpRift.at(spawnX, spawnY);

                    // Spawn Ping
                    KVRUnits.ping.spawn(event.player.team(), spawnX, spawnY);
                }
            });
        });
    }
}
