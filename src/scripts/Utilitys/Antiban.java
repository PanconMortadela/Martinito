package scripts.Utilitys;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Martinito on 17/05/2018.
 */
public class Antiban extends ClientAccessor{

    public Antiban(ClientContext ctx) {
        super(ctx);
    }
    Areas areas=new Areas();

    int h,m;
    public void start(){
        h= 2;//Random.nextInt(0,10);
        m=Random.nextInt(0,10);
    }
    int time;
    public void camera(final int angle,final int pitch){
        System.out.println("Camaraaaaa");
        //ctx.input.move(areas.punto(ctx.widgets.widget(161).component(24).centerPoint()));
        //ctx.input.click(true);
        time=Random.nextInt(500,800);
        ctx.camera.angle(Random.nextInt(angle,angle+10));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.yaw()>=angle&&ctx.camera.yaw()<angle+30;
            }
        }, time, 10);
        ctx.camera.pitch(Random.nextInt(pitch-4,pitch));
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.camera.pitch()>=95&&ctx.camera.pitch()<=100;
            }
        }, time, 10);

    }
    public boolean stop(int H, int M){
        return h==H&&M>=m;
    }


}

