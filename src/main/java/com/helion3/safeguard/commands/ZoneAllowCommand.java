package com.helion3.safeguard.commands;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.ProfileNotFoundException;
import org.spongepowered.api.text.Text;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class ZoneAllowCommand implements CommandCallable {
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Format.error("Command usable only by a player."));
            return CommandResult.empty();
        }

        Player player = (Player) source;
        String[] args = arguments.split(" ");

        List<Zone> zones = SafeGuard.getZoneManager().getZones(player.getLocation(), player);
        if (zones.isEmpty()) {
            source.sendMessage(Format.error("No zone at your location. Try defining it by name. /sg zone [name] ..."));
            return CommandResult.empty();
        }

        if (zones.size() > 1) {
            source.sendMessage(Format.error("Overlapping zones at your location. Try defining one by name. /sg zone [name] ..."));
            return CommandResult.empty();
        }

        if (args[0].isEmpty()) {
            source.sendMessage(Format.error("Invalid command syntax. /sg zone allow [player] (flag)"));
            return CommandResult.empty();
        }

        final Zone zone = zones.get(0);

        ListenableFuture<GameProfile> future = SafeGuard.getGame().getServer().getGameProfileManager().get(args[0]);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    GameProfile profile = future.get();

                    // Grant permissions and save
                    zone.allow(profile);
                    zone.save();

                    // Success
                    source.sendMessage(Format.success(profile.getName() + " granted permission in this zone!"));
                } catch (InterruptedException | ExecutionException e) {
                    if (e instanceof ProfileNotFoundException) {
                        source.sendMessage(Format.error("Could not find player with name " + args[0]));
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }, MoreExecutors.sameThreadExecutor());

        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("safeguard.create");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of("Create a new zone."));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of("Create a new zone."));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("/sg zone allow [user] (flags)");
    }
}
