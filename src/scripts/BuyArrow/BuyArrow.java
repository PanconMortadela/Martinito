package scripts.BuyArrow;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.World;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;
/**
 * Created by Martinito on 20/05/2018.
 */
@Script.Manifest(name="000000Buy", description = "Money making",properties = "autor: Pos yo!")
public class BuyArrow extends PollingScript<ClientContext> implements PaintListener {
    int widget=300,component=16, item=886, npc=8694, cantidad=0,cant=1450,widgetb=0,componentb=1,count=0,count1=0;    //npc=536

    String est="";
    int[] worlds={1,8,16,26,35,82,83,84,93,94}, ex={81,85},
            c={2,8,14,20,26,32,38,44,50,56,62,68};
    int world,stado=1;
    int s2;
    int coin=ctx.inventory.select().id(995).count(true);
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 1, 12);
    int value;
    int h, m, s;
    GeItem barr= new GeItem(item);
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        camera();
    }
    int time;
    public void camera(){
        System.out.println("Camaraaaaa");
        //ctx.input.move(areas.punto(ctx.widgets.widget(161).component(24).centerPoint()));
        //ctx.input.click(true);
        time=Random.nextInt(500,800);
        ctx.camera.pitch(Random.nextInt(95,99));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.pitch()>=95&&ctx.camera.pitch()<=100;
            }
        }, time, 10);

    }
    @Override
    public void repaint(Graphics g1) {

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
        g.drawString("Estado: " + est, 11, 87);
        value=barr.price;
        g.drawString("Profit: " + value*cantidad, 12, 106);
        //g.drawString("State:" + state, 144, 71);
    }
    World mundito;
    long llegada, Salida;
    int H_stop=3,M_stop=Random.nextInt(50,55);
    @Override
    public void poll() {

        if(h==H_stop&&m==M_stop){
            ctx.controller.stop();
            detener();
        }
        coin();
        if(stado==1){
            llegada=(int)((System.currentTimeMillis()- initialTime)/1000);
            npc();
            stado++;
        }
        if(stado==2){
            compra();
            stado++;
        }
        if(stado==3){
            Salida=(int)((System.currentTimeMillis()- initialTime)/1000);;
            espera();
            stado++;
        }
        if(stado==4){
            hop();
            stado=1;
        }

    }
    public void coin(){
        if(ctx.inventory.select().id(995).count(true)<50){
            JOptionPane.showInputDialog("Se te acabo el dinero perraaa");
            ctx.controller.stop();
            detener();
        }
    }

    int seg; int resto;
    private void espera() {   ///Acomodar tiempo de espera que no toma en cuenta los segundos por long :/
        System.out.println("Llegada= " + llegada + " Salida = " + Salida);
        resto=(int)(Salida-llegada);
        if(resto<20)
            seg=Random.nextInt((20-resto),(25-resto));
        else if (resto>=20&&resto<25)
            seg=2;
        else
            seg=1;
        System.out.println("Tiempo de espera= "+ seg);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return false;
            }
        }, segundos(), seg);
    }

    public int iteracion_de_tiempo(){
        int time=Random.nextInt(1,4);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(800,1200);
        return segundos;
    }
    public void hop(){  //Este hop Funciona Bien

        ctx.worlds.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(69).component(12).component(0).visible();
            }
        }, segundos(), iteracion_de_tiempo());
        boolean hay_mundo=false;
        stay();
        Iterator<World> mundo= ctx.worlds.select().types(World.Type.FREE).joinable().iterator();
        if(WolrdStay==504){             // ultimo mundo ctx.worlds.types(World.Type.MEMBERS).reverse().iterator().next().id()
            ctx.worlds.select().id(301).poll().hop();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (ctx.game.clientState() != 30) {
                        return true;
                    }
                    return false;
                }
            }, segundos(), iteracion_de_tiempo());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Voy a esperar 1");
                    if (ctx.game.clientState() == 30) {
                        return true;
                    }
                    return false;
                }
            }, segundos(), iteracion_de_tiempo());
            stay();
        }else {
            while (mundo.hasNext()) {
                mundito = mundo.next();
                System.out.println("Mundito es: " + mundito.id());
                if (mundito.id() >= WolrdStay) {
                    hay_mundo = true;
                    break;
                }
            }

            if (hay_mundo == true) {
                final World aux;
                aux = mundito;
                System.out.println("aux es: " + aux.id());
                if(aux.hop()) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (ctx.game.clientState() != 30) {
                                return true;
                            }
                            return false;
                        }
                    }, segundos(), iteracion_de_tiempo());
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Voy a esperar 1");
                            if (ctx.game.clientState() == 30) {
                                return true;
                            }
                            return false;
                        }
                    }, segundos(), iteracion_de_tiempo());
                    stay();
                }else{
                    for(int i=2;i<ctx.widgets.widget(69).component(17).componentCount();i=i+6) {
                        if (Integer.parseInt(ctx.widgets.widget(69).component(17).component(i).text()) == aux.id()) {
                            ctx.widgets.widget(69).component(17).component(i).click();
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    if (ctx.game.clientState() != 30) {
                                        return true;
                                    }
                                    return false;
                                }
                            }, segundos(), iteracion_de_tiempo());
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    System.out.println("Voy a esperar 1");
                                    if (ctx.game.clientState() == 30) {
                                        return true;
                                    }
                                    return false;
                                }
                            }, segundos(), iteracion_de_tiempo());
                        }
                    }
                    stay();
                }
            }
        }


    }
    int WolrdStay;
    public void stay(){
        String x= ctx.widgets.widget(429).component(3).text();
        String[] parts = x.split(" ");
        try {
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);
    }
    public void npc(){
        do {
            if(detener())break;
            est="Voy a buscar al NPC";
            Npc NPC= ctx.npcs.select().id(npc).poll();
            s2=(int)(System.currentTimeMillis()-initialTime);
            est="s2: " + s2;
            if(!NPC.inViewport()){
                ctx.camera.turnTo(NPC.tile());
            }

            NPC.interact("Trade");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(widget).component(component).component(1).visible();
                }
            }, 1000, 4);
        }while (!ctx.widgets.widget(widget).component(component).visible());
    }

    Point p;
    public void clickcomprar(){
        p=new Point(Random.nextInt(87,110),Random.nextInt(82,102));
        ctx.input.move(p);
        ctx.input.click(false);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.menu.opened();
            }
        }, Random.nextInt(200,300), 10);
        p=new Point(Random.nextInt(p.x-50,p.x+30),Random.nextInt(p.y+68,p.y+74));
        ctx.input.move(p);
        ctx.input.click(true);
    }

    public void compra(){
        est="Voy a comprar";
        int time=Random.nextInt(300,400);
        while(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()>cant){
            coin();
            if(detener())break;
            if(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()<cant+5) break; // detener la compra
            cantidad=ctx.inventory.select().id(886).count(true);
            if(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()>1495)
                clickcomprar();//ctx.widgets.widget(widget).component(component).component(componentb).interact("Buy 10"); // cambie el de 50 por 10
            else
                clickcomprar();//ctx.widgets.widget(widget).component(component).component(componentb).interact("Buy 10");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (cantidad!=ctx.inventory.select().id(886).count(true));
                }
            }, time, 10);
        }
        time=Random.nextInt(300,400);
        ctx.widgets.close(ctx.widgets.widget(widget),true);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.widgets.widget(widget).component(component).component(1).visible();
            }
        }, time, 10);
    }

    public boolean detener(){

        if(ctx.controller.isStopping()){
            System.out.println("me detendre1");
            ctx.controller.stop();
            stop();
            return true;

        }else if(!ctx.game.loggedIn()){
            System.out.println("me detendre2");
            ctx.controller.stop();
            stop();
            return true;
        }else{
            return false;
        }
    }
}
