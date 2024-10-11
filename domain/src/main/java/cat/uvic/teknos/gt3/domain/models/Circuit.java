package cat.uvic.teknos.gt3.domain.models;

import java.util.Set;

public interface Circuit {
    int getId();
    void setId(int id);

    String getCircuitName();
    void setCircuitName(String circuitName);

    String getCountry();
    void setCountry(String country);

    double getLengthKm();
    void setLengthKm(double lengthKm);

    Set<Race> getRaces();
    void setRaces(Race race);
}
