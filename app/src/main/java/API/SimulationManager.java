package API;

/**
 * This is a manager class made of static functions. It is a singleton 
 * that gets its own instance to prevent the need of calling getInstance 
 * every time. This file should have been provided to you with the accompanying
 * documentation.
 * 
 * @author ZackRauen
 * @version 1.2
 * @see Simulation
 * @see SimulationSetup
 *
 */
public class SimulationManager implements SetupListener {

    final static String KEY_OBSERVATION = "pref_observation_name";
    final static String KEY_SENSOR_COUNT = "pref_sensor_count";
    final static String KEY_THETA = "pref_theta";
    final static String KEY_POWER = "pref_power";
    final static String KEY_N = "pref_variance_n";
    final static String KEY_V = "pref_variance_v";
    final static String KEY_K = "pref_k_value";
    final static String KEY_RICIAN = "pref_rician";
    final static String KEY_UNIFORM = "pref_uniform";

    private SetupListener listener;

    public static int currentSort = 0;
    public static boolean desc = false;

    public static SimulationManager instance = null;
    public static SimulationManager getInstance() {
        if (instance == null)
            instance = new SimulationManager();
        return instance;
    }
    private SimulationSetup simulationSetup = new SimulationSetup();
    private Simulation lastSimulation = new Simulation(simulationSetup);

    private SimulationManager() {
    	this.simulationSetup.addListener(this);
    }

    public static void setSetupListener(SetupListener l) {
        SimulationManager.getInstance().listener = l;}
    public static SetupListener getListener() {return SimulationManager.getInstance().listener;}
    public static Simulation getLastSimulation() {
        return SimulationManager.getInstance().lastSimulation;
    }

    public static SimulationSetup getSimulationSetup() {
        return SimulationManager.getInstance().simulationSetup;
    }

    public static void runSimulation() {
        SimulationManager.getInstance().lastSimulation = new Simulation(SimulationManager.getSimulationSetup());
        SimulationManager.sortSensors();
    }

// 	  UNCOMMENT FOR ANDROID
//    public static void updateSimulation(SharedPreferences prefs) {
//        SimulationManager.getSimulationSetup().setObservation(prefs.getString(KEY_OBSERVATION, SimulationSetup.DEFAULT_OBSERVATION));
//        SimulationManager.getSimulationSetup().setSensorCount(prefs.getInt(KEY_SENSOR_COUNT, SimulationSetup.DEFAULT_SENSOR_COUNT));
//
//        SimulationManager.getSimulationSetup().setTheta(prefs.getFloat(KEY_THETA, (float) SimulationSetup.DEFAULT_THETA));
//        SimulationManager.getSimulationSetup().setPower(prefs.getFloat(KEY_POWER, (float) SimulationSetup.DEFAULT_POWER));
//        SimulationManager.getSimulationSetup().setVarianceN(prefs.getFloat(KEY_N, (float) SimulationSetup.DEFAULT_N));
//        SimulationManager.getSimulationSetup().setVarianceV(prefs.getFloat(KEY_V, (float) SimulationSetup.DEFAULT_V));
//        SimulationManager.getSimulationSetup().setK(prefs.getFloat(KEY_K, (float) SimulationSetup.DEFAULT_K));
//
//        Boolean isRician = prefs.getBoolean(KEY_RICIAN, SimulationSetup.DEFAULT_RICIAN);
//        Boolean isUniform = !isRician && prefs.getBoolean(KEY_UNIFORM, SimulationSetup.DEFAULT_UNIFORM);
//        SimulationManager.getSimulationSetup().setRician(isRician);
//        SimulationManager.getSimulationSetup().setUniform(isUniform);
//
//        SimulationManager.runSimulation();
//        SimulationManager.getListener().setupChanged();
//    }

    public static void sortSensors() {
        switch (SimulationManager.currentSort) {
            case 0: SimulationManager.sortSensorsByNumber(desc);
                break;
            case 1: SimulationManager.sortSensorsByAlpha(desc);
                break;
            case 2: SimulationManager.sortSensorsByH(desc);
                break;
            case 3: SimulationManager.sortSensorsByN(desc);
                break;
            default:
        }
    }

    /**
     * @param field 0 for sensor number, 1 for alpha, 2 for H, 3 for N
     */
    public static void sortSensors(int field) {
        Boolean desc = SimulationManager.currentSort == field ? !SimulationManager.desc : false;
        switch (field) {
            case 0: SimulationManager.sortSensorsByNumber(desc);
                break;
            case 1: SimulationManager.sortSensorsByAlpha(desc);
                break;
            case 2: SimulationManager.sortSensorsByH(desc);
                break;
            case 3: SimulationManager.sortSensorsByN(desc);
                break;
            default:
        }
        SimulationManager.desc = desc;
        SimulationManager.currentSort = field;
    }

    public static void sortSensorsByNumber(final boolean descending) {
        SimulationManager.getLastSimulation().sortSensorsByNumber(descending);
    }

    public static void sortSensorsByAlpha(final boolean descending) {
        SimulationManager.getLastSimulation().sortSensorsByAlpha(descending);
    }

    public static void sortSensorsByH(final boolean descending) {
        SimulationManager.getLastSimulation().sortSensorsByH(descending);
    }

    public static void sortSensorsByN(final boolean descending) {
        SimulationManager.getLastSimulation().sortSensorsByN(descending);
    }

    @Override
    public void setupChanged() {
        SimulationManager.runSimulation();
        if (this.listener != null)
            this.listener.setupChanged();
    }


}
