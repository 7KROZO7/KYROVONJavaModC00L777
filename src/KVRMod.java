package extra;

import arc.Core;
import arc.Events;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import extra.ui.PingDialog;
import mindustry.Vars;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        Events.run(Trigger.update, () -> {
            if (!Vars.state.isPlaying() || Vars.player == null) return;

            Unit playerUnit = Vars.player.unit();
            if (playerUnit == null || !playerUnit.isValid() || playerUnit.dead) return;

            Unit ping = Groups.unit.find(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());

            if (ping == null || !ping.isValid() || ping.dead) {
                float sx = playerUnit.x + 24f;
                float sy = playerUnit.y + 24f;

                KVREffects.warpRift.at(sx, sy);
                Unit newPing = KVRUnits.ping.spawn(Vars.player.team(), sx, sy);

                // Sector Visit Memory Greeting
                String sectorKey = Vars.state.rules.sector != null ? "krv-visited-" + Vars.state.rules.sector.id : "krv-sandbox";
                boolean isReturn = Core.settings.getBool(sectorKey, false);
                Core.settings.put(sectorKey, true);

                PingDialog.showGreeting(isReturn, sx, sy);
            }
        });
    }
}
