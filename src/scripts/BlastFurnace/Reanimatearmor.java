package scripts.BlastFurnace;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="00Prueba", description = "Money making",properties = "autor: Pos yo!")
public class Reanimatearmor extends PollingScript<ClientContext> implements PaintListener {
    Item coal= ctx.inventory.select().id(12019).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int markcount=0;
    String vueltas;
    boolean vuelta=false;
    org.powerbot.script.rt4.GeItem barr= new org.powerbot.script.rt4.GeItem(8009);
    org.powerbot.script.rt4.GeItem ore= new org.powerbot.script.rt4.GeItem(440);
    org.powerbot.script.rt4.GeItem coal1= new org.powerbot.script.rt4.GeItem(453);
    int value;
    int h, m, s;
    int mostrar1=0,mostrar2=0;
    int skill;
    Combat.Style style;
    int Expericiencia;

    public void determinarSkill(){
        if(ctx.combat.style()==Combat.Style.ACCURATE){
            skill= Constants.SKILLS_ATTACK;
            style=Combat.Style.ACCURATE;
        }else if(ctx.combat.style()==Combat.Style.AGGRESSIVE){
            skill= Constants.SKILLS_STRENGTH;
            style=Combat.Style.AGGRESSIVE;
        }else if(ctx.combat.style()==Combat.Style.DEFENSIVE){
            skill= Constants.SKILLS_DEFENSE;
            style=Combat.Style.DEFENSIVE;
        }
    }

    @Override
    public void repaint(Graphics g1){

        Graphics2D g= (Graphics2D)g1;
        int x= (int) ctx.input.getLocation().getX();
        int y= (int) ctx.input.getLocation().getY();

        g.drawLine(x,y - 10,x,y+10);
        g.drawLine(x-10,y,x+10,y);
        h= (int)((System.currentTimeMillis()- initialTime)/3600000);
        m= (int)((System.currentTimeMillis()- initialTime)/60000%60);
        s= (int)((System.currentTimeMillis()- initialTime)/1000)%60;
        runTime=(double)(System.currentTimeMillis()-initialTime)/3600000;
        Color text=new Color(0,0,0);
        g.setColor(color1);
        g.fillRect(7, 54, 220, 130);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 130);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString(" Atack: " + ctx.skills.level(skill) + " RealAttack: " +ctx.skills.realLevel(skill), 11, 87);
        g.drawString("Exp/tiempo: " + mostrar1,10,106);
        g.drawString( "Tiempo:" + mostrar2,10,126);

    }

    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        determinarSkill();
        Expericiencia=ctx.skills.experience(skill);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return s>0;
            }
        }, 200, 10);

    }

    int[] Suelo={1159,1071,1121};
    int tokken=8851,cantidad;
    int time;
    int machine=23955;
    int npc=2454;
    int[] Attack={2436,145,147,149};
    int[] Str={2440,159,157,161};
    int vida=Random.nextInt(20,30);
    int comida=7946;
    int inventario;
    @Override
    public void poll() {
        if(ctx.groundItems.select().id(Suelo).poll().inViewport()){
            System.out.println("Entre en suelo");
            inventario=ctx.inventory.select().count();
            ctx.groundItems.select().id(Suelo).poll().interact("Take");
            time= Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()>inventario;
                }
            }, time, 10);
        }

        if(ctx.groundItems.select().id(tokken).poll().inViewport()) {
            cantidad=ctx.inventory.select().id(tokken).count(true);
            System.out.println("Entre en tokken");
            ctx.groundItems.select().id(tokken).poll().interact("Take");
            time = Random.nextInt(200, 400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(Suelo).count(true) > cantidad;
                }
            }, time, 10);
        }

        if(ctx.inventory.select().id(Suelo[0]).count()>0&&ctx.inventory.select().id(Suelo[1]).count()>0&&ctx.inventory.select().id(Suelo[2]).count()>0){
            System.out.println("Entre en Machine");
            ctx.objects.select().id(machine).nearest().poll().interact("Animate");
            time = Random.nextInt(200, 400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.npcs.select().id(npc).poll().inViewport();
                }
            }, time, 10);

        }
        if(ctx.combat.health()<vida){
            if(ctx.inventory.select().id(comida).count()>0) {
                System.out.println("Entre en vida");
                ctx.inventory.select().id(comida).poll().interact("Eat");
                time = Random.nextInt(500, 1000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.combat.health()>vida;
                    }
                }, time, 10);
                vida = Random.nextInt(20, 30);
            }
        }
        if(ctx.combat.specialPercentage()==100){
            if(ctx.game.tab()!= Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }

            if(ctx.inventory.select().id(21742).count()>0){
                ctx.inventory.select().id(21742).poll().interact("Wield");
                time=Random.nextInt(400,500);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(21742).count()==0;
                    }
                }, time, 10);
            }
            ctx.combat.specialAttack(true);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.combat.specialPercentage()<100;
                }
            }, time, 10);
            if(ctx.inventory.select().id(4587).count()>0){
                ctx.inventory.select().id(4587).poll().interact("Wield");
                time=Random.nextInt(400,500);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(4587).count()==0;
                    }
                }, time, 10);
            }
        }

        if((ctx.skills.level(Constants.SKILLS_ATTACK)<=(ctx.skills.realLevel(Constants.SKILLS_ATTACK)+6))&&ctx.inventory.select().id(Attack).count()>0){
            System.out.println("Entre en pocion");
            tomarPocion(Attack);
        }
        if((ctx.skills.level(Constants.SKILLS_STRENGTH)<=(ctx.skills.realLevel(Constants.SKILLS_STRENGTH)+6))&&ctx.inventory.select().id(Str).count()>0){
            tomarPocion(Str);
        }

    }

    public void tomarPocion(int[] pocion){
        System.out.println("Entre en Funcion");
        ctx.inventory.select().id(pocion).poll().interact("Drink");
        System.out.println("tome pocion");
        time = Random.nextInt(200, 400);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.skills.level(Constants.SKILLS_ATTACK)>ctx.skills.realLevel(Constants.SKILLS_ATTACK)+5||ctx.skills.level(Constants.SKILLS_STRENGTH)>ctx.skills.realLevel(Constants.SKILLS_STRENGTH)+5;
            }
        }, time, 10);
    }


}