package scripts.Woodcutting;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import org.powerbot.script.*;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;
import scripts.Utilitys.Areas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="000Woodcutting", description = "Money making",properties = "autor: Pos yo!")
public class Woodcutting extends PollingScript<ClientContext> implements PaintListener {

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

    int WoodID=Integer.parseInt(JOptionPane.showInputDialog(null,"Seleccione roca","Rock mine",JOptionPane.QUESTION_MESSAGE,null,new Object[] {"1278","10820","10819",""},"1278").toString());
    int ItemID=Integer.parseInt(JOptionPane.showInputDialog(null,"Seleccione roca","Rock mine",JOptionPane.QUESTION_MESSAGE,null,new Object[] {"1511","1521","1519",""},"1278").toString());
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
        g.fillRect(7, 54, 220, 100);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 100);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("Level WoodCut:" +ctx.skills.level(Constants.SKILLS_WOODCUTTING), 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Profit: " + value*count, 12, 107);
        g.drawString("MaterialC: " , 12, 127);
    }

    @Override
    public void start() {
        initialTime=System.currentTimeMillis();
    }
    int time;
    boolean Drop;
    @Override
    public void poll() {

        count=ctx.inventory.select().count();

        if(count==28||Drop==true){
            System.out.println("Entre en drop");
            Drop=true;
            Drop();
        }else if(ctx.inventory.select().count()>=0&&ctx.inventory.select().count()<=27){
            System.out.println("Entre en Cortar");
            Cortar();
        }


    }

    private void Drop() {
        if(ctx.game.tab()!=Game.Tab.INVENTORY){
            ctx.game.tab(Game.Tab.INVENTORY);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.game.tab().equals(Game.Tab.INVENTORY);
                }
            }, 200, 20);
        }
        ctx.inventory.select().id(ItemID).poll().interact("Drop");
        time= Random.nextInt(200,400);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("Esperando que empieze a minar  Woods 1");

                return ctx.inventory.select().count()==count-1;   //esperare a que cambie a animacion de minar
            }
        }, time, 50);

        if(ctx.inventory.select().id(ItemID).count()==0){
            Drop=false;
        }

    }

    private void Cortar() {
        GameObject Woods;
        time= Random.nextInt(200,400);
        if (ctx.objects.select().id(WoodID ).nearest().poll().valid()) {//////////////////////////////////////////////////////////////////////////
            Woods=ctx.objects.select().id(WoodID ).nearest().poll();
            if( Woods.interact("Chop Down")) {

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que empieze a minar  Woods 1");

                        return ctx.players.local().animation() != -1;   //esperare a que cambie a animacion de minar
                    }
                }, time, 25);
                //Espera para dejar de minar
                time= Random.nextInt(200,1000);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Esperando que se detenga o desaparezca");

                        return ctx.players.local().animation() == -1;
                    }
                }, time, 50);
            }
        }
    }

}