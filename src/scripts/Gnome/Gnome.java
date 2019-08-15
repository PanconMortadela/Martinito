package scripts.Gnome;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;

import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by Putito on 13/05/2018.
 */
@Script.Manifest(name="zGnoAun_no_se_prueba", description = "Money making",properties = "autor: Pos yo!")
public class Gnome extends PollingScript<ClientContext> implements PaintListener {
    Tile[] Salto={
            new Tile(2474,3435,0),
            new Tile(2474,3425,0),
            new Tile(2473,3422,1),
            new Tile(2478,3420,2),
            new Tile(2486,3419,2),
            new Tile(2485,3426,0),
            new Tile(2484,3431,0)};
    Tile[] Llegada={
            new Tile(2474,3429,0),
            new Tile(2473,3423,1),
            new Tile(2473,3420,2),
            new Tile(2483,3420,2),
            new Tile(2487,3420,0),
            new Tile(2485,3428,0),
            new Tile(2484,3437,0)};

    Tile actual,siguiente;

    int count=0,missclick=0,espera=0;
    Areas areas=new Areas();
    Area cuadro;

    @Override
    public void repaint(Graphics graphics) {

    }

    @Override
    public void poll() {
        cuadro=areas.area2(Salto[count]);
        if(cuadro.contains(ctx.players.local().tile())){
            camara(Salto[count]);
            Salto[count].matrix(ctx).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if(missclick==10){
                        Salto[count].matrix(ctx).click();
                        missclick=0;
                    }
                    missclick++;
                    return ctx.players.local().animation()!=-1;
                }
            }, 100, 30);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return areas.area2(Llegada[count]).contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;
                }
            }, 100, 30);
        }

        if(areas.area2(Llegada[count]).contains(ctx.players.local().tile())){
            count++;
            if(count==7){
                count=0;
                ctx.movement.step(Salto[0]);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        return areas.area2(Salto[count]).contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;
                    }
                }, 100, 30);

            }
        }

    }
    public boolean camara(Tile Mine){
        if(Mine.y()>ctx.players.local().tile().y()){
            System.out.println("Movere n");
            ctx.camera.angle('n');
            return false;
        }
        if(Mine.x()>ctx.players.local().tile().x()){
            System.out.println("Movere e");
            ctx.camera.angle('e');
            return false;
        }
        if(Mine.y()<ctx.players.local().tile().y()){
            System.out.println("Movere s");
            ctx.camera.angle('s');
            return false;
        }
        if(Mine.x()<ctx.players.local().tile().x()){
            System.out.println("Movere w");
            ctx.camera.angle('w');
            return false;
        }else{
            return true;
        }
    }
}
