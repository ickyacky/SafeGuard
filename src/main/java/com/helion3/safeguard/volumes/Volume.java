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

    public boolean contains(Vector3i pos) {
        return false;
    }

    public boolean intersects(Volume v2) {
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
        int xLength = max.getX() - min.getX();
        int yLength = max.getY() - min.getY();
        int zLength = max.getZ() - min.getZ();

        return (xLength * yLength * zLength);
    }
}
