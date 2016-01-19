package com.helion3.safeguard.listeners;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;

public class DropItemListener {
    @Listener
    public void onDropItem(final DropItemEvent.Dispense event) {
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
