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

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class BlockUtil {
    private BlockUtil() {}

    /**
     * Determine if we'll ignore messaging the player for this event/transaction combo.
     *
     * @param event Event
     * @param transaction Block transaction
     * @return
     */
    public static boolean ignore(ChangeBlockEvent event, Transaction<BlockSnapshot> transaction) {
        if (event instanceof ChangeBlockEvent.Place) {
            return ignorePlaceCombination(transaction.getOriginal().getState().getType(), transaction.getFinal().getState().getType());
        } else {
            return ignoreBreakCombination(transaction.getOriginal().getState().getType(), transaction.getFinal().getState().getType());
        }
    }

    /**
     * Sponge's ChangeBlockEvent.Place covers a lot, but it also includes a lot
     * we don't want. So here we can setup checks to filter out block combinations.
     *
     * @param a BlockType original
     * @param b BlockType final
     * @return boolean True if combo should be rejected
     */
    public static boolean ignorePlaceCombination(BlockType a, BlockType b) {
        return (
            // Just basic state changes
            a.equals(BlockTypes.LIT_FURNACE) || b.equals(BlockTypes.LIT_FURNACE) ||
            a.equals(BlockTypes.LIT_REDSTONE_LAMP) || b.equals(BlockTypes.LIT_REDSTONE_LAMP) ||
            a.equals(BlockTypes.LIT_REDSTONE_ORE) || b.equals(BlockTypes.LIT_REDSTONE_ORE) ||
            a.equals(BlockTypes.UNLIT_REDSTONE_TORCH) || b.equals(BlockTypes.UNLIT_REDSTONE_TORCH) ||
            (a.equals(BlockTypes.POWERED_REPEATER) && b.equals(BlockTypes.UNPOWERED_REPEATER)) ||
            (a.equals(BlockTypes.UNPOWERED_REPEATER) && b.equals(BlockTypes.POWERED_REPEATER)) ||
            (a.equals(BlockTypes.POWERED_COMPARATOR) && b.equals(BlockTypes.UNPOWERED_COMPARATOR)) ||
            (a.equals(BlockTypes.UNPOWERED_COMPARATOR) && b.equals(BlockTypes.POWERED_COMPARATOR)) ||

            // It's all water...
            (a.equals(BlockTypes.WATER) && b.equals(BlockTypes.FLOWING_WATER)) ||
            (a.equals(BlockTypes.FLOWING_WATER) && b.equals(BlockTypes.WATER)) ||

            // It's all lava....
            (a.equals(BlockTypes.LAVA) && b.equals(BlockTypes.FLOWING_LAVA)) ||
            (a.equals(BlockTypes.FLOWING_LAVA) && b.equals(BlockTypes.LAVA)) ||

            // Natural flow.
            // Note: If moved back to EventUtil these will allow tracking player-caused flow
            // on a per-block basis. This would allow rollbacks of every single flow block.
            // However, it's incredibly heavy on the database and usually satisfied by a drain.
            // This will still allow liquids which break non-air blocks to be tracked
            (a.equals(BlockTypes.AIR) && (b.equals(BlockTypes.FLOWING_WATER) || b.equals(BlockTypes.FLOWING_LAVA))) ||

            // Crap that fell into lava
            (a.equals(BlockTypes.LAVA) && b.equals(BlockTypes.GRAVEL)) ||
            (a.equals(BlockTypes.FLOWING_LAVA) && b.equals(BlockTypes.GRAVEL)) ||
            (a.equals(BlockTypes.LAVA) && b.equals(BlockTypes.SAND)) ||
            (a.equals(BlockTypes.FLOWING_LAVA) && b.equals(BlockTypes.SAND)) ||

            // It's fire (which didn't burn anything)
            (a.equals(BlockTypes.FIRE) && b.equals(BlockTypes.AIR)) ||
            (a.equals(BlockTypes.AIR) && b.equals(BlockTypes.FIRE)) ||

            // Piston
            a.equals(BlockTypes.PISTON_EXTENSION) || b.equals(BlockTypes.PISTON_EXTENSION) ||
            a.equals(BlockTypes.PISTON_HEAD) || b.equals(BlockTypes.PISTON_HEAD) ||

            // You can't place air
            b.equals(BlockTypes.AIR)
        );
    }

    /**
     * Sponge's ChangeBlockEvent.Break covers a lot, but it also includes a lot
     * we don't want. So here we can setup checks to filter out block combinations.
     *
     * @param a BlockType original
     * @param b BlockType final
     * @return boolean True if combo should be rejected
     */
    public static boolean ignoreBreakCombination(BlockType a, BlockType b) {
        return (
            // You can't break these...
            a.equals(BlockTypes.FIRE) ||
            a.equals(BlockTypes.AIR) ||

            // Note, see "natural flow" comment above.
            ((a.equals(BlockTypes.FLOWING_WATER) || a.equals(BlockTypes.FLOWING_LAVA)) && b.equals(BlockTypes.AIR)) ||

            // Piston
            a.equals(BlockTypes.PISTON_EXTENSION) || b.equals(BlockTypes.PISTON_EXTENSION) ||
            a.equals(BlockTypes.PISTON_HEAD) || b.equals(BlockTypes.PISTON_HEAD)
        );
    }
}
