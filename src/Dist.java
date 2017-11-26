import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Dist extends Agent
{

    @Override
    protected void setup()
    {
        super.setup();
        System.out.println("Dodaje Server Agent");
        List elem = new LinkedList();
        Integer sizeX = 4;
        int tab[][] = new int[sizeX][sizeX];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeX; j++) {
                tab[i][j] = i+1;
                elem.add(i+" "+j);
            }
        }

        DFAgentDescription DFAD = new DFAgentDescription();
        DFAD.setName(getAID());
        ServiceDescription SD = new ServiceDescription();
        SD.setType("Sender");
        SD.setName("Token");
        DFAD.addServices(SD);
        try
        {
            DFService.register(this, DFAD);
        }
        catch(FIPAException fe)
        {
            fe.printStackTrace();
        }

        Dist_beh behav = new Dist_beh(tab,elem);//wywołuje action()
        addBehaviour(behav);
    }
}