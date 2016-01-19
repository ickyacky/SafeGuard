package com.helion3.safeguard.listeners;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class DamageEntityListener {
    @Listener
    public void onDamageEntity(final DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Optional<EntityDamageSource> optionalSource = event.getCause().first(EntityDamageSource.class);
        if (!optionalSource.isPresent()) {
            return;
        }

        Player attacker = null;

        if (optionalSource.get().getSource() instanceof Player) {
            attacker = (Player) optionalSource.get().getSource();
        }
        else if(optionalSource.get() instanceof IndirectEntityDamageSource) {
            Entity source = ((IndirectEntityDamageSource) optionalSource.get()).getIndirectSource();
            if (source instanceof Player) {
                attacker = (Player) source;
            }
        }

        if (attacker == null) {
            return;
        }

        Player victim = (Player) event.getTargetEntity();

        List<Zone> zones = SafeGuard.getZoneManager().getZones(victim.getLocation());
        if (zones.isEmpty()) {
            return;
        }

        for (Zone zone : zones) {
            if (!zone.allows(attacker, event)) {
                attacker.sendMessage(Format.error("Victim is protected from damage by a zone."));

                victim.sendMessage(Format.message("Deflected damage inside your zone from " + attacker.getName()));
                event.setCancelled(true);
                break;
            }
        }
    }
}
