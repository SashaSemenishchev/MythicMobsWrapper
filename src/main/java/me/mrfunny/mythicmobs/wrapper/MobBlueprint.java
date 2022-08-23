package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;

import java.util.Optional;

public class MobBlueprint {
    private String mobId;

    public MobBlueprint() {
        String ret = getClass().getSimpleName().replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");
        this.mobId = ret.toUpperCase();
        if(mobId.equals("")) {
            mobId = "DeveloperMob";
        }
    }

    public MobBlueprint(String id) {
        this.mobId = id;
    }

    public MythicMob getMythicMob() {
        Optional<MythicMob> mob = Mobs.mobManager.getMythicMob(mobId);
        if(mob.isPresent()) {
            return mob.get();
        }
        throw new IllegalArgumentException("No mob present with ID " + mobId);
    }

    public SpawnedMob spawn(Location location) {
        ActiveMob mob = getMythicMob().spawn(Locations.fromBukkit(location), 0);
        return new SpawnedMob(mob);
    }
}
