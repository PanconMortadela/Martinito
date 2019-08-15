package BlueDragon.Estados;

import BlueDragon.Constantes;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import scripts.Utilitys.Areas;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class Atacar extends ClientAccessor {
    public Atacar(ClientContext ctx) {
        super(ctx);
    }

    int[] items={536,1751,1213,1161,561,207,1617,2366,985,987,1247,1249,13511,13510};
    String[] itemsName={
            "Dragon bones","Blue dragonhide",
            "Rune dagger","Adamant full helm",
            "Nature rune","Grimy ranarr weed",
            "Uncut diamond","Shield left half",
            "Tooth half of key","Loop half of key",
            "Rune spear","Dragon spear","Ensouled dragon head","Ensouled dragon head"};

    int[] dragones={265};
    //int[] dragones={265,268,267};
    Tile[] safe={
            new Tile(2900,9809,0),
            new Tile(2902,9807,0),
            new Tile(2903,9807,0)

    };
    int[] rangePotion={2444,173,169,171};
    Npc dragon;
    int posicion;
    boolean hop;
    int flechas=886;
    boolean stay1=true;

    public Constantes.Estados Atacar(){
        if(stay1){
            stay();
            stay1=false;
        }

        if(ctx.camera.yaw()<140||ctx.camera.yaw()> 270){
            ctx.camera.angle(angulo);
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.camera.yaw()>=140||ctx.camera.yaw()<= 160;
                }
            }, time, 40);
        }
        if(detener())System.out.println("Me detuve");
        switch (estadodragon){
            case Atacar:
                System.out.println("Estado Atacar: Atacar()");
                atacar();
                break;
            case Buscar:
                System.out.println("Estado Atacar: BuscarDragon()");
                BuscarDragon();
                break;
            case Esperar:
                break;
            case Recoger:
                System.out.println("Estado Atacar: Recoger()");
                recoger();
                break;
            case Ubicarme:
                break;
        }

        if(ctx.inventory.select().count()==28&&ctx.inventory.select().id(flechas).count()==0){
            return Constantes.Estados.Teleport;
        }else{
            return Constantes.Estados.Atacar;
        }
    }

    public enum Estadodragon{
        Buscar,Atacar,Ubicarme,Recoger,Esperar,hop
    }

    Estadodragon estadodragon=Estadodragon.Buscar;

    Tile Centro=new Tile(2901,9805,0);
    Areas areas=new Areas();
    int angulo= Random.nextInt(140,180);
    int time;
    Tile safe268;

    public void BuscarDragon(){
        if(!safe[0].equals(ctx.players.local().tile())){
            ctx.movement.step(safe[0]);
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return areas.area2(safe[0]).contains(ctx.players.local());
                }
            }, time, 40);
        }
        if(huyo==true){
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !miVida();
                }
            }, time, 40);
        }
        if(dragon==null) {
            for (int i = 0; i < dragones.length; i++) {
                Npc prueba;
                prueba = ctx.npcs.select().id(dragones[i]).poll();
                System.out.println("Entre en for: " + i);
                if (prueba.inViewport()) {
                    System.out.println("No esta vaciado: " + i);
                    if (!prueba.healthBarVisible()) {
                        System.out.println("Dragon: " + i + " Barra no visible");
                        dragon = prueba;
                        posicion = i;
                        estadodragon = Estadodragon.Atacar;
                        break;
                    }
                }

            }
        }
        if(dragon==null){
            if(ctx.players.select().size()>2){
                hop();
            }

        }
    }
    boolean huyo=false;
    Tile safeaux,safeExtra=new Tile(2903,9806,0);
    public void atacar(){
        if(ctx.skills.level(Constants.SKILLS_RANGE)==ctx.skills.realLevel(Constants.SKILLS_RANGE)){
            final Item pocion=ctx.inventory.select().id(rangePotion).poll();
            pocion.interact("Drink");
            time= Random.nextInt(200,300);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(pocion.id()).count()==0;
                }
            }, time, 40);
        }
        dragon=ctx.npcs.select().id(dragones[posicion]).poll();
        posicionDragon();
        if((!dragon.healthBarVisible())||huyo==true){
            huyo=false;
            if(!safeaux.equals(ctx.players.local().tile())){
                ctx.movement.step(safeaux);
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return areas.area2(safeaux).contains(ctx.players.local());
                    }
                }, time, 40);
            }
            dragon.interact(false,"Attack");
            if(ctx.game.crosshair()== Game.Crosshair.ACTION){
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()!=-1;
                    }
                }, time, 40);
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation()==-1;
                    }
                }, time, 40);
            }
            if(!safeaux.equals(ctx.players.local().tile())){
                ctx.movement.step(safeaux);
                time= Random.nextInt(200,300);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return areas.area2(safeaux).contains(ctx.players.local())||wiged()||BabyDragon()||BlueDragon()||Atacarlocal()||dragonRes();
                    }
                }, time, 40);
                dragon.interact(false, "Attack");
                if (ctx.game.crosshair() == Game.Crosshair.ACTION) {
                    System.out.println("Espero que muera");
                    time = Random.nextInt(120, 180);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return (!ctx.npcs.select().id(dragon.id()).poll().inViewport())||wiged()||BabyDragon()||BlueDragon()||Atacarlocal()||dragonRes();
                        }
                    }, 1000, time);
                    System.out.println("Ya murio");
                    if(miVida()||dragonRes()){
                        estadodragon = Estadodragon.Buscar;
                        huyo=true;
                        dragon = null;
                    }else {
                        dragon = null;
                        estadodragon = Estadodragon.Recoger;
                    }

                }
            }else{
                System.out.println("Espero que muera");
                time = Random.nextInt(120, 180);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (!ctx.npcs.select().id(dragon.id()).poll().inViewport())||wiged()||BabyDragon()||BlueDragon()||Atacarlocal()||dragonRes();
                    }
                }, 1000, time);
                System.out.println("Ya murio");
                if(miVida()||dragonRes()){
                    estadodragon = Estadodragon.Buscar;
                    huyo=true;
                    dragon = null;
                }else {
                    dragon = null;
                    estadodragon = Estadodragon.Recoger;
                }
            }



        }
    }

    public boolean safe(){
        if(!safeaux.equals(ctx.players.local().tile())){
            return true;
        }else{
            return false;
        }
    }

    public void posicionDragon(){ //No sirve
        if(dragon.tile().matrix(ctx).bounds().getBounds().x>2905&&miVida()){
            System.out.println("El safespot es: SafeExtra");
            safeaux=safeExtra;
        }else{
            System.out.println("El safespot es: SafePosicion");
            safeaux=safe[posicion];
        }
    }

    public boolean wiged(){
        if(ctx.widgets.widget(233).component(1).visible()){
            return true;
        }else
            return false;
    }

    public boolean Atacarlocal(){
        if(!dragon.interacting().name().equals(ctx.players.local().name())){
            return true;
        }else{
            return false;
        }
    }
    public boolean BlueDragon(){

        if(!ctx.players.local().interacting().name().equals("Blue dragon")){
            return true;
        }else{
            return false;
        }
    }

    public boolean miVida(){
        if(ctx.players.local().healthBarVisible()){
            return true;
        }else{
            return false;
        }
    }

    public boolean dragonRes(){
        if(ctx.npcs.select().id(dragones[0]).poll().inViewport()&&(!ctx.npcs.select().id(dragones[0]).poll().healthBarVisible())){
            return true;
        }else{
            return false;
        }
    }

    public boolean BabyDragon(){
        if(ctx.players.local().interacting().name().equals("Baby dragon")){
            return true;
        }else{
            return false;
        }
    }

    public boolean detener(){
        if(ctx.controller.isStopping()){
            System.out.println("me detendre");
            ctx.controller.stop();
            return true;

        }else if(!ctx.game.loggedIn()){
            System.out.println("me detendre");
            ctx.controller.stop();

            return true;
        }else{
            return false;
        }
    }
    public void Jugadores(){
        if(ctx.players.select().size()>4){
            estadodragon=Estadodragon.hop;
        }
    }
    GroundItem item;
    int cantidad;
    int[] drop={flechas,995,2444,173,169,171};
    public void recoger(){
        System.out.println("entre en recoger");
        if(ctx.inventory.select().id(drop).count()>0){
            drop();
        }
        time = Random.nextInt(200, 500);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.groundItems.select(15).id(items[0]).nearest().poll().inViewport();
            }
        }, time, 10);
        for(int i=0;i<items.length;i++) {
            System.out.println("entre en for recoger: " + i);
            if(ctx.inventory.select().count()==28){
                break;
            }
            item = ctx.groundItems.select(11).id(items[i]).poll();
            if(item.inViewport()) {
                item = ctx.groundItems.select(11).id(items[i]).poll();
                if(ctx.inventory.select().id(drop).count()>0){
                    drop();
                }
                System.out.println("Hay Item para recoger");
                item = ctx.groundItems.select(15).id(items[i]).nearest().poll();
                cantidad = ctx.inventory.select().id(items[i]).count();
                item.interact(false,"Take",itemsName[i]);
                time = Random.nextInt(200, 400);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (!item.valid()) || ctx.inventory.select().id(item.id()).count() > cantidad;
                    }
                }, time, 50);
            }
        }
        if(ctx.groundItems.select(15).id(items).nearest().poll().inViewport()){
            estadodragon = Estadodragon.Recoger;
        }else {
            estadodragon = Estadodragon.Buscar;
        }


    }

    public void drop(){
        if(ctx.inventory.select().count()==28){
            final int x=ctx.inventory.select().count();
            ctx.inventory.select().id(drop).poll().interact("Drop");
            time = Random.nextInt(200, 400);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return  ctx.inventory.select().count()==x-1;
                }
            }, time, 50);

        }
    }
    
    int WolrdStay;
    public int iteracion_de_tiempo(){

        int time=Random.nextInt(5,4);
        return time;
    }
    public int segundos(){
        int segundos=Random.nextInt(800,1200);
        return segundos;
    }
    World mundito;
    String est="";
    public void stay(){
        est="Voy a ver en que world estoy";
        String x= ctx.widgets.widget(429).component(3).text();
        String[] parts = x.split(" ");
        try {
            WolrdStay = Integer.parseInt(parts[4]);
        }catch (Exception e){
            System.out.println("No se pudo cargar el world");
        }
        System.out.println("El World en el que estoy es: " + WolrdStay);

    }
    public void hop(){  //Este hop Funciona Bien

        ctx.worlds.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(69).component(12).component(0).visible();
            }
        }, segundos(), iteracion_de_tiempo());
        boolean hay_mundo=false;
        stay();
        Iterator<World> mundo= ctx.worlds.select().types(World.Type.MEMBERS).joinable().iterator();
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


}