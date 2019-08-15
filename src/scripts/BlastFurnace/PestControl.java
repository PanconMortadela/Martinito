package scripts.BlastFurnace;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Combat;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;


import java.awt.*;
import java.util.concurrent.Callable;

public class PestControl extends PollingScript<ClientContext> implements PaintListener {

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
    int mostrar1=0,mostrar2=0;
    int skill;
    Combat.Style style;
    int Expericiencia;

    public void determinarSkill(){
        if(ctx.combat.style()==Combat.Style.ACCURATE){
            skill= Constants.SKILLS_ATTACK;
            style=Combat.Style.ACCURATE;
        }else if(ctx.combat.style()==Combat.Style.AGGRESSIVE){
            skill= Constants.SKILLS_STRENGTH;
            style=Combat.Style.AGGRESSIVE;
        }else if(ctx.combat.style()==Combat.Style.DEFENSIVE){
            skill= Constants.SKILLS_DEFENSE;
            style=Combat.Style.DEFENSIVE;
        }
    }

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
        g.drawString(" Atack: " + ctx.skills.level(skill) + " RealAttack: " +ctx.skills.realLevel(skill), 11, 87);
        g.drawString("Exp/tiempo: " + mostrar1,10,106);
        g.drawString( "Tiempo:" + mostrar2,10,126);

    }


    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        determinarSkill();
        Expericiencia=ctx.skills.experience(skill);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return s>0;
            }
        }, 200, 10);

    }
    int tabla=14315;
    int time;
    Tile entrada=new Tile(2657,2639,0);
    Tile llegada=new Tile(2661,2639,0);
    @Override
    public void poll(){
        if(ctx.players.local().tile().equals(entrada)){
            if(ctx.objects.select().id(tabla).poll().inViewport()) {
                ctx.objects.select().id(tabla).poll().interact("Cross");
                time= Random.nextInt(200,400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().tile().equals(llegada);
                    }
                }, time, 10);
            }
        }
        if(ctx.players.local().tile().x()>10000){

        }
    }
}