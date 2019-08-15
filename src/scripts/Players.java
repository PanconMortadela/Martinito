package scripts;


import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Hop;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

@Script.Manifest(name="0Prayersssssssssssssssssssssssssssssssssssssssssss", description = "Ver distancia",properties = "autor: Pos yo!")
public class Players extends PollingScript<ClientContext> implements PaintListener,MouseListener{
    int x[]={3721,3720,3723,3718,3718};int y[]={5686,5681,5675,5671,5666};
    int z=28;

    Point mouse;

    ArrayList<GameObject> Listarocas =new ArrayList<GameObject>();
    ArrayList<GameObject> noSelect =new ArrayList<GameObject>();
    ArrayList<GameObject> Select=new ArrayList<GameObject>();
    Iterator<GameObject> rock=ctx.objects.select(10).id(7454,7487).nearest().iterator();
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
    float mostrar1=0,mostrar2=0;
    Tile lugar;
    int count;
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
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 68);
        g.drawString("RangeLv:" + ctx.skills.realLevel(Constants.SKILLS_RANGE), 11, 88);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 108);
        g.drawString("Exp/tiempo: " + mostrar1,10,128);
        g.drawString("Tiempo para subir:" + mostrar2,10,148);
        ctx.npcs.select().id(268).poll().tile().matrix(ctx).draw(g);
        g.setColor(color1);
        g.fillPolygon(ctx.npcs.select().id(268).poll().tile().matrix(ctx).bounds());
        g.setColor(Color.BLACK);
        g.drawPolygon(ctx.npcs.select().id(268).poll().tile().matrix(ctx).bounds());

        /*

        GameObject rocki;
        int i=0;
        while(i< noSelect.size()){
            rocki= noSelect.get(i);
            g.setColor(color1);
            g.fillPolygon(rocki.tile().matrix(ctx).bounds());
            g.setColor(Color.BLACK);
            g.drawPolygon(rocki.tile().matrix(ctx).bounds());
            i++;
        }*/
        /*
        i=0;
        while(i<Select.size()){
            rocki=Select.get(i);
            g.setColor(Color.blue);
            g.fillPolygon(rocki.tile().matrix(ctx).bounds());
            g.setColor(Color.BLACK);
            g.drawPolygon(rocki.tile().matrix(ctx).bounds());
            i++;
        }
        i=0;
        if(mouse!=null){
            while(i< Listarocas.size()) {
                rocki= Listarocas.get(i);
                if (rocki.contains(mouse)) {
                    if(noSelect.contains(rocki)){
                        Select.add(rocki);
                        noSelect.remove(rocki);
                        break;
                    }
                    if(Select.contains(rocki)){
                        noSelect.add(rocki);
                        Select.remove(rocki);
                        break;
                    }
                }
                i++;
            }
          mouse=null;
        }
        */

    }
    @Override
    public void start(){
        initialTime=System.currentTimeMillis();
        hop.buscar_nombre("-");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return false;
            }
        }, 1000, 5);
    }
    boolean fail,condicion=false;int time,Hop_randomTime;
    double Acumulada=0;
    double expH;
    double Timeleft;
    int Faltante=ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_RANGE)+1)-ctx.skills.experience(Constants.SKILLS_RANGE);
    int exp=ctx.skills.experience(Constants.SKILLS_RANGE);
    int ti=s;
    Hop hop=new Hop(ctx);

    @Override
    public void poll() {


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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()==3){
            mouse=e.getPoint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}




    /*Obtener nombre de jugadores                                           PD: para comparar String usa .equals()
*        for(int i=0;i<ctx.players.get().size();i++ ){
        Player player=ctx.players.get().get(i);
        JOptionPane.showMessageDialog(null,
                "El jugador  se llama : " +player.name());}




                for (int i = 0; i < ctx.objects.get().size(); i++) {
            player = ctx.objects.get(4).get(i);

         for(int j=0;j<rocasminar.length;j++){
            if(player.id()==rocasminar[j]) {
                JOptionPane.showMessageDialog(null,
                        "El objeto es : " + player.toString() + " Es valido?: " + player.valid());

                JOptionPane.showMessageDialog(null,
                        "El objeto Es valido?: " + player.valid());
                //while (ctx.players.local().animation() == -1) {
               //     player.interact("Mine", "Ore vein");
                //}
            }}

        }
        JOptionPane.showMessageDialog(null,
                "El objeto es : " + player.valid());
        ctx.controller.stop();
    }





            String ms="";
        FileWriter fw;
        BufferedWriter bw;
        PrintWriter pw;

        if(!ctx.widgets.widget(162).component(58).component(1).text().isEmpty()){ //cuando alguien escriba por primera vez
            ms=ctx.widgets.widget(162).component(58).component(1).text();
        }

        if(!ctx.widgets.widget(162).component(58).component(1).text().equals(ms)){ //esperar a que escriban algo distinto
            try {
                fw = new FileWriter("C:/Users/Martinito/Desktop/Runescape/prueba.txt");
                bw= new BufferedWriter(fw);
                pw = new PrintWriter(bw);
                pw.write(ms);
            }catch (Exception e){

            }
        }

        ctx.controller.stop();
        detener();

























* */

//ctx.players.get().size() Obtener cantidad de jugadores