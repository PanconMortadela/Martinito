package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.GroundItem;
import scripts.Utilitys.Areas;
import org.powerbot.script.*;

import javax.swing.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="Varrock", description = "Agility",properties = "autor: Pos yo!")
public class Varrock extends PollingScript<ClientContext> {
    Tile[] inicio={
            new Tile(3221,3414,0),
            new Tile(3214,3414,3),
            new Tile(3201,3416,3),
            new Tile(3194,3416,1),
            new Tile(3192,3402,3),
            new Tile(3208,3399,3),
            new Tile(3232,3402,3),
            new Tile(3237,3408,3),
            new Tile(3236,3415,3)};
    Tile[] llegada={
            new Tile(3219,3414,3),
            new Tile(3208,3414,3),
            new Tile(3197,3416,1),
            new Tile(3192,3406,3),
            new Tile(3193,3398,3),
            new Tile(3218,3399,3),
            new Tile(3236,3403,3),
            new Tile(3237,3410,3),
            new Tile(3236,3417,0)};
    Tile Mark=new Tile(3186,3395,3);
    String[] accion={
            "Climb",
            "Cross",
            "Leap",
            "Balance",
            "Leap",
            "Leap",
            "Leap",
            "Hurdle",
            "Jump-off"};

    Areas areas=new Areas();
    Area cuadro;
    @Override
    public void poll() {
        regreso();
        estados();
        detener();

    }

    private void regreso() {


    }
    public boolean detener(){
        if(ctx.controller.isStopping()){
            System.out.println("me detendre");
            ctx.controller.stop();
            stop();
            return true;

        }else if(!ctx.game.loggedIn()){
            System.out.println("me detendre");
            ctx.controller.stop();
            stop();
            return true;
        }else{
            return false;
        }
    }


    int vueltas=0;
    boolean vuelta=false;
    int l,count=0,time=0; boolean camara;
    int[] id={10586,10587,10642,10777,10778,10779,10780,10781,10817};
    GameObject punto;
    int missclick=0;
    Tile gue;
    public void estados(){

        count = estoy();
        cuadro=areas.area2(llegada[count]);
        System.out.println("Posicion: "+count);
        if(count==0&&ctx.skills.level(Constants.SKILLS_AGILITY)==40){
            JOptionPane.showInputDialog("Mera puto ya estas listo para canifi");
            ctx.controller.stop();
            detener();
        }

        if(count==1||count==2) l=ctx.players.local().tile().floor();
        if(count==99) {
            time = Random.nextInt(2, 4);
            vuelta=false;
        }else {
            time = Random.nextInt(10, 14);
            vuelta=true;
        }
        if((count==5||count==3||count==6||count==7)&&areas.area2(llegada[count-1]).contains(ctx.players.local().tile())&&ctx.groundItems.select(10).id(11849).poll().valid())marks();

        punto=ctx.objects.select().id(id[count]).poll();
        if(count==0)punto.bounds(12, 40, -180, 0, 12, 96);
        missclick=0;
        if(punto.valid()){
            if(punto.interact(accion[count])){
                System.out.println("Hare Interact");
                gue=ctx.players.local().tile();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar Interact");
                        if(count==1||count==3){
                            return l!=ctx.players.local().tile().floor();
                        }
                        if(areas.area2(gue).contains(ctx.players.local().tile()))missclick++;
                        else gue=ctx.players.local().tile();
                        if(missclick==3){
                            return true;
                        }
                        return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().animation()==-1;
                    }
                }, 1000, time);
            }else if(inicio[count].matrix(ctx).click()) {
                System.out.println("Hare click");
                gue=ctx.players.local().tile();
                ctx.camera.turnTo(punto.tile());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar Click");
                        if(count==1||count==3){
                            return l!=ctx.players.local().tile().floor();
                        }
                        if(areas.area2(gue).contains(ctx.players.local().tile()))missclick++;
                        else gue=ctx.players.local().tile();
                        if(missclick==3){
                            return true;
                        }
                        return cuadro.contains(ctx.players.local().tile());
                    }
                }, 500, time);
            }else{
                System.out.println("Hare Step");
                cuadro=areas.area2(inicio[count]);
                ctx.movement.step(inicio[count]);
                final Tile fin=ctx.movement.destination();
                ctx.camera.turnTo(inicio[count]);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar Step");
                        return cuadro.contains(ctx.players.local().tile())||areas.area2(fin).contains(ctx.players.local().tile());
                    }
                }, 1000, time);
            }
        }



    }

    private int estoy() {
        for(int i=0;i<inicio.length;i++){
            if(i==inicio.length-1){
                if(areas.area2(inicio[i]).contains(ctx.players.local().tile())){
                    return i;
                }
                return 0;
            }
            if(areas.area2(inicio[i]).contains(ctx.players.local().tile())){
                return i;
            }
            if(areas.area2(llegada[i]).contains(ctx.players.local().tile())){
                return i+1;
            }
        }
        return 99;
    }

    public void interact(){


    }
    public void marks(){
        System.out.println("Hay markitas??");
        GroundItem mark=ctx.groundItems.select(10).id(11849).poll();
        final int markCount=ctx.inventory.select().id(11849).count(true);
        while(mark.valid()){
            if(detener())break;
            if(!mark.interact("Take")){
                ctx.camera.turnTo(mark.tile());
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero mark");
                    return ctx.inventory.select().id(11849).count(true)>markCount;
                }
            }, 1000, 3);
        }
        if(ctx.inventory.select().id(11849).count(true)>markCount){
            System.out.println("Hare Step");
            cuadro=areas.area2(inicio[count]);
            ctx.movement.step(inicio[count]);
            ctx.camera.turnTo(inicio[count]);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Voy a esperar Step");
                    return cuadro.contains(ctx.players.local().tile());
                }
            }, 1000, time);
        }
            //markcount++;

    }
}
