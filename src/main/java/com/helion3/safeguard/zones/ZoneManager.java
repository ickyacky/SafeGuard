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
package com.helion3.safeguard.zones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.helion3.safeguard.SafeGuard;

public class ZoneManager {
    private final List<Zone> zones = new ArrayList<Zone>();

    /**
     * Add a zone.
     * @param zone
     */
    public void add(Zone zone) {
        zones.add(zone);
    }

    /**
     * Get all zones.
     * @return List of zones.
     */
    public List<Zone> getZones() {
        return zones;
    }

    /**
     * Is the player allowed to perform an event at this location.
     * @param player Player
     * @param location Location
     * @return
     */
    public boolean allows(Player player, Event event, Location<World> location) {
        boolean allowed = false;

        List<Zone> zones = getZones(location);
        if (!zones.isEmpty()) {
            for (Zone zone : zones) {
                if (zone.allows(player, event)) {
                    allowed = true;
                    break;
                }
            }
        } else {
            allowed = true;
        }

        return allowed;
    }

    /**
     * Clear all zones currently in memory.
     */
    public void clearAll() {
        zones.clear();
    }

    /**
     * Get all zones for a given location.
     * @return List of zones.
     */
    public List<Zone> getZones(Location<World> location) {
        List<Zone> matches = new ArrayList<Zone>();

        for (Zone zone : SafeGuard.getZoneManager().getZones()) {
            if (zone.getVolume().contains(location)) {
                matches.add(zone);
            }
        }

        return matches;
    }

    /**
     * Get all zones for a given location owned by player.
     * @return List of zones.
     */
    public List<Zone> getZones(Location<World> location, Player owner) {
        List<Zone> matches = new ArrayList<Zone>();

        for (Zone zone : SafeGuard.getZoneManager().getZones()) {
            if (zone.getVolume().contains(location) && zone.getOwners().contains(owner.getProfile())) {
                matches.add(zone);
            }
        }

        return matches;
    }

    /**
     * Deletes a zone permanently.
     * @param zone
     */
    public void delete(Zone zone) {
        Iterator<Zone> iterator = zones.iterator();
        while (iterator.hasNext()) {
            Zone z = iterator.next();
            if (z.equals(zone)) {
                zone.delete();

                iterator.remove();
            }
        }
    }

    /**
     * Whether a zone includes this location.
     * @param location
     * @return
     */
    public boolean zoneExists(Location<World> location) {
        boolean exists = false;

        for (Zone zone : zones) {
            if (zone.getVolume().contains(location)) {
                exists = true;
                break;
            }
        }

        return exists;
    }
}
