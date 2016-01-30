package com.helion3.safeguard.listeners;

import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;

import com.helion3.safeguard.SafeGuard;

public class MoveEntityListener {
    @Listener
    public void onMove(final DisplaceEntityEvent.Move event) {
        // Only apply to Monsters
        if (!(event.getTargetEntity() instanceof Monster)) {
            return;
        }

        if (SafeGuard.getZoneManager().zoneExists(event.getTargetEntity().getLocation())) {
            event.getTargetEntity().remove();
        }
    }
}
