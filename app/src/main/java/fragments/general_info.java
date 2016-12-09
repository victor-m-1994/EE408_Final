package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.os.Bundle;

import com.example.victor.final_project_ee408.R;

import API.SimulationManager;

import static android.R.attr.fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link general_info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link general_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class general_info extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public general_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment general_info.
     */
    // TODO: Rename and change types and number of parameters
    public static general_info newInstance(String param1, String param2) {
        general_info fragment = new general_info();
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
        return inflater.inflate(R.layout.fragment_general_info, container, false);
    }

    private ListView list;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        list = (ListView)view.findViewById(R.id.list);//Creates list
        String[] values = new String[] { "Current Setup: ",
                "Theta Hat: ",
                "Theta: ",
                "Number of Sensors: ",
                "Power: ",
                "N Variance: ",
                "V Variance: ",
                "K Value: ",
                "Channels: ",
                "Alphas: "
        };

        values[0] += SimulationManager.getSimulationSetup().getObservation();
        values[1] += SimulationManager.getLastSimulation().getThetaHat().toFormattedString();
        values[2] += SimulationManager.getSimulationSetup().getTheta();
        values[3] += SimulationManager.getSimulationSetup().getSensorCount();
        values[4] += SimulationManager.getSimulationSetup().getPower();
        values[5] += SimulationManager.getSimulationSetup().getVarianceN();
        values[6] += SimulationManager.getSimulationSetup().getVarianceV();
        values[7] += SimulationManager.getSimulationSetup().getK();
        //values[8] += SimulationManager.getSimulationSetup().isRician();
        if(SimulationManager.getSimulationSetup().isRician()){
            values[8] += "Rician";
        }
        else{
            values[8] += "AWGN";
        }
        if(SimulationManager.getSimulationSetup().isUniform()){
            values[9] += "Uniform";
        }
        else{
            values[9] += "Optimum";
        }
        //values[9] += SimulationManager.getSimulationSetup().isUniform();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        list.setAdapter(adapter);

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
       /* if (context instanceof OnFragmentInteractionListener) {
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
