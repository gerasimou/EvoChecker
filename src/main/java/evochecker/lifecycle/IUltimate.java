package evochecker.lifecycle;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public interface IUltimate {

    void setTargetModelId(String id);

    void setInternalParameters(HashMap <String, String> internalParameters);

    void resetResults();

    void execute();

    void setVerificationProperty(String property);

    HashMap <String, String> getResults();

    void updateSynthesisProgress(int evaluations);

}
