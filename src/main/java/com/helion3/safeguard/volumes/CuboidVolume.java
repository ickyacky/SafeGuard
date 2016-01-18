package com.helion3.safeguard.volumes;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.util.DataQueries;

public class CuboidVolume extends Volume {
    private final Extent extent;
    private final Vector3i min;
    private final Vector3i max;

    public CuboidVolume(Extent extent, Vector3i min, Vector3i max) {
        this.extent = extent;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean contains(Location<? extends Extent> location) {
        boolean contains = false;

        if (location.getExtent().equals(extent)) {
            Vector3i pos = location.getPosition().toInt();
            contains = (Math.abs(min.getX()) <= Math.abs(pos.getX()) && Math.abs(max.getX()) >= Math.abs(pos.getX()) &&
                        Math.abs(min.getY()) <= Math.abs(pos.getY()) && Math.abs(max.getY()) >= Math.abs(pos.getY()) &&
                        Math.abs(min.getZ()) <= Math.abs(pos.getZ()) && Math.abs(max.getZ()) >= Math.abs(pos.getZ()));
        }

        return contains;
    }

    @Override
    public int getContentVersion() {
        return 1;
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
