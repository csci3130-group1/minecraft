/*******************************************************************************
 * LogicHelper.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.gis.logic;

import com.gis.logic.Point3D;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Defines many different helper methods.
 */
public final class LogicHelper
{
	/**
	 * Gets whether or not there is a certain block close to the entity
	 * provided.
	 * 
	 * @param entity
	 *            The entity being used as a base point for searching.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance from the entity to search.
	 * 
	 * @return True if the specified block is within the maximum distance of the
	 *         specified entity. False if otherwise.
	 */
	public static boolean isBlockNearby(Entity entity, Block block, int maxDistanceAway)
	{
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			if (entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov) == block) { return true; }

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == 3)
			{
				break;
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				// This makes the whole loop restart with yMov increased by one,
				// getting blocks another level above the entity.
				yMov++;
				zMov = 0 - maxDistanceAway;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}

		return false;
	}

	/**
	 * Gets the distance from one entity to another.
	 * 
	 * @param entity1
	 *            An entity whose position will be used with the second provided
	 *            entity to find the distance between them.
	 * @param entity2
	 *            An entity whose position will be used with the first provided
	 *            entity to find the distance between them.
	 * 
	 * @return The distance between the two provided entities.
	 */
	public static double getDistanceToEntity(Entity entity1, Entity entity2)
	{
		return getDistanceToXYZ(entity1.posX, entity1.posY, entity1.posZ, entity2.posX, entity2.posY, entity2.posZ);
	}

	/**
	 * Returns the coordinates of the first block found near the entity that is
	 * the provided block. Scanning for the block starts 3 blocks above the
	 * entity and moves down.
	 * 
	 * @param entity
	 *            The entity used as a base point to search for a block.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the entity to search for
	 *            blocks.
	 * 
	 * @return Point3D object containing the coordinates of the first block
	 *         found.
	 */
	public static Point3D getNearbyBlock_StartAtTop(Entity entity, Block block, int maxDistanceAway)
	{
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = 3;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			final Block currentBlock = entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov);

			if (currentBlock == block) { return new Point3D(x + xMov, y + yMov, z + zMov); }

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == -3)
			{
				break;
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov--;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
				continue;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}

		return null;
	}

	/**
	 * Returns the coordinates of the first block found near the entity that is
	 * the specified block. Scanning for the block starts 3 blocks below the
	 * entity and moves up.
	 * 
	 * @param entity
	 *            The entity used as a base point to search for a block.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the entity to search for
	 *            blocks.
	 * 
	 * @return Point3D object containing the coordinates of the first block
	 *         found.
	 */
	public static Point3D getNearbyBlock_StartAtBottom(Entity entity, Block block, int maxDistanceAway)
	{
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			final Block currentBlock = entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov);

			if (currentBlock == block) { return new Point3D(x + xMov, y + yMov, z + zMov); }

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == 3)
			{
				break;
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
				continue;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}

		return null;
	}

	/**
	 * Returns the coordinates of the blocks found near the entity that are the
	 * specified block. Scanning for the blocks starts 3 blocks below the entity
	 * and moves up.
	 * 
	 * @param entity
	 *            The entity used as a base point to search for a block.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the entity to search for
	 *            blocks.
	 * 
	 * @return List of Point3D objects representing the coordinates of each
	 *         found block's location.
	 */
	public static List<Point3D> getNearbyBlocks_StartAtBottom(Entity entity, Block block, int maxDistanceAway)
	{
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		final List<Point3D> pointsList = new ArrayList<Point3D>();

		while (true)
		{
			final Block currentBlock = entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov);

			if (currentBlock == block)
			{
				pointsList.add(new Point3D(x + xMov, y + yMov, z + zMov));
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == 3)
			{
				break;
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
				continue;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}

		return pointsList;
	}

	/**
	 * Returns the coordinates of the blocks found near the entity that are
	 * specified block. Scanning for the blocks starts 3 blocks below the entity
	 * and moves up.
	 * 
	 * @param entity
	 *            The entity used as a base point to search for a block.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the entity to search for
	 *            blocks.
	 * @param maxY
	 *            How high away from the entity's current Y axis to scan.
	 * 
	 * @return List of Point3D objects representing the coordinates of each
	 *         found block's location.
	 */
	public static List<Point3D> getNearbyBlocks_StartAtBottom(Entity entity, Block block, int maxDistanceAway, int maxY)
	{
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		final List<Point3D> pointsList = new ArrayList<Point3D>();

		while (true)
		{
			final Block currentBlock = entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov);

			if (currentBlock == block)
			{
				pointsList.add(new Point3D(x + xMov, y + yMov, z + zMov));
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == maxY)
			{
				break;
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
				continue;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}

		return pointsList;
	}

	/**
	 * Gets list of coordinates containing the coordinates of all of the land
	 * that can be turned into tilled field.
	 * 
	 * @param entity
	 *            The entity that is farming.
	 * @param startCoordinatesX
	 *            The x coordinate at which the entity began farming.
	 * @param startCoordinatesY
	 *            The y coordinate at which the entity began farming.
	 * @param startCoordinatesZ
	 *            The z coordinate at which the entity began farming.
	 * @param areaX
	 *            The x size of the land that will be farmed.
	 * @param areaZ
	 *            The z size of the land that will be farmed.
	 * 
	 * @return List containing coordinates of valid farmable land.
	 */
	public static List<Point3D> getNearbyFarmableLand(Entity entity, int startCoordinatesX, int startCoordinatesY, int startCoordinatesZ, int areaX, int areaZ)
	{
		final List<Point3D> pointsList = new LinkedList<Point3D>();

		final int x = startCoordinatesX;
		final int y = startCoordinatesY;
		final int z = startCoordinatesZ;
		int xMov = 0;
		final int yMov = -1; // Look at the block underneath the entity.
		int zMov = 0;

		while (true)
		{
			final Block block = entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov);

			if (block == Blocks.grass || block == Blocks.dirt)
			{
				if (entity.worldObj.isAirBlock(x + xMov, y + yMov + 1, z + zMov))
				{
					pointsList.add(new Point3D(x + xMov, y + yMov, z + zMov));
				}
			}

			if (zMov == areaZ - 1 && xMov == areaX - 1)
			{
				break;
			}

			if (xMov == areaX - 1)
			{
				zMov++;
				xMov = 0;
				continue;
			}

			xMov++;
		}

		return pointsList;
	}

	/**
	 * Uses 3D distance formula to determine the distance between two 3d
	 * coordinates.
	 * 
	 * @param x1
	 *            An entity's x position.
	 * @param y1
	 *            An entity's y position.
	 * @param z1
	 *            An entity's z position.
	 * @param x2
	 *            Another entity's x position.
	 * @param y2
	 *            Another entity's y position.
	 * @param z2
	 *            Another entity's z position.
	 * 
	 * @return Double expressing the distance between the two 3d coordinates.
	 */
	public static double getDistanceToXYZ(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		final double deltaX = x2 - x1;
		final double deltaY = y2 - y1;
		final double deltaZ = z2 - z1;

		return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	/**
	 * Uses 3D distance formula to determine the distance between two 3d
	 * coordinates.
	 * 
	 * @param point1
	 * @param point2
	 * 
	 * @return Double expressing the distance between the two 3d coordinates.
	 */
	public static double getDistanceToPoint(Point3D point1, Point3D point2)
	{
		final double deltaX = point2.dPosX - point1.dPosX;
		final double deltaY = point2.dPosY - point1.dPosY;
		final double deltaZ = point2.dPosZ - point1.dPosZ;

		return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	/**
	 * Gets the coordinates of each block close to the entity that is the
	 * specified block.
	 * 
	 * @param entity
	 *            The entity being used as a base point to search for a block.
	 * @param block
	 *            The block that is being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the player to search for
	 *            blocks.
	 * 
	 * @return List containing the coordinates of each block with the provided
	 *         ID within the specified distance of the entity.
	 */
	public static List<Point3D> getNearbyBlockCoordinates(Entity entity, Block block, int maxDistanceAway)
	{
		final List<Point3D> CoordinatesList = new LinkedList<Point3D>();

		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = 0 - maxDistanceAway;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			if (entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov) == block)
			{
				CoordinatesList.add(new Point3D(x + xMov, y + yMov, z + zMov));
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == maxDistanceAway)
			{
				return CoordinatesList;
			}

			else if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}
	}

	/**
	 * Gets the coordinates of each block close to the entity that is the
	 * specified block and has the specified metadata.
	 * 
	 * @param entity
	 *            The entity being used as a base point to search for a block.
	 * @param block
	 *            The ID block that is being searched for.
	 * @param metadata
	 *            The desired metadata value of the block.
	 * @param maxDistanceAway
	 *            The maximum distance away from the entity to search for
	 *            blocks.
	 * 
	 * @return List containing the coordinates of each block with the provided
	 *         ID within the specified distance of the entity.
	 */
	public static List<Point3D> getNearbyBlockCoordinatesWithMetadata(Entity entity, Block block, int metadata, int maxDistanceAway)
	{
		final List<Point3D> CoordinatesList = new LinkedList<Point3D>();

		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		int xMov = 0 - maxDistanceAway;
		int yMov = 0 - maxDistanceAway;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			if (entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov) == block)
			{
				if (entity.worldObj.getBlockMetadata(x + xMov, y + yMov, z + zMov) == metadata)
				{
					CoordinatesList.add(new Point3D(x + xMov, y + yMov, z + zMov));
				}
			}

			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == maxDistanceAway)
			{
				return CoordinatesList;
			}

			else if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
			}

			if (xMov == maxDistanceAway)
			{
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}
	}

	/**
	 * Gets a list containing instances of all entities around the specified
	 * entity up to the specified distance away.
	 * 
	 * @param entity
	 *            The entity that is being used as the starting point to search
	 *            for more entities.
	 * @param maxDistanceAway
	 *            The maximum distance from the specified entity that should be
	 *            searched.
	 * 
	 * @return List containing all entities within the specified distance of the
	 *         specified entity.
	 */
	public static List<Entity> getAllEntitiesWithinDistanceOfEntity(Entity entity, int maxDistanceAway)
	{
		final double posX = entity.posX;
		final double posY = entity.posY;
		final double posZ = entity.posZ;

		final List<Entity> entitiesAroundMe = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, AxisAlignedBB.getBoundingBox(posX - maxDistanceAway, posY - maxDistanceAway, posZ - maxDistanceAway, posX + maxDistanceAway, posY + maxDistanceAway, posZ + maxDistanceAway));
		return entitiesAroundMe;
	}

	/**
	 * Gets a list containing instances of all entities around the specified
	 * coordinates up to the specified distance away.
	 * 
	 * @param worldObj
	 *            The world that the entity should be in.
	 * @param posX
	 *            The X position to begin searching at.
	 * @param posY
	 *            The Y position to begin searching at.
	 * @param posZ
	 *            The Z position to begin searching at.
	 * @param maxDistanceAway
	 *            The maximum distance away from the points to search.
	 * 
	 * @return List containing all entities within the specified distance of the
	 *         specified entity.
	 */
	public static List<Entity> getAllEntitiesWithinDistanceOfCoordinates(World worldObj, double posX, double posY, double posZ, int maxDistanceAway)
	{
		final List<Entity> entitiesAroundMe = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX - maxDistanceAway, posY - maxDistanceAway, posZ - maxDistanceAway, posX + maxDistanceAway, posY + maxDistanceAway, posZ + maxDistanceAway));
		return entitiesAroundMe;
	}

	/**
	 * Gets a list containing instances of all the entities of the specified
	 * type up to the specified distance away.
	 * 
	 * @param entity
	 *            The entity that is being used as the starting point to search
	 *            for more entities.
	 * @param entityType
	 *            The type of entity that should be put in the list returned.
	 * @param maxDistanceAway
	 *            The maximum distance from the specified entity that should be
	 *            searched.
	 * 
	 * @return Object containing a list of the entities matching the specified
	 *         search credentials. List is expected to be cast to the
	 *         appropriate type of list.
	 */
	public static Object getAllEntitiesOfTypeWithinDistanceOfEntity(Entity entity, Class entityType, int maxDistanceAway)
	{
		try
		{
			final double posX = entity.posX;
			final double posY = entity.posY;
			final double posZ = entity.posZ;

			final List<Entity> validEntities = new ArrayList();
			final List<Entity> entitiesAroundMe = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, AxisAlignedBB.getBoundingBox(posX - maxDistanceAway, posY - maxDistanceAway, posZ - maxDistanceAway, posX + maxDistanceAway, posY + maxDistanceAway, posZ + maxDistanceAway));

			for (final Entity entityNearMe : entitiesAroundMe)
			{
				try
				{
					entityType.cast(entityNearMe);
					validEntities.add(entityNearMe);
				}

				catch (final ClassCastException e)
				{
					continue;
				}
			}

			return validEntities;
		}

		catch (final ConcurrentModificationException e)
		{
			return null;
		}

		catch (final NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * Gets the distance the entity is away from the player.
	 * 
	 * @param player
	 *            The player to find the distance from.
	 * @param entity
	 *            The entity whose position is being compared to the player's.
	 * 
	 * @return Floating point decimal expressing the distance that the provided
	 *         entity is from the player.
	 */
	public static float getEntityDistanceFromPlayer(EntityPlayer player, Entity entity)
	{
		final float deltaX = (float) (player.posX - entity.posX);
		final float deltaY = (float) (player.posY - entity.posY);
		final float deltaZ = (float) (player.posZ - entity.posZ);

		return MathHelper.sqrt_float(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	/**
	 * Gets the coordinates of a random block of the specified type within 10
	 * blocks away from the provided entity.
	 * 
	 * @param entity
	 *            The entity being used as a base point to start the search.
	 * @param block
	 *            The block being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the point that the search
	 *            should scan.
	 * 
	 * @return An coordinates object containing the coordinates of the randomly
	 *         selected block.
	 */
	public static Point3D getRandomNearbyBlockCoordinatesOfType(Entity entity, Block block, int maxDistanceAway)
	{
		// Create a list to store valid coordinates and specify the maximum
		// distance away.
		final List<Point3D> validCoordinatesList = new LinkedList<Point3D>();

		// Assign entity's position.
		final int x = (int) entity.posX;
		final int y = (int) entity.posY;
		final int z = (int) entity.posZ;

		// Assign x, y, and z movement.
		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			// If the block ID at the following coordinates matches the block ID
			// being searched for...
			if (entity.worldObj.getBlock(x + xMov, y + yMov, z + zMov) == block)
			{
				// Add the block's coordinates to the coordinates list.
				validCoordinatesList.add(new Point3D(x + xMov, y + yMov, z + zMov));
			}

			// If z and x movement has reached the maximum distance and y
			// movement has reached 2, then return the list as searching has
			// completed.
			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == 2)
			{
				if (validCoordinatesList.size() > 0)
				{
					return validCoordinatesList.get(entity.worldObj.rand.nextInt(validCoordinatesList.size()));
				}

				else
				{
					return null;
				}
			}

			// But if y movement isn't 2 then searching should continue.
			else if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				// Increase y movement by 1 and reset x and z movement, bringing
				// the search up another level.
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
			}

			// If x movement has reached the maximum distance...
			if (xMov == maxDistanceAway)
			{
				// Increase z movement by one and reset x movement, restarting
				// the loop.
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}
	}

	/**
	 * Gets the coordinates of a random block of the specified type.
	 * 
	 * @param world
	 *            The world the search will take place in.
	 * @param point
	 *            The base point at which to start the search.
	 * @param block
	 *            The block being searched for.
	 * @param maxDistanceAway
	 *            The maximum distance away from the point that the search
	 *            should scan.
	 * 
	 * @return An coordinates object containing the coordinates of the randomly
	 *         selected block.
	 */
	public static Point3D getRandomNearbyBlockCoordinatesOfType(World world, Point3D point, Block block, int maxDistanceAway)
	{
		// Create a list to store valid coordinates and specify the maximum
		// distance away.
		final List<Point3D> validCoordinatesList = new LinkedList<Point3D>();

		// Assign entity's position.
		final int x = point.iPosX;
		final int y = point.iPosY;
		final int z = point.iPosZ;

		// Assign x, y, and z movement.
		int xMov = 0 - maxDistanceAway;
		int yMov = -3;
		int zMov = 0 - maxDistanceAway;

		while (true)
		{
			// If the block ID at the following coordinates matches the block ID
			// being searched for...
			if (world.getBlock(x + xMov, y + yMov, z + zMov) == block)
			{
				// Add the block's coordinates to the coordinates list.
				validCoordinatesList.add(new Point3D(x + xMov, y + yMov, z + zMov));
			}

			// If z and x movement has reached the maximum distance and y
			// movement has reached 2, then return the list as searching has
			// completed.
			if (zMov == maxDistanceAway && xMov == maxDistanceAway && yMov == 2)
			{
				if (!validCoordinatesList.isEmpty())
				{
					return validCoordinatesList.get(world.rand.nextInt(validCoordinatesList.size()));
				}

				else
				{
					return null;
				}
			}

			// But if y movement isn't 2 then searching should continue.
			else if (zMov == maxDistanceAway && xMov == maxDistanceAway)
			{
				// Increase y movement by 1 and reset x and z movement, bringing
				// the search up another level.
				yMov++;
				xMov = 0 - maxDistanceAway;
				zMov = 0 - maxDistanceAway;
			}

			// If x movement has reached the maximum distance...
			if (xMov == maxDistanceAway)
			{
				// Increase z movement by one and reset x movement, restarting
				// the loop.
				zMov++;
				xMov = 0 - maxDistanceAway;
				continue;
			}

			xMov++;
		}
	}

	/**
	 * Gets an entity's heading relative to the direction that the player is
	 * facing and the direction they should be moving in. Used to determine
	 * which way is which from the player's perspective, not the entity that
	 * should be moving.
	 * 
	 * @param player
	 *            The player to use as a reference.
	 * @param direction
	 *            The name of the direction that the entity should be moving in.
	 * 
	 * @return Integer with value of 0, 90, 180, or -90, depending on the
	 *         correct heading of the entity that should move.
	 */
	public static int getHeadingRelativeToPlayerAndSpecifiedDirection(EntityPlayer player, int direction)
	{
		final int directionPlayerFacing = MathHelper.floor_double(player.rotationYaw * 4F / 360F + 0.5D) & 3;

		// Entity wants to go forward.
		if (direction == 0)
		{
			// What is forward for the direction the player is facing?
			switch (directionPlayerFacing)
			{
				case 0:
					return 0;
				case 1:
					return 90;
				case 2:
					return 180;
				case 3:
					return -90;
			}
		}

		// Entity wants to go back.
		else if (direction == 1)
		{
			switch (directionPlayerFacing)
			{
				case 0:
					return 180;
				case 1:
					return -90;
				case 2:
					return 0;
				case 3:
					return 90;
			}
		}

		// Entity wants to go left.
		else if (direction == 2)
		{
			switch (directionPlayerFacing)
			{
				case 0:
					return -90;
				case 1:
					return 0;
				case 2:
					return 90;
				case 3:
					return 180;
			}
		}

		// Entity wants to go right.
		else if (direction == 3)
		{
			switch (directionPlayerFacing)
			{
				case 0:
					return 90;
				case 1:
					return 180;
				case 2:
					return -90;
				case 3:
					return 0;
			}
		}

		return 0;
	}

	/**
	 * Spawns a group of entities of the provided type near the player.
	 * 
	 * @param player
	 *            The player around which the entities will spawn.
	 * @param entityClass
	 *            The entity's class.
	 * @param minimum
	 *            The minimum number of entities to spawn.
	 * @param maximum
	 *            The maximum number of entities to spawn.
	 */
	public static void spawnGroupOfEntitiesAroundPlayer(EntityPlayer player, Class entityClass, int minimum, int maximum)
	{
		spawnGroupOfEntitiesAroundPoint(player.worldObj, new Point3D(player.posX, player.posY, player.posZ), entityClass, minimum, maximum);
	}

	/**
	 * Spawns a group of entities of the provided type near a point.
	 * 
	 * @param world
	 *            The world the entities should be spawned in.
	 * @param point
	 *            The point around which the entities will spawn.
	 * @param entityClass
	 *            The entity's class.
	 * @param minimum
	 *            The minimum number of entities to spawn.
	 * @param maximum
	 *            The maximum number of entities to spawn.
	 */
	public static void spawnGroupOfEntitiesAroundPoint(World world, Point3D point, Class entityClass, int minimum, int maximum)
	{
		try
		{
			final int amountToSpawn = LogicHelper.getNumberInRange(minimum, maximum);

			for (int i = 0; i < amountToSpawn; i++)
			{
				final EntityLivingBase entity = (EntityLivingBase) entityClass.getDeclaredConstructor(World.class).newInstance(world);
				final Point3D spawnPoint = getRandomNearbyBlockCoordinatesOfType(world, point, Blocks.air, 10);

				if (spawnPoint != null)
				{
					int blocksUntilGround = 0;

					while (world.isAirBlock(point.iPosX, point.iPosY + blocksUntilGround, point.iPosZ) && blocksUntilGround != 255)
					{
						blocksUntilGround--;
					}

					entity.setPosition(spawnPoint.dPosX, spawnPoint.dPosY + blocksUntilGround + 1, spawnPoint.dPosZ);
					world.spawnEntityInWorld(entity);
				}
			}
		}

		catch (final NoSuchMethodException e)
		{
			//RadixCore.getInstance().getLogger().log("Entity class provided doesn't contain a constructor accepting only a World as an argument.");
			//RadixCore.getInstance().getLogger().log(e);
			System.out.println("Entity class provided doesn't contain a constructor accepting only a World as an argument.\n"+e);
		}

		catch (final Exception e)
		{
			//RadixCore.getInstance().getLogger().log("Unexpected exception while spawning a group of entities.");
			//RadixCore.getInstance().getLogger().log(e);
			System.out.println("Unexpected exception while spawning a group of entities.\n"+e);
		}
	}

	/**
	 * Spawns an entity of the provided type near the player.
	 * 
	 * @param player
	 *            The player the entity should be spawned near.
	 * @param entityClass
	 *            The entity's class.
	 */
	public static void spawnEntityAtPlayer(EntityPlayer player, Class entityClass)
	{
		try
		{
			final EntityLivingBase entity = (EntityLivingBase) entityClass.getDeclaredConstructor(World.class).newInstance(player.worldObj);
			final Point3D spawnPoint = LogicHelper.getRandomNearbyBlockCoordinatesOfType(player, Blocks.air, 10);

			if (spawnPoint != null)
			{
				entity.setPosition(spawnPoint.iPosX, spawnPoint.iPosY, spawnPoint.iPosZ);
				player.worldObj.spawnEntityInWorld(entity);
			}
		}

		catch (final NoSuchMethodException e)
		{
			//RadixCore.getInstance().getLogger().log("Entity class provided doesn't contain a constructor accepting only a World as an argument.");
			//RadixCore.getInstance().getLogger().log(e);
			System.out.println("Entity class provided doesn't contain a constructor accepting only a World as an argument.\n"+e);
		}

		catch (final Exception e)
		{
			//RadixCore.getInstance().getLogger().log("Unexpected exception while spawning entity at player.");
			//RadixCore.getInstance().getLogger().log(e);
			System.out.println("Unexpected exception while spawning entity at player.\n"+e);
		}
	}

	/**
	 * Gets the closest player to the specified entity, up to a maximum of 64
	 * blocks away.
	 * 
	 * @param entity
	 *            The entity looking for a player.
	 * 
	 * @return The player closest to the provided entity.
	 */
	public static EntityPlayer getNearestPlayer(Entity entity)
	{
		return (EntityPlayer) getNearestEntityOfType(entity, EntityPlayer.class, 64);
	}

	/**
	 * Gets the closest entity of the provided type.
	 * 
	 * @param entityOrigin
	 *            The entity that will serve as the origin point.
	 * @param entityType
	 *            The type of entity to search for.
	 * @param maxDistanceAway
	 *            The maximum distance from the origin to search.
	 * 
	 * @return The entity of the provided type closest to the provided entity.
	 */
	public static Entity getNearestEntityOfType(Entity entityOrigin, Class entityType, int maxDistanceAway)
	{
		final double posX = entityOrigin.posX;
		final double posY = entityOrigin.posY;
		final double posZ = entityOrigin.posZ;

		final List<Entity> entitiesAroundMe = entityOrigin.worldObj.getEntitiesWithinAABBExcludingEntity(entityOrigin, AxisAlignedBB.getBoundingBox(posX - maxDistanceAway, posY - maxDistanceAway, posZ - maxDistanceAway, posX + maxDistanceAway, posY + maxDistanceAway, posZ + maxDistanceAway));

		Entity entityCandidate = null;

		for (final Entity entityAroundMe : entitiesAroundMe)
		{
			if (entityType.isAssignableFrom(entityAroundMe.getClass()))
			{
				if (entityCandidate != null)
				{
					if (getDistanceToEntity(entityOrigin, entityCandidate) > getDistanceToEntity(entityOrigin, entityAroundMe))
					{
						entityCandidate = entityAroundMe;
					}
				}

				else
				{
					entityCandidate = entityAroundMe;
				}
			}
		}

		return entityCandidate;
	}

	/**
	 * Gets an entity of the specified type located at the XYZ coordinates in
	 * the specified world.
	 * 
	 * @param type
	 *            The type of entity to get.
	 * @param world
	 *            The world the entity is in.
	 * @param x
	 *            The X position of the entity.
	 * @param y
	 *            The Y position of the entity.
	 * @param z
	 *            The Z position of the entity.
	 * 
	 * @return The entity located at the specified XYZ coordinates. Null if one
	 *         was not found.
	 */
	public static Object getEntityOfTypeAtXYZ(Class type, World world, int x, int y, int z)
	{
		// This would have no reason to fail. Continue to use the loaded entity
		// list.
		for (final Object obj : world.loadedEntityList)
		{
			if (type.isInstance(obj))
			{
				final Entity entity = (Entity) obj;
				final int posX = (int) entity.posX;
				final int posY = (int) entity.posY;
				final int posZ = (int) entity.posZ;

				if (x == posX && y == posY && z == posZ) { return obj; }
			}
		}

		// If the above fails, search for and return the nearest AbstractEntity
		// to the point that was clicked.
		Entity nearestEntity = null;

		for (final Object obj : getAllEntitiesWithinDistanceOfCoordinates(world, x, y, z, 3))
		{
			if (type.isInstance(obj))
			{
				if (nearestEntity == null)
				{
					nearestEntity = (Entity) obj;
				}

				else
				{
					final Entity otherEntity = (Entity) obj;

					final double nearestEntityDistance = getDistanceToXYZ(nearestEntity.posX, nearestEntity.posY, nearestEntity.posZ, x, y, z);
					final double nearestCandidateDistance = getDistanceToXYZ(otherEntity.posX, otherEntity.posY, otherEntity.posZ, x, y, z);

					// In the very rare occurrence that either distance is
					// exactly 1.0, that entity is perfectly
					// in between four blocks, and is most likely the reason
					// that this code is running in the first place.
					if (nearestEntityDistance == 1.0)
					{
						return nearestEntity;
					}

					else if (nearestCandidateDistance == 1.0)
					{
						return otherEntity;
					}

					else if (nearestCandidateDistance < nearestEntityDistance)
					{
						nearestEntity = otherEntity;
					}
				}
			}
		}

		return nearestEntity;
	}

	/**
	 * Produces a number within the specified range.
	 * 
	 * @param minimum
	 *            The minimum number generated. Inclusive.
	 * @param maximum
	 *            The maximum number generated. Inclusive.
	 * 
	 * @return Number in range [minimum] to [maximum], both inclusive.
	 */
	public static int getNumberInRange(int minimum, int maximum)
	{
		return new Random().nextInt(maximum - minimum + 1) + minimum;
	}

	/**
	 * Gets a random boolean with a probability of being true.
	 * 
	 * @param probabilityOfTrue
	 *            The probability that true should be returned.
	 * 
	 * @return A randomly generated boolean.
	 */
	public static boolean getBooleanWithProbability(int probabilityOfTrue)
	{
		if (probabilityOfTrue <= 0)
		{
			return false;
		}

		else
		{
			return new Random().nextInt(100) + 1 <= probabilityOfTrue;
		}
	}
}
