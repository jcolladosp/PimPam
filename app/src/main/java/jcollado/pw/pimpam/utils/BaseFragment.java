package jcollado.pw.pimpam.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.MainActivity;

public abstract class BaseFragment extends Fragment {


    protected ProgressDialog progressBar;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = new ProgressDialog(getActivity());
        progressBar.setIndeterminate(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



    public void onPreStartConnection() {
        progressBar.setMessage(getString(R.string.loading));
        progressBar.setCancelable(false);
        progressBar.show();
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onConnectionFinished() {
        progressBar.hide();
    }

    public void comicUploaded(){}
    public void onImageUploaded(String filename){}

    public void openFragment(BaseFragment fragment,int drawerSelection,String tag){

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.container, fragment,"")
                .commit();
        MainActivity.getResult().setSelection(drawerSelection);
    }

}
