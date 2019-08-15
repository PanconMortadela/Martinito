package scripts;

import com.sun.javafx.sg.prism.NGParallelCamera;
import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="BF1", description = "Money making",properties = "autor: Pos yo!")
public class BF1 extends PollingScript<ClientContext> implements PaintListener{
    int Cinta= 9100,bank= 26707,barra=9093,control=0; // numero del objeto
    int[] component= {110,111,112,113,114,115};//steel,mithril,adamant,runite,silver,gold

    int widget=28;
    final Tile tcinta=new Tile(1942,4967);
    boolean f=true,t,p=false;
    Item coal= ctx.inventory.select().id(12019).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int count=0;
    GeItem barr= new GeItem(2353);
    GeItem ore= new GeItem(440);
    GeItem coal1= new GeItem(453);
    int value;
    int h, m, s;
    final static int energy[]={3008,3010,3012,3014,229};
    final static int Senergy[]={3016,3018,3020,3022,229};
    final static int Stamina[]={12625, 12627, 12629, 12631,229};
    final static int Select[]={0,1,2,3};
    static int[] AUX= new int[Stamina.length];
    boolean fo=true,reco=false;
    int est=0;
    Point c;
    GameObject banco= ctx.objects.select().id(26707).poll();
    public static final Tile[] path = {new Tile(1942, 4967, 0), new Tile(1939, 4967, 0), new Tile(1940, 4964, 0)};

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
        g.drawString("Baraas:" + count, 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Profit: " + value*count, 12, 106);
        //g.drawString("State:" + state, 144, 71);
    }

    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        camera();
        AUX=Stamina;
    }
    @Override
    public void poll(){


        estado();
    }
    public void estado(){
        if(est==0&& (ctx.inventory.select().count()<=28||ctx.inventory.select().id(2353).count()>1)){
            bank();}
        else if(est==1&&(ctx.inventory.select().count()>1)){llevar();}
        else if ((est==2&&reco==true)&&ctx.inventory.select().count()<2){recoger();est=0;}
        else{
            ctx.controller.stop();
        }
    }

    public void bank(){
        System.out.println("Banco");


        while(!ctx.bank.opened()){
            if(detener())break;
            if(f==true){    //inicializar
                    banco= ctx.objects.select().id(26707).poll();
                    banco.interact("Use");
                    ctx.bank.depositInventory();

                }else{
                    banco.interact("Use");
                }
            Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
            }, 100, 10);
        }



        c=new Point(Random.nextInt(430, 445), Random.nextInt(120, 130)); //P coal
        if(ctx.inventory.select().id(12019).count()!=1){ctx.bank.withdraw(12019,1);
            ctx.input.move(c);
        }
        c=new Point(Random.nextInt(607, 619), Random.nextInt(264, 276));
            if(ctx.inventory.select().count()>=1)count=count+ctx.inventory.select().count()-1;// aumenta barras
            while(ctx.inventory.select().id(2359).count()>=1){ //mientras tenga barras de steel
                if(detener())break;
                ctx.input.move(c);
                ctx.input.click(false);
                ctx.input.move(c.x-Random.nextInt(0,50),c.y+Random.nextInt(68,74));
                ctx.input.click(true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()<=2;
                    }
                }, 200, 10);
            }
            //if(m%8==0)fo=true;
            //Foreman();
            t = energia();
            beber();
        c=new Point(Random.nextInt(430, 445), Random.nextInt(120, 130));
        ctx.input.move(c);

            while(ctx.inventory.select().count()==1&&ctx.inventory.select().id(12019).count()==1) { // saca coal
                if(detener())break;
                ctx.bank.withdraw(453,200000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()!=1;
                    }
                }, 200, 10);

            }
        c=new Point(Random.nextInt(610,620),Random.nextInt(225,240));

        //cerrar banco
        while(ctx.bank.opened()) {
            if(detener())break;
            ctx.widgets.close(ctx.widgets.widget(12), true);
            ctx.input.move(c);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, 200, 10);

        }
        //llenar coalbag
        c=new Point(Random.nextInt(360, 385), Random.nextInt(245, 270));
            while(ctx.inventory.select().count()>2) {
                if(detener())break;;
                ctx.inventory.select().id(12019).poll().click();

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()!=28;
                    }
                }, 100, 10);

                if(ctx.widgets.widget(193).component(1).visible()){
                    ctx.input.move(Random.nextInt(360, 385), Random.nextInt(245, 270));
                    ctx.bank.open();
                    ctx.bank.deposit(453,200000);
                }

            }

            ctx.input.move(c);
            ctx.bank.open();
        c=new Point(Random.nextInt(390, 400), Random.nextInt(125, 130));
            ctx.input.move(c);
            if(p==true){
                while(ctx.inventory.select().count()==1) { //sacar steel
                    if(detener())break;

                    est=1;
                    ctx.bank.withdraw(447,200000);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count()>1;
                        }
                    }, 200, 10);
                }
            }else{
                while(ctx.inventory.select().count()==1&&ctx.inventory.select().id(12019).count()==1) { // saca coal
                    if(detener())break;
                    p=true;
                    est=1;
                    ctx.bank.withdraw(453,200000);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count()!=1;
                        }
                    }, 200, 10);

                }
            }

        //cierra el banco y muevo el cursor a la cinta
        while(ctx.bank.opened()){
            if(detener())break;
                ctx.widgets.close(ctx.widgets.widget(12),true);
            ctx.input.move(Random.nextInt(530, 560), Random.nextInt(33, 41));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, 200, 10);

        }

    }



    public void llevar(){
        c= new Point(Random.nextInt(600,625),Random.nextInt(223,244));
        GameObject cinta= ctx.objects.select().id(9100).poll();
        while(ctx.inventory.select().count()>2){
            if(detener())break;;
            if(reco==true){
                est=2;
            }
            if(!(ctx.inventory.select().id(453).count()>0)){
                control++;
                if(control>0){
                    reco=true;
                    est=2;

                }
            }

            do{
                if(detener())break;;
                cinta.click();                              //clic en la cinta
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.destination().x()==1943||ctx.movement.destination().x()==1942;
                    }
                },500,2);
            }while(ctx.movement.destination().x()!=1942&&ctx.movement.destination().y()!=1967);
            ctx.input.move(c);
            ctx.input.click(false);
            ctx.input.move(c.x-Random.nextInt(0,54),c.y+Random.nextInt(68,74));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=28;//
                }
            }, 500, 30);
        }
        //boolean m=true;
        while(ctx.inventory.select().count()==1){
            if(detener())break;;
            ctx.input.click(true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=1;
                }
            }, 200, 10);

        }
        while(ctx.inventory.select().count()>=2&&ctx.inventory.select().id(453).count()>0){
            if(detener())break;;
            cinta.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !(ctx.inventory.select().count()>1);
                }
            }, 200, 2);

            if(!(ctx.movement.destination().y()<4967)){
                cinta.click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !(ctx.inventory.select().count()>1);
                    }
                }, 500, 2);
            }
        //f=true;
        }
        if(reco==true){
            dispenser.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if(ctx.widgets.widget(233).component(1).visible()){
                        dispenser.click();
                    }
                    return ctx.movement.distance(ctx.players.local().tile(), ctx.movement.destination()) < 2;
                }
            }, 200, 20);
        }else{
            ctx.input.move(Random.nextInt(700, 706), Random.nextInt(80, 85));
            ctx.movement.step(new Tile(1948,4957));
            est=0;
            ctx.input.move(Random.nextInt(354,392),Random.nextInt(266,280));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.distance(ctx.players.local().tile(),ctx.movement.destination())<5;
                }
            }, 200, 30);
        }



    }


    public void camera(){
        System.out.println("Camaraaaaa");
        ctx.input.move(Random.nextInt(629, 649), Random.nextInt(18, 28));
        ctx.input.click(true);
        ctx.camera.turnTo(new Tile(1936,4961));
        ctx.camera.pitch(Random.nextInt(95,99));


    }
    GameObject dispenser= ctx.objects.select().id(9093).poll();

    public void recoger(){
        System.out.println("voy a recogerl");
        ctx.input.move(Random.nextInt(241, 259), Random.nextInt(139, 148));

        if(control==2){
            control=0;
            reco=false;
            p = false;
        }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.objects.select().name("Bar dispenser").poll().id() != 9093;
                }
            }, 200, 50);



        while(ctx.inventory.select().count()<=2) {
            if(detener())break;;
            if(ctx.widgets.widget(widget).component(76).visible()){
                ctx.widgets.widget(widget).component(111).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()!=1;
                    }
                }, 200, 10);
                ctx.widgets.close(ctx.widgets.widget(widget),true);
            }else{

                if(!dispenser.click()){
                    ctx.widgets.close(ctx.widgets.widget(widget),true);
                }
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(widget).component(76).visible();
                    }
                }, 500, 2);
            }

        }

        ctx.input.move(Random.nextInt(700, 706), Random.nextInt(80, 85));
        ctx.movement.step(new Tile(1948,4957));
        if(f=true) {
            f = false;
            dispenser=ctx.objects.select().name("Bar dispenser").poll();
        }
        ctx.input.move(Random.nextInt(354,392),Random.nextInt(266,280));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.movement.distance(ctx.players.local().tile(),ctx.movement.destination())<5;
            }
        }, 200, 30);
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

    public void Foreman(){
        if((m==0||m%9==0)&&fo==true) {
            fo=false;
            ctx.bank.open();
            ctx.bank.withdraw(995, 200000);
            while(ctx.bank.opened()){
                if(detener())break;
                ctx.widgets.close(ctx.widgets.widget(12),true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 200, 10);

            }
            Npc fore = ctx.npcs.select().id(2923).poll();
            while(ctx.inventory.select().id(995).count(true)==200000) {
                if(detener())break;;
                fore.interact(false, "Pay");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(219).component(0).component(0).visible();
                    }
                }, 200, 10);
                ctx.widgets.widget(219).component(0).component(1).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(995).count(true)!=200000;
                    }
                }, 200, 10);
            }
            ctx.movement.step(new Tile(1948,4957));
            ctx.input.move(Random.nextInt(354,392),Random.nextInt(266,280));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.distance(ctx.players.local().tile(),new Tile(1948,4957))<2;
                }
            }, 200, 10);
            ctx.bank.open();
            ctx.bank.deposit(995,200000);
        }


    }


    int Pot=0;
    public boolean energia(){
        System.out.println("Revisando niveles de stamina");
        int bota= ctx.movement.energyLevel();
        while(bota<=50&&AUX[Pot]!=0){
            if(detener())break;;
            System.out.println("Sacar stamina "+ AUX[Pot]);
            Item pote= ctx.inventory.select().id(AUX[Pot]).poll();

            while(ctx.inventory.select().id(AUX[Pot]).count()<1) {
                if(detener())break;;
                ctx.bank.withdraw(AUX[Pot], 1);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(AUX[Pot]).count()==1;
                    }
                }, 500, 3);
            }
            return true;
        }
        return false;
    }


    public void  beber(){
        while(t==true){
            if(detener())break;;
            System.out.println("Vamo a vbebe");
            while(ctx.bank.opened()){
                if(detener())break;;
                ctx.widgets.close(ctx.widgets.widget(12),true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 200, 10);

            }
            Item Super_Energy= ctx.inventory.select().id(AUX[Pot]).poll();
            boolean r=t;
            while(ctx.inventory.select().id(AUX[Pot+1]).count()!=1) {
                if(detener())break;;
                Super_Energy.click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(AUX[Pot+1]).count()==1;
                    }
                }, 200, 10);

            }
            Pot++;
            ctx.bank.open();

            while (ctx.inventory.select().id(AUX[Pot]).count()==1){
                if(detener())break;;
                System.out.println("Meter stamina "+ AUX[Pot]);
                ctx.bank.deposit(AUX[Pot],1);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(AUX[Pot]).count()!=1;
                    }
                }, 200, 10);
                if(ctx.inventory.select().id(AUX[Pot]).count()!=1)t=false;
            }

        }
        if(Pot==3){
            Pot=0;
        }
    }


}
