package jcollado.pw.pimpam.accountDetails;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import jcollado.pw.pimpam.utils.FirebaseModule;

/**
 * Created by jcolladosp on 12/06/2017.
 */

public class AccountDetailsInteractorImpl implements AccountDetailsInteractor {
    private FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();;

    public void AddUserInfoRequest(String name, String url, final AccountDetailsInteractor.OnAccountDetailsListener listener){
        Uri picUri = Uri.parse(url);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(picUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            listener.onUserInfoUpdateSuccess();
                        }
                    }
                });
    }



    @Override
    public void uploadImageRequest(String url, String name,AccountDetailsInteractor.OnAccountDetailsListener listener) {
        FirebaseModule.getInstance().uploadImageToFirebase(url,name,listener);
    }
}
