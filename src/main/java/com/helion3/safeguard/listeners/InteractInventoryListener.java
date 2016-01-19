package com.helion3.safeguard.listeners;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;

public class InteractInventoryListener {
    @Listener
    public void onOpenInventory(final InteractInventoryEvent.Open event) {
        Optional<Player> optionalPlayer = event.getCause().first(Player.class);
        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();

        if (!SafeGuard.getZoneManager().allows(player, event, player.getLocation())) {
            player.sendMessage(Format.error("Sorry, this zone doesn't allow you to do that."));
            event.setCancelled(true);
        }
    }
}
