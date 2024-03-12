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

package top.cmarco.nuclearbomb;

import org.bukkit.plugin.java.JavaPlugin;
import top.cmarco.nuclearbomb.command.NuclearBombCommand;

public final class NuclearBomb extends JavaPlugin {
    NuclearBombCommand nuclearBombCommand = null;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        this.nuclearBombCommand = new NuclearBombCommand(this);
        super.getCommand("nuclearbomb").setExecutor(nuclearBombCommand);
    }

}
