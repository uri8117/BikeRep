package cat.uvic.teknos.gt3.clients.console.utils;

import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.domain.models.Circuit;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestClientImplTest {
    @Test
    void getTest() {
        var restClient = new RestClientImpl("localhost", 8080);
        try {
            Circuit course = restClient.get("circuits/1", CircuitDto.class);

            assertNotNull(course);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllTest() {
        var restClient = new RestClientImpl("localhost", 8080);
        try {
            Circuit[] circuits = restClient.getAll("circuits", CircuitDto[].class);

            assertNotNull(circuits);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void postTest() {
        var restClient = new RestClientImpl("localhost", 8080);
        try {
            var circuit = new CircuitDto();
            circuit.setCircuitName("Test");
            circuit.setCountry("Italy");
            circuit.setLengthKm(10.18);

            restClient.post("courses", Mappers.get().writeValueAsString(circuit));

        } catch (RequestException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
