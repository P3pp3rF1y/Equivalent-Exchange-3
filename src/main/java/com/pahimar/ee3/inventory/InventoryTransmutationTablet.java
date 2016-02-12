package com.pahimar.ee3.inventory;

import com.pahimar.ee3.reference.Comparators;
import com.pahimar.ee3.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

import java.util.*;

public class InventoryTransmutationTablet implements IInventory
{
    private ItemStack[] inventory;
    private Set<ItemStack> knownTransmutations;

    public InventoryTransmutationTablet()
    {
        this(null);
    }

    public InventoryTransmutationTablet(Collection<ItemStack> knownTransmutations)
    {
        inventory = new ItemStack[30];

        this.knownTransmutations = new TreeSet<ItemStack>(Comparators.idComparator);
        if (knownTransmutations != null)
        {
            this.knownTransmutations.addAll(knownTransmutations);
        }

        List<ItemStack> knownTransmutationsList = new ArrayList<ItemStack>(this.knownTransmutations);
        if (knownTransmutationsList.size() <= 30)
        {
            inventory = knownTransmutationsList.toArray(inventory);
        }
        else
        {
            inventory = knownTransmutationsList.subList(0, 30).toArray(inventory);
        }

        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] instanceof ItemStack)
            {
                inventory[i].stackSize = 1;
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        if (slotIndex < getSizeInventory())
        {
            return inventory[slotIndex];
        }

        return null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= decrementAmount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                itemStack = itemStack.splitStack(decrementAmount);

                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }

            setInventorySlotContents(slotIndex, itemStack);
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex)
    {
        if (getStackInSlot(slotIndex) != null)
        {
            ItemStack itemStack = inventory[slotIndex];
            inventory[slotIndex] = null;
            return itemStack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        if (slotIndex < inventory.length)
        {
            if (itemStack != null)
            {
                ItemStack copiedItemStack = itemStack.copy();
                copiedItemStack.stackSize = 1;
                inventory[slotIndex] = copiedItemStack;
            }
            else
            {
                inventory[slotIndex] = itemStack;
            }
        }
    }

    @Override
    public String getName()
    {
        return Names.Containers.TRANSMUTATION_TABLET;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public void markDirty()
    {
        // NOOP
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer)
    {
        // NOOP
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer)
    {
        // NOOP
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    public Set<ItemStack> getKnownTransmutations()
    {
        return knownTransmutations;
    }
}
