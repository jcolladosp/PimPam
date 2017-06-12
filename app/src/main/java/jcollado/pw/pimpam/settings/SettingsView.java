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
    void setCheckedEnglishRB();
    void setCheckedSpanishRB();
    void setCheckedValencianRB();
    void profileIVfromURI(String url);
    String getNameED();
    void showDialogInfoUpdatedCorrectly();

}
