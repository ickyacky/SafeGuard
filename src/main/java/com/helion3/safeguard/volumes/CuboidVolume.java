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
package com.helion3.safeguard.volumes;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.DataQueries;

public class CuboidVolume extends Volume {
    public CuboidVolume(Extent extent, Vector3i min, Vector3i max) {
        super(extent, min, max);
    }

    @Override
    public boolean contains(Location<? extends Extent> location) {
        if (location.getExtent().equals(extent)) {
            return this.contains(location.getPosition().toInt());
        }

        return false;
    }

    @Override
    public boolean contains(Vector3i pos) {
        return (pos.getX() >= min.getX() && pos.getX() <= max.getX() &&
                pos.getY() >= min.getY() && pos.getY() <= max.getY() &&
                pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ());
    }

    @Override
    public boolean intersects(Volume v2) {
        return (contains(v2.getMin()) ||
                contains(v2.getMax()) ||
                v2.contains(min) ||
                v2.contains(max));
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static Volume from(DataView data) throws Exception {
        int minX = data.getInt(DataQueries.Min.then(DataQueries.X)).get();
        int minY = data.getInt(DataQueries.Min.then(DataQueries.Y)).get();
        int minZ = data.getInt(DataQueries.Min.then(DataQueries.Z)).get();
        Vector3i min = new Vector3i(minX, minY, minZ);

        int maxX = data.getInt(DataQueries.Max.then(DataQueries.X)).get();
        int maxY = data.getInt(DataQueries.Max.then(DataQueries.Y)).get();
        int maxZ = data.getInt(DataQueries.Max.then(DataQueries.Z)).get();
        Vector3i max = new Vector3i(maxX, maxY, maxZ);

        // @todo where are these quotes coming from...
        UUID worldUuid = UUID.fromString(data.getString(DataQueries.World).get().replace("\"", ""));

        Optional<World> world = SafeGuard.getGame().getServer().getWorld(worldUuid);
        if (!world.isPresent()) {
            throw new Exception("Invalid volume data: world");
        }

        return new CuboidVolume(world.get(), min, max);
    }

    @Override
    public DataContainer toContainer() {
        // Volume
        DataContainer data = new MemoryDataContainer();

        // Min Coord
        DataContainer minData = new MemoryDataContainer();
        minData.set(DataQueries.X, min.getX());
        minData.set(DataQueries.Y, min.getY());
        minData.set(DataQueries.Z, min.getZ());
        data.set(DataQueries.Min, minData);

        // Max Coord
        DataContainer maxData = new MemoryDataContainer();
        maxData.set(DataQueries.X, max.getX());
        maxData.set(DataQueries.Y, max.getY());
        maxData.set(DataQueries.Z, max.getZ());
        data.set(DataQueries.Max, maxData);

        // World
        data.set(DataQueries.World, extent.getUniqueId().toString());

        return data;
    }
}
