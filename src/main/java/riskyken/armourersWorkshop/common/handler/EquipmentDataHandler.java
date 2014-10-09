package riskyken.armourersWorkshop.common.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import riskyken.armourersWorkshop.api.common.equipment.IEntityEquipment;
import riskyken.armourersWorkshop.api.common.equipment.IEquipmentDataHandler;
import riskyken.armourersWorkshop.api.common.equipment.armour.EnumEquipmentType;
import riskyken.armourersWorkshop.api.common.lib.LibCommonTags;
import riskyken.armourersWorkshop.common.equipment.EntityEquipmentData;
import riskyken.armourersWorkshop.common.equipment.EquipmentDataCache;
import riskyken.armourersWorkshop.common.equipment.EquipmentNBTHelper;
import riskyken.armourersWorkshop.common.equipment.ExtendedPropsEntityEquipmentData;
import riskyken.armourersWorkshop.common.equipment.ExtendedPropsPlayerEquipmentData;
import riskyken.armourersWorkshop.common.equipment.data.CustomArmourItemData;
import riskyken.armourersWorkshop.common.items.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EquipmentDataHandler implements IEquipmentDataHandler {

    public static final EquipmentDataHandler INSTANCE = new EquipmentDataHandler();
    
    @Override
    public EntityEquipmentData getCustomEquipmentForEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {
            ExtendedPropsPlayerEquipmentData entityProps;
            entityProps = ExtendedPropsPlayerEquipmentData.get((EntityPlayer) entity);
            if (entity.worldObj.isRemote) {
                EntityClientPlayerMP localPlayer = getLocalPlayer();
                if (entity.getPersistentID() == localPlayer.getPersistentID()) {
                    entityProps = ExtendedPropsPlayerEquipmentData.get(localPlayer);
                }
            }
            if (entityProps != null) {
                return entityProps.getEquipmentData();
            }
        } else {
            ExtendedPropsEntityEquipmentData entityProps = ExtendedPropsEntityEquipmentData.get(entity);
            if (entityProps != null) {
                return entityProps.getEquipmentData();
            }
        }
        return null;
    }

    @Override
    public void removeAllCustomEquipmentFromEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {  
            ExtendedPropsPlayerEquipmentData entityProps = ExtendedPropsPlayerEquipmentData.get((EntityPlayer) entity);
            if (entityProps == null) {
                return;
            }
            entityProps.removeAllCustomEquipment();
        } else {
            ExtendedPropsEntityEquipmentData entityProps = ExtendedPropsEntityEquipmentData.get(entity);
            if (entityProps == null) {
                return;
            }
            entityProps.removeAllCustomEquipment();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private EntityClientPlayerMP getLocalPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void removeCustomEquipmentFromEntity(Entity entity, EnumEquipmentType armourType) {
        if (entity instanceof EntityPlayer) {
            ExtendedPropsPlayerEquipmentData entityProps = ExtendedPropsPlayerEquipmentData.get((EntityPlayer) entity);
            if (entityProps == null) {
                return;
            }
            entityProps.removeCustomEquipment(armourType);
        } else {
            ExtendedPropsEntityEquipmentData entityProps = ExtendedPropsEntityEquipmentData.get(entity);
            if (entityProps == null) {
                return;
            }
            entityProps.removeCustomEquipment(armourType);
        }
    }

    @Override
    public void setCustomEquipmentOnEntity(Entity entity, IEntityEquipment equipmentData) {
        if (entity instanceof EntityPlayer) {
            ExtendedPropsPlayerEquipmentData entityProps = ExtendedPropsPlayerEquipmentData.get((EntityPlayer) entity);
            if (entityProps == null) {
                ExtendedPropsPlayerEquipmentData.register((EntityPlayer) entity);
            }
            entityProps = ExtendedPropsPlayerEquipmentData.get((EntityPlayer) entity);
            entityProps.setEquipmentData(equipmentData); 
        } else {
            ExtendedPropsEntityEquipmentData entityProps = ExtendedPropsEntityEquipmentData.get(entity);
            if (entityProps == null) {
                ExtendedPropsEntityEquipmentData.register(entity);
            }
            entityProps = ExtendedPropsEntityEquipmentData.get(entity);
            entityProps.setEquipmentData(equipmentData); 
        }
    }

    @Override
    public EnumEquipmentType getEquipmentType(int equipmentId) {
        CustomArmourItemData data = EquipmentDataCache.INSTANCE.getEquipmentData(equipmentId);
        if (data != null) {
            return data.getType();
        }
        return EnumEquipmentType.NONE;
    }
    
    @Override
    public boolean hasItemStackGotEquipmentData(ItemStack stack) {
        return EquipmentNBTHelper.itemStackHasCustomEquipment(stack);
    }
    
    @Override
    public int getEquipmentIdFromItemStack(ItemStack stack) {
        return EquipmentNBTHelper.getEquipmentIdFromStack(stack);
    }

    @Override
    public ItemStack getCustomEquipmentItemStack(int equipmentId) {
        CustomArmourItemData armourItemData = EquipmentDataCache.INSTANCE.getEquipmentData(equipmentId);
        if (armourItemData == null) { return null; }
        ItemStack stackOutput = new ItemStack(ModItems.equipmentSkin, 1, armourItemData.getType().ordinal() - 1);
        NBTTagCompound armourNBT = new NBTTagCompound();
        armourItemData.writeClientDataToNBT(armourNBT);
        stackOutput.setTagCompound(new NBTTagCompound());
        stackOutput.getTagCompound().setTag(LibCommonTags.TAG_ARMOUR_DATA, armourNBT);;
        return stackOutput;
    }

    @Override
    public IInventory getPlayersEquipmentInventory(EntityPlayer player) {
        ExtendedPropsPlayerEquipmentData entityProps = ExtendedPropsPlayerEquipmentData.get(player);
        if (entityProps == null) {
            return null;
        }
        return entityProps;
    }

    @Override
    public ItemStack getEquipmentStackFromEntity(Entity entity, EnumEquipmentType armourType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEquipmentStackOnEntity(Entity entity, EnumEquipmentType armourType, ItemStack stack) {
        // TODO Auto-generated method stub
        
    }
}