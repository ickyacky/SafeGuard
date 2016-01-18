package com.helion3.safeguard.zones;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;

public class ZonePermissions implements DataSerializable {

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static ZonePermissions from(DataView data) throws Exception {
        return new ZonePermissions();
    }

    @Override
    public DataContainer toContainer() {
        DataContainer permsissions = new MemoryDataContainer();
        permsissions.set(DataQuery.of("Test"), "*");

        return permsissions;
    }
}
