package com.helion3.safeguard.listeners;

import java.util.function.Predicate;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.helion3.safeguard.SafeGuard;

public class SpawnEntityListener {
    @Listener
    public void onSpawn(final SpawnEntityEvent event) {
        // Skip any intentionally-spawned creatures
        if (event.getCause().first(Player.class).isPresent()) {
            return;
        }

        // Filter spawns
        event.filterEntities(new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                if (entity instanceof Monster) {
                    return !SafeGuard.getZoneManager().zoneExists(entity.getLocation());
                }

                return true;
            }
        });
    }
}
