package martinezruiz.javier.pmdm003.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Esta clase pretende manipular el valor del switch que permite borrar pokemons capturados
 * Según la documentación oficial, un ViewModel permite conservar el estado de un objeto
 * y es más fácilmente ejecutar pruebas sobre ella
 */
public class SettingsViewModel extends ViewModel {

    public SettingsViewModel() {
        this.allowDelete = new MutableLiveData<>();
        this.loginState = new MutableLiveData<>();
    }

    public LiveData<Boolean> getAllowDelete() { return allowDelete; }

    public LiveData<Boolean> getLoginState(){ return loginState; }

    public void setAllowDelete(boolean value){
        allowDelete.setValue(value);
    }

    public void setLoginState(boolean state){
        loginState.setValue(state);
    }

    private final MutableLiveData<Boolean> allowDelete;
    private final MutableLiveData<Boolean> loginState;


}