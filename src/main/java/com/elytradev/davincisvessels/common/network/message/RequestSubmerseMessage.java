package com.elytradev.davincisvessels.common.network.message;

import com.elytradev.davincisvessels.DavincisVesselsMod;
import com.elytradev.davincisvessels.common.entity.EntityShip;
import com.elytradev.davincisvessels.common.object.DavincisVesselsObjects;
import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.field.MarshalledAs;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import com.elytradev.davincisvessels.common.network.DavincisVesselsNetworking;
import com.elytradev.movingworld.common.network.marshallers.EntityMarshaller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by darkevilmac on 1/29/2017.
 */
@ReceivedOn(Side.SERVER)
public class RequestSubmerseMessage extends Message {

    @MarshalledAs(EntityMarshaller.MARSHALLER_NAME)
    public EntityShip ship;
    public boolean doSumberse;

    public RequestSubmerseMessage(EntityShip ship, boolean doSumberse) {
        super(DavincisVesselsNetworking.NETWORK);
        this.ship = ship;
        this.doSumberse = doSumberse;
    }

    public RequestSubmerseMessage(NetworkContext ctx) {
        super(ctx);
    }

    @Override
    protected void handle(EntityPlayer sender) {
        if (ship != null) {
            if (doSumberse && !ship.canSubmerge()) {
                if (sender != null && sender instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) sender).connection.disconnect("Invalid submerse request!" +
                            "\nCheating to go underwater... reconsider your life choices.");
                    if (sender != null && sender.getGameProfile() != null)
                        DavincisVesselsMod.LOG.warn("A user tried to submerse in a vessel that can't, user info: " + sender.getGameProfile().toString());
                }
                return;
            }

            ship.setSubmerge(doSumberse);
            sender.addStat(DavincisVesselsObjects.achievementSubmerseShip);
        }
    }
}
