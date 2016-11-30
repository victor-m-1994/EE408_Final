package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.victor.final_project_ee408.R;

import java.util.ArrayList;

import API.Complex;
import API.Sensor;
import API.SimulationManager;

import static API.SimulationManager.getSimulationSetup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link sensors.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sensors#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sensors extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button sensor_sort, alpha_sort, h_val_sort, n_val_sort;
    private ListView lv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public sensors() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sensors.
     */
    // TODO: Rename and change types and number of parameters
    public static sensors newInstance(String param1, String param2) {
        sensors fragment = new sensors();
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
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        sensor_sort = (Button) getActivity().findViewById(R.id.sort_by_sensor_number);
        alpha_sort = (Button) getActivity().findViewById(R.id.sort_by_alpha);
        n_val_sort = (Button) getActivity().findViewById(R.id.sort_by_N_value);
        h_val_sort = (Button) getActivity().findViewById(R.id.sort_by_H_value);
        sensor_sort.setOnClickListener(sortSensors);
        alpha_sort.setOnClickListener(sortSensors);
        h_val_sort.setOnClickListener(sortSensors);
        n_val_sort.setOnClickListener(sortSensors);
        lv = (ListView)getActivity().findViewById(R.id.sensor_list);
        // DEBUG: uncomment here
//        getSimulationSetup().setObservation("Temperature");
//        getSimulationSetup().setSensorCount(20);
//        getSimulationSetup().setTheta(65);
//        getSimulationSetup().setPower(1d);
//        getSimulationSetup().setVarianceN(5d);
//        getSimulationSetup().setVarianceV(1d);
//        getSimulationSetup().setK(1.5d);
//        getSimulationSetup().setRician(true);
//        getSimulationSetup().setUniform(false);
//        SimulationManager.runSimulation();
//        setDisplay();
    }
    View.OnClickListener sortSensors = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            if (b.getId() == sensor_sort.getId()) {
//                SimulationManager.sortSensorsByAlpha(false);
//                SimulationManager.sortSensorsByH(false);
//                SimulationManager.sortSensorsByN(false);
                SimulationManager.sortSensorsByNumber(true);
                setDisplay();
            }
            else if (b.getId() == alpha_sort.getId()) {
                SimulationManager.sortSensorsByAlpha(true);
                setDisplay();
            }
            else if (b.getId() == h_val_sort.getId()) {
                SimulationManager.sortSensorsByH(true);
                setDisplay();
            }
            else if (b.getId() == n_val_sort.getId()) {
                SimulationManager.sortSensorsByN(true);
                setDisplay();
            }
        }
    };
    private void setDisplay(){
        int h = SimulationManager.getSimulationSetup().getSensorCount();
        String[] holder = new String[h];
        String holder2 = "";
        int i =0;
        String h1 = "";
        for (Sensor s : SimulationManager.getLastSimulation().getSensorList()) {
            holder2 += s.getID() + "\t\t";
            h1 = "";
            for ( int f = 0; f < 4; f++ ){
                h1 += Double.toString(s.getAlpha().getMagnitude()).charAt(f);
            }
            holder2 += h1 + "\t\t";
            h1 = "";
            for ( int f = 0; f < 4; f++ ){
                h1 += Double.toString(s.getHVal().getMagnitude()).charAt(f);
            }
            holder2 += h1 + "\t\t";
            h1 = "";
            for ( int f = 0; f < 4; f++ ){
                h1 += Double.toString(s.getNVal().getReal()).charAt(f);
            }
            holder2 += h1 + " + ";
            h1 = "";
            for ( int f = 0; f < 4; f++ ){
                h1 += Double.toString(s.getNVal().getImaginary()).charAt(f);
            }
            holder2 += h1 + "j";
            holder[i] = holder2;
            i++;
            System.out.println(holder[i-1]);
            holder2 = "";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, holder);
        // Assign adapter to ListView
        lv.setAdapter(adapter);
        System.out.println("hello world5");
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
}
