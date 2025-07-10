package evochecker.auxiliary;

import ultimate.Ultimate;

public class UltimateInstancer {
 
    private static Ultimate ultimateInstance;

    public static void createInstance(String modelFilename){
        ultimateInstance = new Ultimate();
        ultimateInstance.loadProjectFromFile(modelFilename);
    }

    public static Ultimate getInstance(){
        return ultimateInstance;
    }

}
