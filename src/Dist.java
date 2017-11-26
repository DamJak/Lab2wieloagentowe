import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;



public class Dist extends Agent {

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Dodaje Server Agent");

        DFAgentDescription DFAD = new DFAgentDescription();
        DFAD.setName(getAID());
        ServiceDescription SD = new ServiceDescription();
        SD.setType("Sender");
        SD.setName("Token");
        DFAD.addServices(SD);
        try {
            DFService.register(this, DFAD);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        Dist_beh behav = new Dist_beh();//wywołuje action()
        addBehaviour(behav);
    }
}