package BlueDragon.Estados;


import BlueDragon.Constantes;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Banco extends ClientAccessor {
    public Banco(ClientContext ctx) {
        super(ctx);
    }

    int Teleport=8009, Bones=0, Cuero=0;
    int[] rangePotion={2444,173,169,171};
    int time;

    public Constantes.Estados retirar(){
        if(!ctx.bank.opened()){
            if(!ctx.bank.inViewport()){
                return Constantes.Estados.caminarBanco;
            }
            ctx.bank.open();
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, time, 40);

        }else if(ctx.inventory.select().id(rangePotion[0]).count()<1){
            ctx.bank.withdraw(rangePotion[0],1);
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(rangePotion[0]).count()==1;
                }
            }, time, 40);
            if(ctx.combat.health()>0&&ctx.combat.health()<30){
                ctx.bank.withdraw(391,2);
            }else if(ctx.combat.health()>=30&&ctx.combat.health()<50){
                ctx.bank.withdraw(391,1);
            }
        }
        if(ctx.inventory.select().id(rangePotion[0]).count()==1){
            return Constantes.Estados.caminarPared;
        }
        else{
            return Constantes.Estados.bacoRetirar;
        }
    }

    int flechas=886;
    public Constantes.Estados depositar(){
        if(!ctx.bank.opened()){
            ctx.bank.open();
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, time, 40);
        }else if(ctx.inventory.select().count()>1){
            ctx.bank.depositAllExcept(Teleport);
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()==1;
                }
            }, time, 40);

        }
        if(ctx.inventory.select().count()==(1+ctx.inventory.select().id(flechas).count())){
            return Constantes.Estados.bacoRetirar;
        }else
            return Constantes.Estados.bancoDepositar;

    }

    public Constantes.Estados Teleport(){
        ctx.inventory.select().id(Teleport).poll().interact("Break");
        time= Random.nextInt(200,300);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation()!=-1;
            }
        }, time, 40);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation()==-1;
            }
        }, time, 40);

        if(ctx.players.local().tile().x()<2971&&ctx.players.local().tile().x()>2943){
            return Constantes.Estados.bancoDepositar;
        }else{
            return Constantes.Estados.Teleport;
        }

    }

}