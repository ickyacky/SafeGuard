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
package com.helion3.safeguard.util;

import org.spongepowered.api.data.DataQuery;

public class DataQueries {
    private DataQueries() {}

    public final static DataQuery Min = DataQuery.of("Min");
    public final static DataQuery Max = DataQuery.of("Max");
    public final static DataQuery Owners = DataQuery.of("Owners");
    public final static DataQuery UserPermissions = DataQuery.of("UserPermissions");
    public final static DataQuery Uuid = DataQuery.of("Uuid");
    public final static DataQuery Volume = DataQuery.of("Volume");
    public final static DataQuery World = DataQuery.of("World");
    public final static DataQuery X = DataQuery.of("X");
    public final static DataQuery Y = DataQuery.of("Y");
    public final static DataQuery Z = DataQuery.of("Z");
    public final static DataQuery ZoneName = DataQuery.of("ZoneName");
    public final static DataQuery ZonePermissions = DataQuery.of("ZonePermissions");
}
