package evochecker.auxiliary;

import ultimate.Ultimate;

public class UltimateInstancer {

    //TODO: safely delete

    private static Ultimate ultimateInstance;

    public static void createInstance(String modelFilename) {
        ultimateInstance = new Ultimate();
        try {
            ultimateInstance.loadProject(modelFilename);
        } catch (Exception e) {
            System.err.println("ULTIMATE could not open the model file '" + modelFilename + "'");
            e.printStackTrace();
        }
    }

    public static Ultimate getInstance() {
        return ultimateInstance;
    }

}
