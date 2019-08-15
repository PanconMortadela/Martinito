package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Npc;

import java.awt.*;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

@Script.Manifest(name="Buy0", description = "Money making",properties = "autor: Pos yo!")
public class Buy0 extends PollingScript<ClientContext> implements PaintListener {

    private long initialTime;
    long initialTime2;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int value;
    int h, m, s;
    int item=886;
    GeItem barr= new GeItem(item);
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
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        initialTime2=System.currentTimeMillis();
    }
    @Override
    public void poll() {
        estado();
    }

    String est="";
    int[] worlds={1,8,16,26,35,81,82,83,84,85,93,94}, ex={81,85},
            c={2,8,14,20,26,32,38,44,50,56,62,68};
    int world,stado=0;
    int s2,s1;
    int coin=ctx.inventory.select().id(995).count(true);
    private void estado() {
        if(stado==0){
            stay();
            stado++;
        }
        if(stado==1){
            npc();
            stado++;
        }
        if(stado==2){
            compra();
            stado++;
        }
        if(stado==3){
            hop();
            stado=1;
        }
        if(stado==4){
            //reset();
            stado=1;
        }

    }
    public void stay(){////////////////////////////////////Funcion para ver en que world estoy..
        est="Voy a ver en que world estoy"; //Muestra en el repaint
        String t=ctx.widgets.widget(69).component(2).text();
        StringTokenizer st = new StringTokenizer(t, " ");
        while(st.hasMoreTokens()){
            t=st.nextToken();
            if(Integer.parseInt(t)>300){
                world=Integer.parseInt(t)-300;

            }else System.out.println("No encontre world");
        }
    }

    public void npc(){

        do {
            if(detener())break;
            est="Voy a buscar al NPC";
            Npc NPC= ctx.npcs.select().id(npc).poll();

            if(!NPC.inViewport()){
                ctx.camera.turnTo(NPC.tile());
            }
            NPC.interact("Trade");
            initialTime2=System.currentTimeMillis();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(widget).component(component).component(1).visible();
                }
            }, 500, 20);// Espero 10 segundos para que sea visible la ventana de trade del NPC
        }while (!ctx.widgets.widget(widget).component(component).visible()); // Lo voy a repetir hasta que sea visible por lag :v

    }
    int widget=300,component=2,  npc=1309, cantidad=0,cant=1430,widgetb=0,componentb=0,count=0,count1=0;    //npc=536
    public void compra(){
        est="Voy a comprar";
        while(ctx.widgets.widget(widget).component(2).component(1).itemStackSize()>cant){
            if(detener())break;
            if(ctx.widgets.widget(widget).component(2).component(1).itemStackSize()<cant+5) break; // detener la compra
            cantidad=ctx.inventory.select().id(886).count(true);
            if(ctx.widgets.widget(widget).component(2).component(1).itemStackSize()>1470) {
                ctx.widgets.widget(widget).component(2).component(1).interact("Buy 50");
                cantidad = cantidad + 50;
            }else{
            ctx.widgets.widget(widget).component(2).component(1).interact("Buy 10");cantidad = cantidad + 10;}
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (ctx.inventory.select().id(item).count(true)==cantidad);
                }
            }, 300, 40);
        }
        ctx.widgets.close(ctx.widgets.widget(widget));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.widgets.widget(widget).component(component).component(1).visible();
            }
        }, 300, 40);
    }
    public void hop(){
        est="Voy a cambiar de world";
        if(System.currentTimeMillis()-s2<=11000){/////////////////////// Esperar 11 seg para cambiar de world
            est="Esperare que pasen 11 seg";

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return System.currentTimeMillis()-s2>=11000;
                }
            }, 1000, 10);
        }

        while(ctx.worlds.open()){
            System.out.println("Entre en el while del hop()");
            if(detener())break;
            for(int i=0;i<worlds.length;i++){/////////////////////////Buscar a que world me voy
                System.out.println("entre en el for del hop");
                if(world==94){////////////////////////////////////////si wold llega al final debo ir al inicio
                    ctx.widgets.widget(69).component(7).component(c[0]).click();
                    world=1;
                    count++;
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Voy a esperar");
                            return ctx.game.clientState()==30;
                        }
                    }, 5000, 4);
                    break;
                }else if(worlds[i]==world){///////////////////////////////////para determinar el mundo siguiente
                    System.out.println("entre en el if del for hop");
                    System.out.println("voy para el world " + worlds[i+1]);
                    world=worlds[i+1];
                    if(world==ex[0]||world==ex[1]){
                        world=worlds[i+2];
                        ctx.widgets.widget(69).component(7).component(c[i+2]).click();
                        count++;
                    }else {
                        ctx.widgets.widget(69).component(7).component(c[i + 1]).click();
                        count++;
                    }

                    //reset();
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Voy a esperar");
                            return ctx.game.clientState()==30;
                        }
                    }, 5000, 4);
                    break;
                }
            }
            break;
        }



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

/*
        for(int i=2;i<ctx.widgets.widget(69).component(7).componentCount();i=i+6){
            if((ctx.widgets.widget(69).component(7).component(i).textColor()==8355711)) // verificar el world atravez del color
                if ((Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text())==ex[0])||(Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text())==ex[1])) {
                    System.out.println("este world no es :D");
                }else{
                    world=Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text());
                    System.out.println("Estoy en el world " + world);
                    break;
                }
        }



            Las mujeres viven la vida esperando sin estar pendiente de buscar..
            Los hombres viven la vida buscando sin estar pendiente de esperar..

 */