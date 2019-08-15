package scripts.BFPremium;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import scripts.BFPremium.Clases.Banco;

import java.awt.*;

//@Script.Manifest(name="000BlastFurnace", description = "Money making",properties = "autor: Pos yo!")
public class BFPremium extends PollingScript<ClientContext> implements PaintListener {
    @Override
    public void repaint(Graphics graphics) {

    }

    Banco banco=new Banco(ctx);
    @Override
    public void poll() {


    }
}