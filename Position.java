package org.ady.game.model;

import com.rs.utils.Utils;
import com.rs.Settings;
import com.rs.game.RSLocation;


public class Position {

	//private static final long serialVersionUID = -6567346497259686765L;

	private short x, y;
	private byte plane;

	public Position(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public Position(Position tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}

	public Position(Position tile, int randomize) {
		this.x = (short) (tile.x + Utils.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Utils.getRandom(randomize * 2) - randomize);
		this.plane = tile.plane;
	}

	public Position(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.plane = (byte) (hash >> 28);
	}

	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		plane += planeOffset;
	}

	public final void setLocation(Position tile) {
		setLocation(tile.x, tile.y, tile.plane);
	}

	public final void setLocation(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public int getX() {
		return x;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getY() {
		return y;
	}

	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(Position tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalY(Position tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalX(Position tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(Position tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getRegionBitmap() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}

	public int getCoordsBitmap() {
		return y + (x << 14) + (plane << 28);
	}

	public boolean withinDistance(Position tile, int distance) {
		if (tile.plane != plane)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}

	public boolean withinDistance(Position tile) {
		if (tile.plane != plane)
			return false;
		// int deltaX = tile.x - x, deltaY = tile.y - y;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;// deltaX
																		// <= 14
																		// &&
																		// deltaX
																		// >=
																		// -15
																		// &&
																		// deltaY
																		// <= 14
																		// &&
																		// deltaY
																		// >=
																		// -15;
	}

	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public Position transform(int x, int y, int plane) {
		return new Position(this.x + x, this.y + y, this.plane + plane);
	}

	/**
	 * Checks if this world tile's coordinates match the other world tile.
	 * @param other The world tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(Position other) {
		return x == other.x && y == other.y && plane == other.plane;
	}

	public Position randomise(int bound) {
		return RSLocation.random(this, bound);
	}
	
	public long getFixedUniqueId(){
		return x * 2311 + y * 23113 + plane * 33;
	}
	
	public Position() {
		//dummy
	}
}
