package scripts.Draynor;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;


@Script.Manifest(
        name="PMDraynorCourse",
        description = "Train the agility from level 10 to 30, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class DraynorCurse extends PollingScript<ClientContext> implements PaintListener{

    int[] id={
            11404,
            11405,
            11406,
            11430,
            11630,
            11631,
            11632
    };

    String[] accion={
            "Climb",
            "Cross",
            "Cross",
            "Balance",
            "Jump-up",
            "Jump",
            "Climb-down"

    };

    Area ini= new Area(new Tile(3103,3274,0),new Tile(3104,3279,0));
    Area[] area={
            ini,
            new Area(new Tile(3103,3282,3),new Tile(3096,3276,3)),
            new Area(new Tile(3086,3278,3),new Tile(3093,3273,3)),
            new Area(new Tile(3096,3268,3),new Tile(3087,3264,3)),
            new Area(new Tile(3089,3262,3),new Tile(3087,3256,3)),
            new Area(new Tile(3086,3254,3),new Tile(3096,3256,3)),
            new Area(new Tile(3096,3255,3),new Tile(3102,3262,3))

    };

    Area[] llegada={
            area[1],
            area[2],
            area[3],
            area[4],
            area[5],
            area[6],
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
        g.drawString("LvAgility:" + ctx.skills.realLevel(Constants.SKILLS_AGILITY) +"    Estoy en el mundo: "+WolrdStay , 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 106);
        //g.setColor(color1);
        //g.fillRect(area[count].getPolygon().getBounds().x,area[count].getPolygon().getBounds().y,area[count].getPolygon().getBounds().width,area[count].getPolygon().getBounds().height);
        //g.setColor(Color.BLACK);
        //g.drawRect(area[count].getPolygon().getBounds().x,area[count].getPolygon().getBounds().y,area[count].getPolygon().getBounds().width,area[count].getPolygon().getBounds().height);
    }

    int level;
    Experiencia experiencia=new Experiencia(ctx);
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        experiencia.iniciar(30);
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
        if(ctx.skills.realLevel(Constants.SKILLS_AGILITY)==30){
            ctx.controller.stop();
            detener();
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
            objeto.bounds(8, 32, -204, -56, 8, 96);
        }
        if(count==1){
            objeto.bounds(20, 56, -56, -36, 8, 76);
        }
        if(count==2){
            objeto.bounds(28, 80, -36, -8, -4, 32);
        }
        if(count==3){
            objeto.bounds(-68, -48, -32, 76, -32, 28);
        }
        if(count==4){
            objeto.bounds(-84, 32, -64, 4, -32, 4);
        }
        //No necesita
        if(count==6){
            objeto.bounds(-56, 16, -64, 0, -20, 56);
        }


        System.out.println("Revisare Mark");
        if(area[count].contains(ctx.groundItems.select().id(11849).poll())){
            System.out.println("hay mark!");
            mark();
        }
        if(ctx.players.local().tile().floor()==0&&!area[0].contains(ctx.players.local().tile())){ //Si estoy en el piso entoces voy a buscar escalar la pared
            System.out.println("Ire a punto de inicio");
            inicio();
        }else if(objeto.inViewport()){
            System.out.println("El Objeto esta a la vista");

            if(objeto.interact(accion[count])){
                System.out.println("Interactue con el objeto");
                final int exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
                time=Random.nextInt(500,800);
                if(ctx.game.crosshair()==Game.Crosshair.ACTION) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {


                            if (count != 0 && ctx.players.local().tile().floor() == 0) {
                                fail = true;
                                return true;
                            }
                            fail = false;
                            return llegada[count].contains(ctx.players.local().tile()) && (ctx.skills.experience(Constants.SKILLS_AGILITY) != exp);
                        }
                    }, time, 40);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {

                            return false;
                        }
                    }, 500, 1);
                }

            }else{
                objeto.click(Game.Crosshair.DEFAULT);
                lugar = ctx.movement.destination();
                time=time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return areas.area2(lugar).contains(ctx.players.local().tile()) ;
                    }
                }, time, 50);

            }
        }else{
                ctx.movement.step(objeto.tile());
                lugar = ctx.movement.destination();
                time=Random.nextInt(200,400);
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
    }

    Tile lugar;
    int count;
    public void inicio() {
        int time;
        ctx.movement.step(ini.getRandomTile());
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return ctx.movement.destination().tile().x()!=-1;
            }
        }, 200, 10);
        lugar = ctx.movement.destination();
        time=Random.nextInt(500,800);
        ctx.camera.angle(Random.nextInt(60,83));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.yaw()>=22&&ctx.camera.yaw()<86;
            }
        }, time, 10);
        time=Random.nextInt(10,16);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return areas.area2(lugar).contains(ctx.players.local().tile())||ini.contains(ctx.players.local());
            }
        }, 500, time);
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
        }, 1000, 3);
        if(ctx.inventory.select().id(11849).count(true)>markCount){
            markcount++;
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