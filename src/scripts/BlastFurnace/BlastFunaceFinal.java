package scripts.BlastFurnace;

import org.powerbot.script.*;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Hop;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="000BlastFurnace", description = "Money making",properties = "autor: Pos yo!")
public class BlastFunaceFinal extends PollingScript<ClientContext> implements PaintListener{
    Tile Banco=new Tile(1948,4957,0);
    Areas areas=new Areas();
    Area cuadro,player;
    int esperar=0;
    int Materialc,MaterialI;

    boolean Cinta,bank,barra; // numero del objeto
    int[] component= {110,111,112,113,114,115};//steel,mithril,adamant,runite,silver,gold

    int widget=270;
    final Tile tcinta=new Tile(1942,4967);
    boolean f=true,t,p;
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
    final static int energy[]={3008,3010,3012,3014};
    final static int Senergy[]={3016,3018,3020,3022};
    final static int Stamina[]={12625, 12627, 12629, 12631,229};
    final static int Select[]={0,1,2,3};
    static int[] AUX= new int[Stamina.length];
    boolean fo=true;
    int est=0;
    Point Objeto;
    GameObject banco= ctx.objects.select().id(26707).poll();
    public static final Tile[] path = {new Tile(1942, 4967, 0), new Tile(1939, 4967, 0), new Tile(1940, 4964, 0)};

