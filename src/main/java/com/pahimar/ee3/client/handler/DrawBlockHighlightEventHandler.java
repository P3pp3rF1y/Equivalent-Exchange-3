package com.pahimar.ee3.client.handler;

import com.pahimar.ee3.EquivalentExchange3;
import com.pahimar.ee3.api.array.AlchemyArray;
import com.pahimar.ee3.array.AlchemyArrayRegistry;
import com.pahimar.ee3.client.util.RenderUtils;
import com.pahimar.ee3.item.*;
import com.pahimar.ee3.reference.ToolMode;
import com.pahimar.ee3.settings.ChalkSettings;
import com.pahimar.ee3.tileentity.TileEntityAlchemyArray;
import com.pahimar.ee3.tileentity.TileEntityDummyArray;
import com.pahimar.ee3.tileentity.TileEntityEE;
import com.pahimar.ee3.util.IModalTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DrawBlockHighlightEventHandler
{
    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event)
    {
        if (event.currentItem != null)
        {
            if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                if (event.currentItem.getItem() instanceof ItemDarkMatterShovel)
                {
                    drawSelectionBoxForShovel(event, (IModalTool) event.currentItem.getItem());
                }
                else if (event.currentItem.getItem() instanceof ItemDarkMatterPickAxe)
                {
                    drawSelectionBoxForPickAxe(event, (IModalTool) event.currentItem.getItem());
                }
                else if (event.currentItem.getItem() instanceof ItemDarkMatterHammer)
                {
                    drawSelectionBoxForHammer(event, (IModalTool) event.currentItem.getItem());
                }
                else if (event.currentItem.getItem() instanceof ItemDarkMatterAxe)
                {
                    drawSelectionBoxForAxe(event, (IModalTool) event.currentItem.getItem());
                }
                else if (event.currentItem.getItem() instanceof ItemDarkMatterHoe)
                {
                    drawSelectionBoxForHoe(event, (IModalTool) event.currentItem.getItem());
                }
                else if (event.currentItem.getItem() instanceof ItemChalk)
                {
                    // if should draw
                    drawAlchemyArrayOverlay(event);
                }
            }
        }
    }

    private void drawSelectionBoxForShovel(DrawBlockHighlightEvent event, IModalTool modalTool)
    {
        ToolMode toolMode = modalTool.getCurrentToolMode(event.currentItem);
        int facing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (toolMode != ToolMode.UNKNOWN)
        {
            event.setCanceled(true);
            if (toolMode == ToolMode.STANDARD)
            {
                drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos()), 0, event.partialTicks);
            }
        }
    }

    private void drawSelectionBoxForPickAxe(DrawBlockHighlightEvent event, IModalTool modalTool)
    {
        ToolMode toolMode = modalTool.getCurrentToolMode(event.currentItem);
        int facing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (toolMode != ToolMode.UNKNOWN)
        {
            event.setCanceled(true);
            drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos()), 0, event.partialTicks);
            if (toolMode == ToolMode.WIDE)
            {
                if (event.target.sideHit == EnumFacing.NORTH || event.target.sideHit == EnumFacing.SOUTH)
                {
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().west()), 0, event.partialTicks);
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().east()), 0, event.partialTicks);
                } else if (event.target.sideHit == EnumFacing.EAST || event.target.sideHit == EnumFacing.WEST)
                {
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().north()), 0, event.partialTicks);
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().south()), 0, event.partialTicks);
                }
                else
                {
                    if (facing == 0 || facing == 2)
                    {
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().west()), 0, event.partialTicks);
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().east()), 0, event.partialTicks);
                    }
                    else
                    {
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().north()), 0, event.partialTicks);
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().south()), 0, event.partialTicks);
                    }
                }
            }
            else if (toolMode == ToolMode.TALL)
            {
                if (event.target.sideHit == EnumFacing.NORTH || event.target.sideHit == EnumFacing.SOUTH || event.target.sideHit == EnumFacing.EAST || event.target.sideHit == EnumFacing.WEST)
                {
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().up()), 0, event.partialTicks);
                    drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().down()), 0, event.partialTicks);
                }
                else
                {
                    if (facing == 1 || facing == 3)
                    {
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().west()), 0, event.partialTicks);
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().east()), 0, event.partialTicks);
                    }
                    else
                    {
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().north()), 0, event.partialTicks);
                        drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos().south()), 0, event.partialTicks);
                    }
                }
            }
        }
    }

    private void drawSelectionBoxForHammer(DrawBlockHighlightEvent event, IModalTool modalTool)
    {
        ToolMode toolMode = modalTool.getCurrentToolMode(event.currentItem);
        int facing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (toolMode != ToolMode.UNKNOWN)
        {
            event.setCanceled(true);
            if (toolMode == ToolMode.STANDARD)
            {
                drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos()), 0, event.partialTicks);
            }
        }
    }

    private void drawSelectionBoxForAxe(DrawBlockHighlightEvent event, IModalTool modalTool)
    {
        ToolMode toolMode = modalTool.getCurrentToolMode(event.currentItem);
        int facing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (toolMode != ToolMode.UNKNOWN)
        {
            event.setCanceled(true);
            if (toolMode == ToolMode.STANDARD)
            {
                drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos()), 0, event.partialTicks);
            }
        }
    }

    private void drawSelectionBoxForHoe(DrawBlockHighlightEvent event, IModalTool modalTool)
    {
        ToolMode toolMode = modalTool.getCurrentToolMode(event.currentItem);
        int facing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (toolMode != ToolMode.UNKNOWN)
        {
            event.setCanceled(true);
            if (toolMode == ToolMode.STANDARD)
            {
                drawSelectionBox(event.context, event.player, new MovingObjectPosition(event.target.hitVec, event.target.sideHit, event.target.getBlockPos()), 0, event.partialTicks);
            }
        }
    }

    private void drawAlchemyArrayOverlay(DrawBlockHighlightEvent event)
    {
        ChalkSettings chalkSettings = EquivalentExchange3.proxy.getClientProxy().chalkSettings;
        AlchemyArray alchemyArray = AlchemyArrayRegistry.getInstance().getAlchemyArray(chalkSettings.getIndex());
        ResourceLocation texture = alchemyArray.getTexture();
        int rotation = chalkSettings.getRotation();

        double x = event.target.getBlockPos().getX() + 0.5F;
        double y = event.target.getBlockPos().getY() + 0.5F;
        double z = event.target.getBlockPos().getZ() + 0.5F;
        double iPX = event.player.prevPosX + (event.player.posX - event.player.prevPosX) * event.partialTicks;
        double iPY = event.player.prevPosY + (event.player.posY - event.player.prevPosY) * event.partialTicks;
        double iPZ = event.player.prevPosZ + (event.player.posZ - event.player.prevPosZ) * event.partialTicks;

        float xScale, yScale, zScale;
        float xShift, yShift, zShift;
        float xRotate, yRotate, zRotate;
        int zCorrection = 1;
        int rotationAngle = 0;
        int playerFacing = MathHelper.floor_double(event.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int facingCorrectionAngle = 0;

        xScale = yScale = zScale = 1;
        xShift = yShift = zShift = 0;
        xRotate = yRotate = zRotate = 0;

        int chargeLevel = ((chalkSettings.getSize() - 1) * 2) + 1;

        TileEntity tileEntity = event.player.worldObj.getTileEntity(event.target.getBlockPos());
        boolean shouldRender = true;

        if (tileEntity instanceof TileEntityEE)
        {
            if (((TileEntityEE) tileEntity).getFacing() != event.target.sideHit)
            {
                shouldRender = false;
            }
        }

        if (!canPlaceAlchemyArray(event.currentItem, event.player.worldObj, event.target.getBlockPos(), event.target.sideHit))
        {
            shouldRender = false;
        }

        if (shouldRender)
        {
            switch (event.target.sideHit)
            {
                case UP:
                {
                    xScale = zScale = chargeLevel;
                    yShift = 0.001f;
                    xRotate = -1;
                    rotationAngle = (-90 * (rotation + 2)) % 360;
                    facingCorrectionAngle = (-90 * (playerFacing + 2)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        y -= 1;
                    }

                    if (tileEntity instanceof TileEntityDummyArray)
                    {
                        x = ((TileEntityDummyArray) tileEntity).getTrueXCoord() + 0.5f;
                        y = ((TileEntityDummyArray) tileEntity).getTrueYCoord() + 0.5f - 1;
                        z = ((TileEntityDummyArray) tileEntity).getTrueXCoord() + 0.5f;
                    }
                    break;
                }
                case DOWN:
                {
                    xScale = zScale = chargeLevel;
                    yShift = -0.001f;
                    xRotate = 1;
                    rotationAngle = (-90 * (rotation + 2)) % 360;
                    facingCorrectionAngle = (-90 * (playerFacing + 2)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        y += 1;
                    }
                    break;
                }
                case NORTH:
                {
                    xScale = yScale = chargeLevel;
                    zCorrection = -1;
                    zShift = -0.001f;
                    zRotate = 1;
                    rotationAngle = (-90 * (rotation + 1)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        z += 1;
                    }
                    break;
                }
                case SOUTH:
                {
                    xScale = yScale = chargeLevel;
                    zShift = 0.001f;
                    zRotate = -1;
                    rotationAngle = (-90 * (rotation + 1)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        z -= 1;
                    }
                    break;
                }
                case EAST:
                {
                    yScale = zScale = chargeLevel;
                    xShift = 0.001f;
                    yRotate = 1;
                    rotationAngle = (-90 * (rotation + 2)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        x -= 1;
                    }
                    break;
                }
                case WEST:
                {
                    yScale = zScale = chargeLevel;
                    xShift = -0.001f;
                    yRotate = -1;
                    rotationAngle = (-90 * (rotation + 2)) % 360;
                    if (tileEntity instanceof TileEntityAlchemyArray)
                    {
                        x += 1;
                    }
                    break;
                }
                default:
                    break;
            }

            if (shouldRender)
            {
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glPushMatrix();
                GL11.glTranslated(-iPX + x + xShift, -iPY + y + yShift, -iPZ + z + zShift);
                GL11.glScalef(1F * xScale, 1F * yScale, 1F * zScale);
                GL11.glRotatef(rotationAngle, event.target.sideHit.getFrontOffsetX(), event.target.sideHit.getFrontOffsetY(), event.target.sideHit.getFrontOffsetZ());
                GL11.glRotatef(facingCorrectionAngle, event.target.sideHit.getFrontOffsetX(), event.target.sideHit.getFrontOffsetY(), event.target.sideHit.getFrontOffsetZ());
                GL11.glRotatef(90, xRotate, yRotate, zRotate);
                GL11.glTranslated(0, 0, 0.5f * zCorrection);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                RenderUtils.renderPulsingQuad(texture, 1f);
                GL11.glPopMatrix();
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glDepthMask(true);
            }
        }
    }

    private boolean canPlaceAlchemyArray(ItemStack itemStack, World world, BlockPos blockPos, EnumFacing facing)
    {
        ChalkSettings chalkSettings = EquivalentExchange3.proxy.getClientProxy().chalkSettings;

        int coordOffset = chalkSettings.getSize() - 1;
        AlchemyArray alchemyArray = AlchemyArrayRegistry.getInstance().getAlchemyArray(chalkSettings.getIndex());
        boolean canPlaceAlchemyArray = isValidForArray(world, blockPos, facing);

        int chargeLevel = ((chalkSettings.getSize() - 1) * 2) + 1;

        if (itemStack.getItemDamage() == itemStack.getMaxDamage() && (chargeLevel * chargeLevel) * alchemyArray.getChalkCostPerBlock() == 1) {
            canPlaceAlchemyArray = true;
        } else if (itemStack.getMaxDamage() - itemStack.getItemDamage() + 1 < (chargeLevel * chargeLevel) * alchemyArray.getChalkCostPerBlock()) {
            canPlaceAlchemyArray = false;
        }

        if (canPlaceAlchemyArray)
        {
            Iterable<BlockPos> blocksInPlane = null;
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN)
            {
                blocksInPlane = BlockPos.getAllInBox(blockPos.add(-coordOffset, 0, -coordOffset), blockPos.add(coordOffset, 0, coordOffset));
            } else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH)
            {
                blocksInPlane = BlockPos.getAllInBox(blockPos.add(-coordOffset, -coordOffset, 0), blockPos.add(coordOffset, coordOffset, 0));
            } else if (facing == EnumFacing.EAST || facing == EnumFacing.WEST)
            {
                blocksInPlane = BlockPos.getAllInBox(blockPos.add(0, -coordOffset, -coordOffset), blockPos.add(0, coordOffset, coordOffset));
            }
            if (blocksInPlane != null) {
                for (BlockPos currentBlockPos : blocksInPlane) {
                    if (!currentBlockPos.equals(blockPos) && !isValidForArray(world, currentBlockPos, facing)) {
                        canPlaceAlchemyArray = false;
                    }
                }
            }
        }

        return canPlaceAlchemyArray;
    }

    private boolean isValidForArray(World world, BlockPos blockPos, EnumFacing sideHit)
    {

        if (world != null && blockPos != null && sideHit != null) {

            BlockPos placementBlockPos = blockPos.offset(sideHit);
            if (placementBlockPos != null) {
                return world.isSideSolid(blockPos, sideHit) && world.getBlockState(placementBlockPos).getBlock().isReplaceable(world, placementBlockPos);
            }
        }

        return false;
    }

    /**
     * @see RenderGlobal#drawSelectionBox(EntityPlayer, MovingObjectPosition, int, float)
     */
    private void drawSelectionBox(RenderGlobal renderGlobal, EntityPlayer entityPlayer, MovingObjectPosition rayTrace, int i, float partialTicks)
    {
        if (i == 0 && rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1f, 1f, 1f, 0.5f);
            GL11.glLineWidth(3.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            Block block = entityPlayer.worldObj.getBlockState(rayTrace.getBlockPos()).getBlock();

            if (block.getMaterial() != Material.air)
            {
                block.setBlockBoundsBasedOnState(entityPlayer.worldObj, rayTrace.getBlockPos());
                double d0 = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double) partialTicks;
                double d1 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double) partialTicks;
                double d2 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double) partialTicks;
                renderGlobal.drawSelectionBoundingBox(block.getSelectedBoundingBox(entityPlayer.worldObj, rayTrace.getBlockPos()).expand(0.002d, 0.002d, 0.002d).offset(-d0, -d1, -d2));
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}
