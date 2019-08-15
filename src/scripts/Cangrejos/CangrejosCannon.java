package scripts.Cangrejos;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;
import scripts.Utilitys.Experiencia;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Callable;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.*;


import java.awt.*;

public class CangrejosCannon extends PollingScript<ClientContext> implements PaintListener {

    Item coal= ctx.inventory.select().id(12019).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int markcount=0;
    String vueltas;
    boolean vuelta=false;
    org.powerbot.script.rt4.GeItem barr= new org.powerbot.script.rt4.GeItem(8009);
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
        g.fillRect(7, 54, 220, 130);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 130);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString(" LevelMagic: " + ctx.skills.level(Constants.SKILLS_MAGIC), 11, 87);
        g.drawString("Exp/tiempo: " + mostrar1,10,106);
        g.drawString( "Tiempo:" + mostrar2,10,126);

    }

    int mostrar1=0,mostrar2=0;
    Tile Best=new Tile(3433,3463,0);
    Areas areas=new Areas();
    int count=0,balas=ctx.inventory.select().id(2).count(true);
    @Override
    public void poll() {

        if(!ctx.players.local().tile().equals(Best)){
            ctx.movement.step(Best);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (detener()) return true;

                    return areas.area2(Best).contains(ctx.players.local());
                }
            }, Random.nextInt(1000,2000), Random.nextInt(5,7));
        }else if(count<5){
            ctx.objects.select().id(6).poll().interact("Fire");
            balas=ctx.inventory.select().id(2).count(true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (detener()) return true;

                    return balas!=ctx.inventory.select().id(2).count(true);
                }
            }, Random.nextInt(2000,4000), Random.nextInt(5,7));
        }else if(!ctx.players.local().interacting().healthBarVisible()){
            Iterator<Npc> a= ctx.npcs.select().id(289).nearest().iterator();
            Npc aa;
            while(a.hasNext()){
                aa=a.next();
                if(!aa.interacting().tile().equals(ctx.players.local().tile())){
                    aa.interact("Attack");
                }
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
}