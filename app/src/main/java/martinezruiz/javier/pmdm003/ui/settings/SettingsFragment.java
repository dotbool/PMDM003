package martinezruiz.javier.pmdm003.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
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

        binding.btnDeleteSettings.setOnClickListener(view -> {
            settingsViewModel.setAllowDelete(binding.btnDeleteSettings.isChecked());
        });
        binding.btnLogoutSettings.setOnClickListener(view-> {

            settingsViewModel.setLoginState(false);

        });
        settingsViewModel.getAllowDelete().observe(getViewLifecycleOwner(), value-> {
            System.out.println("estado cambiado");

        });

        return binding.getRoot();
    }

}