package scripts;

import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;

import java.awt.Graphics;

import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="Cuero Dragon", description = "Money making",properties = "autor: Pos yo!")



public class Cuero extends PollingScript<ClientContext> implements PaintListener{ //el pollingscript es para llamar clase main
    final static int Coin=995;
    private static Tile BANK_TILE = new Tile(3270,3167);
    private static  Tile TRADER = new Tile(3275,3191);
    final static int nDhide[]={0,1753,1751,1749,1747};
    final static int energy[]={3008,3010,3012,3014,229};
    final static int Senergy[]={3016,3018,3020,3022,229};
    final static int Stamina[]={12625, 12627, 12629, 12631,229};
    final static int Select[]={0,1,2,3};
    static int[] AUX= new int[Stamina.length];
    final static int elistrade= 3231;
    private int Hide[]= {0,1745,2505,2507,2059};
    private boolean inv=false;
    Tile x1= new Tile(3277,3177);
    Tile x2= new Tile(3280,3191);
    int state;
    final iCUERO CMenu= new iCUERO();
    Npc elis = ctx.npcs.select().id(elistrade).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(0, 0, 0, 0);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 1, 11);
    int count=0;
    int prince=0;
    //Graphics
    @Override
    public void repaint(Graphics g1){
        Graphics2D g= (Graphics2D)g1;
        int x= (int) ctx.input.getLocation().getX();
        int y= (int) ctx.input.getLocation().getY();

        g.drawLine(x,y - 10,x,y+10);
        g.drawLine(x-10,y,x+10,y);

        int h= (int)((System.currentTimeMillis()- initialTime)/3600000);
        int m= (int)((System.currentTimeMillis()- initialTime)/60000%60);
        int s= (int)((System.currentTimeMillis()- initialTime)/1000)%60;
        runTime=(double)(System.currentTimeMillis()-initialTime)/3600000;
        Color text=new Color(0,0,0);


        g.setColor(color1);
        g.fillRect(7, 54, 220, 58);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 58);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("Leather:" + count, 11, 87);
        g.drawString("Profit: " + prince*count, 12, 106);
        g.drawString("State:" + state, 144, 71);



    }


    @Override
    public void start(){
        System.out.println("Interface bien chingona que no me corre :D");
        CMenu.setVisible(true);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return CMenu.Aceptar;
            }
        },200,1000);
        CMenu.setVisible(false);
        initialTime=System.currentTimeMillis();

        if(Select[CMenu.Energy]==1){
                AUX=energy;
            }
            if(Select[CMenu.Energy]==2){
                AUX=Senergy;

            }
            if(Select[CMenu.Energy]==3){
                AUX=Stamina;
            }
            if(Select[CMenu.Energy]==0){
                for(int j=0;j<AUX.length;j++){
                    AUX[j]=0;
            }

        }
        ctx.camera.pitch(99);

        //GeItem.lookup(nDhide[CMenu.Leather]).getPrince();

    }


//Funcion main
    @Override
    public void poll() {
        System.out.println("Programa bien chingon que aun no termino xD");
        state= getState();
        System.out.println("Valor de Estado= " + state);

        switch (state){
            case 1:
                Bank();                     // Primera entrada al banco saca dinero, revisa pocion y saca cuero
                break;
            case 2:
                Walktoeli();  // Deveria caminar al pela cuero
                break;
            case 3:
                Buy();  //Activa la ventana para seleccionar lo que se va a comprar
                break;

            case 5:
                Walktobank();// se devuelve al banco
                break;
            case 6:
                start();
                break;
            case 7:
                stop();
        }

    }

//Controlador
    private int getState() {
        System.out.println("A ver que hago?");

        if((!CMenu.Aceptar==true)){
            return 6;
        }
        if(CMenu.Cancelar==true||nDhide[CMenu.Leather]==0){
            return 7;
        }

        else if((ctx.inventory.select().id(Hide[CMenu.Leather]).count()==27&&ctx.movement.distance(BANK_TILE)>10) ) {
            return 5;
        }

        else if(ctx.movement.distance(TRADER)<=17&&ctx.inventory.select().id(nDhide[CMenu.Leather]).count()==27 &&ctx.inventory.select().id(Coin).count(true)>540){
            return 3;
        }

        else if((ctx.inventory.select().id(nDhide[CMenu.Leather]).count()==27 && ctx.inventory.select().id(Coin).count(true)>540)&&(ctx.movement.distance(TRADER)>15)){
            return 2;
        }

        else if(bank==-1&&ctx.movement.distance(BANK_TILE)<12){
            return 1;
        }

        else if(ctx.controller.isStopping()){
            return 7;
        }else{
            return 7;
        }


    }

