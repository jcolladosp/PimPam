package jcollado.pw.pimpam.settings;


/**
 * Created by jcolladosp on 11/06/2017.
 */

public class SettingsPresenterImpl implements SettingsPresenter,SettingsInteractor.OnSettingsFinishedListener {

    private SettingsView settingsView;
    private SettingsInteractor settingsInteractor;

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
        settingsInteractor.sendRequestDeleteAllComics(this);
    }

    @Override
    public void onComicsDeleted() {
        settingsView.stopRefreshing();
        settingsView.showDialogsComicsDeletedSuccess();
    }

    @Override
    public void onUserInfoUpdateSucess() {
        settingsView.stopRefreshing();
        setLocaleSelection(settingsView.getLocaleSelected());

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
    public void addUserInfo(String name, String url) {
        settingsInteractor.sendRequestAddUserInfo(name,url,this);
    }
}
