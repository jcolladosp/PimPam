package jcollado.pw.pimpam.settings;

/**
 * Created by jcolladosp on 11/06/2017.
 */

public interface SettingsInteractor {
    interface OnSettingsFinishedListener {
       void onComicsDeleted();
        void onUserInfoUpdateSucess();

    }
    void sendRequestDeleteAllComics(OnSettingsFinishedListener listener);
    void sendRequestAddUserInfo(String name,String url,OnSettingsFinishedListener listener);
}
