package extra;

import arc.Events;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import extra.ui.PingBubbleUI;
import extra.ui.PingHUDWidget;
import mindustry.Vars;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    public KVRMod() {
    }

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        // Build the draggable HUD grip & button
        PingHUDWidget.build();

        Events.run(Trigger.update, () -> {
            if (!Vars.state.isPlaying() || Vars.player == null) return;

            Unit playerUnit = Vars.player.unit();
            if (playerUnit == null || !playerUnit.isValid() || playerUnit.dead) return;

            Unit ping = Groups.unit.find(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());

            // Auto-spawn Ping through a rift on spawn/drop
            if (ping == null || !ping.isValid() || ping.dead) {
                float sx = playerUnit.x + 24f;
                float sy = playerUnit.y + 24f;

                KVREffects.warpRift.at(sx, sy);
                Unit newPing = KVRUnits.ping.create(Vars.player.team());
                if (newPing != null) {
                    newPing.set(sx, sy);
                    newPing.elevation = 1f;
                    newPing.add();

                    // Play in-world arrival greeting
                    PingBubbleUI.showGreeting(newPing);
                }
            }
        });
    }
}