    Hop hop= new Hop(ctx);

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
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("Baraas:" + count+"   "+"Profit:"+profit, 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Profit: " + value*count, 12, 107);
        g.drawString("MaterialC: " + Materialc + "  " + "MaterialI: " + MaterialI, 12, 127);
        g.drawString("Estoy en el mundo:" + hop.WolrdStay + "  Estaba en el mundo: " + hop.estaba, 12, 144);
    }

    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        if(ctx.game.clientState()==10||ctx.game.clientState()==20||ctx.game.clientState()==25){

            ctx.input.move(new Point(Random.nextInt(300,340),Random.nextInt(320,330)));
            ctx.input.click(true);
            time=1000;
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(378).component(85).visible();
                }
            }, time, 10);
        }

        if(ctx.widgets.widget(378).component(85).visible()){
            ctx.widgets.widget(378).component(85).click();
            time=1000;
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().inViewport();
                }
            }, time, 10);
        }
        camera();
        AUX=Stamina;

        hop.buscar_nombre("Blast Furnace");
        hope=Random.nextBoolean();

    }

    int H_Close=2,M_Close=Random.nextInt(0,10),Estado_Close=Random.nextInt(1,2);
    boolean hope;
    @Override
    public void poll(){
        estado();
    }


    public void estado(){

        if(!ctx.game.tab().equals(Game.Tab.INVENTORY)){
            ctx.game.tab(Game.Tab.INVENTORY);
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab().equals(Game.Tab.INVENTORY);
                }
            },time,50);
            camera();
        }

        if(ctx.widgets.widget(474).component(3).component(3).itemStackSize()==0){
            JOptionPane.showInputDialog("Metele dinero al coffer tacaño!");
            ctx.controller.stop();
            detener();
        }
        if(est==0&& (ctx.inventory.select().count()<28||ctx.inventory.select().id(2353).count()>1)){
            bank(); est++;}
        else if(est==1&&(ctx.inventory.select().id(440).count()>1)){llevar();est++;}
        else if (est==2&&ctx.inventory.select().count()<=2){recoger();est=0;}
        else{
            ctx.controller.stop();
        }
    }

    int time=Random.nextInt(200,300);
    boolean bankCode=true;
    int profit=0;
    public void bank(){
         System.out.println("Banco");
        if (ctx.widgets.widget(233).component(1).visible()) {
            ctx.movement.step(new Tile(1948, 4957));
            cuadro = areas.area2(Banco);
            ctx.input.move(Random.nextInt(354, 392), Random.nextInt(266, 280));
            final Tile ti = ctx.movement.destination().tile();
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return cuadro.contains(ctx.players.local().tile())||ctx.bank.select().name("Bank chest").poll().inViewport();
                }
            }, time, 40);
        }



        while(!(ctx.bank.open()||ctx.widgets.widget(213).component(16).visible())){    //Perfect
            if(detener())break;
            if(ctx.inventory.select().count()==0||ctx.inventory.select().count()==28||ctx.inventory.select().count()==1) {
                banco.interact("Use");
                time=Random.nextInt(200,400);
                if(ctx.game.crosshair().equals(Game.Crosshair.ACTION)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened() || ctx.widgets.widget(213).component(16).visible();
                        }
                    }, time, 80);
                }else{

                }
            }
        }
        if (bankCode==true||ctx.widgets.widget(213).component(16).visible()){
            System.out.println("Entre en bankcode");
            bankcode();
            bankCode=false;
        }

        Materialc=ctx.widgets.widget(12).component(13).component(15).itemStackSize();
        MaterialI=ctx.widgets.widget(12).component(13).component(14).itemStackSize();
        if(ctx.widgets.widget(12).component(13).component(15).itemStackSize()<27||ctx.widgets.widget(12).component(13).component(14).itemStackSize()<27){
            ctx.controller.stop();
            detener();
        }


        if(ctx.inventory.select().id(12019).count()!=1){
            ctx.bank.withdraw(12019,1);
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(12019).count()==1;
                }
            }, time, 15);
        }

        if(ctx.inventory.select().id(2353).count()>=1) {
            count = count + ctx.inventory.select().id(2353).count();// aumenta barras
            profit=((count*14400)/((h*3600)+(m*60)+s));
        }
        while(ctx.inventory.select().id(2353).count()>=1||ctx.inventory.select().count()>1){ //mientras tenga barras de steel
            if(detener())break;;
            ctx.bank.depositAllExcept(12019);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()<=2;
                }
            }, time, 15);
        }
        if(Estado_Close==1&&H_Close==h&&m>=M_Close){
            close();
        }
        if(ctx.skills.level(Constants.SKILLS_SMITHING)<60) {
            if (m % 8 == 0) fo = true;    //
            if (m == 0 && h == 0 && s > 30) fo = false;//
            Foreman();//
        }
        t = energia();
        if(t==true){
            ctx.widgets.close(ctx.widgets.widget(12), true);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, time, 10);
            beber();
        }
        ctx.bank.open();
        if(t==true)depositarpote();
        while(ctx.inventory.select().count()<=2&&ctx.inventory.select().id(12019).count()==1) {
            if(detener())break;
            ctx.bank.withdraw(453,200000);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()>1;
                }
            }, time, 20);
        }
        while(ctx.bank.opened()) {
            if(detener())break;;
            ctx.widgets.close(ctx.widgets.widget(12), true);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, time, 10);
        }
        while(ctx.inventory.select().count()>2) {
            if(detener())break;;
            ctx.inventory.select().id(12019).poll().interact("Fill");
            p=true;
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=28;
                }
            }, time, 10);

            if(ctx.widgets.widget(193).component(1).visible()){
                ctx.bank.open();
                ctx.bank.deposit(453,200000);
            }

        }

        while(ctx.inventory.select().count()<=2&&p==true) {
            if(detener())break;
            p = false;
            ctx.bank.open();
            ctx.bank.withdraw(440,200000);
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()>1;
                }
            }, time, 20);
            if(ctx.inventory.select().count()<=2){
                p=true;
            }
        }
        if(hope==false){
            hop.start(m);
            hop.condicion_nombre();
            if(hop.hope){
                hope= Random.nextBoolean();
                hop.hope=false;
            }
        }


    }

    public void close(){
        ctx.controller.stop();
        detener();
    }
    GameObject cinta;
    public void llevar(){
        cinta= ctx.objects.select().id(9100).poll();
        cinta.bounds(-32, 32, -240, -160, -84, 20);
        System.out.println("llevar 1");
        while(ctx.inventory.select().count()==28&&ctx.inventory.select().id(440).count()>2){
            if(detener())break;
            do {
                if(detener())break;;
                cinta= ctx.objects.select().id(9100).poll();
                cinta.bounds(-32, 32, -240, -160, -84, 20);
                cinta.interact("Put-ore-on");
                time=Random.nextInt(400,600);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.destination().x()==1943||ctx.movement.destination().x()==1942;
                    }
                },time,2);
                if(ctx.movement.destination().x()!=1942&&ctx.movement.destination().y()!=4967){
                    ctx.movement.step(new Tile(1942,4967));
                    Cinta=true;
                }
            }while(ctx.movement.destination().x()!=1942&&ctx.movement.destination().y()!=4967);
            System.out.println("llevar 2");
            if(Cinta==true){
                Cinta=false;
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.distance(new Tile(1942,4967))<3;
                    }
                },time,50);
                cinta= ctx.objects.select().id(9100).poll();
                cinta.bounds(-32, 32, -240, -160, -84, 20);
                cinta.interact("Put-ore-on");

            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            },time,50);
            ctx.inventory.select().id(12019).poll().click(false);

            ctx.input.move(ctx.input.getLocation().x-Random.nextInt(0,50), ctx.input.getLocation().y+Random.nextInt(68,74));
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=28&&ctx.movement.destination().x()==-1;//
                }
            }, time, 70);
        }

        System.out.println("llevar 3");
        int co=0;
        while(ctx.inventory.select().count()<=2){
            if(detener())break;
            if(co==0) {
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()<=2;
                    }
                }, time, 10);
                ctx.input.click(true);
                co = 1;
            }else {
                ctx.inventory.select().id(12019).poll().interact("Empty");
            }
            time=Random.nextInt(200,400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !(ctx.inventory.select().count()<=2);
                }
            }, time, 10);

        }
        System.out.println("llevar 4");

        while(ctx.inventory.select().count()>2&&ctx.inventory.select().id(453).count()>0){
            if(detener())break;;
            cinta.interact("Put-ore-on");
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()<=2;
                }
            }, time, 5);

            if(!(ctx.movement.destination().y()<4967)){
                cinta.interact("Put-ore-on");
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(ctx.widgets.widget(229).component(0).visible()){
                            return true;
                        }
                        return !(ctx.inventory.select().count()>2);
                    }
                }, time, 10);
            }
        }

    }


    public void camera(){
        System.out.println("Camaraaaaa");
        //ctx.input.move(areas.punto(ctx.widgets.widget(161).component(24).centerPoint()));
        //ctx.input.click(true);
        time=Random.nextInt(500,800);
        ctx.camera.angle(Random.nextInt(100,104));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.yaw()>=85&&ctx.camera.yaw()<105;
            }
        }, time, 10);
        ctx.camera.pitch(Random.nextInt(95,99));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.pitch()>=95&&ctx.camera.pitch()<=100;
            }
        }, time, 10);

    }

    int mis=0,IDdis;    GameObject dispen;
    public void recoger(){
        System.out.println("voy a recoger");
        cuadro=areas.area2(new Tile(1940, 4963));
        while(ctx.inventory.select().count()<=2) {
            if(detener())break;
            System.out.println("recoger 1.1");
            player=areas.area1(ctx.players.local().tile());

            if(ctx.widgets.widget(229).component(0).visible())dispenser(); //ver si este widget de lv up sigue intacito
            if(ctx.inventory.select().count()<=2) {
                System.out.println("recoger 1.2");
                dispenser();
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(detener())return true;
                        if (ctx.widgets.widget(233).component(1).visible()) {
                            dispen.click(); //Click xq con interact no hace naa
                        }
                        if(ctx.widgets.widget(229).component(0).visible()&&mis%3==0){
                            dispen.click();    //Click xq con interact no hace naaa
                            mis++;
                        }else{
                            mis++;
                        }
                        if(player.contains(ctx.players.local().tile())){
                            esperar++;
                            if(esperar==10){
                                dispenser();
                                esperar=0;
                            }
                        }
                        return cuadro.contains(ctx.players.local().tile());
                    }
                }, time, 80);
                break;
            }
            mis=0;esperar=0;

        }
        time=Random.nextInt(200,300);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(widget).component(14).visible()||ctx.players.local().speed()==0;
            }
        }, time, 10);

        if((ctx.inventory.select().count()>1&&t==false)||(ctx.inventory.select().count()>2&&t==true)){//drop si agarra algo :v
            System.out.println("Voy a drop un item que agare 1");
            itemfeo();
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()<=2;
                }
            }, time, 20);
            dispenser();
        }

        while((ctx.inventory.select().count()==1&&t==false)||(ctx.inventory.select().count()<=2&&t==true)) {
            if(detener())break;
            if (ctx.widgets.widget(widget).component(14).visible()) {
                System.out.println("recoger 1.3");
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(270).component(5).visible();
                    }
                }, time, 20);
                ctx.input.send(" ");
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(229).component(1).visible();
                    }
                }, time, 20);
                System.out.println("recoger 2");
                if((ctx.inventory.select().count()>20&&t==false)||(ctx.inventory.select().count()>20&&t==true)) {

                    ctx.movement.step(new Tile(1948, 4957));
                    cuadro = areas.area2(Banco);
                    ctx.input.move(Random.nextInt(354, 392), Random.nextInt(266, 280));
                    final Tile ti = ctx.movement.destination().tile();
                    time=Random.nextInt(200,400);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return cuadro.contains(ctx.players.local().tile())||ctx.bank.inViewport();
                        }
                    }, time, 40);
                    if(hope){
                        hop.start(m);
                        hop.condicion_nombre();
                        if(hop.hope){
                            hope= Random.nextBoolean();
                            hop.hope=false;
                        }
                    }
                    break;
                }
            } else {
                dispenser();
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(widget).component(14).visible();
                    }
                }, time, 3);
                if(!(ctx.inventory.select().id(2353).count()>20)&&ctx.inventory.select().count()>1&&!ctx.widgets.widget(widget).component(14).visible()){//drop si agarra algo :v
                    System.out.println("Voy a drop un item que agare 2");
                    itemfeo();
                    time=Random.nextInt(200,300);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count()==1;
                        }
                    }, time, 20);
                }
            }
        }

        if(Estado_Close==1&&H_Close==h&&m>=M_Close){
            close();
        }
    }
    public void itemfeo() {
        Item[] items = ctx.inventory.items();

        for(int i=0;i<items.length;i++) {
            if(detener())break;
            if (items[i].id()== 12019){

            }else if(t==true){
                if(items[i].id()== AUX[Pot]) {
                }
            } else {
                items[i].interact("Drop");
            }

        }
    }

    public void dispenser(){
        dispen = ctx.objects.select().name("Bar dispenser").poll();
        dispen.bounds(-92, -24, -64, 0, -24, 16);
        IDdis=dispen.id();
        if(dispen.id()==9093)
            dispen.interact("Check");
        else if(dispen.id()==9094){
            dispen.click();
        }else
            dispen.interact("Take");
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
            ctx.bank.withdraw(995, 100000);
            while(ctx.bank.opened()){
                if(detener())break;
                ctx.widgets.close(ctx.widgets.widget(12),true);
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, time, 10);

            }
            Npc fore = ctx.npcs.select().id(2923).poll();
            while(ctx.inventory.select().id(995).count(true)==100000) {
                if(detener())break;;
                fore.interact(false, "Pay");
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(219).component(0).component(0).visible();
                    }
                }, time, 10);
                ctx.widgets.widget(219).component(1).component(1).click();
                time=Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(995).count(true)!=100000;
                    }
                }, time, 10);
            }
            ctx.movement.step(new Tile(1948,4957));
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.distance(ctx.players.local().tile(),new Tile(1948,4957))<2;
                }
            }, time, 10);
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
            int ite=0;
            while(ctx.inventory.select().id(AUX[Pot]).count()<1) {
                if(detener())break;;
                ctx.bank.withdraw(AUX[Pot], 1);
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(AUX[Pot]).count()==1;
                    }
                }, time, 10);
                if(ctx.inventory.select().id(AUX[Pot]).count()!=1){
                    Pot++;
                    if(Pot==AUX.length){
                        Pot=0;
                        ite++;
                        if(ite>2){
                            JOptionPane.showInputDialog("Compra potes puto");
                        }
                    }
                }
            }
            if(ctx.inventory.select().id(AUX[Pot]).count()==1)
                return true;
        }
        return false;
    }


    public void  beber(){
        while(t==true){
            if(detener())break;;
            System.out.println("Vamo a vbebe");
            Item Super_Energy= ctx.inventory.select().id(AUX[Pot]).poll();
            boolean r=t;
            while(ctx.inventory.select().id(AUX[Pot]).count()==1) {
                if(detener())break;;
                Super_Energy.interact("Drink");
                time=Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(ctx.inventory.select().id(229).count()==1){
                            return true;
                        }
                        return ctx.inventory.select().id(AUX[Pot+1]).count()==1;
                    }
                }, time, 10);

            }
            if(ctx.inventory.select().id(229).count()==1){
                Pot=0;
                break;
            }
            if(ctx.inventory.select().id(AUX[Pot+1]).count()==1) {
                Pot++;
                break;
            }
        }

    }
    public void depositarpote(){
        while (ctx.inventory.select().id(AUX[Pot]).count()==1){
            if(detener())break;;
            System.out.println("Meter stamina "+ AUX[Pot]);
            ctx.bank.deposit(AUX[Pot],1);
            time=Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(AUX[Pot]).count()!=1;
                }
            }, time, 10);
            if(ctx.inventory.select().id(AUX[Pot]).count()!=1)
                t=false;
        }
        if(Pot==3){
            Pot=0;
        }
    }
    public void bankcode() {

        ArrayList<String> a = new ArrayList<String>();
        a.add("7");
        a.add("9");
        a.add("1");
        a.add("0");
        if (ctx.widgets.widget(213).component(16).visible()) {
            String s = ctx.widgets.widget(213).component(10).text();

            for (int j = 0; j < a.size(); ) {
                for (int i = 16; i <= 34; i = i + 2) {
                    if (ctx.widgets.widget(213).component(i).component(1).text().equals(a.get(j))) {
                        j = j + 1;
                        final String t = s;
                        ctx.widgets.widget(213).component(i).click();
                        ctx.input.move(ctx.widgets.widget(213).component(10).nextPoint());
                        time=Random.nextInt(1000,2000);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return !t.equals(ctx.widgets.widget(213).component(10).text());
                            }
                        }, time, 10);

                        s = ctx.widgets.widget(213).component(10).text();
                        if (j == a.size()) break;
                    }
                }


            }
        }else if(ctx.widgets.widget(14).component(33).visible()){
            ctx.widgets.widget(14).component(33).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(14).component(33).visible();
                }
            }, time, 10);
        }

    }
}
