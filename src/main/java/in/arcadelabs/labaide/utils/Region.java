/*
 *                LabAide - Common utility library for our products.
 *                Copyright (C) 2022  ArcadeLabs Production.
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.labaide.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is a region/Region from one location to another. Used to create protected regions and things like WorldEdit.
 *
 * @author desht (Original code), Edward (Editor of code)
 */
@SuppressWarnings("unused")
public class Region implements Iterable<Block>, Cloneable, ConfigurationSerializable {
  protected final String worldName;
  protected final int x1, y1, z1;
  protected final int x2, y2, z2;

  /**
   * Construct a Region given two Location objects which represent any two corners of the Region.
   * Note: The 2 locations must be on the same world.
   *
   * @param location1 - One of the corners
   * @param location2 - The other corner
   */
  public Region(Location location1, Location location2) {
    if (!Objects.equals(location1.getWorld(), location2.getWorld()))
      throw new IllegalArgumentException("Locations must be on the same world");
    this.worldName = Objects.requireNonNull(location1.getWorld()).getName();
    this.x1 = Math.min(location1.getBlockX(), location2.getBlockX());
    this.y1 = Math.min(location1.getBlockY(), location2.getBlockY());
    this.z1 = Math.min(location1.getBlockZ(), location2.getBlockZ());
    this.x2 = Math.max(location1.getBlockX(), location2.getBlockX());
    this.y2 = Math.max(location1.getBlockY(), location2.getBlockY());
    this.z2 = Math.max(location1.getBlockZ(), location2.getBlockZ());
  }

  /**
   * Construct a one-block Region at the given Location of the Region.
   *
   * @param location1 location of the Region
   */
  public Region(Location location1) {
    this(location1, location1);
  }

