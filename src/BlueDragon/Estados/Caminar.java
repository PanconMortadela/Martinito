package BlueDragon.Estados;


import BlueDragon.Constantes;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.Areas;

import java.util.concurrent.Callable;


public class Caminar extends ClientAccessor {
    public Caminar(ClientContext ctx) {
        super(ctx);
    }

    Tile Banco=new Tile(2947,3375,0);
    Tile paredSalto=new Tile(2937,3355,0);
    Tile Escalera=new Tile(2885,3397,0);

    int pared=24222,escaleras=16680,tuberia=16509;
    String saltar="Climb-over",bajar="Climb-down",entrar="Squeeze-through";

    Area llegadaPared=new Area(new Tile(2936,3358,0),new Tile(2932,3352,0));
    Area llegadaEscalera=new Area(new Tile(2886,9798,0),new Tile(2882,9794,0));
    Area llegadaTubo= new Area(new Tile(2890,9797,0),new Tile(2894,9801,0));

    public Constantes.Estados ir(String s){
        if(s=="Bank"){
            caminar(Banco);
            if(ctx.bank.inViewport()){
                return Constantes.Estados.bancoDepositar;
            }else{
                return Constantes.Estados.caminarBanco;
            }
        }else if(s=="Pared"){
            caminar(paredSalto);
            if(obstaculo(pared,saltar)){
                return Constantes.Estados.caminarEscalera;
            }else{
                return Constantes.Estados.caminarPared;
            }
        }else if(s=="Escaleras"){
            caminar(Escalera);
            if (obstaculo(escaleras,bajar)){
                return Constantes.Estados.entrarDragon;
            }else{
                return Constantes.Estados.caminarEscalera;
            }
        }else{
            if(obstaculo(tuberia,entrar)){
                return Constantes.Estados.Atacar;
            }else{
                return Constantes.Estados.entrarDragon;
            }
        }
    }


    int time;
    Tile destino;
    Areas areas=new Areas();

    public void caminar(Tile t){
        if(!areas.area2(t).contains(ctx.players.local())) {
            ctx.movement.step(t);
            time = Random.nextInt(500, 800);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.destination().x() != -1;
                }
            }, time, 40);
            destino = ctx.movement.destination();
            time = Random.nextInt(200, 300);
            if(ctx.inventory.select().id(391).count()>0){
                final int cantidad=ctx.inventory.select().id(391).count();
                final Item i =ctx.inventory.select().id(391).poll();
                i.interact("Eat");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(391).count()<cantidad;
                    }
                }, time, 40);
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return areas.area3(destino).contains(ctx.players.local());
                }
            }, time, 40);
        }else{
            System.out.println("Ya estoy en area! Puto Objeto!");
        }

    }

    GameObject go;

    public boolean obstaculo(int i,String s){
        go=ctx.objects.select(10).id(i).nearest().poll();
        if(go.inViewport()){
            go.interact(s);
            if(ctx.game.crosshair()== Game.Crosshair.ACTION){
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()!=-1;
                    }
                }, time, 40);
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()==-1;
                    }
                }, time, 40);
            }
            if(ctx.game.clientState()!=30){
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.clientState()==30;
                    }
                }, time, 40);
            }
            if (i==tuberia){
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()!=-1;
                    }
                }, time, 40);
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()==-1;
                    }
                }, time, 40);
            }
        }

        if(llegadaPared.contains(ctx.players.local())){
            return true;
        }else if(llegadaEscalera.contains(ctx.players.local())){
            return true;
        }else if(llegadaTubo.contains(ctx.players.local())){
            return true;
        }else{
            return false;
        }
    }
}