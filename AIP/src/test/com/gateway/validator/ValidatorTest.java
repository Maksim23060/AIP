package test.com.gateway.validator;

import main.com.gateway.validator.RequestValidator;
import main.com.gateway.validator.ServiceInstanceValidator;
import org.testng.annotations.Test;
import main.com.gateway.exception.InvalidDataException;
import main.com.gateway.model.Request;
import main.com.gateway.model.ServiceInstance;

import static org.testng.Assert.*;

public class ValidatorTest {

    @Test
    public void testServiceInstanceValidator_Valid() {
        ServiceInstance instance = new ServiceInstance("s1", "http://localhost", 5, true);
        ServiceInstanceValidator.validate(instance); // не должно выбросить исключение
    }

    @Test(expectedExceptions = InvalidDataException.class)
    public void testServiceInstanceValidator_InvalidUrl() {
        ServiceInstance instance = new ServiceInstance("s1", "invalid", 5, true);
        ServiceInstanceValidator.validate(instance);
    }

    @Test(expectedExceptions = InvalidDataException.class)
    public void testServiceInstanceValidator_NegativeWeight() {
        ServiceInstance instance = new ServiceInstance("s1", "http://localhost", -1, true);
        ServiceInstanceValidator.validate(instance);
    }

    @Test
    public void testRequestValidator_Valid() {
        Request req = new Request("/api", "GET", null, null, "1.1.1.1");
        RequestValidator.validate(req);
    }

    @Test(expectedExceptions = InvalidDataException.class)
    public void testRequestValidator_InvalidMethod() {
        Request req = new Request("/api", "DELETE", null, null, "1.1.1.1");
        RequestValidator.validate(req);
    }
}