package com.elytradev.davincisvessels.common.network.message;

import com.elytradev.davincisvessels.common.network.DavincisVesselsNetworking;
import com.elytradev.davincisvessels.common.network.marshallers.TileEntityMarshaller;
import com.elytradev.davincisvessels.common.tileentity.TileHelm;
import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.field.MarshalledAs;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by darkevilmac on 2/2/2017.
 */
@ReceivedOn(Side.SERVER)
public class RenameShipMessage extends Message {

    @MarshalledAs(TileEntityMarshaller.MARSHALLER_NAME)
    public TileHelm helm;
    public String newShipName;

    public RenameShipMessage(TileHelm helm, String newShipName) {
        super(DavincisVesselsNetworking.NETWORK);
        this.newShipName = newShipName;
        this.helm = helm;
    }

    public RenameShipMessage(NetworkContext ctx) {
        super(ctx);
    }

    @Override
    protected void handle(EntityPlayer sender) {
        if (helm == null)
            return;

        helm.getInfo().setName(newShipName);
    }
}
