package martinezruiz.javier.pmdm003.ui.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import martinezruiz.javier.pmdm003.R;

/**
 * Esta clase pretende manipular el valor del switch que permite borrar pokemons capturados
 * Según la documentación oficial, un ViewModel permite conservar el estado de un objeto
 * y es más fácilmente ejecutar pruebas sobre ella
 */
public class SettingsViewModel extends ViewModel {

    public SettingsViewModel() {
        allowDelete = new MutableLiveData<>();
        loginState = new MutableLiveData<>();
        language = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginState(){ return loginState; }

    public void setAllowDelete(boolean value){
        allowDelete.setValue(value);
    }
    public void setLoginState(boolean state){
        loginState.setValue(state);
    }
    public void setLanguage(String language){ this.language.setValue(language);}

    private final MutableLiveData<Boolean> allowDelete;
    private final MutableLiveData<Boolean> loginState;
    private final MutableLiveData<String> language;


}