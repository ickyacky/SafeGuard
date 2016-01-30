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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.DataQueries;
import com.helion3.safeguard.util.DataUtil;
import com.helion3.safeguard.volumes.CuboidVolume;
import com.helion3.safeguard.volumes.Volume;

public class Zone implements DataSerializable {
    private final String name;
    private final Volume volume;
    private final List<GameProfile> owners = new ArrayList<GameProfile>();
    private final Map<GameProfile, ZonePermissions> userPermissions = new HashMap<GameProfile, ZonePermissions>();
    private final UUID uuid;
    private final ZonePermissions permissions;

    public Zone(String name, Volume volume) {
        this(UUID.randomUUID(), name, volume, new ZonePermissions());
    }

    public Zone(UUID uuid, String name, Volume volume, ZonePermissions permissions) {
        this.name = name;
        this.volume = volume;
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public void addOwner(GameProfile profile) {
        owners.add(profile);
    }

    public void allow(GameProfile profile) {
        allow(profile, new ZonePermissions());
    }

    public void allow(GameProfile profile, ZonePermissions zonePermissions) {
        userPermissions.put(profile, zonePermissions);
    }

    public boolean allows(String flag) {
        // Allow if the flag is enable for the whole zone
        if (permissions.allows(flag)) {
            return true;
        }

        // Nope
        return false;
    }

    public boolean allows(Player player, String flag) {
        // All owners to do everything
        if (owners.contains(player.getProfile())) {
            return true;
        }

        // Allow if the flag is enable for the whole zone
        if (permissions.allows(flag)) {
            return true;
        }

        // Allow if this player is allowed the flag
        if (userPermissions.containsKey(player.getProfile())) {
            ZonePermissions perms = userPermissions.get(player.getProfile());

            if (perms != null) {
                return perms.allows(flag);
            }
        }

        // Nope
        return false;
    }

    public boolean deny(GameProfile profile) {
        return userPermissions.remove(profile) != null;
    }

    public ZonePermissions getPermissions() {
        return permissions;
    }

    public Map<GameProfile, ZonePermissions> getUserPermissions() {
        return userPermissions;
    }

    public Volume getVolume() {
        return volume;
    }

    public boolean allows(GameProfile profile) {
        return owners.contains(profile);
    }

    public String getName() {
        return name;
    }

    public List<GameProfile> getOwners() {
        return owners;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public void delete() {
        File file = new File(SafeGuard.getParentDirectory().getAbsolutePath() + "/zones/" + uuid.toString() + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    public void save() {
        try {
            // If files do not exist, we must create them
            File dir = new File(SafeGuard.getParentDirectory().getAbsolutePath() + "/zones");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = dir.getAbsolutePath() + "/" + uuid.toString() + ".json";

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(filePath);
            writer.write(DataUtil.jsonFromDataView(toContainer()).toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Zone) {
            Zone zone = (Zone) object;
            if (zone.getUUID().equals(this.uuid)) {
                return true;
            }
        }

        return false;
    }

    public static Zone from(DataContainer data) throws Exception {
        Optional<String> optionalUuid = data.getString(DataQueries.Uuid);
        if (!optionalUuid.isPresent()) {
            throw new Exception("Invalid zone data: uuid");
        }

        Optional<String> optionalName = data.getString(DataQueries.ZoneName);
        if (!optionalName.isPresent()) {
            throw new Exception("Invalid zone data: name");
        }

        Optional<DataView> optionalVolume = data.getView(DataQueries.Volume);
        if (!optionalVolume.isPresent()) {
            throw new Exception("Invalid zone data: volume");
        }

        // @todo need way to determine specific type
        Volume volume = CuboidVolume.from(optionalVolume.get());

        Optional<DataView> optionalZonePerms = data.getView(DataQueries.ZonePermissions);
        if (!optionalZonePerms.isPresent()) {
            throw new Exception("Invalid zone data: zone permissions");
        }

        // Zone perms
        ZonePermissions perms = ZonePermissions.from(optionalZonePerms.get());

        // Restore zone object
        Zone zone = new Zone(UUID.fromString(optionalUuid.get()), optionalName.get(), volume, perms);

        // Owners
        Optional<List<?>> optionalOwners = data.getList(DataQueries.Owners);
        if (!optionalOwners.isPresent()) {
            throw new Exception("Invalid zone data: owners list");
        }

        for (Object object : optionalOwners.get()) {
            if (object instanceof String) {
                UUID uuid = UUID.fromString((String) object);
                Future<GameProfile> future = SafeGuard.getGame().getServer().getGameProfileManager().get(uuid);
                zone.addOwner(future.get());
            }
        }

        // Permissions
        Optional<DataView> optionalPermissions = data.getView(DataQueries.UserPermissions);
        if (optionalPermissions.isPresent()) {
            for (DataQuery query : optionalPermissions.get().getKeys(false)) {
                UUID uuid = UUID.fromString(query.toString());

                Future<GameProfile> future = SafeGuard.getGame().getServer().getGameProfileManager().get(uuid);
                ZonePermissions userPerms = ZonePermissions.from(optionalPermissions.get().getView(query).get());

                zone.allow(future.get(), userPerms);
            }
        }

        return zone;
    }

    @Override
    public DataContainer toContainer() {
        List<String> ownerContainers = new ArrayList<String>();
        for (GameProfile profile : owners) {
            ownerContainers.add(profile.getUniqueId().toString());
        }

        DataContainer data = new MemoryDataContainer();
        data.set(DataQueries.Uuid, uuid.toString());
        data.set(DataQueries.ZoneName, name);
        data.set(DataQueries.Owners, ownerContainers);
        data.set(DataQueries.ZonePermissions, permissions);

        // Zone permissions
        Map<String, DataContainer> profileMap = new HashMap<String, DataContainer>();
        for (Entry<GameProfile, ZonePermissions> entry : userPermissions.entrySet()) {
            profileMap.put(entry.getKey().getUniqueId().toString(), entry.getValue().toContainer());
        }

        data.set(DataQueries.UserPermissions, profileMap);

        // Volume
        data.set(DataQueries.Volume, volume.toContainer());

        return data;
    }
}
