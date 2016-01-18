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
        boolean contains = false;

        if (location.getExtent().equals(extent)) {
            Vector3i pos = location.getPosition().toInt();
            contains = (min.getX() <= pos.getX() && max.getX() >= pos.getX() &&
                        min.getY() <= pos.getY() && max.getY() >= pos.getY() &&
                        min.getZ() <= pos.getZ() && max.getZ() >= pos.getZ());
        }

        return contains;
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
