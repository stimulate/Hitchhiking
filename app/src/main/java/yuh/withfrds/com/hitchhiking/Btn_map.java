package yuh.withfrds.com.hitchhiking;

/**
 * Created by a_yu_ on 2018/5/24.
 */
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Btn_map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Btn_map extends Fragment {
    Maps_Activity ma = new Maps_Activity();

    private static final Pair<String,String> Loc = new Pair("Latitude","Longitude");

    private String p1, p2;

    private OnFragmentInteractionListener mListener;

    public Btn_map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param p1 Parameter 1.
     * @param p2 Parameter 2.
     * @return A new instance of fragment buttons.
     */
    // TODO: Rename and change types and number of parameters
    public static Btn_map newInstance(String p1, String p2) {
        Btn_map fragment = new Btn_map();
        Bundle args = new Bundle();
        args.putString(Loc.first, p1);
        args.putString(Loc.second,p2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            p1 = getArguments().getString(Loc.first);
            p2 = getArguments().getString(Loc.second);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btn = getActivity().findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListener!= null)
                {BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromResource(R.drawable.unitec);
                            MarkerOptions itemMarker = new MarkerOptions()
                                   .position(new LatLng(-36.881305, 174.706192))
                                    .icon(itemBitmap);}
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_btn, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
