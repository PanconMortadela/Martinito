package scripts.Pollavilage;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;

@Script.Manifest(
        name="PMPollinvilage",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class Polanvilage extends PollingScript<ClientContext> implements PaintListener {

    int[] id={
            14935,
            14936,
            14937,
            14938,
            14939,
            14940,
            14941,
            14944,
            14945

    };

    String[] accion={
            "Climb-on",
            "Jump-on",
            "Grab",
            "Leap",
            "Jump-to",
            "Climb",
            "Cross",
            "Jump-on",
            "Jump-to"



    };
    Tile inicio= new Tile(3352,2962,0);
    Area ini= new Area(new Tile(3353,2965,0),new Tile(3348,2960,0));
    Area[] area={
            ini,
            new Area(new Tile(3352,2963,1),new Tile(3345,2969,1)),
            new Area(new Tile(3351,2972,1),new Tile(3356,2977,1)),
            new Area(new Tile(3359,2976,1),new Tile(3363,2980,1)),
            new Area(new Tile(3365,2973,1),new Tile(3371,2977,1)),
            new Area(new Tile(3364,2981,1),new Tile(3370,2987,1)),
            new Area(new Tile(3354,2980,2),new Tile(3366,2986,2)),
            new Area(new Tile(3356,2989,2),new Tile(3371,2996,2)),
            new Area(new Tile(3355,2999,2),new Tile(3363,3005,2)),

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
        g.fillRect(7, 54, 220, 100);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 100);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 68);
        g.drawString("AgilitiLv:" + ctx.skills.level(Constants.SKILLS_AGILITY) +"    Mundo: "+WolrdStay +" - " + estaba , 11, 88);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 108);
        g.drawString("Exp/tiempo: " + mostrar1,10,128);
        g.drawString("Tiempo para subir:" + mostrar2,10,148);





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
    int WolrdStay,estaba=0;
    public int iteracion_de_tiempo(){
        int time= Random.nextInt(5,4);
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
            estaba=WolrdStay;
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);

    }
    boolean fail,condicion=false;int time,Hop_randomTime;
    double exp=0;
    double Acumulada=0;
    double expH;
    double Timeleft;
    int Faltante=ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_AGILITY)+1)-ctx.skills.experience(Constants.SKILLS_AGILITY);
    @Override
    public void poll() {
        exp=exp+ctx.skills.experience(Constants.SKILLS_AGILITY);
        if(ctx.players.local().healthPercent()<30){
            if(ctx.game.tab()!=Game.Tab.INVENTORY){
                ctx.game.tab(Game.Tab.INVENTORY);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.game.tab().equals(Game.Tab.INVENTORY);
                    }
                }, 200, 20);
            }
            if(ctx.inventory.select().id(379).count()>0){
                ctx.inventory.select().id(379).poll().click();
            }
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


        if(count==0){
            objeto.bounds(-28, 20, -60, -8, -20, 24);
        }
        // 1 bien
        if(count==2){
            objeto.bounds(-32, 32, -180, -84, -176, -72);
        }
       // 3 bien

       //4 bien
        if(count==5){
            objeto.bounds(28, 72, -172, -76, 84, 108);
        }
        //bien

        //bien


        if(area[count].contains(ctx.groundItems.select().id(11849).poll())){
            mark();
        }
        if(ctx.players.local().tile().floor()==0&&!area[0].contains(ctx.players.local().tile())){ //Si estoy en el piso entoces voy a buscar escalar la pared
            System.out.println("Ire a punto de inicio");
            inicio();
        }else if(objeto.inViewport()){
            System.out.println("El Objeto esta a la vista");

            if(objeto.interact(accion[count])){
                System.out.println("Interactue con el objeto");

                int Time2=Random.nextInt(1,2);

                Time2=Random.nextInt(1,3);
                time=Random.nextInt(200,300);
                final int exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
                if(!ctx.game.crosshair().name().equals(Game.Crosshair.ACTION.name())){
                    System.out.println("El cross no es de accion");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return false;
                        }
                    }, time, Time2);

                    objeto.interact(accion[count]);
                }
                time=Random.nextInt(300,500);
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
                }, time, 30);
                if(ctx.skills.experience(Constants.SKILLS_AGILITY)-exp!=0){
                    Acumulada=Acumulada+(ctx.skills.experience(Constants.SKILLS_AGILITY)-exp);
                    expH=(Acumulada*3600)/((System.currentTimeMillis()- initialTime)/1000);
                    Faltante=ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_AGILITY)+1)-ctx.skills.experience(Constants.SKILLS_AGILITY);
                    Timeleft=Faltante/expH;


                }
                if(count==0){
                    mostrar1=(float) expH;
                    mostrar2=(float) Timeleft;
                }


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
            ctx.camera.turnTo(objeto.tile());
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
    float mostrar1=0,mostrar2=0;
    Tile lugar;
    int count;
    boolean k;
    //Area camelot= new Area(new Tile(2702,3465,0),new Tile(2708,3460,0));
    Area aleatoriedad=new Area(new Tile(3366,2999,0),new Tile(3362,2996,0));
    public void inicio() {

        System.out.println("Joder");
        if(aleatoriedad.contains(ctx.players.local())){
            k=Random.nextBoolean();
        }
        if(k==true){
            ctx.movement.step(new Tile(3363,2982,0));
            k=false;
        }else{
            ctx.movement.step(inicio);
        }


        lugar = ctx.movement.destination();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return areas.area3(lugar).contains(ctx.players.local().tile())||area[0].contains(ctx.players.local());
            }
        }, time, 50);

    }

    public int estoy(){
        time=Random.nextInt(200,300);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return ctx.players.local().animation()==-1;
            }
        }, time, 50);
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
