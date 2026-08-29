package extra.abilities;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import mindustry.entities.abilities.Ability;
import mindustry.type.UnitType;

public class DevourAbility extends Ability {
    public float healPercent = 0.05f; // 5% HP on kill

    public DevourAbility(float healPercent) {
        this.healPercent = healPercent;
    }

    public DevourAbility() {
        this(0.05f);
    }

    @Override
    public void displayBars(mindustry.gen.Unit unit, Table bars) {
        // Displays ability info in unit inspect window
    }

    @Override
    public String localized() {
        return "[#c084fc]Devour[]: Restores 5% HP upon devouring an enemy.";
    }
}
