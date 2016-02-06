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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;

import com.helion3.safeguard.SafeGuard;

public class ZonePermissions implements DataSerializable {
    private Map<String, Boolean> permissions = new HashMap<String, Boolean>();

    /**
     * Check if a flag is enabled or disabled.
     *
     * @param flag
     * @return boolean
     */
    public boolean allows(String flag) {
        if (permissions.containsKey(flag)) {
            return permissions.get(flag);
        }

        // Default
        if (SafeGuard.getZoneManager().getFlagDefaults().containsKey(flag)) {
            return SafeGuard.getZoneManager().getFlagDefaults().get(flag);
        }

        return false;
    }

    /**
     * Get all flags and their boolean values.
     *
     * @return ImmutableMap<String, Boolean>
     */
    public ImmutableMap<String, Boolean> getPermissions() {
        ImmutableMap.Builder<String, Boolean> builder = ImmutableMap.builder();
        return builder.putAll(permissions).build();
    }

    /**
     * Add/update a specific flag and allow/deny boolean.
     *
     * @param flag String flag name.
     * @param value boolean Allow/deny bool
     */
    public void put(String flag, boolean value) {
        // @todo verify flag
        permissions.put(flag, value);
    }

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

        for (Entry<String, Boolean> entry : permissions.entrySet()) {
            permsissions.set(DataQuery.of(entry.getKey()), entry.getValue());
        }

        return permsissions;
    }
}
