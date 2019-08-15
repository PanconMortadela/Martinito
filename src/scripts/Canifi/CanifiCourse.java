package scripts.Canifi;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Created by putito on 01/01/2013.
 */
@Script.Manifest(
        name="PMCanifiCourse",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class CanifiCourse extends PollingScript<ClientContext> implements PaintListener{

    int[] id={
            14843,
            14844,
            14845,
            14848,
            14846,
            14894,
            14847,
            14897
    };

    String[] accion={
            "Climb",
            "Jump",
            "Jump",
            "Jump",
            "Jump",
            "Vault",
            "Jump",
            "Jump"
    };

    Area ini= new Area(new Tile(3511,3485),new Tile(3504,3488,0));
    Area[] area={
            ini,
            new Area(new Tile(3505,3488,2),new Tile(3504,3495,2),new Tile(3504,3499,2),new Tile(3509,3499,2),new Tile(3510,3494,2),new Tile(3508,3491,2)),
            new Area(new Tile(3504,3503,2),new Tile(3496,3507,2)),
            new Area(new Tile(3493,3505,2),new Tile(3489,3505,2),new Tile(3486,3502,2),new Tile(3486,3498,2),new Tile(3492,3498,2)),
            new Area(new Tile(3480,3500,3),new Tile(3474,3491,3)),
            new Area(new Tile(3476,3486,2),new Tile(3485,3488,2),new Tile(3485,3484,2),new Tile(3482,3480,2),new Tile(3476,3480,2)),
            new Area(new Tile(3486,3479,3),new Tile(3504,3468,3)),
            new Area(new Tile(3508,3474,2),new Tile(3516,3483,2))
    };

    Area[] llegada={
            area[1],
            area[2],
            area[3],
            area[4],
            area[5],
            area[6],
            area[7],
            area[0]

    };
    Areas areas=new Areas();
    GameObject objeto;
    boolean Antiban;



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
        g.drawString("AgilitiLv:" + ctx.skills.level(Constants.SKILLS_AGILITY) +"   Mundo: "+WolrdStay , 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 106);
    }

    int level;
    Experiencia experiencia=new Experiencia(ctx);
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        experiencia.iniciar(60);
        level=ctx.skills.level(Constants.SKILLS_AGILITY);
        stay();
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
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);

    }
    boolean fail,condicion=false;int time,Hop_randomTime;
    @Override
    public void poll() {

        if(ctx.players.local().healthPercent()<10){
            if(ctx.inventory.select().id(379).count()>0){
                ctx.inventory.select().id(379).poll().click();
            }
        }

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
        if(ctx.game.tab()!=Game.Tab.INVENTORY){
            ctx.game.tab(Game.Tab.INVENTORY);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab().equals(Game.Tab.INVENTORY);
                }
            }, 200, 20);
        }
        count=estoy();
        System.out.println("Count: "+count);
        objeto=ctx.objects.select().id(id[count]).nearest().poll();
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
        time=Random.nextInt(800,1200);

        if(count==0){
            objeto.bounds(148, 216, -292, -156, -32, 16);
        }
        if(count==1){
            objeto.bounds(-108, 96, -64, 0, -32, 32);
        }
        if(count==2){
            objeto.bounds(-32, 32, -64, 0, -56, 100);
        }
        if(count==3){
            objeto.bounds(-32, 32, -64, 0, -80, 76);
        }
        if(count==4){
            objeto.bounds(-32, 32, -64, 0, -80, 72);
        }
        if(count==5){
            objeto.bounds(-84, 32, -64, 0, -32, 32);
        }
        if(count==6){
            objeto.bounds(-32, 32, -64, 0, -128, 92);
        }
        if(count==7){
            objeto.bounds(-32, 48, -64, 0, -32, 32);
        }


        if(area[count].contains(ctx.groundItems.select().id(11849).poll())){
            mark();
        }
        if(ctx.players.local().tile().floor()==0&&!area[0].contains(ctx.players.local().tile())){ //Si estoy en el piso entoces voy a buscar escalar la pared
            System.out.println("Ire a punto de inicio");
            inicio();
        }else if(objeto.inViewport()){
            System.out.println("El Objeto esta a la vista");

            if(objeto.click()){
                System.out.println("Interactue con el objeto");
                Antiban=Random.nextBoolean();
                int Time2=Random.nextInt(2,5);
                if(Antiban==true||count==1) {

                    int click = Random.nextInt(0,2);
                    System.out.println("Antiban: "+click);
                    for (int i = 0; i < click; i++) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return false;
                            }
                        }, 200, Time2);
                        objeto.click();
                        Time2=Random.nextInt(2,5);
                    }
                }
                Time2=Random.nextInt(2,5);
                final int exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
                if(!ctx.game.crosshair().name().equals(Game.Crosshair.ACTION.name())){
                    System.out.println("El cross no es de accion");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return false;
                        }
                    }, 200, Time2);

                    objeto.interact(accion[count]);
                }
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
                }, time, 10);


            }else{
                System.out.println("Ire a mitad de tile");
                final Tile mita=areas.mitad(ctx.players.local().tile(),objeto.tile(),ctx.players.local().tile().floor());

                mita.matrix(ctx).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return areas.area2(mita).contains(ctx.players.local().tile());
                    }
                }, time, 50);

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
        int time=Random.nextInt(200,400);
        ctx.movement.step(ini.getRandomTile());
        lugar = ctx.movement.destination();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return areas.area3(lugar).contains(ctx.players.local().tile());
            }
        }, time, 25);
    }

    public int estoy(){
        for(int i=0;i<area.length;i++){
            if(area[i].contains(ctx.players.local().tile())){
                return i;
            }
        }
        if(ctx.players.local().tile().floor()==0)
        return 0;
        else
            return estoy();
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
        }, Random.nextInt(800,1000), Random.nextInt(8,10));
        if(ctx.inventory.select().id(11849).count(true)>markCount){
            markcount++;
        }
    }

}
