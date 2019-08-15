package scripts;

import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Toreto on 13/07/2017.
 */

@Script.Manifest(name="Dragonkill", description = "Money making",properties = "autor: Pos yo!")
public class KillDragon extends PollingScript<ClientContext>{

    final Area DragonA = new Area(new Tile(2907, 2906), new Tile(2894, 9795));
    final int  foodID =0;
    Item food = ctx.inventory.select().id(foodID).poll();
    final String[] Items = {"Dragon bones","Blue dragonhide","Rune dagger","Grimy ranarr weed"};
    final int[] Dragon = {267,268,265};
    final Tile Climb= new Tile(2938,3355);
    final Tile Bank= new Tile(2951,3378);
    final Tile[] escaleras= {new Tile(2921,3364),new Tile(2909,3376),new Tile(2895,3387),new Tile(2889,3394)};
    Npc BD = ctx.npcs.select().name("Blue dragon").nearest().poll(); // Targeting chicken to attack
    GroundItem DragonPU = ctx.groundItems.select().name("Dragon Bone").nearest().poll(); //Selecting nearest feather on ground.

    @Override
    public void poll(){
combate();


    }
    public void pipe(){
        GameObject obj = ctx.objects.select().id(16509).poll();
        obj.interact(false,"Squeeze-through");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("LA animacion es: "+ ctx.players.local().animation());
                return ctx.players.local().animation()==749;//animacion de tubo :3
            }
        }, 500, 80);
    }
    public void walktoMuro(){
        ctx.movement.step(Climb);
        GameObject obj = ctx.objects.select().id(16509).poll();
        obj.interact(false,"Climb-over");
        //obj.interact(false,"Squeeze-through");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return ctx.players.local().animation()!=-1;
            }
        }, 500, 80);
        if(ctx.controller.isStopping()){
            ctx.controller.stop();
        }
    }
    public void walktobank(){
        ctx.movement.step(Bank);
        ctx.bank.open();
        ctx.bank.depositInventory();
        ctx.bank.withdraw(8009,1);
        ctx.bank.withdraw(11951,1);
        ctx.bank.withdraw(2442,1);
        ctx.bank.withdraw(2440,1);
        ctx.bank.withdraw(2436,1);
        ctx.bank.withdraw(7946,10);
    }
    public void walktoescaleras(){
        for( int i = 0; i<escaleras.length; i++){
            System.out.println("Voy al punto: " + i);
            ctx.movement.step(escaleras[i]);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return ctx.movement.distance(ctx.movement.destination(),ctx.players.local().tile())<3;

                }
            }, 500, 80);

        }

        GameObject obj = ctx.objects.select().id(16680).poll();
        obj.interact(false,"Climb-down");
        //obj.interact(false,"Squeeze-through");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return !ctx.movement.running();
            }
        }, 500, 80);
        if(ctx.controller.isStopping()){
            ctx.controller.stop();
            System.out.println("Me detendre");
        }
    }

    public void combate(){




    }
    public void pickup(){

        for(int i=0;i<Items.length;i++){
            DragonPU = ctx.groundItems.select().name(Items).nearest().poll();
            while (DragonPU != ctx.groundItems.nil() && DragonPU.valid() ) {

            if (DragonPU.valid()) {
                    if (ctx.camera.pitch() < 50) {
                        ctx.camera.pitch(55);
                    }
                    ctx.camera.turnTo(DragonPU);
                    DragonPU.interact("Take", DragonPU.name());

                }
            }

        }
    }

    public void eat(){
        while (ctx.players.local().healthPercent() <= 40) {
            food = ctx.inventory.select().id(foodID).poll();
            food.interact("Eat");
        }
    }

}
