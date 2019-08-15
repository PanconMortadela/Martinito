package scripts.MineMother;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.powerbot.script.*;
import org.powerbot.script.rt4.World;
import scripts.Utilitys.*;

/**
 * Created by Martinito on 17/05/2018.
 */
//@Script.Manifest(name="0MineMother", description = "Money making",properties = "autor: Pos yo!")
public class MotherMine extends PollingScript<ClientContext> implements PaintListener {
    int PaydirtID=12011; int[] RockID={26664,26663,26662};int[] RockNMineID={26668,26667,26666};
    int RockfallID=26680; int[] uncut={1617,1619,1621,1623,1625,1627,1629};
    int nugID=12012,id;
    Tile Hopper=new Tile(3748,5672,0); //Hopper donde entrego las pepas
    Tile Bank=new Tile(3761,5666,0);   //bank en Mother
    Tile Sack=new Tile(3748,5659,0);   // Sack para recoger las pepas
    GameObject objecto;
    Tile[] Camino_Hopper={
            new Tile(3726,5652,0),//0 Tile para romper las rocas
            new Tile(3744,5653,0),//1
            new Tile(3750,5664,0),//2
            new Tile(3749,5672,0)};//3 Posicion del hopper
    Tile[] Camino_mine={
            new Tile(3750,5659,0),//0 estando en banco
            new Tile(3735,5654,0),//1
            new Tile(3728,5652,0),//2
    };
    Tile CenterMine=new Tile(3722,5653,0);
    Tile Zona= new Tile(3719,5635,0);
    Area Area_Mine =new Area(new Tile(3712,5630,0),new Tile(3725,5663,0)) ;
    Tile Random;
    int vuelta=0;
    int widget=382,component1=4,component2=2,max=28,espera=0,minasfail=0,missclick=0;
    boolean usarsaco=false;
    Areas areas=new Areas();
    Area cuadro;
    Area Ban_area=new Area(new Tile(3747,5656,0),new Tile(3762,5676,0));                                         //Construir area de bank
    int countinv;
    GameObject mina;
    int h, m, s;
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    Polygon p;
    int nug=0;
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
        g.drawString("Nugged:" +nug +" Vuelta: " +vuelta, 11, 87);
        g.drawString("Piedra: " + RockID[id], 12, 106);
        //g.drawString("State:" + state, 144, 71);
        //cuadro.getCentralTile().matrix(ctx).draw(g);
        //g.drawPolygon(p);
    }

    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        ctx.camera.pitch(org.powerbot.script.Random.nextInt(95,99));
    }
    public void close(){
        ctx.controller.stop();
        detener();
    }
    int time,Hop_randomTime;boolean condicion=false;
    int H_Close=3,M_Close= org.powerbot.script.Random.nextInt(50,55);
    @Override
    public void poll() {

        int t=0;//variable para llevar count de saco
        try {
            String a = ctx.widgets.widget(widget).component(component1).component(component2).text();
            t = Integer.parseInt(a);
        }catch(Exception e){

        }
        if(t>=max){
            usarsaco=true;
        }
        if(t==0){
            usarsaco=false;
        }
        if(m==29||m==59){
            condicion=true;
            if(m==29)
                Hop_randomTime= org.powerbot.script.Random.nextInt(30,35);
            else
                Hop_randomTime=org.powerbot.script.Random.nextInt(0,5);
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
        while(ctx.widgets.widget(595).component(37).visible()){
            ctx.widgets.widget(595).component(37).click();
            time= org.powerbot.script.Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(595).component(37).visible();
                }
            }, time, 20);
        }
        if(!ctx.movement.running()&&ctx.movement.energyLevel()>20){
            ctx.movement.running(true);
        }
        if(Area_Mine.contains(ctx.players.local().tile())&&ctx.inventory.select().id(PaydirtID).count()!=27) {
            Comprobaruncut();
            minar();            //Ejecuta accion de minar
            if(H_Close==h&&m>=M_Close){
                close();
            }
        }else if(ctx.inventory.select().id(PaydirtID).count()==27&&(Area_Mine.contains(ctx.players.local().tile())||areas.area3(Camino_Hopper[0]).contains(ctx.players.local().tile()))) {
            Caminar_Hopper();   //Camina a llevar los pay_dirt
        }else if(ctx.inventory.select().id(PaydirtID).count()==27) {
            Hopper();           //hace click en el Hoppe
            if(H_Close==h&&m>=M_Close){
                close();
            }
        }else if(ctx.inventory.select().count()==1&&usarsaco==false) {
            Caminar_Mina();     //Camina a la mina
        }else if(ctx.inventory.select().count()==1&&usarsaco==true) {
            Sack();                //Busca en el sack
            if(H_Close==h&&m>=M_Close){
                close();
            }
        }else if(ctx.inventory.select().count()>20&&areas.area2(Sack).contains(ctx.players.local().tile())) {
            Caminar_Bank();     //Va del sack al banco
        }else if(ctx.inventory.select().count()>20&&ctx.bank.open()) {
            Bank();             //Deja todos los items y se va a mina
            if(H_Close==h&&m>=M_Close){
                close();
            }
        }
    }
    public int iteracion_de_tiempo(){
        int time= org.powerbot.script.Random.nextInt(1,4);
        return time;
    }
    public int segundos(){
        int segundos=org.powerbot.script.Random.nextInt(800,1200);
        return segundos;
    }
    World mundito;
    public void hop(){
        ctx.worlds.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return false;
            }
        }, segundos(), 25);
        boolean hay_mundo=false;
        stay();
        Iterator<World> mundo= ctx.worlds.select().types(World.Type.MEMBERS).joinable().iterator();
        if(WolrdStay==525){             // ultimo mundo
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
    int WolrdStay;
    public void stay(){
        String x= ctx.widgets.widget(69).component(2).text();
        String[] parts = x.split(" ");
        try {
            WolrdStay = Integer.parseInt(parts[3]);
        }catch (Exception e){

        }
        System.out.println("El World en el que estoy es: " + WolrdStay);
    }

    public void minar(){

        mina=ctx.objects.select(7).name("Ore vein").nearest().poll();
        mina.bounds(32, 104, -224, -120, 24, 80);
        Buscar();

        if(Area_Mine.contains(mina.tile())){
            if(mina.interact("Mine")){
                cuadro=areas.area3(mina.tile());

                //esperar a llegar al sitio de la piedra
                time= org.powerbot.script.Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando llegar al sitio de minado");
                        if(detener())return true;
                        return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue a la piedra
                    }
                }, time, 20);
                mina=ctx.objects.select(7).name("Ore vein").nearest().poll();
                Buscar();
                cuadro=areas.area3(mina.tile());
                camara(mina.tile());
                //esperar que empiece a minar
                time= org.powerbot.script.Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que empiece a minar");
                        if(detener())return true;
                        if(missclick>=15){

                            if(!mina.tile().matrix(ctx).click()){
                                mina=ctx.objects.select(7).name("Ore vein").nearest().poll();
                                mina.interact("mine");
                            }
                            else missclick=0;
                            espera++;

                        }
                        if(espera==2){
                            return true;
                        }

                        missclick++;
                        return ctx.players.local().animation()!=-1;   //esperare a que llegue a la piedra
                    }
                }, time, 20);
                mina=ctx.objects.select(1).name("Ore vein").nearest().poll();
                Buscar();
                cuadro=areas.area1(mina.tile());
                missclick=0;
                espera=0;
                countinv=ctx.inventory.select().count();
                final int random= org.powerbot.script.Random.nextInt(10,15);
                //esperar que se detenga.
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que Termine de minar");
                        if(detener())return true;
                        if(ctx.inventory.select().count()==28){
                            return true;
                        }
                        if(countinv==ctx.inventory.select().count())espera++;
                        else missclick=0;
                        if(missclick==50){
                            return true;
                        }
                        if(ctx.players.local().animation()==-1){
                            espera++;
                            if(espera==random){
                                return true;
                            }
                        }else{
                            espera=0;
                        }
                        return cuadro.contains(ctx.objects.select(1).id(RockNMineID[id]).poll().tile());   //esperare a que llegue a la piedra
                    }
                }, time, 30);
                missclick=0;
                espera=0;

                if(ctx.players.local().tile().x()==3725&&ctx.players.local().tile().y()==5638){
                    cuadro=areas.area2(Zona);
                    ctx.movement.step(Zona);
                    time= org.powerbot.script.Random.nextInt(800,1200);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Esperar llegar al sitio de minado por step");
                            if(detener())return true;
                            return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue al RandomTile
                        }
                    }, time, 50);
                }

            }else{
                cuadro=areas.area2(Zona);
                ctx.movement.step(Zona);
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperar llegar al sitio de minado por step");
                        if(detener())return true;
                        return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue al RandomTile
                    }
                }, time, 50);
                camara(mina.tile());
            }

        }else{
            ctx.objects.select(12).name("Ore vein").nearest().reverse().iterator().next().interact("Mine");
            time= org.powerbot.script.Random.nextInt(800,1200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperar moverme a RandomTile");
                    if(detener())return true;
                    return ctx.players.local().animation()!=-1;   //esperare a que llegue al RandomTile
                }
            }, time, 30);
            if(ctx.players.local().tile().x()==3725&&ctx.players.local().tile().y()==5638){
                cuadro=areas.area2(Zona);
                ctx.movement.step(Zona);
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperar llegar al sitio de minado por step");
                        if(detener())return true;
                        return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue al RandomTile
                    }
                }, time, 50);
            }
        }
    }
    public void Buscar(){
        for(int i=0;i<RockID.length;i++){
            if(RockID[i]==mina.id()){
                id=i;
                System.out.println("La posicion es: "+id);
            }
        }
    }

    public boolean camara(Tile Mine){
        if(Mine.y()>ctx.players.local().tile().y()){
            System.out.println("Movere n");
            ctx.camera.angle('n');
            return false;
        }
        if(Mine.x()>ctx.players.local().tile().x()){
            System.out.println("Movere e");
            ctx.camera.angle('e');
            return false;
        }
        if(Mine.y()<ctx.players.local().tile().y()){
            System.out.println("Movere s");
            ctx.camera.angle('s');
            return false;
        }
        if(Mine.x()<ctx.players.local().tile().x()){
            System.out.println("Movere w");
            ctx.camera.angle('w');
            return false;
        }else{
            return true;
        }
    }

    public void Caminar_Hopper(){

        ctx.movement.step(CenterMine);
        cuadro=areas.area3(CenterMine);
        time= org.powerbot.script.Random.nextInt(800,1200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperando ir al centro");
                if(detener())return true;
                return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue a la piedra
            }
        }, time, 30);

        ctx.movement.step(Camino_Hopper[0]);
        cuadro=areas.area2(Camino_Hopper[0]);
        time= org.powerbot.script.Random.nextInt(1500,2000);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperando al punto de mina");
                if(detener())return true;
                return (cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0)||areas.area2(Camino_Hopper[0]).contains(ctx.players.local().tile());   //esperare a que llegue a la piedra
            }
        }, time, 30);
        if(ctx.objects.select(5).id(26680).size()>1) {
            mina = ctx.objects.select(2).id(26680).nearest().poll();
            mina.bounds(-24, 28, -68, -8, -24, 24);
            cuadro = areas.area1(mina.tile());
            if (mina.valid()) {
                if (mina.interact("Mine")) {
                    time= org.powerbot.script.Random.nextInt(800,1200);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (detener()) return true;
                            if (!cuadro.contains(mina.tile())) { // Si en el area no hay lo que quiero minar me salgo
                                System.out.println("Fue quitado la piedra fea");
                                return true;
                            }
                            if (espera == 20) {
                                mina.tile().matrix(ctx).click();
                            }
                            espera++;
                            System.out.println("Espero que empiece a minar");
                            return ctx.players.local().animation() != -1||ctx.objects.select(5).id(26680).size()<2;   //esperare a que cambie a animacion de minar
                             //esperare a que cambie a animacion de minar
                        }
                    }, time, 7);
                    espera = 0;
                    time= org.powerbot.script.Random.nextInt(800,1200);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (detener()) return true;
                            if (!cuadro.contains(mina.tile())) { // Si en el area no hay lo que quiero minar me salgo
                                System.out.println("Fue quitado la piedra fea");
                                return true;
                            }
                            if (espera == 20) {
                                mina.tile().matrix(ctx).click();
                            }
                            espera++;
                            System.out.println("Espero que empiece a minar");
                            return ctx.players.local().animation() == -1||ctx.objects.select(5).id(26680).size()<2;   //esperare a que cambie a animacion de minar
                        }
                    }, time, 20);
                }
            }
        }
        for (int i=1;i<Camino_Hopper.length;i++){
            cuadro=areas.area2(Camino_Hopper[i]);
            ctx.movement.step(Camino_Hopper[i]);
            time= org.powerbot.script.Random.nextInt(800,1200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperando ir al Hopper");
                    if(detener())return true;
                    return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue a la piedra
                }
            }, time, 20);
        }
    }

    public void Caminar_Mina(){                     // requiere mejorar
        for (int i=0;i<Camino_mine.length;i++){
            cuadro=areas.area3(Camino_mine[i]);
            ctx.movement.step(Camino_mine[i]);
            time= org.powerbot.script.Random.nextInt(800,1200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperando ir al centro");
                    if(detener())return true;
                    return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue a la piedra
                }
            }, time, 10);
        }
        if(ctx.objects.select(5).id(26680).size()>1) {
            mina = ctx.objects.select(5).id(26680).nearest().poll();
            mina.bounds(-24, 28, -68, -8, -24, 24);

        cuadro=areas.area3(mina.tile());
        if(mina.valid()) {
            if (mina.interact("Mine")) {
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        if (!cuadro.contains(mina.tile())) { // Si en el area no hay lo que quiero minar me salgo
                            System.out.println("Fue quitado la piedra fea");
                            return true;
                        }
                        if (espera == 20) {
                            mina.tile().matrix(ctx).click();
                        }
                        espera++;
                        System.out.println("Espero que empiece a minar");
                        return ctx.players.local().animation() != -1||ctx.objects.select(5).id(26680).size()<2;   //esperare a que cambie a animacion de minar
                    }
                }, time, 10);
                espera = 0;
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (detener()) return true;
                        if (!cuadro.contains(mina.tile())) { // Si en el area no hay lo que quiero minar me salgo
                            System.out.println("Fue quitado la piedra fea");
                            return true;
                        }
                        if (espera == 20) {
                            mina.tile().matrix(ctx).click();
                        }
                        espera++;
                        System.out.println("Espero que empiece a minar");
                        return ctx.players.local().animation() == -1||ctx.objects.select(5).id(26680).size()<2;   //esperare a que cambie a animacion de minar
                    }
                }, time, 10);
            }
        }
        }
        cuadro=areas.area2(new Tile(3720,5637,0));
        ctx.movement.step(new Tile(3720,5637,0));
        time= org.powerbot.script.Random.nextInt(800,1200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperando ir al centro");
                if(detener())return true;
                return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;   //esperare a que llegue a la piedra
            }
        }, time, 10);

    }

    public void Comprobaruncut(){
        System.out.println("Entre en ComprobarUncut");
        for(int i=0;i<uncut.length;i++){
            ctx.inventory.select().id(uncut[i]).poll().interact("Drop");

        }
    }
    int[] picos={1265,1267,1269,12297,1273,1271,1275};// 7 items
    public void Bank(){
        System.out.println("Entre en Bank");
        if(ctx.bank.opened()){      //Si el bank esta abierto hare lo que esta debajo
            if(ctx.inventory.select().count()>20) {    // si inventario == 28
                nug=ctx.bank.id(nugID).count();
                ctx.bank.depositAllExcept(picos[0],picos[1],picos[2],picos[3],picos[4],picos[5],picos[6]);
                time= org.powerbot.script.Random.nextInt(800,1200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        return ctx.inventory.select().count()<5;
                    }
                }, time, 12);
            }
        }else{
            ctx.bank.open();
            time= org.powerbot.script.Random.nextInt(800,1200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperando que el banco abra");
                    if(detener())return true;
                    return ctx.bank.opened();           // Espero a que el banco este abierto
                }
            }, time, 7);
        }
    }
    public void Hopper(){
        System.out.println("Entre en hopper");
        objecto=ctx.objects.select().id(26674).poll();
        objecto.bounds(-32, 32, -64, 0, -32, 32);
        objecto.interact("Deposit");
        time= org.powerbot.script.Random.nextInt(800,1200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperando a que se vacie el inventario");
                if(missclick==10){
                    missclick=0;
                    Hopper.matrix(ctx).click();
                }
                missclick++;
                if(detener())return true;
                return ctx.inventory.select().count()<28;           // Espero a que el banco este abierto
            }
        }, time, 7);
        vuelta++;

    }
    public void Sack(){
        System.out.println("Entre en Sack");
        if(!areas.area3(Sack).contains(ctx.players.local().tile())) {
            ctx.movement.step(Sack);
            cuadro = areas.area2(Sack);
            time= org.powerbot.script.Random.nextInt(800,1200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Esperando a llegar al Sack");
                    if (detener()) return true;
                    return cuadro.contains(ctx.players.local().tile()) && ctx.players.local().speed() == 0; // Espero a llegar al sack
                }
            }, time, 7);
        }
        objecto=ctx.objects.select().name("Sack").poll();
        objecto.bounds(20, 96, -32, 8, 4, 88);
        objecto.interact("Search");
        time= org.powerbot.script.Random.nextInt(800,1200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if(detener())return true;
                System.out.println("Esperando a que se llene");
                return ctx.inventory.select().count()>0;           // Espero a que se vacie el inventario
            }
        }, time, 7);
    }

    public void Caminar_Bank(){
        System.out.println("Entre en caminar a bank");
        ctx.movement.step(Bank);
        cuadro=areas.area1(Bank);
        time= org.powerbot.script.Random.nextInt(800,1200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if(detener())return true;
                System.out.println("Esperando a llegar al Sack");
                return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0; // Espero a llegar al sack
            }
        }, time, 7);
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
