package com.helion3.safeguard.volumes;

import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3i;

public abstract class Volume implements DataSerializable {
    protected final Extent extent;
    protected final Vector3i min;
    protected final Vector3i max;

    public Volume(Extent extent, Vector3i min, Vector3i max) {
        this.extent = extent;
        this.min = min;
        this.max = max;
    }

    public boolean contains(Location<? extends Extent> location) {
        return false;
    }

    public static Volume from(DataView data) throws Exception {
        return null;
    }

    public Vector3i getMax() {
        return max;
    }

    public Vector3i getMin() {
        return min;
    }

    public int getVolume() {
        // @todo clean this up
        int xLength = Math.abs(Math.abs(min.getX()) - Math.abs(max.getX()));
        int yLength = Math.abs(Math.abs(min.getY()) - Math.abs(max.getY()));
        int zLength = Math.abs(Math.abs(min.getZ()) - Math.abs(max.getZ()));

        return (xLength * yLength * zLength);
    }
}
