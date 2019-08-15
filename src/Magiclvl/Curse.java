package Magiclvl;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.Antiban;

import java.awt.*;
import java.util.concurrent.Callable;
/**
 * Created by user on 1/1/2019.
 */

@Script.Manifest(
        name="00Magiclvl",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class Curse extends PollingScript<ClientContext> implements PaintListener{
    Item coal= ctx.inventory.select().id(12019).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int markcount=0;
    int vueltas=0;
    boolean vuelta=false;
    org.powerbot.script.rt4.GeItem barr= new org.powerbot.script.rt4.GeItem(2353);
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
        g.fillRect(7, 54, 220, 58);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 58);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("Magiclv" + ctx.skills.level(Constants.SKILLS_MAGIC), 11, 87);
        value=barr.price-ore.price-coal1.price;
        //g.drawString("Intentos: " + markcount, 12, 106);
    }
    public int iteracion_de_tiempo () {
        int time = Random.nextInt(5, 4);
        return time;
    }
    public int segundos () {
        int segundos = Random.nextInt(250, 400);
        return segundos;
    }
    Magic.Spell spell=Magic.Spell.CONFUSE;
    Antiban antiban=new Antiban(ctx);
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        antiban.start();
        antiban.camera(101,99);

    }

    @Override
    public void poll() {



        if(ctx.skills.level(Constants.SKILLS_MAGIC)>=3&&ctx.skills.level(Constants.SKILLS_MAGIC)<11){
            equipar(1383);
            spell=Magic.Spell.CONFUSE;
        }else if(ctx.skills.level(Constants.SKILLS_MAGIC)>=11&&ctx.skills.level(Constants.SKILLS_MAGIC)<19){
            equipar(1383);
            spell=Magic.Spell.WEAKEN;
        }else if(ctx.skills.level(Constants.SKILLS_MAGIC)>=19){
            equipar(1385);
            spell=Magic.Spell.CURSE;
        }

        if(!ctx.game.tab().equals(Game.Tab.MAGIC)) {
            ctx.game.tab(Game.Tab.MAGIC);
            Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperare a estar en tab magic");
                return ctx.game.tab().equals(Game.Tab.MAGIC);
                }
            }, segundos(), iteracion_de_tiempo());
        }

        final int exp=ctx.skills.experience(Constants.SKILLS_MAGIC);

        if(!ctx.magic.casting(spell)) {
            ctx.magic.cast(spell);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperare a castear magic 1");
                    return ctx.magic.casting(spell);
                }
            }, segundos(), iteracion_de_tiempo());
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperare interaccion");
                return ctx.players.local().animation()==-1;
            }
        }, segundos(), iteracion_de_tiempo());
        
	if(ctx.magic.casting(spell))
	if(!ctx.game.crosshair().equals(Game.Crosshair.ACTION)) {
            ctx.npcs.select().id(520).poll().interact("Cast",ctx.npcs.select().id(520).poll().name());
            detener();
        }
        ctx.input.move(ctx.magic.component(spell).nextPoint());

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperare interaccion");
                return ctx.players.local().animation()!=-1;
            }
        }, segundos(), iteracion_de_tiempo());



        if(ctx.skills.level(Constants.SKILLS_MAGIC)==37||(!ctx.magic.ready(spell))||antiban.stop(h,m)){
            ctx.controller.stop();
            detener();
        }

    }
    public void equipar(final int ID){
        if(ctx.magic.casting(spell)&&ctx.inventory.select().id(ID).count() == 1) {
            ctx.magic.cast(spell,"Cancel");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperare a castear magic 2");
                    return !ctx.magic.casting(spell);
                }
            }, segundos(), iteracion_de_tiempo());
        }

        if(ctx.inventory.select().id(ID).count() == 1) {
            ctx.game.tab(Game.Tab.INVENTORY);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperare a estar en tab Inventory");
                    return ctx.game.tab().equals(Game.Tab.INVENTORY);
                }
            }, segundos(), iteracion_de_tiempo());
            if (ctx.inventory.select().id(ID).count() == 1) {
                ctx.inventory.select().id(ID).poll().interact("Wield");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperare a estar en tab magic");
                        return ctx.inventory.select().id(ID).count()==0;
                    }
                }, segundos(), iteracion_de_tiempo());
            }
        }
        if(ctx.inventory.select().id(ID).count() == 1){
            equipar(ID);
        }
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
}
