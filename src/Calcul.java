
import jade.core.Agent;

public class Calcul extends Agent{

    @Override
    protected void setup()
    {
        super.setup();

        Calcul_beh behav = new Calcul_beh();//wywołuje action()
        addBehaviour(behav);

    }
}
