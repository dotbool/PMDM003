package martinezruiz.javier.pmdm003.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.databinding.FragmentSettingsBinding;

//Tu actividad no se cierra por completo cada vez que el usuario sale de la actividad.
//Cuando la app está en segundo plano, en general, no debe estar activa a fin de conservar
// los recursos del sistema
//Cuando se llama a onPause(), la app ya no tiene foco. Después de onStop(), la app deja de estar
//        visible en la pantalla. Aunque la actividad se detuvo, el objeto Activity todavía está
//        en la memoria, en segundo plano. El SO Android no destruyó la actividad. El usuario podría
//        regresar a la app, por lo que Android conserva los recursos de la actividad.

//El cambio de configuración hace que se llame a onDestroy()
//    finish() llama a on destroy()

/*Existen algunas situaciones en las que finaliza tu actividad debido al comportamiento normal de la
 app, por ejemplo, cuando el usuario presiona el botón Atrás o tu actividad indica su propia
  finalización llamando al método finish().
 */
//Cuando se destruye tu actividad porque el usuario presiona Atrás o la actividad se termina sola,
//tanto para el concepto de sistema como para el usuario esa instancia Activity desapareció par siempre

/*
Sin embargo, si el sistema finaliza la actividad debido a restricciones del sistema
(como un cambio de configuración o presión de la memoria), entonces, aunque la Activity desapareció,
 el sistema recuerda que existía.
 */

/** El propósito de los view model es que los datos asociados a una vista sobrevivan a los cambios
 * de configuración. Para crear un view model usamos un viewmodel factory (new ModelProvider). Éste
 * guarda una referencia al view model en un viewModelStore y limpia esta referencia cuando
 * el contexto para el que fue creado es destruido
 *
 * ¿Cómo un fragmento es asociado con una actividad? -> requireActivity()
 * https://stackoverflow.com/questions/34011271/how-does-a-fragment-attach-itself-to-an-activity
 * If you've added a <fragment> tag to your layout XML, this is an indication that the fragment with
 * the specified class name should be instantiated and added to the Activity's view hierarchy at the
 * same time that the rest of the view hierarchy is being instantiated
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    public SettingsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //el viewModelProvider crea instancias que mantienen valores cuando se cambia la
        //configuración del dispositivo. Su alcance, es el determinado por el primer argumento
        //que recibe la instancia de viewModelProvider y el tipo que devuelve es el argumento
        //que recibe el método get. Cuando el alcance es el mismo, android devuelve la misma
        //instancia
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        sp = getActivity().getSharedPreferences(getString(R.string.preference_file_settings), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //leemos en el sp el valor de permitir borrar
        boolean allow = sp.getBoolean(getString(R.string.delete_preference_key), false);
        settingsViewModel.setAllowDelete(allow); //establecemos en el modelo ese valor
        binding.btnDeleteSettings.setChecked(allow); //establecemos en la vista ese valor
        binding.btnDeleteSettings.setOnClickListener(view -> { //listener en la vista para que cambie
            //el valor del modelo
            boolean allow_delete = binding.btnDeleteSettings.isChecked();
            settingsViewModel.setAllowDelete(allow_delete);
            editor.putBoolean(getString(R.string.delete_preference_key), allow_delete);
            editor.apply();
        });


        //establecemos el valor de la vista en función del valor del archivo de preferencias o si no
        //existe este valor, en función del idioma por defecto del sistema
        setLanguage();


        binding.btnRadioLanguage.setOnCheckedChangeListener((group, id) -> {
            String rbLanguage = id == R.id.btn_rb_spanish ? "es-ES" : "en-US";
            settingsViewModel.setLanguage(rbLanguage);
            editor.putString(getString(R.string.language_preference_key), rbLanguage);
            editor.apply();
            setApplicationLocale(rbLanguage);

        });

        binding.btnLogoutSettings.setOnClickListener(view-> {
            settingsViewModel.setLoginState(false);
        });



        return binding.getRoot();
    }

    /**
     * Este método setea el modelo y vista de la opción Lanaguage. Si hay algo guardado en el sharedPreference
     * lo recupera y lo establece. Si no hay nada establece en la vista el language del sistema
     */
    private void setLanguage(){

        String defaultLanguage = Resources.getSystem().getConfiguration().getLocales().get(0).toLanguageTag();
        String language = sp.getString(getString(R.string.language_preference_key), defaultLanguage);

        settingsViewModel.setLanguage(language); //establecemos el valor del language en el modelo
        int id_rb = language.equals("es-ES") ? R.id.btn_rb_spanish: R.id.btn_rb_english;
        binding.btnRadioLanguage.check(id_rb); // lo establecemos en la vista
    }

    /**
     * Cuando se cambia el language se llama a este método para que android cambie el language
     * @param locale
     */
    private void setApplicationLocale(String locale){
        LocaleListCompat localeList = LocaleListCompat.forLanguageTags(locale);
        AppCompatDelegate.setApplicationLocales(localeList);
    }

    SharedPreferences sp;

}