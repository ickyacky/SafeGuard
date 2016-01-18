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
import java.util.List;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.world.extent.MutableBlockVolume;

public class Zone {
    private final String name;
    private final MutableBlockVolume volume;
    private final List<GameProfile> owners = new ArrayList<GameProfile>();

    public Zone(String name, MutableBlockVolume volume) {
        this.name = name;
        this.volume = volume;
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
}
