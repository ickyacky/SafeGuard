/**
 * This file is part of SafeGuard, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
    private final String pvpFlag = "damage.player";

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
            if (!zone.allows(attacker, pvpFlag)) {
                attacker.sendMessage(Format.error("Victim is protected from damage by a zone."));
                event.setCancelled(true);
                break;
            }
        }
    }
}
