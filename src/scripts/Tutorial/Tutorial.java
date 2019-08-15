package scripts.Tutorial;


import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Npc;
import scripts.Utilitys.Areas;

import java.awt.*;
import java.util.concurrent.Callable;

public class Tutorial extends PollingScript<ClientContext> implements PaintListener{

    int estado=0;

    int widgetMe;int componentMe;

    int widgetnpc=231,componentnpc=2,widgetopc1=0,componentopc1=0;

    int[] npcs={3308,8503,3312,3311,3311,3307,3313,10083,3310,3319};
    int[] puerta={9398,9470,9709,9710,9716,9717,9720,9721,9722};
    int[] objetos={3317,9736,9726,10080,10079,10082,9727,26801};

    @Override
    public void repaint(Graphics graphics) {

    }



    @Override
    public void poll() {

    }

    public void estados(){
        switch (estado){
            case 0:
                if(true)estado++; //Verificacion
                break;
            case 1:
                if(true)estado++; //Verificacion
                break;
            case 2:
                if(true)estado++; //Verificacion
                break;
            case 3:
                if(true)estado++; //Verificacion
                break;
            case 4:
                if(true)estado++; //Verificacion
                break;
            case 5:
                if(true)estado++; //Verificacion
                break;
            case 6:
                if(true)estado++; //Verificacion
                break;
            case 7:
                if(true)estado++; //Verificacion
                break;
            case 8:
                if(true)estado++; //Verificacion
                break;
            case 9:
                if(true)estado++; //Verificacion
                break;

        }
    }

    Npc npc;
    public void hablarNpc(String s, int id, final int widget, final int component){
        npc= ctx.npcs.select().id(id).poll();
        if(!ctx.widgets.widget(widget).component(component).visible()){
            npc.interact("Talk-to");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return ctx.widgets.widget(widget).component(component).visible()||ctx.widgets.widget(widget).component(component).visible();
                }
            }, Random.nextInt(200,400), Random.nextInt(5,7));
            ctx.input.send(" ");
        }else{
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return ctx.widgets.widget(widget).component(component).visible()||!ctx.widgets.widget(widget).component(component).visible();
                }
            }, Random.nextInt(500,1000), Random.nextInt(5,7));
            ctx.input.send(" ");
        }
    }
    Areas areas=new Areas();
    public void puertas(int x,final Tile tile){
        ctx.objects.select().id(x).poll().interact("Open");
        if(ctx.game.crosshair().equals(Game.Crosshair.ACTION)) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return areas.area1(tile).contains(ctx.players.local());
                }
            }, Random.nextInt(500, 1000), Random.nextInt(5, 7));
        }
    }

    public void interactuar_objeto(int x,final int y){
        ctx.objects.select().id(x).poll().interact("");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return ctx.inventory.select().id(y).count()==0;
            }
        }, Random.nextInt(500,1000), Random.nextInt(5,7));
    }

    public void seleccionar_opcion(final int x, final int y,final int z){
        if(ctx.widgets.widget(x).component(y).visible()){
            ctx.widgets.widget(x).component(y).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return !ctx.widgets.widget(x).component(y).visible();
                }
            }, Random.nextInt(500,1000), Random.nextInt(5,7));
        }
    }

    public void menu(final Game.Tab tab){
        if(!ctx.game.tab().equals(tab)){
            ctx.game.tab(tab);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return ctx.game.tab().equals(tab);
                }
            }, Random.nextInt(500,1000), Random.nextInt(5,7));
        }
    }
}