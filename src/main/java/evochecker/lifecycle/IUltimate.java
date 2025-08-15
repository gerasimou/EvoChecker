package evochecker.lifecycle;
import java.util.HashMap;
import java.util.List;

public interface IUltimate {

    void setTargetModelId(String id);

    void setInternalParameters(HashMap <String, String> internalParameters);

    void generateModelInstances();

    void resetResults();

    void execute();

    void setVerificationProperty(String property);

    HashMap <String, String> getResults();

}
