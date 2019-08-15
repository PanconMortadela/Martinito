import com.sun.org.apache.bcel.internal.generic.Select;
import org.powerbot.script.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.IdQuery;
import org.powerbot.script.rt4.Widgets;

public class Pupu extends Widgets{


    public Pupu(org.powerbot.script.rt4.ClientContext arg0) {
        super(arg0);
    }

    public void Pupu(){
        ctx.widgets.widget(324).component(129).interact("Tan","All");
    }
}
