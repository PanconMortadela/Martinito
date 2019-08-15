package scripts;

import org.powerbot.bot.rt4.RandomEvents;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt6.Hiscores;
import scripts.Utilitys.*;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Created by putito on 01/01/2013.
 */
@Script.Manifest(
        name="PMVarrockCourse",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class Varrock1 extends PollingScript<ClientContext> implements PaintListener{

    int[] id={
            14412,
            14413,
            14414,
            14832,
            14833,
            14834,
            14835,
            14836,
            14841
    };

    String[] accion={
            "Climb",
            "Cross",
            "Leap",
            "Balance",
            "Leap",
            "Leap",
            "Leap",
            "Hurdle",
            "Jump-off"
    };


    Area ini= new Area(new Tile(3221,3412,0),new Tile(3224,3417,0));//Area de inicio

    Area[] area={
            ini,
            new Area(new Tile(3220,3420,3),new Tile(3213,3409,3)),
            new Area(new Tile(3209,3413,3),new Tile(3200,3418,3)),
            new Area(new Tile(3198,3415,1),new Tile(3195,3417,1)),
            new Area(new Tile(3191,3407,3),new Tile(3199,3401,3)),
            new Area(new Tile(3181,3399,3),new Tile(3181,3381,3), new Tile(3190,3381,3),new Tile(3209,3394,3),new Tile(3209,3404,3),new Tile(3201,3404,3),new Tile(3201,3399,3)),
            new Area(new Tile(3217,3392,3),new Tile(3233,3403,3)),
            new Area(new Tile(3235,3402,3),new Tile(3241,3409,3)),
            new Area(new Tile(3235,3409,3),new Tile(3241,3416,3)),
    };
    Area[] llegada={
            area[1],
            area[2],
            area[3],
            area[4],
            area[5],
            area[6],
            area[7],
            area[8],
            area[0]

    };
    Experiencia experiencia=new Experiencia(ctx);


    Areas areas=new Areas();
    GameObject objeto;

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
        g.drawString("Lv Agilidad:" + ctx.skills.level(Constants.SKILLS_AGILITY), 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 106);
    }

    int level;
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        experiencia.iniciar(40);
        level=ctx.skills.level(Constants.SKILLS_AGILITY);
    }
    int WolrdStay;
    public int iteracion_de_tiempo(){
        int time=Random.nextInt(5,4);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(800,1200);
        return segundos;
    }
    World mundito;
    public void hop(){
        ctx.worlds.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(69).component(2).visible();
            }
        }, segundos(), iteracion_de_tiempo());
        boolean hay_mundo=false;
        stay();
        Iterator<World> mundo= ctx.worlds.select().types(World.Type.MEMBERS).joinable().iterator();
        if(WolrdStay==525){             // ultimo mundo
            ctx.worlds.select().id(492).poll().hop();
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
                aux.hop();
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


    }
    String est="";
    public void stay(){
        est="Voy a ver en que world estoy";
        for(int i=0;i<ctx.widgets.widget(69).component(8).componentCount();i=i+6){
            if((ctx.widgets.widget(69).component(8).component(i).textColor()==901389)) {
                WolrdStay = Integer.parseInt(ctx.widgets.widget(69).component(8).component(i + 2).text());
                System.out.println("El World en el que estoy es: " + WolrdStay);
                break;
            }
        }
    }
    boolean fail,condicion=false;int time,Hop_randomTime;
    @Override
    public void poll() {
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
        if(ctx.skills.level(Constants.SKILLS_AGILITY)==40){
            ctx.controller.stop();
            detener();
        }
        count=estoy();
        System.out.println("Count: "+count);
        objeto=ctx.objects.select().id(id[count]).nearest().poll();
        time=Random.nextInt(800,1200);
        if(!ctx.movement.running()){
            ctx.game.tab(Game.Tab.INVENTORY);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab().equals(Game.Tab.INVENTORY);
                }
            }, time, 20);
            if(ctx.inventory.select().id(12625).poll().interact("Drink")) ;

            else if(ctx.inventory.select().id(12627).poll().interact("Drink"));
            else if(ctx.inventory.select().id(12629).poll().interact("Drink"));
            else if(ctx.inventory.select().id(12631).poll().interact("Drink"));

            ctx.movement.running(true);
        }
        if(level<ctx.skills.level(Constants.SKILLS_AGILITY))experiencia.b();

        if(count==0){
            objeto.bounds(12, 40, -180, 0, 12, 96);
        }
        if(count==4){
            objeto.bounds(-464, -256, -64, 0, -32, 32);
        }
        if(count==5){
            objeto.bounds(-32, 32, -64, 0, -300, -40);
        }
        if(count==6){
            objeto.bounds(-232, 32, -64, 0, -32, 32);
        }
        if(count==7){
            objeto.bounds(-300, 32, -64, 0, -32, 32);
        }
        if(area[count].contains(ctx.groundItems.select().id(11849).poll())){
            mark();
        }
        if(ctx.players.local().tile().floor()==0&&!area[0].contains(ctx.players.local().tile())){ //Si estoy en el piso entoces voy a buscar escalar la pared
            System.out.println("Ire a punto de inicio");
            inicio();
        }else if(objeto.inViewport()){
            System.out.println("El Objeto esta a la vista");
            final int exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
            if(objeto.interact(accion[count])){
                System.out.println("Interactue con el objeto");
                time=Random.nextInt(400,600);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {


                        if(count!=0&&ctx.players.local().tile().floor()==0){
                            fail=true;
                            return true;
                        }
                        fail=false;
                        return llegada[count].contains(ctx.players.local().tile())&&(ctx.skills.experience(Constants.SKILLS_AGILITY)!=exp);
                    }
                }, time, 40);


            }else{
                System.out.println("Ire a mitad de tile");
                final Tile mita=areas.mitad(ctx.players.local().tile(),objeto.tile(),ctx.players.local().tile().floor());

                mita.matrix(ctx).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return areas.area3(mita).contains(ctx.players.local().tile());
                    }
                }, time, 55);

            }
        }else{
            if(!objeto.inViewport()) {
                System.out.println("Ire al punto de salto count: " + count);
                ctx.movement.step(areas.area2(objeto.tile()).getRandomTile());
                lugar = ctx.movement.destination();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(ctx.movement.destination().x()!=-1){
                            lugar = ctx.movement.destination();
                        }
                        objeto = ctx.objects.select().id(id[count]).poll();
                        return areas.area2(lugar).contains(ctx.players.local().tile()) || objeto.inViewport()||areas.area3(objeto.tile()).contains(ctx.players.local().tile());
                    }
                }, time, 50);
            }
                if(fail!=true) {
                    System.out.println("Movere la camara");
                    ctx.camera.pitch(Random.nextInt(85,99));
                    ctx.camera.turnTo(objeto.tile());
                }

        }
    }

    Tile lugar;
    int count;
    public void inicio() {
        int time=Random.nextInt(7,12);

        ctx.movement.step(ini.getRandomTile());
        lugar = ctx.movement.destination();
        ctx.camera.turnTo(objeto.tile());
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return areas.area3(lugar).contains(ctx.players.local().tile());
            }
        }, time,10);
    }

    public int estoy(){
        for(int i=0;i<area.length;i++){
            if(area[i].contains(ctx.players.local().tile())){
                return i;
            }
        }
        return 0;
    }

    public void mark(){
        GroundItem mark=ctx.groundItems.select(10).id(11849).poll();
        final int markCount=ctx.inventory.select().id(11849).count(true);
        if(!mark.interact("Take")){
            Tile medio=new Tile((ctx.players.local().tile().x()+mark.tile().x())/2,(ctx.players.local().tile().y()+mark.tile().y())/2);
            medio.matrix(ctx).click();
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Espero mark");
                return ctx.inventory.select().id(11849).count(true)>markCount;
            }
        }, 1000, 3);
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

