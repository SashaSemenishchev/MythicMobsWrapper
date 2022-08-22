package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.bukkit.utils.serialize.Position;
import org.bukkit.Location;

public class Locations {
    public static AbstractLocation fromBukkit(Location location) {
        return new AbstractLocation(Position.of(location));
    }
}
