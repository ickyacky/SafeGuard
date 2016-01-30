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

import java.util.Optional;

import org.spongepowered.api.entity.hanging.Hanging;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.vehicle.Boat;
import org.spongepowered.api.entity.vehicle.minecart.Minecart;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;

public class InteractEntityListener {
    private final String blockLikeEntityFlag = "block.change";

    @Listener
    public void onInteractEntity(final InteractEntityEvent event) {
        if (event.getTargetEntity() instanceof Hanging || event.getTargetEntity() instanceof Minecart || event.getTargetEntity() instanceof Boat) {
            Optional<Player> player = event.getCause().first(Player.class);
            if (player.isPresent()) {
                // Staff
                if (player.get().hasPermission("safeguard.mod")) {
                    return;
                }

                if (!SafeGuard.getZoneManager().allows(player.get(), blockLikeEntityFlag, event.getTargetEntity().getLocation())) {
                    player.get().sendMessage(Format.error("Sorry, this zone doesn't allow you to do that."));
                    event.setCancelled(true);
                }
            } else {
                if (!SafeGuard.getZoneManager().allows(blockLikeEntityFlag, event.getTargetEntity().getLocation())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
