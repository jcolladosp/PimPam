package jcollado.pw.pimpam.settings;

/**
 * Created by jcolladosp on 11/06/2017.
 */

public interface SettingsView {
    void setLocale(String lang);
    void onDestroy();
    void onPreStartConnection();
    void stopRefreshing();
    void showDialogDeleteAllComics();
    void showDialogsComicsDeletedSuccess();
    int getLocaleSelected();

}
