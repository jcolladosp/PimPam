package jcollado.pw.pimpam.settings;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.FirebaseModule;

/**
 * Created by jcolladosp on 11/06/2017.
 */

public class SettingsInteractorImpl implements SettingsInteractor {
    private FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();;

    @Override
    public void sendRequestDeleteAllComics(OnSettingsFinishedListener listener) {
        Database.getInstance().deleteAllData(listener);
    }

    public void sendRequestAddUserInfo(String name, String url, final OnSettingsFinishedListener listener){
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

                                listener.onUserInfoUpdateSucess();
                        }
                    }
                });
    }
}
