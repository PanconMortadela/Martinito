package scripts.Utilitys;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class Hop extends ClientAccessor {
    public Hop(ClientContext ctx) {
        super(ctx);
        stay();
    }

    public void start(int M){
        m=M;
    }
    int m;
    boolean condicion=false;
    int time,Hop_randomTime;
    public void condicion(World.Type Tipo_mundo){
        if(m==29||m==59){
            condicion=true;
            if(m==29)
                Hop_randomTime=Random.nextInt(30,35);
            else
                Hop_randomTime=Random.nextInt(0,5);
        }

        if((m==Hop_randomTime)&&condicion==true){
            hop(Tipo_mundo);
            condicion=false;
        }
    }

    public void condicion_nombre(){
        if(m==29||m==59){
            condicion=true;
            if(m==29)
                Hop_randomTime=Random.nextInt(30,35);
            else
                Hop_randomTime=Random.nextInt(0,5);
        }

        if((m==Hop_randomTime)&&condicion==true){
            if(ctx.bank.opened()){
                ctx.widgets.close(ctx.widgets.widget(12),true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                    }
                }, segundos(), iteracion_de_tiempo());
            }
            hop_Nombre();
            condicion=false;
        }
    }

    public int WolrdStay=0,estaba=0;
    public int iteracion_de_tiempo(){
        int time= Random.nextInt(5,4);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(800,1200);
        return segundos;
    }
    World mundito;
    public void hop(World.Type Tipo_mundo){  //Este hop Funciona Bien
        ctx.worlds.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(69).component(12).component(0).visible();
            }
        }, segundos(), iteracion_de_tiempo());
        boolean hay_mundo=false;
        stay();
        Iterator<World> mundo= ctx.worlds.select().types(Tipo_mundo).joinable().iterator();
        if(WolrdStay==525){             // ultimo mundo ctx.worlds.types(World.Type.MEMBERS).reverse().iterator().next().id()
            ctx.worlds.select().id(491).poll().hop();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (ctx.game.clientState() != 30) {
                        return true;
                    }
                    return false;
                }
            }, segundos(), iteracion_de_tiempo());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Voy a esperar 1");
                    if (ctx.game.clientState() == 30) {
                        return true;
                    }
                    return false;
                }
            }, segundos(), iteracion_de_tiempo());
            stay();
        }else {
            while (mundo.hasNext()) {
                mundito = mundo.next();
                System.out.println("Mundito es: " + mundito.id());
                if (mundito.id() >= WolrdStay) {
                    hay_mundo = true;
                    break;
                }
            }

            if (hay_mundo == true) {
                final World aux;
                aux = mundito;
                System.out.println("aux es: " + aux.id());
                if(aux.hop()) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (ctx.game.clientState() != 30) {
                                return true;
                            }
                            return false;
                        }
                    }, segundos(), iteracion_de_tiempo());
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Voy a esperar 1");
                            if (ctx.game.clientState() == 30) {
                                return true;
                            }
                            return false;
                        }
                    }, segundos(), iteracion_de_tiempo());
                    stay();
                }else{
                    for(int i=2;i<ctx.widgets.widget(69).component(17).componentCount();i=i+6) {
                        if (Integer.parseInt(ctx.widgets.widget(69).component(17).component(i).text()) == aux.id()) {
                            ctx.widgets.widget(69).component(17).component(i).click();
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    if (ctx.game.clientState() != 30) {
                                        return true;
                                    }
                                    return false;
                                }
                            }, segundos(), iteracion_de_tiempo());
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    System.out.println("Voy a esperar 1");
                                    if (ctx.game.clientState() == 30) {
                                        return true;
                                    }
                                    return false;
                                }
                            }, segundos(), iteracion_de_tiempo());
                        }
                    }
                    stay();
                }
            }
        }


    }
    String est="";
    int Worldin;
    public void stay(){
        est="Voy a ver en que world estoy";
        String x= ctx.widgets.widget(429).component(3).text();
        String[] parts = x.split(" ");
        try {
            estaba=WolrdStay;
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);

    }
    Component component=ctx.widgets.widget(69).component(17);
    ArrayList<World> worlds=new ArrayList<World>();
    String nombre;

    public void buscar_nombre(String s){
        System.out.println("Entre en buscar_Nombre");
        if(component.componentCount()<4){
            ctx.worlds.open();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(69).component(12).component(0).visible();
                }
            }, segundos(), iteracion_de_tiempo());
        }else {
            for (int i = 0; i < component.componentCount(); i++) {
                if (component.component(i).text().equals(s)) {
                    System.out.println("Se añadio el mundo: " + component.component(i - 3).text());
                    worlds.add(ctx.worlds.select().id(Integer.parseInt(component.component(i - 3).text())).poll());
                }
            }
        }
        nombre=s;
    }


    public boolean hope;
    boolean reinicio=false;
    public void hop_Nombre(){
        Iterator<World> iterator= worlds.iterator();
        while(iterator.hasNext()){
            if(!ctx.widgets.widget(69).component(12).component(0).visible()) {
                ctx.worlds.open();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(69).component(12).component(0).visible();
                    }
                }, segundos(), iteracion_de_tiempo());
            }
            World w=iterator.next();

            if(w.id()> WolrdStay||reinicio) {
                reinicio=false;
                System.out.println("Voy al World: " + w.id());
                w.hop();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (ctx.game.clientState() != 30) {
                            return true;
                        }
                        return false;
                    }
                }, segundos(), iteracion_de_tiempo());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar 1");
                        if (ctx.game.clientState() == 30) {
                            return true;
                        }
                        return false;
                    }
                }, segundos(), iteracion_de_tiempo());

                stay();
                if(w.id()==WolrdStay)hope=true;
                break;
            }
        }
        if(!iterator.hasNext()){
            reinicio=true;
            hop_Nombre();
        }
    }
}