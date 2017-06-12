package jcollado.pw.pimpam.settings;


/**
 * Created by jcolladosp on 11/06/2017.
 */

public class SettingsPresenterImpl implements SettingsPresenter,SettingsInteractor.OnSettingsFinishedListener {

    private SettingsView settingsView;
    private SettingsInteractor settingsInteractor;

    private boolean imageChanged = false;


    public SettingsPresenterImpl(SettingsView settingsView) {
        this.settingsView = settingsView;
        this.settingsInteractor = new SettingsInteractorImpl();

    }


    @Override
    public void onDestroy() {
        settingsView = null;
    }

    @Override
    public void deleteAllComics() {
        settingsView.onPreStartConnection();
        settingsInteractor.DeleteAllComicsRequest(this);
    }

    @Override
    public void onComicsDeleted() {
        settingsView.stopRefreshing();
        settingsView.showDialogsComicsDeletedSuccess();
    }

    @Override
    public void onUserInfoUpdateSuccess() {
        settingsView.stopRefreshing();
        settingsView.showDialogInfoUpdatedCorrectly();

    }


    public void setLocaleSelection(int idx){
        switch (idx) {
            case 0:
                settingsView.setLocale("es");
                break;
            case 1:
                settingsView.setLocale("en");
                break;
            case 2:
                settingsView.setLocale("ca");
                break;
        }
    }

    @Override
    public void updateUserInfo(String name, String url) {
        if(imageChanged) {
            uploadProfileImage(url, java.util.UUID.randomUUID().toString());
        }
        else{
            settingsView.onPreStartConnection();
            settingsInteractor.AddUserInfoRequest(name, url, this);
        }
    }

    @Override
    public void checkLocaleRadioButton(String locale) {
        if(locale.contains("en")) {
            settingsView.setCheckedEnglishRB();
        }

        else if(locale.contains("ca")) {
            settingsView.setCheckedValencianRB();

        }
        else {
            settingsView.setCheckedSpanishRB();
        }
    }

    @Override
    public void uploadProfileImage(String url, String name) {
        settingsView.onPreStartConnection();
        settingsInteractor.uploadImageRequest(url,name,this);
    }

    @Override
    public void setImageChangedTrue() {
        imageChanged = true;

    }

    @Override
    public void onUploadImageSuccess(String url) {
        settingsInteractor.AddUserInfoRequest(settingsView.getNameED(), url, this);

    }
}
