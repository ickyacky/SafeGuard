package com.helion3.safeguard.listeners;

import java.util.Optional;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;

import com.helion3.safeguard.SafeGuard;

public class ExplosionListener {
    @Listener
    public void onExplosion(final ExplosionEvent.Detonate event) {
        Optional<Entity> optional = event.getCause().first(Entity.class);
        if (optional.isPresent()) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                if (!SafeGuard.getZoneManager().zoneExists(transaction.getOriginal().getLocation().get())) {
                    transaction.setValid(false);
                }
            }
        }
    }
}
