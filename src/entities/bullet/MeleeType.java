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

        // Custom bite.ogg audio lookup
        Sound biteSound = Vars.tree.loadSound("krv-bite");
        if (biteSound == null || biteSound == Sounds.none) {
            biteSound = Vars.tree.loadSound("bite");
        }
        this.hitSound = (biteSound != null && biteSound != Sounds.none) ? biteSound : Sounds.plantBreak;
        this.hitSoundVolume = 1.4f;

        this.hitEffect = KVREffects.voidBite;
        this.despawnEffect = Fx.none;
        this.shootEffect = Fx.none;
        this.smokeEffect = Fx.none;
        this.hitColor = Color.valueOf("ef4444");

        // Exactly 1.0s slow (60 ticks)
        this.status = StatusEffects.slow;
        this.statusDuration = 60f;
    }

    public MeleeType(float damage) {
        this(damage, 20f);
    }

    @Override
    public void draw(Bullet b) {
        // Pure melee: no bullet sprites drawn
    }
}
