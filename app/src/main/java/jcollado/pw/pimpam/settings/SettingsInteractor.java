package jcollado.pw.pimpam.settings;

import jcollado.pw.pimpam.utils.UploadImageListener;

/**
 * Created by jcolladosp on 11/06/2017.
 */

public interface SettingsInteractor {
    interface OnSettingsFinishedListener extends UploadImageListener {
       void onComicsDeleted();
       void onUserInfoUpdateSuccess();


    }
    void DeleteAllComicsRequest(OnSettingsFinishedListener listener);
    void AddUserInfoRequest(String name,String url,OnSettingsFinishedListener listener);
    void uploadImageRequest(String url, String name,OnSettingsFinishedListener listener);
}
