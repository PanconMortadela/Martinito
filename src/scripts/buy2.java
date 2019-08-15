package scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Npc;

import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="BF0Buy2", description = "Money making",properties = "autor: Pos yo!")
public class buy2 extends PollingScript<ClientContext> implements PaintListener {
    int widget=300,component=16, item=886, npc=1309, cantidad=0,cant=1440,widgetb=0,componentb=1,count=0,count1=0;    //npc=536
    String est="";
    int[] worlds={1,8,16,26,35,81,82,83,84,85,93,94}, ex={81,85},
               c={2,8,14,20,26,32,38,44,50,56,62,68};
    int world,stado=0;
    int s2;
    int coin=ctx.inventory.select().id(995).count(true);
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int value;
    int h, m, s;
    GeItem barr= new GeItem(item);
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
        g.drawString("Estado: " + est, 11, 87);
        value=barr.price;
        g.drawString("Profit: " + value*cantidad, 12, 106);
        //g.drawString("State:" + state, 144, 71);
    }
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        ctx.camera.pitch(Random.nextInt(95,99));
        while(!ctx.widgets.widget(69).component(7).component(0).visible()) {
            ctx.widgets.widget(161).component(38).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(182).component(12).visible();
                }
            }, 200, 30);
            ctx.widgets.widget(182).component(7).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(69).component(7).component(0).visible();
                }
            }, 200, 30);
        }
    }
    @Override
    public void poll() {
        estado();
        if(detener())ctx.controller.stop();
        if(count==6){
            est="Voy a esperar por 6 hop 20seg";
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return false;
                }
            }, 1000, 20);
            count=0;
        }
    }

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

    public void compra(){
        est="Voy a comprar";
        while(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()>cant){
            if(detener())break;
            if(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()<cant+5) break; // detener la compra
            cantidad=ctx.inventory.select().id(886).count(true);
            if(ctx.widgets.widget(widget).component(component).component(componentb).itemStackSize()>1485)
                ctx.widgets.widget(widget).component(component).component(componentb).interact("Buy 50");
            else
                ctx.widgets.widget(widget).component(component).component(componentb).interact("Buy 10");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (cantidad!=ctx.inventory.select().id(886).count(true));
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
    int s3;
    public void hop(){
        est="Voy a cambiar de world";
        s3=(int)(System.currentTimeMillis()-initialTime);
        est="s3: " + s3 ;
        int time= Random.nextInt(15000,17000);
        if(s3-s2<time){
            time=(time-(s3-s2))/1000;
            est="Esperare por no comprar: " + time +"seg";
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Voy a esperar");
                    return false;
                }
            }, 1000, time);
        }
        while(ctx.worlds.open()){
            System.out.println("entre en el while del hop()");
            if(detener())break;
            for(int i=0;i<worlds.length;i++){
                System.out.println("entre en el for del hop");
                if(world==94){
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
                }else if(worlds[i]==world){
                    System.out.println("entre en el if del for hop");
                    System.out.println("voy para el world "+worlds[i+1]);
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
    public void stay(){
        est="Voy a ver en que world estoy";
        for(int i=2;i<ctx.widgets.widget(69).component(7).componentCount();i=i+6){
            if((ctx.widgets.widget(69).component(7).component(i).textColor()==8355711))
                if ((Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text())==ex[0])||(Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text())==ex[1])) {
                    System.out.println("este world no es :D");
            }else{
                world=Integer.parseInt(ctx.widgets.widget(69).component(7).component(i).text());
                System.out.println("Estoy en el world " + world);
                break;
            }
        }
    }

  /*  public void reset(){

        if(world==94){
            System.out.println("Voy a reset world");
            world=1;
        }
    }*/
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
