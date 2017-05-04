package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.OnClick;
import jcollado.pw.pimpam.utils.BaseFragment;

import butterknife.ButterKnife;
import jcollado.pw.pimpam.R;


public class Barcode_Fragment extends BaseFragment {

    @BindView(R.id.buttonScan)
    Button buttonScan;

    //private ZXingScannerView scannerView;
    private OnFragmentInteractionListener mListener;

    public Barcode_Fragment() {
        // Required empty public constructor
    }


    public static Barcode_Fragment newInstance() {
        Barcode_Fragment fragment = new Barcode_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barcode, container, false);
        ButterKnife.bind(this, view);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        return view;
    }
    @OnClick(R.id.buttonScan)
    void onScan()
    {
        new IntentIntegrator(getActivity()).initiateScan(); // `this` is the current Activity

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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



    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
