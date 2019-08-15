package scripts.Utilitys;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

/**
 * Created by putito on 01/01/2013.
 */
public class Experiencia extends ClientAccessor {
    public int Exp, ExpFaltante,levelActual,levelFinal,levelFaltantes,ExpNivel;
    int Porcentaje;
    int cantidad= Random.nextInt(50,80);
    boolean b=true;

    public Experiencia(ClientContext ctx) {
        super(ctx);
    }

    public void iniciar(int level) {
        Exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
        levelActual=ctx.skills.level(Constants.SKILLS_AGILITY);
        levelFinal=level;
        levelFaltantes=levelFinal-levelActual;
        ExpFaltante=ctx.skills.experienceAt(levelActual+1);
        ExpNivel=ctx.skills.experienceAt(levelActual);
        Porcentaje=((Exp-ExpNivel)/(ExpFaltante-ExpNivel))*100;
    }
    public int Porcentaje(){
        Exp=ctx.skills.experience(Constants.SKILLS_AGILITY);
        levelActual=ctx.skills.level(Constants.SKILLS_AGILITY);
        ExpFaltante=ctx.skills.experienceAt(levelActual+1);
        ExpNivel=ctx.skills.experienceAt(levelActual);
        Porcentaje=((Exp-ExpNivel)/(ExpFaltante-ExpNivel))*100;
        if(b==true&&Porcentaje>cantidad) {
            b = false;
            return Porcentaje;
        }else
            return 0;
    }

    public void b(){
        b=true;
    }


}
