package scripts.PrayerBone;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import scripts.Utilitys.Areas;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="00PrayerBones", description = "Money making",properties = "autor: Pos yo!")
public class PrayerBone extends PollingScript<ClientContext> implements PaintListener {
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
        g.drawString(" LevelMagic: " + ctx.skills.level(Constants.SKILLS_MAGIC), 11, 87);
        g.drawString("Exp/tiempo: " + mostrar1,10,106);
        g.drawString( "Tiempo:" + mostrar2,10,126);

    }
    int mostrar1=0,mostrar2=0;
    int bones=532;
    public enum estados{
        Cambiarnotas,BonesAltar,Entrar,Salir;
    }
    @Override
    public void poll() {
        inventario=ctx.inventory.select().id(bones).count();

        if(!ctx.movement.running()){
            ctx.movement.running(true);
        }
        if(ctx.inventory.select().id(bonenote).count(true)<28){
            JOptionPane.showInputDialog("Compra para hacer mas tabs e.e");
            ctx.controller.stop();
        }
        if(ctx.inventory.select().id(bones).count()==0&&ctx.players.local().tile().x()>5000){
            System.out.println("Voy a Exit");
            exit();
        }
        if(ctx.inventory.select().id(bones).count()>0&&ctx.players.local().tile().x()>5000){
            System.out.println("Voy hacer bones");
            bonesAltar();
        }
        if(ctx.inventory.select().id(bones).count()==0&&ctx.players.local().tile().x()<5000){
            System.out.println("Voy a cambiar notes");
            cambiarnotes();
        }
        if(ctx.inventory.select().id(bones).count()>0&&ctx.players.local().tile().x()<5000){
            System.out.println("Voy a casa de amiguito");
            go_Portal();
        }
    }
    Npc Phials;
    int Npcid=1614;
    int bonenote=533;
    int widget_exchangeall=219,component_exchangeall=1,component_exchangeall1=3;
    Tile lugar;
    Areas areas=new Areas();
    GameObject Portal;
    int entrada=15478; String entradas="Friend's house";
    int salida=4525; String Salidas="Enter";
    int widgetPortal=162,componentPortal=40,componentPortal1=0;

    public void exit() {

        Portal = ctx.objects.select().id(salida).poll();
        if (Portal.interact(Salidas)) {
            System.out.println("Interactue con el objeto");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (detener()) return true;

                    return ctx.objects.select().id(entrada).poll().inViewport()&&ctx.players.local().inViewport();
                }
            }, Random.nextInt(1000,2000), Random.nextInt(2,3));

        } else {

        ctx.movement.step(Portal.tile());
        lugar = ctx.movement.destination();
        Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;

                        return areas.area2(lugar).contains(ctx.players.local().tile()) || Portal.inViewport();
                    }
                }, segundos(), iteracion_de_tiempo());

        }
    }

    public void go_Portal(){
        Portal=ctx.objects.select().id(entrada).poll();
        if(Portal.interact(entradas)) {

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (detener()) return true;
                    return ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).visible();
                }
            },segundos(), iteracion_de_tiempo());

            if (ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).visible()) {
                ctx.widgets.widget(widgetPortal).component(componentPortal).component(componentPortal1).click();

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return ctx.objects.select().id(salida).poll().inViewport()&&ctx.players.local().inViewport();
                    }
                }, Random.nextInt(1000,2000), Random.nextInt(2,3));
            }
        }else{
                ctx.movement.step(new Tile(2951,3221,0));
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return areas.area2(lugar).contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;
                    }
                }, segundos(), iteracion_de_tiempo());
        }
    }


    public void cambiarnotes(){
        Phials=ctx.npcs.select().id(Npcid).poll();
        if(!ctx.game.tab().equals(Game.Tab.INVENTORY)){
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        if(Phials.inViewport()) {
            ctx.inventory.select().id(bonenote).poll().interact("Use");
            if (Phials.interact(false,"Use")) {

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).visible();
                    }
                },segundos(), iteracion_de_tiempo());

            }
            if (ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).visible()) {
                ctx.widgets.widget(widget_exchangeall).component(component_exchangeall).component(component_exchangeall1).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return ctx.inventory.select().id(bones).count()>0;
                    }
                }, segundos(), iteracion_de_tiempo());
            }
        }else{

                ctx.movement.step(new Tile(2950,3213));
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        return areas.area2(lugar).contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;
                    }
                }, segundos(), iteracion_de_tiempo());
            }

    }

    public int iteracion_de_tiempo(){
        int time= Random.nextInt(10,25);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(400,800);
        return segundos;
    }
    Item bone;
    GameObject Altar=ctx.objects.select().id(13197).poll();
    int inventario;
    Tile llegar;
    boolean step;
    public void bonesAltar(){

        bone=ctx.inventory.select().id(bones).poll();
        if(areas.area3(ctx.objects.select().id(salida).poll().tile()).contains(ctx.players.local())) {
            ctx.movement.step(ctx.objects.select().id(13197).poll().tile());
            llegar = ctx.movement.destination();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Estoy esperando que sea !=-1");
                    return areas.area2(llegar).contains(ctx.players.local()) || areas.area2(ctx.movement.destination()).contains(ctx.players.local());
                }
            }, segundos(), 100);
        }
        bone.interact(false,"Use");
        Altar=ctx.objects.select().id(13197).poll();
        Altar.bounds(-36, -28, -64, 0, -56, 56);
        if(Altar.interact(false,"Use")){
            detener();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Estoy esperando que sea !=-1");
                    return ctx.players.local().animation() != -1 || ctx.inventory.select().id(bones).count() == 0 || ctx.widgets.widget(233).component(1).visible();
                }
            }, segundos(), 100);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Estoy tirando bones al altar");
                    return ctx.inventory.select().id(bones).count() == 0 || ctx.widgets.widget(233).component(1).visible();
                }
            }, segundos(), 100);
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