package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.victor.final_project_ee408.R;

//import API.Simulation;
import API.*;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link run_multiple.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link run_multiple#newInstance} factory method to
 * create an instance of this fragment.
 */
public class run_multiple extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public run_multiple() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment run_multiple.
     */
    // TODO: Rename and change types and number of parameters
    public static run_multiple newInstance(String param1, String param2) {
        run_multiple fragment = new run_multiple();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        System.out.println("onCreate for run_multiple");
    }

    private simulation_setup sim_setup;

    public void passSimValues(simulation_setup simVals)
    {
        sim_setup = simVals;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        System.out.println("onCreateView for run_multiple");
        return inflater.inflate(R.layout.fragment_run_multiple, container, false);
    }
    private NumberPicker np;
    private Simulation run_sim;
    private SimulationSetup setup;
    public void onViewCreated(View view, Bundle savedInstanceState) {
        np = (NumberPicker)view.findViewById(R.id.np);
        np.setMinValue(0);
        np.setMaxValue(100);

        final Button runBtn = (Button) view.findViewById(R.id.runBtn);
        runBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int val = np.getValue();
                //run_sim = new Simulation(setup);
                SimulationManager.getSimulationSetup().setObservation(sim_setup.getObservationName());
                SimulationManager.getSimulationSetup().setSensorCount(sim_setup.getNumberOfSensors());
                SimulationManager.getSimulationSetup().setTheta(sim_setup.getThetaValue());
                SimulationManager.getSimulationSetup().setPower(sim_setup.getPowerValue());
                SimulationManager.getSimulationSetup().setVarianceN(sim_setup.getNVariance());
                SimulationManager.getSimulationSetup().setVarianceV(sim_setup.getVVariance());
                SimulationManager.getSimulationSetup().setK(sim_setup.getKValue());
                SimulationManager.getSimulationSetup().setRician(sim_setup.ricianChannels());
                SimulationManager.getSimulationSetup().setUniform(sim_setup.uniformAlphas());

                for(int i = 0; i<val; i++){
                    //run_sim.runSimulation();
                    SimulationManager.runSimulation();
                }
            }
        });

    }
    public int getNumberPicker(){
        return np.getValue();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        System.out.println("onButtonPressed for run_multiple");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
        System.out.println("onAttach for run_multiple");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}