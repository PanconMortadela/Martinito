package scripts.Minevarrock;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Hop;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;


/**
 * Created by Martinito on 15/05/2018.
 */
@Script.Manifest(name="0MineVarrock", description = "Money making",properties = "autor: Pos yo!")
public class MineVarrock extends PollingScript<ClientContext> implements PaintListener {

    int[] picos={1265,1267,1269,12297,1273,1271,1275};// 7 items
    int RocksID=Integer.parseInt(JOptionPane.showInputDialog(null,"Seleccione roca","Rock mine",JOptionPane.QUESTION_MESSAGE,null,new Object[] {"11363","11364"},"11363").toString());
    GameObject Rocks;
    int RockItem;
    public Tile[] camino_mine = {
            ctx.players.local().tile(),//0 Punto de partida (banco)
            new Tile(3177, 3428, 0),//1
            new Tile(3171, 3418, 0),//2
            new Tile(3170, 3401, 0),//3
            new Tile(3177, 3387, 0),//4
            new Tile(3180, 3371, 0)};//55 Punto de llegada (mina)

    public Tile[] camino_bank = {
            new Tile(3180, 3371, 0),//0 punto de partida (mina)
            new Tile(3177, 3387, 0),//1
            new Tile(3172, 3400, 0),//2
            new Tile(3172, 3418, 0),//3
            new Tile(3177, 3428, 0),//4
            new Tile(3185, 3436, 0)};//5 Punto de llegada (banco)


    boolean extra=true;
    int count,lock=0;
    Areas areas=new Areas();
    Area cuadro;

    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 1, 12);
    GeItem barr= new GeItem(2353);
    GeItem ore= new GeItem(RockItem);
    GeItem coal1= new GeItem(453);
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
        g.fillRect(7, 54, 220, 88);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 88);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 68);
        g.drawString("Estoy: " + hop.WolrdStay + "Estaba: " +hop.estaba +"  Precio:"+ ore.price, 11, 88);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Cantidad en el banco: " + numero_mineral_bank, 12, 108);
        g.drawString("Profit:" + numero_mineral_bank*ore.price + "LvMining= " + ctx.skills.realLevel(Constants.SKILLS_MINING), 10, 128);
    }
    public void start(){
        initialTime=System.currentTimeMillis();
        if(RocksID==11363){
            RockItem=434;
        }else if(RocksID==11364){
            RockItem=440;
        }

    }
    Hop hop=new Hop(ctx);
    int mrandom=Random.nextInt(0,10);
    @Override
    public void poll() {
        if(extra&&ctx.objects.select(2).id(RocksID).poll().valid()){
            camino_bank[0]=ctx.players.local().tile();
            camino_mine[5]=ctx.players.local().tile();
        }
        System.out.println("Entre en poll");
        if(ctx.skills.level(Constants.SKILLS_MINING)==55||h==5&&m>mrandom){
            ctx.controller.stop();
            detener();
        }

        //hop.start(m);
        //hop.condicion(World.Type.FREE);

        if(ctx.inventory.select().count()<28&&areas.area3(camino_bank[0].tile()).contains(ctx.players.local().tile())){
            System.out.println("poll if 1 minar");
            minar();
        }else if(ctx.inventory.select().count()==28&&ctx.bank.open()) {
            System.out.println("poll if 2 bank");
            bank();
        }else if(ctx.inventory.select().count()==28) {
            System.out.println("poll if 3 go bank");
            go_bank();
        }else if(ctx.inventory.select().count()<2&&!areas.area3(camino_bank[0].tile()).contains(ctx.players.local().tile())){
            System.out.println("poll if 4 go mine");
            go_mine();
        }

    }
    int time;
    public void minar(){
        time= Random.nextInt(700,1500);
        count=ctx.inventory.select().id(RocksID).count();
        lock=0;


        if (ctx.objects.select(3).id(RocksID).poll().valid()) {/////////////////////////////////////////////
            if(true){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que empieze a minar rocks 1");
                        return false;   //esperare
                    }
                }, Random.nextInt(200,400), 2);
            }
            Rocks=ctx.objects.select(3).id(RocksID).poll();
            if(Rocks.interact("Mine")){
                if(ctx.game.crosshair().equals(Game.Crosshair.ACTION)) {
                    time = Random.nextInt(200, 400);
                    cuadro = areas.area1(Rocks.tile());    //area que voy a minar
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Esperando que empieze a minar rocks 1");
                            return ctx.players.local().animation() != -1||(!Rocks.valid());   //esperare a que cambie a animacion de minar
                        }
                    }, time, 50);
                    //Espera para dejar de minar
                    time = Random.nextInt(200, 400);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Esperando que se detenga o desaparezca");
                            return ctx.players.local().animation() == -1||(!Rocks.valid());
                        }
                    }, time, 50);
                }
            }
        }


    }
    int energyLevel= Random.nextInt(25,35);
    public void correr(){
        if(ctx.movement.energyLevel()>=energyLevel&&!ctx.movement.running()){
            ctx.movement.running(true);
            energyLevel= Random.nextInt(25,35);
        }
    }
    public void go_bank(){
        correr();
        System.out.println("Entre a funcion go_bank");
        if (ctx.inventory.select().count()==28) {
            System.out.println("go_bank if 1");
            if(lock!=6) lock++;
            cuadro = areas.area1(camino_bank[lock]);    // establesco el otro punto
            ctx.movement.step(cuadro.getRandomTile());  //busco un punto aleatoreo a ese otro punto y voy alli
            cuadro = areas.area2(camino_bank[lock]);    // establesco un area mas grande para ese otro punto
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return cuadro.contains(ctx.players.local().tile()); //espero hasta llegar al otro punto
                }
            }, 500, 30);
            if(!cuadro.contains(ctx.players.local().tile())){
                lock--;
            }
        }else {
            System.out.println("go_bank else");
            lock++;
        }

    }


    public void go_mine(){
        correr();
        System.out.println("Entre a funcion go_bank");
        if (ctx.inventory.select().count()<2) {
            System.out.println("go_bank if 1");
            if(lock!=6) lock++;
            cuadro = areas.area1(camino_mine[lock]);    // establesco el otro punto
            ctx.movement.step(cuadro.getRandomTile());  //busco un punto aleatoreo a ese otro punto y voy alli
            cuadro = areas.area2(camino_mine[lock]);    // establesco un area mas grande para ese otro punto
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0; //espero hasta llegar al otro punto
                }
            }, 500, 30);
            if(!cuadro.contains(ctx.players.local().tile())){
                lock--;
            }
        }else {
            System.out.println("go_bank else");
            lock++;
        }
    }
    int numero_mineral_bank=0;
    public void bank(){
        if(ctx.bank.opened()){
            if(ctx.inventory.select().count()==28) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que el banco abra");
                        return ctx.bank.opened();
                    }
                }, 200, 60);
                lock=0;
                ctx.bank.depositAllExcept(picos[0],picos[1],picos[2],picos[3],picos[4],picos[5],picos[6]);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()<5;
                    }
                }, 200, 60);
            }
            Buscar_ore();
            numero_mineral_bank=ctx.bank.select().id(RockItem).count(true);
        }else{
            ctx.npcs.select().id(2897).nearest().poll().interact("Bank");
        }
    }

    int n_compo;
    public void Buscar_ore(){
        for(int i=1;i<ctx.widgets.widget(12).component(13).componentCount();i++){
            if(ctx.widgets.widget(12).component(13).component(i).id()==434){
                n_compo=i;
            }
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

    public void hop(){

    }

}
