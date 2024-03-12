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

package top.cmarco.nuclearbomb.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import top.cmarco.nuclearbomb.NuclearBomb;
import top.cmarco.nuclearbomb.bomb.BombType;
import top.cmarco.nuclearbomb.math.ExplosionMath;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class NuclearBombCommand implements CommandExecutor {

    private final NuclearBomb plugin;

    public NuclearBombCommand(NuclearBomb plugin) {
        this.plugin = plugin;

        if (plugin.getServer().getVersion().toLowerCase().contains("beta")) {
            isBeta = true;
        }

        try {
            Method m = plugin.getServer().getClass().getMethod("getJesusCraftVersion");
            if (m != null) {
                isJesusCraft = true;
            }
        } catch (NoSuchMethodException ignored) {
            isJesusCraft = false;
        }
    }

    private static boolean isJesusCraft = false;
    private static boolean isBeta = false;

    public static String color(String s) {
        if (isJesusCraft) {
            return ChatColor.translateAlternateColorCodes('&', s);
        } else {
            return s = s.replace("&", "§");
        }
    }

    private static void help(Player player) {
        player.sendMessage(color("&8[&cNuclearBomb&8]&7: Help Page"));
        player.sendMessage(color("&7Get your desired type of Nuclear Bomb:"));
        player.sendMessage(color("&7/&6nuclearbomb &8<&6type&8>"));
        player.sendMessage(color("&7Available bombs: &6MARK_1&7, &6MARK_3&7, &6AN602"));
    }

    private static Method damageMethod = null;

    private static void damagePlayer(LivingEntity player, int amount) {
        try {
            if (damageMethod == null) {
                if (isJesusCraft || isBeta) {
                    damageMethod = Player.class.getMethod("damage", int.class);
                } else {
                    damageMethod = Player.class.getMethod("damage", double.class);
                }
            }

            if (isJesusCraft) {
                damageMethod.invoke(player, amount);
            } else {
                damageMethod.invoke(player, (double) amount);
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException  exception) {
            exception.printStackTrace();
        }
    }

    private static void doCrazyExplosion(Location origin, float radius) {
        World world = origin.getWorld();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        Collection<LivingEntity> onlinePlayers = origin.getWorld().getLivingEntities();

        onlinePlayers.stream().filter(
                p -> p.getLocation().distanceSquared(origin) < Math.pow(3.0*radius , 2.0)
        ).forEach(
                p -> {
                    if (p instanceof Player) {
                        Player player = (Player) p;
                        if ((isJesusCraft && player.getGamemode() == GameMode.SURVIVAL) || !player.hasPermission("nuclearbomb.immune")) {
                            damagePlayer(player, 1000);
                        }
                    } else {
                        damagePlayer(p, 1000);
                    }
                }
        );

        onlinePlayers.stream().filter(
                p -> p.getLocation().distanceSquared(origin) < Math.pow(10*radius ,2d)
        ).forEach(
                p -> {
                    if (p instanceof Player) {
                        Player player = (Player) p;
                        if ((isJesusCraft && player.getGamemode() == GameMode.SURVIVAL) || !player.hasPermission("nuclearbomb.immune")) {
                            p.setFireTicks(205);
                        }
                    } else {
                        p.setFireTicks(205);
                    }
                }
        );

        onlinePlayers.stream().filter(
                p -> p.getLocation().distanceSquared(origin) < Math.pow(20*radius ,2d)
        ).forEach(
                p -> {
                    if (p instanceof Player) {
                        Player player = (Player) p;
                        if ((isJesusCraft && player.getGamemode() == GameMode.SURVIVAL) || !player.hasPermission("nuclearbomb.immune")) {
                            damagePlayer(player, 8);
                        }
                    } else {
                        damagePlayer(p, 8);
                    }
                }
        );

        ExplosionMath.explosion(radius, origin, threadLocalRandom);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(color("&8[&cNuclearBomb&8]&7: &4Command is only for Players."));
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("nuclearbomb.get")) {
            player.sendMessage(color("&8[&cNuclearBomb&8]&7: &4Missing permission nuclearbomb.get"));
            return true;
        }

        if (args.length != 1) {
            help(player);
            return true;
        }

        BombType[] bombs = BombType.values();
        BombType foundBombType = Arrays.stream(bombs).filter(bomb -> bomb.name().equalsIgnoreCase(args[0])
                || bomb.getNickname().replace(" ", "_").equalsIgnoreCase(args[0])).findFirst().orElse(null);

        if (foundBombType == null) {
            player.sendMessage(color("&8[&cNuclearBomb&8]&7: &4Unknown bomb type &l" + args[0]));
            return true;
        }

        Location playerLoc = player.getLocation();
        World playerWorld = playerLoc.getWorld();

        TNTPrimed tntPrimed = (TNTPrimed) playerWorld.spawn(playerLoc, TNTPrimed.class);
        tntPrimed.setFuseTicks(100);

        final AtomicInteger counter = new AtomicInteger(5);

        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            player.sendMessage(color("&8[&cNuclearBomb&8]&7: &4Detonation in " + counter.getAndDecrement() + " seconds."));
        }, 1L, 20L);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            plugin.getServer().getScheduler().cancelTask(task);
            doCrazyExplosion(playerLoc, foundBombType.getExplosionRadius());
        }, 20L * 5);

        return true;
    }
}
