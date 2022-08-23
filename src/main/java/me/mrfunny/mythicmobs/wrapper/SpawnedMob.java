package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.bukkit.utils.serialize.Position;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.mrfunny.mythicmobs.internal.MobSignal;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SpawnedMob {
    private final ActiveMob mythicMob;

    public SpawnedMob(ActiveMob spawnedMob) {
        this.mythicMob = spawnedMob;
    }

    public ActiveMob getMythicMob() {
        return mythicMob;
    }

    public Entity getBukkit() {
        return mythicMob.getEntity().getBukkitEntity();
    }

    public void signal(MobSignal signal) {
        mythicMob.signalMob(mythicMob.getEntity(), signal.toString());
    }

    public Location getBukkitLocation() {
        return Locations.toBukkit(mythicMob.getLocation());
    }

    public AbstractLocation getLocation() {
        return mythicMob.getLocation();
    }
}