  /**
   * Copy constructor.
   *
   * @param other - The Region to copy
   */
  public Region(Region other, String worldName) {
    this(worldName, other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
  }

  /**
   * Construct a Region in the given World and xyz co-ordinates
   *
   * @param world - The Region's world
   * @param x1    - X co-ordinate of corner 1
   * @param y1    - Y co-ordinate of corner 1
   * @param z1    - Z co-ordinate of corner 1
   * @param x2    - X co-ordinate of corner 2
   * @param y2    - Y co-ordinate of corner 2
   * @param z2    - Z co-ordinate of corner 2
   */
  public Region(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
    this.worldName = world.getName();
    this.x1 = Math.min(x1, x2);
    this.x2 = Math.max(x1, x2);
    this.y1 = Math.min(y1, y2);
    this.y2 = Math.max(y1, y2);
    this.z1 = Math.min(z1, z2);
    this.z2 = Math.max(z1, z2);
  }

  /**
   * Construct a Region in the given world name and xyz co-ordinates.
   *
   * @param worldName - The Region's world name
   * @param x1        - X co-ordinate of corner 1
   * @param y1        - Y co-ordinate of corner 1
   * @param z1        - Z co-ordinate of corner 1
   * @param x2        - X co-ordinate of corner 2
   * @param y2        - Y co-ordinate of corner 2
   * @param z2        - Z co-ordinate of corner 2
   */
  private Region(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
    this.worldName = worldName;
    this.x1 = Math.min(x1, x2);
    this.x2 = Math.max(x1, x2);
    this.y1 = Math.min(y1, y2);
    this.y2 = Math.max(y1, y2);
    this.z1 = Math.min(z1, z2);
    this.z2 = Math.max(z1, z2);
  }

  /**
   * Construct a Region using a map with the following keys: worldName, x1, x2, y1, y2, z1, z2
   *
   * @param map - The map of keys.
   */
  public Region(Map<String, Object> map) {
    this.worldName = (String) map.get("worldName");
    this.x1 = (Integer) map.get("x1");
    this.x2 = (Integer) map.get("x2");
    this.y1 = (Integer) map.get("y1");
    this.y2 = (Integer) map.get("y2");
    this.z1 = (Integer) map.get("z1");
    this.z2 = (Integer) map.get("z2");
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<>();
    map.put("worldName", this.worldName);
    map.put("x1", this.x1);
    map.put("y1", this.y1);
    map.put("z1", this.z1);
    map.put("x2", this.x2);
    map.put("y2", this.y2);
    map.put("z2", this.z2);
    return map;
  }

  /**
   * Get the Location of the lower northeast corner of the Region (minimum XYZ co-ordinates).
   *
   * @return Location of the lower northeast corner
   */
  public Location getLowerNE() {
    return new Location(this.getWorld(), this.x1, this.y1, this.z1);
  }

  /**
   * Get the Location of the upper southwest corner of the Region (maximum XYZ co-ordinates).
   *
   * @return Location of the upper southwest corner
   */
  public Location getUpperSW() {
    return new Location(this.getWorld(), this.x2, this.y2, this.z2);
  }

  /**
   * Get the blocks in the Region.
   *
   * @return The blocks in the Region
   */
  public List<Block> getBlocks() {
    Iterator<Block> blockIterator = this.iterator();
    List<Block> copy = new ArrayList<>();
    while (blockIterator.hasNext())
      copy.add(blockIterator.next());
    return copy;
  }

  /**
   * Get the the centre of the Region.
   *
   * @return Location at the centre of the Region
   */
  public Location getCenter() {
    int x1 = this.getUpperX() + 1;
    int y1 = this.getUpperY() + 1;
    int z1 = this.getUpperZ() + 1;
    return new Location(this.getWorld(), this.getLowerX() + (x1 - this.getLowerX()) / 2.0, this.getLowerY() + (y1 - this.getLowerY()) / 2.0, this.getLowerZ() + (z1 - this.getLowerZ()) / 2.0);
  }

  /**
   * Get the Region's world.
   *
   * @return The World object representing this Region's world
   * @throws IllegalStateException if the world is not loaded
   */
  public World getWorld() {
    World world = Bukkit.getWorld(this.worldName);
    if (world == null) throw new IllegalStateException("World '" + this.worldName + "' is not loaded");
    return world;
  }

  /**
   * Get the size of this Region along the X axis
   *
   * @return Size of Region along the X axis
   */
  public int getSizeX() {
    return (this.x2 - this.x1) + 1;
  }

  /**
   * Get the size of this Region along the Y axis
   *
   * @return Size of Region along the Y axis
   */
  public int getSizeY() {
    return (this.y2 - this.y1) + 1;
  }

  /**
   * Get the size of this Region along the Z axis
   *
   * @return Size of Region along the Z axis
   */
  public int getSizeZ() {
    return (this.z2 - this.z1) + 1;
  }

  /**
   * Get the minimum X co-ordinate of this Region
   *
   * @return the minimum X co-ordinate
   */
  public int getLowerX() {
    return this.x1;
  }

  /**
   * Get the minimum Y co-ordinate of this Region
   *
   * @return the minimum Y co-ordinate
   */
  public int getLowerY() {
    return this.y1;
  }

  /**
   * Get the minimum Z co-ordinate of this Region
   *
   * @return the minimum Z co-ordinate
   */
  public int getLowerZ() {
    return this.z1;
  }

  /**
   * Get the maximum X co-ordinate of this Region
   *
   * @return the maximum X co-ordinate
   */
  public int getUpperX() {
    return this.x2;
  }

  /**
   * Get the maximum Y co-ordinate of this Region
   *
   * @return the maximum Y co-ordinate
   */
  public int getUpperY() {
    return this.y2;
  }

  /**
   * Get the maximum Z co-ordinate of this Region
   *
   * @return the maximum Z co-ordinate
   */
  public int getUpperZ() {
    return this.z2;
  }

  /**
   * Get the Blocks at the eight corners of the Region.
   *
   * @return array of Block objects representing the Region corners
   */
  public Block[] corners() {
    Block[] blocks = new Block[8];
    World world = this.getWorld();
    blocks[0] = world.getBlockAt(this.x1, this.y1, this.z1);
    blocks[1] = world.getBlockAt(this.x1, this.y1, this.z2);
    blocks[2] = world.getBlockAt(this.x1, this.y2, this.z1);
    blocks[3] = world.getBlockAt(this.x1, this.y2, this.z2);
    blocks[4] = world.getBlockAt(this.x2, this.y1, this.z1);
    blocks[5] = world.getBlockAt(this.x2, this.y1, this.z2);
    blocks[6] = world.getBlockAt(this.x2, this.y2, this.z1);
    blocks[7] = world.getBlockAt(this.x2, this.y2, this.z2);
    return blocks;
  }

  /**
   * Expand the Region in the given direction by the given amount.  Negative amounts will shrink the Region in the given direction.  Shrinking a cuboid's face past the opposite face is not an error and will return a valid Region.
   *
   * @param direction - The direction in which to expand
   * @param amount    - The number of blocks by which to expand
   * @return A new Region expanded by the given direction and amount
   */
  public Region expand(RegionDirection direction, int amount) {
    return switch (direction) {
      case North -> new Region(this.worldName, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
      case South -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
      case East -> new Region(this.worldName, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
      case West -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
      case Down -> new Region(this.worldName, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
      case Up -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
      default -> throw new IllegalArgumentException("Invalid direction " + direction);
    };
  }

  /**
   * Shift the Region in the given direction by the given amount.
   *
   * @param direction - The direction in which to shift
   * @param amount    - The number of blocks by which to shift
   * @return A new Region shifted by the given direction and amount
   */
  public Region shift(RegionDirection direction, int amount) {
    return expand(direction, amount).expand(direction.opposite(), -amount);
  }

  /**
   * Outset (grow) the Region in the given direction by the given amount.
   *
   * @param direction - The direction in which to outset (must be Horizontal, Vertical, or Both)
   * @param amount    - The number of blocks by which to outset
   * @return A new Region outset by the given direction and amount
   */
  public Region outset(RegionDirection direction, int amount) {
    return switch (direction) {
      case Horizontal ->
              expand(RegionDirection.North, amount).expand(RegionDirection.South, amount).expand(RegionDirection.East, amount).expand(RegionDirection.West, amount);
      case Vertical -> expand(RegionDirection.Down, amount).expand(RegionDirection.Up, amount);
      case Both -> outset(RegionDirection.Horizontal, amount).outset(RegionDirection.Vertical, amount);
      default -> throw new IllegalArgumentException("Invalid direction " + direction);
    };
  }

  /**
   * Inset (shrink) the Region in the given direction by the given amount.  Equivalent
   * to calling outset() with a negative amount.
   *
   * @param direction - The direction in which to inset (must be Horizontal, Vertical, or Both)
   * @param amount    - The number of blocks by which to inset
   * @return A new Region inset by the given direction and amount
   */
  public Region inset(RegionDirection direction, int amount) {
    return this.outset(direction, -amount);
  }

  /**
   * Return true if the point at (x,y,z) is contained within this Region.
   *
   * @param x - The X co-ordinate
   * @param y - The Y co-ordinate
   * @param z - The Z co-ordinate
   * @return true if the given point is within this Region, false otherwise
   */
  public boolean contains(int x, int y, int z) {
    return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
  }

  /**
   * Check if the given Block is contained within this Region.
   *
   * @param block - The Block to check for
   * @return true if the Block is within this Region, false otherwise
   */
  public boolean contains(Block block) {
    return this.contains(block.getLocation());
  }

  /**
   * Check if the given Location is contained within this Region.
   *
   * @param location - The Location to check for
   * @return true if the Location is within this Region, false otherwise
   */
  public boolean contains(Location location) {
    if (!this.worldName.equals(Objects.requireNonNull(location.getWorld()).getName())) return false;
    return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  /**
   * Get the volume of this Region.
   *
   * @return The Region volume, in blocks
   */
  public int getVolume() {
    return this.getSizeX() * this.getSizeY() * this.getSizeZ();
  }

  /**
   * Get the average light level of all empty (air) blocks in the Region.  Returns 0 if there are no empty blocks.
   *
   * @return The average light level of this Region
   */
  public byte getAverageLightLevel() {
    long total = 0;
    int n = 0;
    for (Block b : this) {
      if (b.isEmpty()) {
        total += b.getLightLevel();
        ++n;
      }
    }
    return n > 0 ? (byte) (total / n) : 0;
  }

  /**
   * Contract the Region, returning a Region with any air around the edges removed, just large enough to include all non-air blocks.
   *
   * @return A new Region with no external air blocks
   */
  public Region contract() {
    return this.contract(RegionDirection.Down).contract(RegionDirection.South).contract(RegionDirection.East).contract(RegionDirection.Up).contract(RegionDirection.North).contract(RegionDirection.West);
  }

  /**
   * Contract the Region in the given direction, returning a new Region which has no exterior empty space.
   * E.g. A direction of Down will push the top face downwards as much as possible.
   *
   * @param dir - The direction in which to contract
   * @return A new Region contracted in the given direction
   */
  public Region contract(RegionDirection dir) {
    Region face = getFace(dir.opposite());
    switch (dir) {
      case Down -> {
        while (face.containsOnly(Material.AIR) && face.getLowerY() > this.getLowerY()) {
          face = face.shift(RegionDirection.Down, 1);
        }
        return new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, face.getUpperY(), this.z2);
      }
      case Up -> {
        while (face.containsOnly(Material.AIR) && face.getUpperY() < this.getUpperY()) {
          face = face.shift(RegionDirection.Up, 1);
        }
        return new Region(this.worldName, this.x1, face.getLowerY(), this.z1, this.x2, this.y2, this.z2);
      }
      case North -> {
        while (face.containsOnly(Material.AIR) && face.getLowerX() > this.getLowerX()) {
          face = face.shift(RegionDirection.North, 1);
        }
        return new Region(this.worldName, this.x1, this.y1, this.z1, face.getUpperX(), this.y2, this.z2);
      }
      case South -> {
        while (face.containsOnly(Material.AIR) && face.getUpperX() < this.getUpperX()) {
          face = face.shift(RegionDirection.South, 1);
        }
        return new Region(this.worldName, face.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
      }
      case East -> {
        while (face.containsOnly(Material.AIR) && face.getLowerZ() > this.getLowerZ()) {
          face = face.shift(RegionDirection.East, 1);
        }
        return new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, face.getUpperZ());
      }
      case West -> {
        while (face.containsOnly(Material.AIR) && face.getUpperZ() < this.getUpperZ()) {
          face = face.shift(RegionDirection.West, 1);
        }
        return new Region(this.worldName, this.x1, this.y1, face.getLowerZ(), this.x2, this.y2, this.z2);
      }
      default -> throw new IllegalArgumentException("Invalid direction " + dir);
    }
  }

  /**
   * Get the Region representing the face of this Region.  The resulting Region will be one block thick in the axis perpendicular to the requested face.
   *
   * @param dir - which face of the Region to get
   * @return The Region representing this Region's requested face
   */
  public Region getFace(RegionDirection dir) {
    return switch (dir) {
      case Down -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
      case Up -> new Region(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
      case North -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
      case South -> new Region(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
      case East -> new Region(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
      case West -> new Region(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
      default -> throw new IllegalArgumentException("Invalid direction " + dir);
    };
  }

  /**
   * Check if the Region contains only blocks of the given type
   *
   * @param block - The block ID to check for
   * @return true if this Region contains only blocks of the given type
   */
  public boolean containsOnly(Material block) {
    for (Block b : this) {
      if (!block.equals(b.getType())) return false;
    }
    return true;
  }

  /**
   * Get the Region big enough to hold both this Region and the given one.
   *
   * @param other - The other cuboid.
   * @return A new Region large enough to hold this Region and the given Region
   */
  public Region getBoundingRegion(Region other) {
    if (other == null) return this;

    int xMin = Math.min(this.getLowerX(), other.getLowerX());
    int yMin = Math.min(this.getLowerY(), other.getLowerY());
    int zMin = Math.min(this.getLowerZ(), other.getLowerZ());
    int xMax = Math.max(this.getUpperX(), other.getUpperX());
    int yMax = Math.max(this.getUpperY(), other.getUpperY());
    int zMax = Math.max(this.getUpperZ(), other.getUpperZ());

    return new Region(this.worldName, xMin, yMin, zMin, xMax, yMax, zMax);
  }

  /**
   * Get a block relative to the lower NE point of the Region.
   *
   * @param x - The X co-ordinate
   * @param y - The Y co-ordinate
   * @param z - The Z co-ordinate
   * @return The block at the given position
   */
  public Block getRelativeBlock(int x, int y, int z) {
    return this.getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
  }

  /**
   * Get a block relative to the lower NE point of the Region in the given World.  This
   * version of getRelativeBlock() should be used if being called many times, to avoid
   * excessive calls to getWorld().
   *
   * @param w - The world
   * @param x - The X co-ordinate
   * @param y - The Y co-ordinate
   * @param z - The Z co-ordinate
   * @return The block at the given position
   */
  public Block getRelativeBlock(World w, int x, int y, int z) {
    return w.getBlockAt(this.x1 + x, y1 + y, this.z1 + z);
  }

  /**
   * Get a list of the chunks which are fully or partially contained in this cuboid.
   *
   * @return A list of Chunk objects
   */
  public List<Chunk> getChunks() {
    List<Chunk> res = new ArrayList<>();

    World w = this.getWorld();
    int x1 = this.getLowerX() & ~0xf;
    int x2 = this.getUpperX() & ~0xf;
    int z1 = this.getLowerZ() & ~0xf;
    int z2 = this.getUpperZ() & ~0xf;
    for (int x = x1; x <= x2; x += 16) {
      for (int z = z1; z <= z2; z += 16) {
        res.add(w.getChunkAt(x >> 4, z >> 4));
      }
    }
    return res;
  }

  public Iterator<Block> iterator() {
    return new RegionIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
  }

  @Override
  public Region clone() throws CloneNotSupportedException {
    Region clone = (Region) super.clone();
    return new Region(this, worldName);
  }

  @Override
  public String toString() {
    return "Region: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2;
  }

  public enum RegionDirection {
    North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown;

    public RegionDirection opposite() {
      return switch (this) {
        case North -> South;
        case East -> West;
        case South -> North;
        case West -> East;
        case Horizontal -> Vertical;
        case Vertical -> Horizontal;
        case Up -> Down;
        case Down -> Up;
        case Both -> Both;
        default -> Unknown;
      };
    }

  }

  public static class RegionIterator implements Iterator<Block> {
    private final World w;
    private final int baseX;
    private final int baseY;
    private final int baseZ;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private int x, y, z;

    public RegionIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
      this.w = w;
      this.baseX = x1;
      this.baseY = y1;
      this.baseZ = z1;
      this.sizeX = Math.abs(x2 - x1) + 1;
      this.sizeY = Math.abs(y2 - y1) + 1;
      this.sizeZ = Math.abs(z2 - z1) + 1;
      this.x = this.y = this.z = 0;
    }

    public boolean hasNext() {
      return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
    }

    public Block next() {
      Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
      if (++x >= this.sizeX) {
        this.x = 0;
        if (++this.y >= this.sizeY) {
          this.y = 0;
          ++this.z;
        }
      }
      return b;
    }

    public void remove() {
    }
  }

}