package mrjake.aunis.gui.container;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.gui.util.ContainerHelper;
import mrjake.aunis.packet.AunisPacketHandler;
import mrjake.aunis.packet.StateUpdatePacketToClient;
import mrjake.aunis.stargate.EnumIrisMode;
import mrjake.aunis.stargate.power.StargateClassicEnergyStorage;
import mrjake.aunis.state.StateTypeEnum;
import mrjake.aunis.tileentity.stargate.StargateClassicBaseTile;
import mrjake.aunis.tileentity.stargate.StargateClassicBaseTile.StargateUpgradeEnum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StargateContainer extends Container implements OpenTabHolderInterface {

    public StargateClassicBaseTile gateTile;

    private BlockPos pos;
    private int lastEnergyStored;
    private int energyTransferedLastTick;
    private float lastEnergySecondsToClose;
    private int lastProgress;
    private int openTabId = -1;
    private EnumIrisMode irisMode;
    private int irisCode;
    private float openedSeconds;

    @Override
    public int getOpenTabId() {
        return openTabId;
    }

    @Override
    public void setOpenTabId(int tabId) {
        openTabId = tabId;
    }

    public StargateContainer(IInventory playerInventory, World world, int x, int y, int z) {
        pos = new BlockPos(x, y, z);
        gateTile = (StargateClassicBaseTile) world.getTileEntity(pos);
        IItemHandler itemHandler = gateTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Upgrades 2x2 (index 0-3)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                addSlotToContainer(new SlotItemHandler(itemHandler, row * 2 + col, 9 + 18 * col, 18 + 18 * row));
            }
        }

        // Capacitors 1x3 (index 4-6)
        for (int col = 0; col < 3; col++) {
            final int capacitorIndex = col;

            addSlotToContainer(new SlotItemHandler(itemHandler, col + 4, 115 + 18 * col, 40) {
                @Override
                public boolean isEnabled() {
                    // getHasStack() is a compatibility thing for when players already had their capacitors in the gate.
                    return (capacitorIndex + 1 <= gateTile.getSupportedCapacitors()) || getHasStack();
                }
            });
        }

        // Page slots (index 7-9)
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, i + 7, -22, 89 + 22 * i));
        }

        // Biome overlay slot (index 10)
        addSlotToContainer(new SlotItemHandler(itemHandler, 10, 0, 0));

        // Shield/Iris Upgrade (index 11)
        addSlotToContainer(new SlotItemHandler(itemHandler, 11, 16 + 18 * 3, 27));

        for (Slot slot : ContainerHelper.generatePlayerSlots(playerInventory, 86))
            addSlotToContainer(slot);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void updateProgressBar(int id, int data) {
        gateTile.setPageProgress(data);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = getSlot(index).getStack();

        // Transfering from Stargate to player's inventory
        if (index < 12) {
            if (!mergeItemStack(stack, 12, inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            putStackInSlot(index, ItemStack.EMPTY);
        }

        // Transfering from player's inventory to Stargate
        else {
            // Capacitors
            if (stack.getItem() == Item.getItemFromBlock(AunisBlocks.CAPACITOR_BLOCK)) {
                for (int i = 4; i < 7; i++) {
                    if (!getSlot(i).getHasStack() && getSlot(i).isItemValid(stack)) {
                        ItemStack stack1 = stack.copy();
                        stack1.setCount(1);

                        putStackInSlot(i, stack1);
                        stack.shrink(1);

                        return stack;
                    }
                }
            } else if (StargateUpgradeEnum.contains(stack.getItem()) && !gateTile.hasUpgrade(stack.getItem())) {
                for (int i = 0; i < 4; i++) {
                    if (!getSlot(i).getHasStack()) {
                        ItemStack stack1 = stack.copy();
                        stack1.setCount(1);

                        putStackInSlot(i, stack1);
                        stack.shrink(1);

                        return ItemStack.EMPTY;
                    }
                }
            } else if (openTabId >= 0 && openTabId <= 2 && getSlot(7 + openTabId).isItemValid(stack)) {
                if (!getSlot(7 + openTabId).getHasStack()) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);

                    putStackInSlot(7 + openTabId, stack1);
                    stack.shrink(1);

                    return ItemStack.EMPTY;
                }
            }

            // Biome override blocks
            else if (openTabId == 3 && getSlot(10).isItemValid(stack)) {
                if (!getSlot(10).getHasStack()) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);

                    putStackInSlot(10, stack1);
                    stack.shrink(1);

                    return ItemStack.EMPTY;
                }
            }
            // Iris upgrade
            else if (StargateClassicBaseTile.StargateIrisUpgradeEnum.contains(stack.getItem()) && !gateTile.hasUpgrade(stack.getItem())) {
                if (!getSlot(11).getHasStack()) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);

                    putStackInSlot(11, stack1);
                    stack.shrink(1);

                    return ItemStack.EMPTY;
                }
            }

            return ItemStack.EMPTY;
        }

        return stack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        StargateClassicEnergyStorage energyStorage = (StargateClassicEnergyStorage) gateTile.getCapability(CapabilityEnergy.ENERGY, null);

        if (lastEnergyStored != energyStorage.getEnergyStoredInternally()
                || lastEnergySecondsToClose != gateTile.getEnergySecondsToClose()
                || energyTransferedLastTick != gateTile.getEnergyTransferedLastTick()
                || irisMode != gateTile.getIrisMode()
                || irisCode != gateTile.getIrisCode()
                || openedSeconds != gateTile.getOpenedSecondsToDisplay()

        ) {
            for (IContainerListener listener : listeners) {
                if (listener instanceof EntityPlayerMP) {
                    AunisPacketHandler.INSTANCE.sendTo(new StateUpdatePacketToClient(pos, StateTypeEnum.GUI_UPDATE, gateTile.getState(StateTypeEnum.GUI_UPDATE)), (EntityPlayerMP) listener);
                }
            }

            lastEnergyStored = energyStorage.getEnergyStoredInternally();
            energyTransferedLastTick = gateTile.getEnergyTransferedLastTick();
            lastEnergySecondsToClose = gateTile.getEnergySecondsToClose();
            openedSeconds = gateTile.getOpenedSecondsToDisplay();
            irisMode = gateTile.getIrisMode();
            irisCode = gateTile.getIrisCode();
        }

        if (lastProgress != gateTile.getPageProgress()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, gateTile.getPageProgress());
            }

            lastProgress = gateTile.getPageProgress();
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);

        if (listener instanceof EntityPlayerMP)
            AunisPacketHandler.INSTANCE.sendTo(new StateUpdatePacketToClient(pos, StateTypeEnum.GUI_STATE, gateTile.getState(StateTypeEnum.GUI_STATE)), (EntityPlayerMP) listener);
    }
}
