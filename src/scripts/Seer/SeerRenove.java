package scripts.Seer;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;

@Script.Manifest(
        name="PMSeerCourseRenove",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class SeerRenove extends PollingScript<ClientContext> implements PaintListener {

    int[] id={
            14927,
            14928,
            14932,
            14929,
            14930,
            14931

    };

    String[] accion={
            "Climb-up",
            "Jump",
            "Cross",
            "Jump",
            "Jump",
            "Jump"



    };
    Tile inicio= new Tile(2729,3489,0);
    Area ini= new Area(new Tile(2728,3490,0),new Tile(2730,3488,0));
    Area[] area={
            ini,
            new Area(new Tile(2730,3490,3),new Tile(2720,3498,3)),
            new Area(new Tile(2714,3495,2),new Tile(2706,3488,2)),
            new Area(new Tile(2709,3482,2),new Tile(2716,3476,2)),
            new Area(new Tile(2715,3473,3),new Tile(2699,3469,3)),
            new Area(new Tile(2703,3466,2),new Tile(2697,3459,2))
    };

    Area[] llegada={
            area[1],
            area[2],
            area[3],
            area[4],
            area[5],
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
        g.drawString("Lv Magic: " + ctx.skills.level(Constants.SKILLS_MAGIC) + "    Law: " + ctx.inventory.select().id(563).count(true), 12, 128);
        //objeto.draw(g); //Dibuja el bount del objeto.




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
    @Override
    public void poll() {

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
            objeto.bounds(24, 96, -24, 0, 76, 112);
        }
        if(count==1){
            objeto.bounds(-52, 32, -64, 0, -128, 120);
        }
        if(count==2){
            objeto.bounds(36, 76, -20, 0, 4, 28);
        }
        if(count==3){
            objeto.bounds(-320, 120, -64, 0, -32, 32);
        }
        if(count==4){
            objeto.bounds(-80, 232, -64, 0, -32, 32);
        }
        if(count==5){
            objeto.bounds(-32, 32, -64, 0, -32, 312);
        }


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
                time=Random.nextInt(800,1200);
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

    Tile lugar;
    int count;
    Magic.Spell spell=Magic.Spell.CAMELOT_TELEPORT;
    boolean k;
    Area camelot= new Area(new Tile(2702,3465,0),new Tile(2708,3460,0));
    Area cai=new Area(new Tile(2708,3483,0),new Tile(2712,3486,0));
    public void inicio() {
        if(ctx.inventory.select().id(563).count(true)>0&&(camelot.contains(ctx.players.local()))){
            if(!ctx.game.tab().equals(Game.Tab.MAGIC)){
                ctx.game.tab(Game.Tab.MAGIC);
                time = Random.nextInt(200, 400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        return ctx.game.tab().equals(Game.Tab.MAGIC);
                    }
                }, time, 25);
            }else{
                time = Random.nextInt(200, 400);
                ctx.magic.cast(spell);
                k=true;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperare a castear Teleport");
                        return ctx.players.local().animation()!=-1;
                    }
                }, time, 25);
                time=Random.nextInt(700, 1000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperare a castear Teleport");
                        return ctx.players.local().animation()==-1;
                    }
                }, time, 25);
            }


        }else {
            time = Random.nextInt(200, 400);
            if(k==true){
                ctx.movement.step(areas.area1(new Tile (2739,3482,0)).getRandomTile());

            }else if(cai.contains(ctx.players.local())) {
                ctx.movement.step(areas.area1(new Tile (2725,3485,0)).getRandomTile());

            }else{
                System.out.println("Joder");
                    ctx.movement.step(inicio);
            }

            lugar = ctx.movement.destination();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return areas.area3(lugar).contains(ctx.players.local().tile())||area[0].contains(ctx.players.local());
                }
            }, time, 50);
            k=false;

        }
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
        if(ctx.players.local().tile().floor()==0||cai.contains(ctx.players.local()))
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
