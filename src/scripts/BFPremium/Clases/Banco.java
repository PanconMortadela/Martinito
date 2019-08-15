package scripts.BFPremium.Clases;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public class Banco extends ClientAccessor{

    public Banco(ClientContext ctx) {
        super(ctx);
    }

    public enum Secundaria{
        sacarCoal, sacarIron, sacarCoalBag
    }
    public Secundaria secundaria;
    public void banco(){

    }
}