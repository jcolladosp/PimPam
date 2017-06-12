package jcollado.pw.pimpam.settings;

/**
 * Created by jcolladosp on 11/06/2017.
 */

public interface SettingsPresenter {
    void onDestroy();
    void deleteAllComics();
    void setLocaleSelection(int idx);
    void addUserInfo(String name,String url);
}
