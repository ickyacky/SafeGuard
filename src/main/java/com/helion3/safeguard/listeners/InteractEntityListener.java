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
