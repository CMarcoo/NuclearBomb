/*
 *     NuclearBomb - Add Nuclear Bombs to your Minecraft Server
 *     Copyright © 2024  CMarco
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.cmarco.nuclearbomb.math;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.concurrent.ThreadLocalRandom;

public final class ExplosionMath {

    private ExplosionMath() {
        throw new RuntimeException();
    }

    public static void explosion(float radius, Location origin, ThreadLocalRandom random) {
        final int roundedRadius = Math.round(radius);
        for (int x = -roundedRadius; x <= roundedRadius; x++) {
            for (int z = -roundedRadius; z <= roundedRadius; z++) {
                for (int y = -roundedRadius; y <= roundedRadius; y++) {
                    double distanceSquared = x * x + y * y + z * z;

                    // Check if the block is within the specified range and outside the core of the sphere
                    if (distanceSquared <= radius*radius) {
                        int blockX = origin.getBlockX() + x;
                        int blockY = origin.getBlockY() + y;
                        int blockZ = origin.getBlockZ() + z;

                        Block block = origin.getWorld().getBlockAt(blockX, blockY, blockZ);

                        // Do something with the block, e.g., set it to air for a hollow effect
                        Material setTo = Material.AIR;

                        if (Math.abs(y) >= roundedRadius -2.00 && random.nextInt(0 , 100) < 16) {
                            setTo = Material.FIRE;
                        }

                        block.setType(setTo);
                    }
                }
            }
        }
    }
}
