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

import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.volumes.CuboidVolume;

public class CubicalZoneBuffer implements ZoneBuffer {
    private List<Vector3i> positions = new ArrayList<Vector3i>();
    private final World world;

    public CubicalZoneBuffer(World world) {
        this.world = world;
    }

    @Override
    public void addPosition(Vector3i position) {
        positions.add(position);
    }

    @Override
    public boolean isComplete() {
        return positions.size() == 2;
    }

    @Override
    public CuboidVolume getZoneVolume() {
        return new CuboidVolume(world, positions.get(0), positions.get(1));
    }
}
