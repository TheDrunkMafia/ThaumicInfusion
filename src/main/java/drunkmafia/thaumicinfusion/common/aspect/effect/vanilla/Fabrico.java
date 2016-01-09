/*
 * @author TheDrunkMafia
 *
 * See http://www.wtfpl.net/txt/copying for licence
 */

package drunkmafia.thaumicinfusion.common.aspect.effect.vanilla;

import drunkmafia.thaumicinfusion.client.util.RGB;
import drunkmafia.thaumicinfusion.common.aspect.AspectEffect;
import drunkmafia.thaumicinfusion.common.util.annotation.BlockMethod;
import drunkmafia.thaumicinfusion.common.util.annotation.Effect;
import drunkmafia.thaumicinfusion.net.ChannelHandler;
import drunkmafia.thaumicinfusion.net.packet.server.EffectSyncPacketC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;

@Effect(aspect = "fabrico", cost = 4)
public class Fabrico extends AspectEffect {

    public Aspect aspect;

    @Override
    @BlockMethod
    public int colorMultiplier(IBlockAccess access, BlockPos pos, int renderPass) {
        return aspect != null ? aspect.getColor() : access.getBlockState(pos).getBlock().getBlockColor();
    }

    @Override
    @BlockMethod(overrideBlockFunc = false)
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        ItemStack phial = player.getCurrentEquippedItem();
        if (world.isRemote) {
            if (phial != null && phial.getItem() instanceof IEssentiaContainerItem) {
                AspectList aspects = ((IEssentiaContainerItem) phial.getItem()).getAspects(phial);
                if (aspects != null && aspects.getAspects()[0] != aspect) {
                    world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), "game.neutral.swim", 0.5F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F, false);

                    RGB rgb = new RGB(((IEssentiaContainerItem) phial.getItem()).getAspects(phial).getAspects()[0].getColor());
                }
            }
            return;
        }

        if (phial != null && phial.getItem() instanceof IEssentiaContainerItem) {
            AspectList aspects = ((IEssentiaContainerItem) phial.getItem()).getAspects(phial);
            aspect = aspects != null ? aspects.getAspects()[0] : null;
            ChannelHandler.instance().sendToAll(new EffectSyncPacketC(this, true));
        }
    }

    @Override
    public void readNBT(NBTTagCompound tagCompound) {
        super.readNBT(tagCompound);
        if (tagCompound.hasKey("aspect"))
            aspect = Aspect.getAspect(tagCompound.getString("aspect"));
        else
            aspect = null;
    }

    @Override
    public void writeNBT(NBTTagCompound tagCompound) {
        super.writeNBT(tagCompound);
        if (aspect != null)
            tagCompound.setString("aspect", aspect.getTag());
    }
}
