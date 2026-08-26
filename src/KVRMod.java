package extra;

import extra.content.KVRUnits;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    public KVRMod() {
        // Constructor
    }

    @Override
    public void loadContent() {
        // Load custom units
        KVRUnits.load();
    }

    @Override
    public void init() {
        // Post-init logic
    }
}
