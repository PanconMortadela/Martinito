package scripts.osrs.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.osrs.Task;
import scripts.osrs.Walker;


public class Walk extends Task {

    public static final Tile pathToBank[] = {new Tile(3228, 3146, 0), new Tile(3231, 3150, 0), new Tile(3231, 3154, 0), new Tile(3234, 3157, 0), new Tile(3234, 3161, 0), new Tile(3235, 3165, 0), new Tile(3235, 3170, 0), new Tile(3237, 3174, 0), new Tile(3238, 3178, 0), new Tile(3238, 3183, 0), new Tile(3241, 3187, 0), new Tile(3244, 3190, 0), new Tile(3242, 3195, 0), new Tile(3240, 3199, 0), new Tile(3237, 3202, 0), new Tile(3236, 3206, 0), new Tile(3235, 3210, 0), new Tile(3234, 3214, 0), new Tile(3232, 3218, 0), new Tile(3228, 3218, 0), new Tile(3224, 3218, 0), new Tile(3220, 3218, 0), new Tile(3216, 3218, 0), new Tile(3215, 3214, 0), new Tile(3212, 3211, 0), new Tile(3208, 3210, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3213, 2), new Tile(3206, 3217, 2), new Tile(3209, 3220, 2)};
    private final Walker walker = new Walker(ctx);


    public Walk(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 || (ctx.inventory.select().count()<28 && pathToBank[0].distanceTo(ctx.players.local())>6);
    }

    @Override
    public void execute() {
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().count()>27){
                walker.walkPath(pathToBank);
            } else {
                walker.walkPathReverse(pathToBank);
            }
        }

    }
}
