package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.victor.final_project_ee408.R;

import API.SimulationManager;
import API.SimulationSetup;

import static com.example.victor.final_project_ee408.R.layout.fragment_sensors;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link simulation_setup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link simulation_setup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class simulation_setup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public simulation_setup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment simulation_setup.
     */
    // TODO: Rename and change types and number of parameters
    public static simulation_setup newInstance(String param1, String param2) {
        simulation_setup fragment = new simulation_setup();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simulation_setup, container, false);
    }

    private EditText obv_name,num_sensors,theta,power,nVariance,vVariance,kValue;
    private CompoundButton rician,uniformAlpha;
    private Button save;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        obv_name = (EditText) view.findViewById(R.id.obv_name);
        num_sensors = (EditText) view.findViewById(R.id.sensor_number);
        theta = (EditText) view.findViewById(R.id.theta_val);
        power = (EditText) view.findViewById(R.id.power_val);
        nVariance = (EditText) view.findViewById(R.id.n_variance);
        vVariance = (EditText) view.findViewById(R.id.v_variance);
        kValue = (EditText) view.findViewById(R.id.k_value);
        rician = (CompoundButton)view.findViewById(R.id.rician_chan);
        uniformAlpha = (CompoundButton)view.findViewById(R.id.uniform_alpha);
        save = (Button)view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SimulationManager.getSimulationSetup().setObservation(getObservationName());
                SimulationManager.getSimulationSetup().setSensorCount(getNumberOfSensors());
                SimulationManager.getSimulationSetup().setTheta(getThetaValue());
                SimulationManager.getSimulationSetup().setPower(getPowerValue());
                SimulationManager.getSimulationSetup().setVarianceN(getNVariance());
                SimulationManager.getSimulationSetup().setVarianceV(getVVariance());
                SimulationManager.getSimulationSetup().setK(getKValue());
                if(ricianChannels())
                {
                    SimulationManager.getSimulationSetup().setUniform(false);
                    SimulationManager.getSimulationSetup().setOptimum(true);
                    SimulationManager.getSimulationSetup().setRician(true);
                    SimulationManager.getSimulationSetup().setAWGN(false);
                }
                else
                {
                    SimulationManager.getSimulationSetup().setRician(false);
                    if(uniformAlphas())
                    {
                        SimulationManager.getSimulationSetup().setUniform(true);
                        SimulationManager.getSimulationSetup().setOptimum(false);
                    }
                    else{
                        SimulationManager.getSimulationSetup().setUniform(false);
                        SimulationManager.getSimulationSetup().setOptimum(true);
                    }
                    SimulationManager.getSimulationSetup().setRician(false);
                    SimulationManager.getSimulationSetup().setAWGN(true);
                }
            }
        });
        rician.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ricianChannels()){
                    uniformAlpha.setEnabled(false);
                }
                else{
                    uniformAlpha.setEnabled(true);
                }
            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    //value returning functions
    public String getObservationName(){
        String observationName = obv_name.getText().toString();
        return observationName;
    }

    public int getNumberOfSensors(){
        int numSensors = Integer.parseInt(num_sensors.getText().toString());
        return numSensors;
    }

    public double getThetaValue(){
        double thetaVal = Double.parseDouble(theta.getText().toString());
        return thetaVal;
    }

    public double getPowerValue(){
        double powerVal = Double.parseDouble(power.getText().toString());
        return powerVal;
    }

    public double getNVariance(){
        double nVar = Double.parseDouble(nVariance.getText().toString());
        return nVar;
    }

    public double getVVariance(){
        double vVar = Double.parseDouble(vVariance.getText().toString());
        return vVar;
    }

    public double getKValue(){
        double k = Double.parseDouble(kValue.getText().toString());
        return k;
    }

    public boolean ricianChannels(){
        if(rician.isChecked())
        {
            return true;
        }
        else return false;
    }

    public boolean uniformAlphas(){
        if(uniformAlpha.isChecked())
        {
            return true;
        }
        else return false;
    }
}
