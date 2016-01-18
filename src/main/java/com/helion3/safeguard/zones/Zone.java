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
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.MutableBlockVolume;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.DataQueries;
import com.helion3.safeguard.util.DataUtil;


public class Zone implements DataSerializable {
    private final String name;
    private final MutableBlockVolume volume;
    private final World world;
    private final List<GameProfile> owners = new ArrayList<GameProfile>();
    private final UUID uuid;

    public Zone(String name, World world, MutableBlockVolume volume) {
        this.name = name;
        this.volume = volume;
        this.world = world;
        this.uuid = UUID.randomUUID();
    }

    public void addOwner(GameProfile profile) {
        owners.add(profile);
    }

    public MutableBlockVolume getVolume() {
        return volume;
    }

    public boolean allows(GameProfile profile) {
        return owners.contains(profile);
    }

    @Override
    public int getContentVersion() {
        return 1;
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
    public DataContainer toContainer() {
        List<String> ownerContainers = new ArrayList<String>();
        for (GameProfile profile : owners) {
            ownerContainers.add(profile.getUniqueId().toString());
        }

        DataContainer data = new MemoryDataContainer();
        data.set(DataQueries.ZoneName, name);
        data.set(DataQueries.Owners, ownerContainers);

        // Volume
        DataContainer volumeData = new MemoryDataContainer();

            // Min Coord
            DataContainer min = new MemoryDataContainer();
            min.set(DataQueries.X, volume.getBlockMin().getX());
            min.set(DataQueries.Y, volume.getBlockMin().getY());
            min.set(DataQueries.Z, volume.getBlockMin().getZ());
            volumeData.set(DataQueries.Min, min);

            // Max Coord
            DataContainer max = new MemoryDataContainer();
            max.set(DataQueries.X, volume.getBlockMax().getX());
            max.set(DataQueries.Y, volume.getBlockMax().getY());
            max.set(DataQueries.Z, volume.getBlockMax().getZ());
            volumeData.set(DataQueries.Max, max);

        data.set(DataQueries.Volume, volumeData);

        // World
        data.set(DataQueries.World, world.getUniqueId().toString());

        return data;
    }
}
