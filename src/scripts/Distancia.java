package scripts;


import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import javax.swing.*;

@Script.Manifest(name="Distancia con", description = "Ver distancia",properties = "autor: Pos yo!")
public class Distancia extends PollingScript<ClientContext> {

    private static Tile BANK_TILE = new Tile(3270,3167);
    private static  Tile TRADER = new Tile(3275,3191);
    Tile x1= new Tile(3277,3177);
    Tile x2= new Tile(3280,3191);


    @Override
    public void poll() {


        //GameObject banco= ctx.objects.select(new Tile(1948,4957),10).id(26707).poll();
        //while(!ctx.bank.opened())banco.interact("Use");
        System.out.println(        "La distancia con Bank es: " + ctx.players.local().interacting() ); ;

       // ctx.controller.stop();
    }
}
