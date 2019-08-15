package scripts;

import com.sun.javafx.sg.prism.NGParallelCamera;
import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@Script.Manifest(name="BF", description = "Money making",properties = "autor: Pos yo!")
public class BF extends PollingScript<ClientContext> implements PaintListener{
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
    final static int energy[]={3008,3010,3012,3014,229};
    final static int Senergy[]={3016,3018,3020,3022,229};
    final static int Stamina[]={12625, 12627, 12629, 12631,229};
    final static int Select[]={0,1,2,3};
    static int[] AUX= new int[Stamina.length];
    boolean fo=true;
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
        if(est==0&& (ctx.inventory.select().count()<28||ctx.inventory.select().id(2353).count()>1)){
            bank(); est++;}
        else if(est==1&&(ctx.inventory.select().id(440).count()>1)){llevar();est++;}
        else if (est==2&&ctx.inventory.select().count()<2){recoger();est=0;}
        else{
            ctx.controller.stop();
        }
    }

    int minuto=Random.nextInt(55,57);
    boolean bankCode=true;

    public void bank(){
      /*  if(h==1&&m>minuto){
            while(!ctx.bank.opened()){
                ctx.bank.open();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 100, 60);

                ctx.bank.depositInventory();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()==0;
                    }
                }, 100, 60);
            }
            while(ctx.bank.opened()){
                if(detener())break;;
                ctx.widgets.close(ctx.widgets.widget(12),true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 200, 30);

            }
            ctx.widgets.widget(161).component(38).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(182).component(12).visible();
                }
            }, 200, 30);
            ctx.widgets.widget(182).component(12).click();

            ctx.controller.stop();
            detener();
        }////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
        System.out.println("Banco");
        if (bankCode==true){
            bankcode();
            bankCode=false;
        }
        while(!ctx.bank.opened()){
            if(detener())break;
            if(ctx.inventory.select().count()==0||ctx.inventory.select().count()==28||ctx.inventory.select().count()==1) {
                banco.interact("Use");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 100, 30);
            }
        }


        if(ctx.inventory.select().id(12019).count()!=1){
            c=new Point(Random.nextInt(430, 445), Random.nextInt(120, 130)); //P coal
            ctx.bank.withdraw(12019,1);
            ctx.input.move(c);
        }

            if(ctx.inventory.select().id(2353).count()>=1)
                count=count+ctx.inventory.select().id(2353).count();// aumenta barras
            while(ctx.inventory.select().id(2353).count()>=1){ //mientras tenga barras de steel
                if(detener())break;;
                c=new Point(Random.nextInt(611, 621), Random.nextInt(316, 326));
                ctx.input.move(c);
                ctx.input.click(false);
                ctx.input.move(c.x-Random.nextInt(0,50),c.y+Random.nextInt(68,74));
                ctx.input.click(true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()<=2;
                    }
                }, 100, 30);
            }
            //if(m%8==0)fo=true;    //
            //if(m==0&&h==0&&s>30)fo=false;//
            //Foreman();//
            t = energia();
            beber();

        while(ctx.inventory.select().count()==1&&ctx.inventory.select().id(12019).count()==1) {
            if(detener())break;
            c=new Point(Random.nextInt(430, 445), Random.nextInt(120, 130));
            ctx.input.move(c);
            ctx.bank.withdraw(453,200000);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()>1;
                }
            }, 100, 20);
        }

        while(ctx.bank.opened()) {
            if(detener())break;;
            c=new Point(Random.nextInt(610,620),Random.nextInt(225,240));
            ctx.widgets.close(ctx.widgets.widget(12), true);
            ctx.input.move(c);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, 200, 10);

        }

            while(ctx.inventory.select().count()>2) {
                if(detener())break;;
                ctx.inventory.select().id(12019).poll().click();
                p=true;
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
        c=new Point(Random.nextInt(360, 385), Random.nextInt(245, 270));
            ctx.input.move(c);
            ctx.bank.open();
            c=new Point(Random.nextInt(390, 400), Random.nextInt(125, 130));
            ctx.input.move(c);
            while(ctx.inventory.select().count()==1&&p==true) {
                if(detener())break;
                p = false;
                ctx.bank.withdraw(440,200000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count()>1;
                    }
                }, 100, 20);
                if(ctx.inventory.select().count()<=2){
                    p=true;
                }
            }

        while(ctx.bank.opened()){
            if(detener())break;;
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
        c= new Point(Random.nextInt(608,626),Random.nextInt(286,294));
        GameObject cinta= ctx.objects.select().id(9100).poll();
        System.out.println("llevar 1");
        while(ctx.inventory.select().count()==28&&ctx.inventory.select().id(440).count()>2){
            if(detener())break;
            do {
                if(detener())break;;
                cinta= ctx.objects.select().id(9100).poll();
                cinta.interact("Put-ore-on");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.destination().x()==1943||ctx.movement.destination().x()==1942;
                    }
                },500,2);
                if(ctx.movement.destination().x()!=1942&&ctx.movement.destination().y()!=4967){
                    ctx.movement.step(new Tile(1942,4967));
                    Cinta=true;
                }
            }while(ctx.movement.destination().x()!=1942&&ctx.movement.destination().y()!=4967);
            System.out.println("llevar 2");
            if(Cinta==true){
                Cinta=false;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.distance(new Tile(1942,4967))<3;
                    }
                },200,50);
                cinta= ctx.objects.select().id(9100).poll();
                cinta.interact("Put-ore-on");

            }
            ctx.input.move(c);
            ctx.input.click(false);
            ctx.input.move(c.x-Random.nextInt(0,54),c.y+Random.nextInt(68,74));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=28&&ctx.movement.destination().x()==-1;//
                }
            }, 200, 70);
        }
        //boolean m=true;
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().count()==1;//
            }
        }, 200, 70);
        System.out.println("llevar 3");
        int co=0;
        while(ctx.inventory.select().count()==1){
            if(detener())break;
            if(co==0)
                ctx.input.click(true);
            else {
                ctx.input.move(c);
                ctx.input.click(false);
                ctx.input.move(c.x - Random.nextInt(0, 54), c.y + Random.nextInt(68, 74));
                ctx.input.click(true);
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count()!=1;
                }
            }, 200, 10);

        }
        System.out.println("llevar 4");
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
                }, 200, 10);
            }
        //f=true;
        }

    }


    public void camera(){
        System.out.println("Camaraaaaa");
        ctx.input.move(Random.nextInt(629, 649), Random.nextInt(18, 28));
        ctx.input.click(true);
        ctx.camera.turnTo(new Tile(1940,4961));
        ctx.camera.pitch(Random.nextInt(95,99));


    }


    public void recoger(){
        System.out.println("voy a recoger");
        System.out.println("recoger 1");
        while(ctx.inventory.select().count()==1) {
            if(detener())break;
            System.out.println("recoger 1.1");
            if(ctx.widgets.widget(229).component(0).visible())dispenser();
            if(ctx.inventory.select().count()==1) {
                System.out.println("recoger 1.2");
                dispenser();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (ctx.widgets.widget(233).component(1).visible()) {
                            dispenser();
                        }
                        return ctx.movement.distance(ctx.players.local().tile(), ctx.movement.destination())<1;
                    }
                }, 100, 80);
                break;
            }

        }

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(widget).component(14).visible();
            }
        }, 200, 10);

        if(ctx.inventory.select().count()>1){//drop si agarra algo :v
            c=new Point(Random.nextInt(647, 666), Random.nextInt(277, 293));
            ctx.input.move(c);
            ctx.input.click(false);
            ctx.input.move(c.x-Random.nextInt(0,50),c.y+Random.nextInt(39,44));
            ctx.input.click(true);
        }

        while(ctx.inventory.select().count()==1) {
            if(detener())break;
            if (ctx.widgets.widget(widget).component(14).visible()) {
                System.out.println("recoger 1.3");
                ctx.widgets.widget(widget).component(14).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(229).component(1).visible();
                    }
                }, 200, 20);
                System.out.println("recoger 2");
                ctx.input.move(Random.nextInt(700, 706), Random.nextInt(80, 85));
                ctx.movement.step(new Tile(1948, 4957));

                ctx.input.move(Random.nextInt(354, 392), Random.nextInt(266, 280));
                final Tile ti=ctx.movement.destination().tile();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.distance(ctx.players.local().tile(), ti ) < 2;
                    }
                }, 200, 80);
                if(ctx.inventory.select().count()>1)
                break;
            } else {
                dispenser();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(widget).component(14).visible();
                    }
                }, 200, 10);
                if(ctx.inventory.select().count()>1&&!ctx.widgets.widget(widget).component(14).visible()){//drop si agarra algo :v
                    c=new Point(Random.nextInt(647, 666), Random.nextInt(277, 293));
                    ctx.input.move(c);
                    ctx.input.click(false);
                    ctx.input.move(c.x-Random.nextInt(0,50),c.y+Random.nextInt(39,44));
                    ctx.input.click(true);
                }
            }
        }
    }

    public void dispenser(){
        Tile z = new Tile(1940, 4963);
        z.matrix(ctx).click();
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
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 200, 10);

            }
            Npc fore = ctx.npcs.select().id(2923).poll();
            while(ctx.inventory.select().id(995).count(true)==100000) {
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
                }, 200, 10);
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
    public void bankcode() {

        ArrayList<String> a = new ArrayList<String>();
        a.add("7");
        a.add("9");
        a.add("1");
        a.add("0");
        ctx.bank.open();
            if (ctx.widgets.widget(213).component(16).visible()) {
                String s = ctx.widgets.widget(213).component(10).text();

                for (int j = 0; j < a.size(); ) {
                    for (int i = 16; i <= 34; i = i + 2) {
                        if (ctx.widgets.widget(213).component(i).component(1).text().equals(a.get(j))) {
                            j = j + 1;
                            final String t = s;
                            ctx.widgets.widget(213).component(i).click();
                            ctx.input.move(ctx.widgets.widget(213).component(10).nextPoint());
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return !t.equals(ctx.widgets.widget(213).component(10).text());
                                }
                            }, 2000, 10);

                            s = ctx.widgets.widget(213).component(10).text();
                            if (j == a.size()) break;
                        }
                    }


                }
            }

    }
}
