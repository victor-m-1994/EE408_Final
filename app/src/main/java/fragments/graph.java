package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.final_project_ee408.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import API.SimulationManager;

import static android.R.attr.fragment;
import static com.example.victor.final_project_ee408.R.id.graph;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link graph.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link graph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class graph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public graph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment graph.
     */
    // TODO: Rename and change types and number of parameters
    public static graph newInstance(String param1, String param2) {
        graph fragment = new graph();
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

    private run_multiple shared_run_multiple;

    public void passRunMultiple(run_multiple passed_run_multiple)
    {
        shared_run_multiple = passed_run_multiple;
    }

    private LineChart chart;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createChart();
    }

    public void createChart(){
        if(shared_run_multiple.thetaHats.isEmpty())
        {
            return;
        }
        LineChart chart = (LineChart)getView().findViewById(graph);

        double mean = SimulationManager.getSimulationSetup().getTheta();
        double variance = (SimulationManager.getSimulationSetup().getC()/SimulationManager.getSimulationSetup().getSensorCount());
        int maximumSamples = 1000;
        Random ran = new Random();
        double [] gaussianSamples = new double[maximumSamples];
        for (int i=0;i<maximumSamples;i++) {
            gaussianSamples[i] = ((ran.nextGaussian()));
        }

        double [] distrVals = new double[maximumSamples];
        double [] samples = new double[maximumSamples];
        double [] distributedThetaHat = new double[shared_run_multiple.thetaHats.size()];
        for (int i=0;   i<maximumSamples;    i++) {
            if (i<maximumSamples/2)
                samples[i] = (mean-(Math.sqrt(variance)*gaussianSamples[i]));
            else
                samples[i] = (mean+(Math.sqrt(variance)*gaussianSamples[i]));

            // This is the renormalized Gaussian formula, specific for this application, reuse for plotting  thetahat
            distrVals[i] = (Math.pow(Math.exp(-(((samples[i] - mean) * (samples[i] - mean)) / ((2 * variance)))), 1 / (Math.sqrt(variance) * Math.sqrt(2 * Math.PI))));
        }

        Iterator it = shared_run_multiple.thetaHats.iterator();
        Float thisThetaHat;
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>(); //for multiple plotting?
        List<Entry> thetaHat = new ArrayList<Entry>();
        while(it.hasNext()){
            thisThetaHat = (Float)it.next();
            thetaHat.add(new Entry((float) (thisThetaHat),(float) (Math.pow(Math.exp(-(((thisThetaHat - mean) * (thisThetaHat - mean)) / ((2 * variance)))), 1 / (Math.sqrt(variance) * Math.sqrt(2 * Math.PI))))));
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (int i=0;i<maximumSamples;i++) {
            entries.add(new Entry((float) (0+samples[i]),(float) distrVals[i]));
        }

        Collections.sort(thetaHat, new EntryXComparator());
        LineDataSet thetaHatData = new LineDataSet(thetaHat,"thetaHat");
        thetaHatData.setLineWidth(0);
        thetaHatData.setFormLineWidth(0);
        thetaHatData.setCircleColors(ColorTemplate.rgb("000000"));
        thetaHatData.setDrawCircleHole(false);
        dataSets.add(thetaHatData);

        Collections.sort(entries, new EntryXComparator());
        LineDataSet distributionData = new LineDataSet(entries, "Default Distribution"); // add entries to dataset
        distributionData.setDrawCircles(false); //This makes sure the circles are not drawn
        distributionData.setLineWidth(2);
        dataSets.add(distributionData);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.invalidate();
    }
    public void addEstimated(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
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