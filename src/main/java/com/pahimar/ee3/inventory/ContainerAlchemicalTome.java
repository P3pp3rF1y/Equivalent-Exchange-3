package com.pahimar.ee3.inventory;

import com.pahimar.ee3.knowledge.TransmutationKnowledgeRegistry;
import com.pahimar.ee3.util.ItemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ContainerAlchemicalTome extends ContainerEE
{
    private final InventoryTransmutationKnowledge inventoryTransmutationKnowledge;
    private int pageOffset, maxPageOffset;
    private String searchTerm;
    private boolean requiresUpdate = false;

    private final static int MAX_ROW_INDEX = 8;
    private final static int MAX_COLUMN_INDEX = 5;

    public ContainerAlchemicalTome(EntityPlayer entityPlayer)
    {
        TreeSet<ItemStack> knownTransmutations = new TreeSet<ItemStack>(ItemHelper.displayNameComparator);
        knownTransmutations.addAll(TransmutationKnowledgeRegistry.getInstance().getPlayersKnownTransmutations(entityPlayer.getUniqueID()));

        inventoryTransmutationKnowledge = new InventoryTransmutationKnowledge(knownTransmutations);
        pageOffset = 0;
        maxPageOffset = knownTransmutations.size() % 80;

        int i = 0;
        for (int rowIndex = 0; rowIndex < MAX_ROW_INDEX; ++rowIndex)
        {
            for (int columnIndex = 0; columnIndex < MAX_COLUMN_INDEX; ++columnIndex)
            {
                this.addSlotToContainer(new Slot(inventoryTransmutationKnowledge, i, 18 + columnIndex * 20, 18 + rowIndex * 19));
                i++;
            }
        }

        i = 40;
        for (int rowIndex = 0; rowIndex < MAX_ROW_INDEX; ++rowIndex)
        {
            for (int columnIndex = 0; columnIndex < MAX_COLUMN_INDEX; ++columnIndex)
            {
                this.addSlotToContainer(new Slot(inventoryTransmutationKnowledge, i, 140 + columnIndex * 20, 18 + rowIndex * 19));
                i++;
            }
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting)
    {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.pageOffset);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (requiresUpdate)
        {
            for (Object crafter : this.crafters)
            {
                ICrafting icrafting = (ICrafting) crafter;
                icrafting.sendProgressBarUpdate(this, 0, this.pageOffset);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int valueType, int updatedValue)
    {
        if (valueType == 0)
        {
            this.pageOffset = updatedValue;
        }
    }

    public int getInventorySize()
    {
        return inventoryTransmutationKnowledge.getSizeInventory();
    }

    public int getPageOffset()
    {
        return this.pageOffset;
    }

    public int getMaxPageOffset()
    {
        return this.maxPageOffset;
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if (buttonName.equalsIgnoreCase("prev") && mouseButton == 0 && this.pageOffset > 0)
        {
            this.pageOffset--;
            updateInventory();
            this.requiresUpdate = true;
        }
        else if (buttonName.equalsIgnoreCase("next") && mouseButton == 0 && this.pageOffset < this.maxPageOffset)
        {
            this.pageOffset++;
            updateInventory();
            this.requiresUpdate = true;
        }
    }

    @Override
    public void handleElementTextFieldUpdate(String buttonName, String updatedText)
    {
        if (buttonName.equalsIgnoreCase("searchField"))
        {
            this.searchTerm = updatedText;
            pageOffset = 0;
            updateInventory();
            this.requiresUpdate = true;
        }
    }

    private void updateInventory()
    {
        boolean shouldUpdateInventory = false;
        ItemStack[] newInventory = new ItemStack[80];
        List<ItemStack> filteredList = new ArrayList(ItemHelper.filterByNameContains(inventoryTransmutationKnowledge.getKnownTransmutations(), searchTerm));

        maxPageOffset = filteredList.size() % 80;
        if (pageOffset > maxPageOffset)
        {
            pageOffset = 0;
        }

        if (pageOffset == 0)
        {
            if (filteredList.size() <= 80)
            {
                newInventory = filteredList.toArray(newInventory);
                shouldUpdateInventory = true;
            }
            else
            {
                newInventory = filteredList.subList(0, 80).toArray(newInventory);
                shouldUpdateInventory = true;
            }
        }
        else if (pageOffset < maxPageOffset)
        {
            newInventory = filteredList.subList(pageOffset * 80, (pageOffset + 1) * 80).toArray(newInventory);
            shouldUpdateInventory = true;
        }
        else if (pageOffset == maxPageOffset)
        {
            newInventory = filteredList.subList(pageOffset * 80, filteredList.size()).toArray(newInventory);
            shouldUpdateInventory = true;
        }

        if (shouldUpdateInventory)
        {
            for (int i = 0; i < 80; i++)
            {
                inventoryTransmutationKnowledge.setInventorySlotContents(i, newInventory[i]);
                inventoryTransmutationKnowledge.markDirty();
            }
        }
    }
}
