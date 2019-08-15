package scripts;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.powerbot.script.*;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Prayer.Effect;
import org.powerbot.script.rt4.*;


@Script.Manifest(name="00Nightmarezone", description = "Money making",properties = "autor: Pos yo!")
public class Nightmarezone extends PollingScript<ClientContext> implements PaintListener {

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
        espadita=ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id();
        System.out.println("Espadita es: " + espadita);

    }

    int mostrar1=0,mostrar2=0;
    int[] Prayer={2434,139,141,143};
    int[] Overload={11730,11731,11732,11733};
    int[] Absorve={11734,11735,11736,11737};
    int Rockcake=7510;
    int time,vida,corazon= Random.nextInt(1,40);
    boolean over=false;
    int tomar=Random.nextInt(300,800);
    float exp,fal,Timeleft,Expericiencia,Acumulada=0;
    int espadita,espadaEspecial=21742;
    @Override
    public void poll() {

        if(ctx.combat.style()!=style) {
            determinarSkill();
        }
        if(tomar%100==0){
            tomar=tomar/100;
            tomar=tomar*100;
        }

        if(ctx.combat.health()>50){
            over=false;
        }
        if(s==59){
            corazon=(corazon+Random.nextInt(40,50));
            if(corazon>59){
                corazon=corazon-60;
            }
        }
        estados();

        if(ctx.skills.experience(skill)-Expericiencia!=0){
            Acumulada=Acumulada+(ctx.skills.experience(skill)-Expericiencia);
            exp=(Acumulada*3600)/((System.currentTimeMillis()- initialTime)/1000);
            fal=ctx.skills.experienceAt(ctx.skills.realLevel(skill)+1)-ctx.skills.experience(skill);
            Timeleft=(fal*60)/exp;
            mostrar1= (int)exp;
            mostrar2= (int)Timeleft;

        }
        Expericiencia=ctx.skills.experience(skill);

    }
    int puntos=Random.nextInt(4,10);
    int puntosAbsrve,time_over;
    int[] espada={6523,4587,4151};
    public void estados(){
        if(ctx.prayer.prayerPoints()<puntos){
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(400,800);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }

            if(ctx.inventory.select().id(Prayer).count()>0) {
                ctx.inventory.select().id(Prayer).poll().interact("Drink");
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.prayer.prayerPoints()>10;
                    }
                }, time, 10);
            }
            puntos=Random.nextInt(4,10);
        }

        if(Integer.parseInt(ctx.widgets.widget(202).component(3).component(5).text())<tomar&&ctx.inventory.select().id(Absorve).count()>0){
            tomar=Random.nextInt(500,800);
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(400,800);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }
            while(Integer.parseInt(ctx.widgets.widget(202).component(3).component(5).text())<900&&ctx.inventory.select().id(Absorve).count()>0){
                puntosAbsrve=Integer.parseInt(ctx.widgets.widget(202).component(3).component(5).text());
                ctx.inventory.select().id(Absorve).poll().interact("Drink");
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return Integer.parseInt(ctx.widgets.widget(202).component(3).component(5).text())>puntosAbsrve;
                    }
                }, time, 10);
            }
        }

        if(ctx.inventory.select().id(espadita).count()>0){
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }
            ctx.inventory.select().id(espadita).poll().interact("Wield");
            time=Random.nextInt(400,500);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(espadita).count()==0;
                }
            }, time, 10);
        }

        if(ctx.combat.health()>50&&over==false){
            if(ctx.prayer.prayerPoints()>0){
                ctx.prayer.quickPrayer(true);
            }
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(400,800);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }
            vida=ctx.combat.health();
            ctx.inventory.select().id(Overload).poll().interact("Drink");
            time_over=m;
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.combat.health()<=vida-30;
                }
            }, time, 100);
            if(ctx.prayer.quickPrayer()==true) {
                ctx.prayer.quickPrayer(false);
            }
            time=Random.nextInt(500,700);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.combat.health()<=vida-40;
                }
            }, time, 100);
            if(ctx.combat.health()<=vida-40){
                over=true;
            }
        }
        if((ctx.combat.health()>1&&ctx.combat.health()<50)&&(over==true||ctx.skills.level(skill)>ctx.skills.realLevel(skill))){
            System.out.println("Moldere la piedra");
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }

            if((ctx.combat.health()>1&&ctx.combat.health()<50)) {
                if(Random.nextBoolean()) {
                    while(ctx.combat.health()>1&&ctx.combat.health()<50) {
                        vida=ctx.combat.health();
                        ctx.inventory.select().id(Rockcake).poll().interact(false, "Guzzle");
                        time = Random.nextInt(200, 600);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.combat.health() == 1 || ctx.combat.health() < vida;
                            }
                        }, time, 10);
                    }
                }
            }
        }
        if(ctx.combat.specialPercentage()==100){
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.INVENTORY;
                    }
                }, time, 10);
            }

            if(ctx.inventory.select().id(espadaEspecial).count()>0){
                ctx.inventory.select().id(espadaEspecial).poll().interact("Wield");
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
            });

            if(ctx.inventory.select().id(espadita).count()>0){
                ctx.inventory.select().id(espadita).poll().interact("Wield");
                time=Random.nextInt(400,500);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(espadita).count()==0;
                    }
                }, time, 10);
            }
        }
        if(ctx.combat.health()==1&&ctx.prayer.quickPrayer()){
            ctx.prayer.quickPrayer(false);
        }
        if(ctx.combat.health()==1&&corazon==s&&ctx.prayer.prayerPoints()>0){
            System.out.println("Entre a corazoncito");
            if(ctx.game.tab()!=Game.Tab.PRAYER){
                ctx.game.tab(Game.Tab.PRAYER);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.PRAYER;
                    }
                }, time, 10);
            }
            ctx.prayer.prayer(Effect.RAPID_HEAL,true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.prayer.prayerActive(Effect.RAPID_HEAL);
                }
            }, time, 10);
            ctx.prayer.prayer(Effect.RAPID_HEAL,false);
        }
    }
    int skill;
    Combat.Style style;
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
}