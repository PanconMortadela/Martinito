package scripts.Seer;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by putito on 01/01/2013.
 */
/*@Script.Manifest(
        name="PMSeerCourse",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
*/
public class SeerCourse extends PollingScript<ClientContext> implements PaintListener{

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
    Area ini= new Area(new Tile(2728,3485),new Tile(2728,3489,0),new Tile(2731,3489,0));
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
        g.drawString("#Lap:" + vueltas, 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 106);


    }
    int level;
    Experiencia experiencia=new Experiencia(ctx);
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        experiencia.iniciar(70);
        level=ctx.skills.level(Constants.SKILLS_AGILITY);
    }
    boolean fail,Antiban;
    @Override
    public void poll() {count=estoy();
        System.out.println("Count: "+count);
        objeto=ctx.objects.select().id(id[count]).nearest().poll();
        time=Random.nextInt(100,500);

        if(count==0){
            objeto.bounds(28, 116, -192, -56, 56, 88);
        }
        if(count==1){
            objeto.bounds(-52, 32, -64, 0, -128, 120);
        }
        if(count==2){
            objeto.bounds(20, 132, -28, 8, -20, 52);
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


        time= Random.nextInt(200,300);
        if(area[count].contains(ctx.groundItems.select().id(11849).poll())){
            mark();
        }
        if(ctx.players.local().tile().floor()==0&&!area[0].contains(ctx.players.local().tile())){ //Si estoy en el piso entoces voy a buscar escalar la pared
            System.out.println("Ire a punto de inicio");
            inicio();
        }else if(objeto.inViewport()){
            System.out.println("El Objeto esta a la vista");
            if(count==0){
                System.out.println("Movere la camara");
                ctx.camera.pitch(Random.nextInt(85,99));
                ctx.camera.turnTo(objeto.tile());
            }
            if(objeto.click()){
                System.out.println("Interactue con el objeto");
                Antiban=Random.nextBoolean();
                int Time2=Random.nextInt(1,2);
                if(Antiban==true&&(count!=0)) {

                    int click = Random.nextInt(0,2);
                    System.out.println("Antiban: "+click);
                    for (int i = 0; i < click; i++) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return false;
                            }
                        }, time, Time2);
                        objeto.click();
                        Time2=Random.nextInt(2,5);
                    }
                }
                time= Random.nextInt(200,400);
                Time2=Random.nextInt(2,5);
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
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(count!=0&&ctx.players.local().tile().floor()==0){
                            fail=true;
                            return true;
                        }
                        fail=false;
                        return llegada[count].contains(ctx.players.local().tile());
                    }
                }, time, 100);


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
            if(fail!=true) {
                System.out.println("Movere la camara");
                ctx.camera.pitch(Random.nextInt(85,99));
                ctx.camera.turnTo(objeto.tile());
            }
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
        }
    }

    Tile lugar;
    int count;int time;
    Magic.Spell spell=Magic.Spell.CAMELOT_TELEPORT;
    Area camelot= new Area(new Tile(2702,3465,0),new Tile(2708,3460,0));
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
            ctx.movement.step(ini.getRandomTile());
            lugar = ctx.movement.destination();

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    return areas.area3(lugar).contains(ctx.players.local().tile())||ctx.movement.distance(lugar,ctx.players.local())<6;
                }
            }, time, 25);
        }
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
}
