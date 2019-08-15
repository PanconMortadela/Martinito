package scripts.TeleHighAlchemist;

import org.powerbot.script.*;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Magic.Spell;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="00TeleHighAlch", description = "Money making",properties = "autor: Pos yo!")
public class TeleHighalch  extends PollingScript<ClientContext> implements PaintListener {

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
        g.drawString(" LevelMagic: " + ctx.skills.level(Constants.SKILLS_MAGIC) + "Mundo: "+WolrdStay +" - " + estaba, 11, 87);
        g.drawString("Exp/tiempo: " + mostrar1,10,106);
        g.drawString( "Tiempo:" + mostrar2,10,126);

    }
    int mostrar1=0,mostrar2=0;
    double Acumulada=0;
    double expH;
    double Timeleft;
    int Faltante=ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_MAGIC)+1)-ctx.skills.experience(Constants.SKILLS_MAGIC);
    Spell HA= Magic.Spell.HIGH_ALCHEMY;
    Spell AT= Spell.TELEPORT_KOUREND;
    int exp;
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        stay();

    }

    int WolrdStay,estaba=0;
    public int iteracion_de_tiempo(){
        int time= Random.nextInt(10,25);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(200,400);
        return segundos;
    }
    World mundito;
    boolean fail,condicion=false;int time,Hop_randomTime;
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
        Iterator<World> mundo= ctx.worlds.select().types(World.Type.MEMBERS).joinable().iterator();
        if(WolrdStay==525){             // ultimo mundo ctx.worlds.types(World.Type.MEMBERS).reverse().iterator().next().id()
            ctx.worlds.select().id(491).poll().hop();
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
    String est="";
    public void stay(){
        est="Voy a ver en que world estoy";
        String x= ctx.widgets.widget(429).component(3).text();
        String[] parts = x.split(" ");
        try {
            estaba=WolrdStay;
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);

    }

    public enum cast{
        Cast1,Cast2,item;
    }
    cast Cast=cast.Cast1;
    @Override
    public void poll() {
        detener();
        if(m==29||m==59){
            condicion=true;
            if(m==29)
                Hop_randomTime=Random.nextInt(30,35);
            else
                Hop_randomTime=Random.nextInt(0,5);
        }

        if((m==Hop_randomTime)&&condicion==true){
            hop();
            condicion=false;
        }

        if(Cast==cast.Cast1){
            cast1();
            Cast=cast.item;
        }else if(Cast==cast.Cast2){
            cast2();
            Cast=cast.Cast1;
        }else if(Cast==cast.item){
            item();
            Cast=cast.Cast2;
        }

        Acumulada=Acumulada+(ctx.skills.experience(Constants.SKILLS_MAGIC)-exp);
        expH=(Acumulada*3600)/((System.currentTimeMillis()- initialTime)/1000);
        Faltante=ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_MAGIC)+1)-ctx.skills.experience(Constants.SKILLS_MAGIC);
        Timeleft=(Faltante*60)/expH;

    }

    public void cast1(){
            if(ctx.game.tab()!=Game.Tab.MAGIC){
                ctx.game.tab(Game.Tab.MAGIC);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab()==Game.Tab.MAGIC;
                    }
                }, time, 25);
            }
            exp=ctx.skills.experience(Constants.SKILLS_MAGIC);
            ctx.magic.cast(HA);

            time= Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab()==Game.Tab.INVENTORY&&ctx.players.local().animation()==-1;
                }
            }, segundos(), iteracion_de_tiempo());

    }

    public void item(){
        ctx.input.move(Random.nextInt(740,751),Random.nextInt(375,386));
        ctx.input.click(true);

        time= Random.nextInt(200,400);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.skills.experience(Constants.SKILLS_MAGIC)!=exp;
            }
        }, segundos(), iteracion_de_tiempo());
    }

    public void cast2(){
        if(ctx.game.tab()!=Game.Tab.MAGIC){
            ctx.game.tab(Game.Tab.MAGIC);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab()==Game.Tab.MAGIC;
                }
            }, segundos(), iteracion_de_tiempo());
        }
        exp=ctx.skills.experience(Constants.SKILLS_MAGIC);
        ctx.magic.cast(AT);
        time= Random.nextInt(200,400);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.skills.experience(Constants.SKILLS_MAGIC)!=exp;
            }
        }, segundos(), iteracion_de_tiempo());

        mostrar1=(int) expH;
        mostrar2=(int) Timeleft;
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