//Trade a elis
    private void Buy(){
        do {

            elis = ctx.npcs.select().id(elistrade).poll();

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return elis.inViewport();
                }
            }, 100, 30);
        }while (!elis.inViewport());

        elis.interact("Trade");
        count=count +27;
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(324).component(97).visible();
            }
        },100,200);


            ctx.widgets.component(324,97).interact("All"); //.select().id(324,97);


        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(Hide[CMenu.Leather]).count()==27;
            }
        },100,200);
        ctx.movement.step(x1);

    }





//Detener all
    @Override
    public void stop(){
        System.out.println("Se paro esta mielda :D");

        ctx.controller.stop();
        System.exit(1);
        System.exit(0);
    }

int bank=-1,Pot=0;boolean t;
    public void Bank() {
        //System.out.println("ahora ire al banco");


        if(!ctx.bank.inViewport()){
            ctx.camera.turnTo(ctx.bank.nearest());} // Mueve la camara al banco cercano
        do{
            ctx.bank.open();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(12).component(4).visible();
                }
            }, 200, 10);
        }while (!ctx.widgets.widget(12).component(4).visible());
        System.out.println("Ya abri el banco");
        if(ctx.widgets.widget(12).component(4).visible()) {// abre el banco
            System.out.println("Vamo a Depositar");
            if(ctx.inventory.select().id(Coin).count(true) < 540) { // la primera iteracion
                ctx.bank.depositInventory();//deposita_todo el inventario
                ctx.bank.withdraw(Coin, 200000);
                bank++;
            }else if(ctx.inventory.select().id(Hide[CMenu.Leather]).count()==27){
                ctx.bank.deposit(Hide[CMenu.Leather],200000);

            }
            t = energia();
            beber();
            System.out.println("Niveles de stamina revisados");
            System.out.println("Cantidad de cuero en inv: " + ctx.inventory.select().id(nDhide[CMenu.Leather]).count() );

            while(ctx.inventory.select().id(nDhide[CMenu.Leather]).count()<27){
                System.out.println("Cantidad de cuero en inv: " + ctx.inventory.select().id(nDhide[CMenu.Leather]).count() );
                ctx.bank.withdraw(nDhide[CMenu.Leather], 200000);}
                //ctx.bank.close();
                bank++;
            }

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Vamo a Colgarnos");
                return ctx.inventory.select().id(nDhide[CMenu.Leather]).count()==27;
            }
        }, 200, 30);
            ctx.movement.step(x1);

        }


    //Check energia
    public boolean energia(){
        System.out.println("Revisando niveles de stamina");
        int bota= ctx.movement.energyLevel();
        while(bota<=CMenu.StaminaLv&&AUX[Pot]!=0){
            System.out.println("Sacar stamina "+ AUX[Pot]);

            ctx.bank.withdraw(AUX[Pot],1);
            return true;
        }
            return false;

    }

    //Drink
    public void  beber(){
        if(t==true){
            System.out.println("Vamo a vbebe");
            ctx.bank.close();
            Item Super_Energy= ctx.inventory.select().id(AUX[Pot]).poll();
            Super_Energy.interact("Drink");
            Pot++;
            ctx.bank.open();
            do{
            ctx.bank.deposit(AUX[Pot],1);
            }while (ctx.inventory.select().id(AUX[Pot]).count()==1);
            System.out.println("Meter stamina "+ AUX[Pot]);
        }
        if(Pot==3){
            Pot=0;
        }
    }

    //Camina a eli
    public  void Walktoeli(){
        //ctx.input.click(760,29,true);
            if(ctx.movement.distance(x1)<=15&&ctx.movement.distance(BANK_TILE)<=15) {
                System.out.println("Vamo a caminar al trade");
                ctx.movement.step(x1);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("La Distancia con el p2 :" + ctx.movement.distance(x2));
                        return ctx.movement.distance(x2) <= 20;
                    }
                }, 200, 300);
                System.out.println("Llegue a punto 1");
            }

            if(ctx.movement.distance(x1)<=16&&ctx.movement.distance(x2)<=20) {
                ctx.movement.step(TRADER);
                System.out.println("Llegue a punto 2");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        return ctx.movement.distance(x2)<=13;
                    }
                }, 200, 300);
            }
    }


    //Camina al banco
    public void Walktobank(){

            System.out.println("Vamo a caminar al Banco");
                if(ctx.movement.distance(BANK_TILE)>14 || ctx.movement.distance(BANK_TILE)==-1){
                   ctx.movement.step(x1);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.movement.distance(x1)<=5;
                        }
                    }, 200, 300);

                }
                    ctx.movement.step(BANK_TILE);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.movement.distance(BANK_TILE)<12;
                        }
                    }, 200, 300);
                    System.out.println("Ya llegue al banco");

        bank=-1;

    }


}

