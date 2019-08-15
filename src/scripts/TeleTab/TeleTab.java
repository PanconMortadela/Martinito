package scripts.TeleTab;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.*;

import javax.sound.sampled.Port;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Martinito on 24/05/2018.
 */
@Script.Manifest(name="0Teletab", description = "Money making",properties = "autor: Pos yo!")
public class TeleTab  extends PollingScript<ClientContext> implements PaintListener {
    //attributos portal
    GameObject Portal;
    int entrada=15478; String entradas="Friend's house";
    int salida=4525; String Salidas="Enter";

    int widgetPortal=162,componentPortal=40,componentPortal1=0;

    //atributos lectern
    GameObject Lectern;
    int object_TP=13647; String teletabs="Study";

    //Atributos NPC
    int Npcid=1614; Npc Phials;

    int widget_lvup=233,Component_lvup=2;

    int widget_exchangeall=219,component_exchangeall=1,component_exchangeall1=3;

    int clay=1761; int claynote=1762;

    int widgetTab=79;
    static Map<Integer,Integer> Tabs=new HashMap<Integer,Integer>();
    int MagicLv,componentTab;

    Areas areas=new Areas();
    Tile lugar;
    int time;
    boolean Antiban;

    state Estado;




    String[] accion={

    };
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
        g.fillRect(7, 54, 220, 58);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 58);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("Tab:" + vueltas + " LevelMagic: " + ctx.skills.level(Constants.SKILLS_MAGIC), 11, 87);
        value=barr.price;
        g.drawString("Tabs_Value: " + (value), 12, 106);
    }

    @Override
    public void start(){
        Tabs.put(25,11);
        Tabs.put(31,12);
        Tabs.put(37,13);
        //Tabs.put(40,17);
        Tabs.put(45,14);
        Tabs.put(51,15);
        Tabs.put(58,16);
        elegir_tab();
        initialTime=System.currentTimeMillis();
    }



    @Override
    public void poll() {
        if(!ctx.movement.running()){
            ctx.movement.running(true);
        }
        if(ctx.inventory.select().id(claynote).count(true)<28){
            JOptionPane.showInputDialog("Compra para hacer mas tabs e.e");
            ctx.controller.stop();
        }
        if(ctx.inventory.select().id(clay).count()==0&&ctx.players.local().tile().x()>5000){
            System.out.println("Voy a Exit");
            exit();
        }
        if(ctx.inventory.select().id(clay).count()>0&&ctx.players.local().tile().x()>5000){
            System.out.println("Voy hacer tabs");
            hacertab();
        }
        if(ctx.inventory.select().id(clay).count()==0&&ctx.players.local().tile().x()<5000){
            System.out.println("Voy a cambiar notes");
            cambiarnotes();
        }
        if(ctx.inventory.select().id(clay).count()>0&&ctx.players.local().tile().x()<5000){
            System.out.println("Voy a casa de amiguito");
            go_Portal();
        }

    }

    public void cambiarnotes(){
        Phials=ctx.npcs.select().id(Npcid).poll();
        if(!ctx.game.tab().equals(Game.Tab.INVENTORY)){
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        if(Phials.inViewport()) {
            ctx.inventory.select().id(claynote).poll().interact("Use");
            if (Phials.interact("Use")) {
                time=Random.nextInt(300,500);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).visible();
                    }
                }, time, 10);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return false;
                    }
                }, 1000, 1);
            }
            if (ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).visible()) {
                ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return ctx.inventory.select().id(clay).count()>0;
                    }
                }, time, 50);
            }
        }else{
            boolean Anti=Random.nextBoolean();
            if(Anti==true){
                ctx.camera.turnTo(Phials.tile());
                if(!Phials.inViewport()){
                    Anti=false;
                }
            }
            if(Anti==false) {
                time = Random.nextInt(100, 500);
                ctx.movement.step(areas.area2(Phials.tile()).getRandomTile());
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return areas.area2(lugar).contains(ctx.players.local().tile()) || Phials.inViewport();
                    }
                }, time, 50);
            }
        }

    }
    public void go_Portal(){
        Portal=ctx.objects.select().id(entrada).poll();
        if(Portal.interact(entradas)) {
            time = Random.nextInt(500, 1000);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (detener()) return true;
                    return ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).visible();
                }
            }, time, 10);

            if (ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).visible()) {
                ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).click();
                time=Random.nextInt(1000,2000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return ctx.objects.select().id(salida).poll().inViewport();
                    }
                }, time, 5);
            }
        }else{
            boolean Anti=Random.nextBoolean();
            if(Anti==true){
                ctx.camera.turnTo(Portal.tile());
                if(!Portal.inViewport()){
                    Anti=false;
                }
            }
            if(Anti==false){
                time = Random.nextInt(100, 500);
                ctx.movement.step(areas.area2(Portal.tile()).getRandomTile());
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return areas.area2(lugar).contains(ctx.players.local().tile()) || Portal.inViewport();
                    }
                }, time, 50);
            }
        }
    }
    public void hacertab(){

        Lectern=ctx.objects.select().id(object_TP).poll();
        Lectern.bounds(24, 36, -96, -28, -12, 4);
        if(Lectern.inViewport()){

                time=Random.nextInt(300,500);
                System.out.println("Interactue con el objeto");
                int Time2=Random.nextInt(2,5);

                Lectern.interact(teletabs);
                Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if(detener())return true;
                            return ctx.widgets.widget(widgetTab).component(componentTab).visible();
                        }
                    }, time, 25);



                if(ctx.widgets.widget(widgetTab).component(componentTab).visible()) {
                    time = Random.nextInt(200, 500);

                    ctx.widgets.widget(widgetTab).component(componentTab).interact("Make-All");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (detener()) return true;
                            if (ctx.widgets.widget(widget_lvup).component(Component_lvup).visible()) {    //Si sube de nivel
                                elegir_tab();
                                return true;
                            }
                            return ctx.inventory.select().id(clay).count() == 0;
                        }
                    }, time, 300);
                }


        }else{
            boolean Anti=true;
            if(Anti==true){
                ctx.camera.turnTo(Lectern.tile());
                Lectern=ctx.objects.select().id(object_TP).poll();
                if(!Lectern.inViewport()){
                    Anti=false;
                }
            }
            if(Anti==false) {
                time = Random.nextInt(100, 300);
                ctx.movement.step(areas.area1(Lectern.tile()).getRandomTile());
                lugar = ctx.movement.destination();
                Lectern=ctx.objects.select().id(object_TP).poll();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return areas.area2(lugar).contains(ctx.players.local().tile()) || Lectern.inViewport();
                    }
                }, time, 30);
            }
        }

    }

    public void exit() {

        Portal = ctx.objects.select().id(salida).poll();
        if (Portal.click()) {
            time = Random.nextInt(100, 500);
            System.out.println("Interactue con el objeto");

                Portal.interact(Salidas);
                time=Random.nextInt(300,700);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;

                        return ctx.objects.select().id(entrada).poll().inViewport();
                    }
                }, time, 25);

        } else {
            boolean Anti=Random.nextBoolean();
            if(Anti==true){
                ctx.camera.turnTo(Portal.tile());
                if(!Portal.inViewport()){
                    Anti=false;
                }
            }
            if(Anti==false) {
                time = Random.nextInt(100, 500);
                ctx.movement.step(areas.area2(Portal.tile()).getRandomTile());
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;

                        return areas.area2(lugar).contains(ctx.players.local().tile()) || Portal.inViewport();
                    }
                }, time, 50);
            }
        }
    }

    public void elegir_tab(){
        MagicLv=ctx.skills.realLevel(Constants.SKILLS_MAGIC);
        if(MagicLv>=25&&MagicLv<31){
            componentTab=Tabs.get(25);
            vueltas="Varrock";
        }
        if(MagicLv>=31&&MagicLv<37){
            componentTab=Tabs.get(31);
            vueltas="Lumbri";
        }
        if(MagicLv>=37&&MagicLv<45){
            componentTab=Tabs.get(37);
            vueltas="Falador";
        }
        /*if(MagicLv>=40&&MagicLv<45){
            componentTab=Tabs.get(40);
        }*/
        if(MagicLv>=45&&MagicLv<51){
            componentTab=Tabs.get(45);
            vueltas="Canifi";
        }
        if(MagicLv>=51&&MagicLv<58){
            componentTab=Tabs.get(51);
            vueltas="Ardo";
        }
        if(MagicLv>=58){
            componentTab=Tabs.get(58);
            vueltas="watch tower";
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
    public enum state{
        cambiarNotes,portalAmiguito,portalExit,Lectern
    }

}
