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

package top.cmarco.nuclearbomb.bomb;

public enum BombType {

    MARK_1("Little Boy", 13.0d, 15.0f,
            "Little Boy was the first atomic bomb used in warfare.",
            "It was dropped on Hiroshima, Japan, on August 6, 1945."),

    MARK_3("Fat Man", 21.0d, 55.0f,
            "Fat Man was the second atomic bomb used in warfare.",
            "It was dropped on Nagasaki, Japan, on August 9, 1945."),

    AN602("Tsar Bomba", 50.0e1d, 75.45f,
            "Tsar Bomba is the most powerful nuclear bomb ever detonated.",
            "It was a Soviet test conducted on October 30, 1961, in the Arctic Circle.")
    ;

    private final String nickname;
    private final String[] lore;
    private final double kilotons;
    private final float explosionRadius;

    BombType(String nickname, double kilotons, float explosionRadius, String... lore) {
        this.nickname = nickname;
        this.explosionRadius = explosionRadius;
        this.kilotons = kilotons;
        this.lore = lore;
    }

    public double getKilotons() {
        return kilotons;
    }

    public String getNickname() {
        return nickname;
    }

    public String[] getLore() {
        return lore;
    }

    public float getExplosionRadius() {
        return explosionRadius;
    }
}

