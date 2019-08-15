package BlueDragon;

import BlueDragon.Estados.Atacar;
import BlueDragon.Estados.Banco;
import BlueDragon.Estados.Caminar;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;

import java.awt.*;
@Script.Manifest(name="00Bluedragon", description = "Money making",properties = "autor: Pos yo!")
public class BlueDragon extends PollingScript<ClientContext> implements PaintListener {
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
    int WolrdStay,estaba=0;
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
        g.drawString("AgilitiLv:" + ctx.skills.level(Constants.SKILLS_AGILITY) +" Mundo: "+WolrdStay +" - " + estaba , 11, 88);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 108);

    }

    Constantes.Estados estados= Constantes.Estados.nill;
    Banco banco=new Banco(ctx);
    Caminar caminar=new Caminar(ctx);
    Atacar atacar=new Atacar(ctx);

    @Override
    public void start() {
        initialTime = System.currentTimeMillis();
    }
    @Override
    public void poll() {
        if(detener())System.out.println("Me detuve");

        switch (estados){
            case Teleport:
                estados=banco.Teleport();
                break;
            case bancoDepositar:
                estados=banco.depositar();
                break;
            case bacoRetirar:
                estados=banco.retirar();
                break;
            case caminarBanco:
                estados=caminar.ir("Bank");
                break;
            case caminarPared:
                estados=caminar.ir("Pared");
                break;
            case caminarEscalera:
                estados=caminar.ir("Escaleras");
                break;
            case entrarDragon:
                estados=caminar.ir("Entrar");
                break;
            case Atacar:
                System.out.println("Estado: Atacar su puta madre");
                estados= atacar.Atacar();
                break;
            case nill:
                control();
                break;
        }


    }












    int flechas=886;
    public void control(){
        if(ctx.inventory.select().count()==28&&(ctx.players.local().tile().x()>2943&&ctx.players.local().tile().x()<2971)){
            estados=Constantes.Estados.caminarBanco;
            if(ctx.bank.inViewport()){
                estados=Constantes.Estados.bancoDepositar;
            }

        }else if(ctx.inventory.select().count()==28&&(ctx.players.local().tile().x()>2894&&ctx.players.local().tile().y()>9700)){
            estados=Constantes.Estados.Teleport;
        }else if((ctx.players.local().tile().x()>2881&&ctx.players.local().tile().x()<2887)&&ctx.players.local().tile().y()>9700&&ctx.inventory.select().count()==(2+ctx.inventory.select().id(flechas).count())){
            estados=Constantes.Estados.entrarDragon;
        }else if(ctx.players.local().tile().x()>2935&&ctx.players.local().tile().x()<2971&&ctx.inventory.select().count()==(2+ctx.inventory.select().id(flechas).count())){
                estados=Constantes.Estados.caminarPared;
        }else if(ctx.players.local().tile().x()>2882&&ctx.players.local().tile().x()<2935&&ctx.inventory.select().count()==(2+ctx.inventory.select().id(flechas).count())){
            estados=Constantes.Estados.caminarEscalera;
        }else if(ctx.inventory.select().count()==(1+ctx.inventory.select().id(flechas).count())) {
            estados = Constantes.Estados.bacoRetirar;
        }else if(ctx.players.local().tile().x()>2887&&ctx.players.local().tile().y()>9700){
            estados=Constantes.Estados.Atacar;
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