import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;

public interface PlateauMBean {
    int getTaille();
    int getIterationSeconde();
    void supprAlea(int quantite);
    void genererAlea(int potirons, int lapins);
    int getNbreLapins();
    int	getNbrePotirons();

    int getTimerMortVivants();
    void setTimerMortVivants(int ms);
    int getPVinitLapin();
    int getPVinitPotiron();
    void setPVinitPotiron(int pv);
    void setPVinitLapin(int pv);
    int getPotironReproduction();
    void setPotironReproduction(int chances);
}
