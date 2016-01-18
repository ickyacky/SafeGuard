package com.helion3.safeguard.volumes;

import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

public abstract class Volume implements DataSerializable {
    public boolean contains(Location<? extends Extent> location) {
        return false;
    }

    public static Volume from(DataView data) throws Exception {
        return null;
    }
}
