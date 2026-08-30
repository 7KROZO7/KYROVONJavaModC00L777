package extra.entities.bullet;

import arc.audio.Sound;
import arc.graphics.Color;
import extra.content.KVREffects;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;

public class MeleeType extends BulletType {

    public MeleeType(float damage, float range) {
        super(3.5f, damage);

        this.lifetime = range / 3.5f;
        this.hitSize = 14f;

        this.drawSize = 0f;
        this.collides = true;
        this.collidesTiles = true;
        this.collidesAir = false;
        this.pierce = false;

        // Sound on enemy crunch (with fallback if custom audio asset is missing)
        Sound customBite = Vars.tree.loadSound("bite");
        this.hitSound = (customBite != null && customBite != Sounds.none) ? customBite : Sounds.plantBreak;
        this.hitSoundVolume = 1.3f;

        // Visual FX & slow debuff
        this.hitEffect = KVREffects.voidBite;
        this.despawnEffect = Fx.none;
        this.shootEffect = Fx.none;
        this.smokeEffect = Fx.none;
        this.hitColor = Color.valueOf("ef4444");

        this.status = StatusEffects.slow;
        this.statusDuration = 45f;
    }

    public MeleeType(float damage) {
        this(damage, 38f);
    }

    @Override
    public void draw(Bullet b) {
        // Pure melee: no bullet sprites drawn
    }
}
