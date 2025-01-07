package martinezruiz.javier.pmdm003.ui.pokedex;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.SharedPreferenceService;
import martinezruiz.javier.pmdm003.databinding.FragmentPokedexBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.ui.ClickListener;

/**
 * Esta clase atiende el modelo de datos que proporciona pokedexviewmodel mostrando en la vista
 * la totalidad de los pokemons que vienen de la api. Cada usuario que use este dispositivo tiene un
 * archivo de preferencias dónse se guardan pares nombrePokemon: statePokemon, por ejemplo
 * Bulbasaur: capture. Cuando el fragmento se crea, se llama a ese archivo para que recupere todos
 * los pokemosn capturados que tenía el usuario. Esa lista se envía a pokedexViewmodel para que
 * solicite a la APi la info de los pokemosn capturados de tal forma que el usuario vea en la lista
 * de pokedex tanto los no capturados como los que si lo están.
 */
public class PokedexFragment extends Fragment implements ClickListener {


    public PokedexFragment() {
        this.pokemons = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        spService = new SharedPreferenceService(requireContext(), pokedexViewModel.getUserEmail());
        //enviámos al viewmodel los que ya están capturados en sesiones anteriores para que pida
        //a firebase la info y podamos verlos en capturados
        pokedexViewModel.setCapturadosSp(spService.iniciarCapturados());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        recycler = (RecyclerView) binding.rvPokedex;
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layout);
        adapter = new PokedexAdapter(this.pokemons, this); //le metemos la lista
        recycler.setAdapter(adapter);

        //hacemos que el fragmento tire de la lista de pokemons cada vez que haya cambios
        pokedexViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {

            if (adapter.getItemCount() == 0) { //la primera vez, la lista está vacía

                spService.setCaptures(pokemons); //este método setea el valor state de los pokemons
                //que vienen en Captured, si es que estaban en el sharedpreference. No se crean nuevos
                // objetos Pokemon, sino que se setean las propiedades de lo que traiga el LiveData
                this.pokemons.addAll(pokemons); //añadimos todos
                adapter.notifyItemRangeChanged(0,this.pokemons.size());

            }
            else {

                this.pokemons = (ArrayList<Pokemon>) pokemons; // si no es la primera vez es que se
                //han producido cambios en la lista, esto es , que hay nuevos Capturados
                this.pokemons.stream() //escribimos en el SharedPreferences los nuevos capturados
                        .filter(p -> p.getState().equals(Pokemon.State.CAPTURED))
                        .forEach(p -> spService.escribirCapturado(p.getNombre(),
                                Pokemon.State.CAPTURED.name()));
                adapter.notifyDataSetChanged(); //notificamos los cambios al adapter
                //está mal diseñado porque lo ideal es saber que ha cambiado con excatitud para
                //darle menos trabajo a la app. En la próxima, esto es algo que mejorar

            }
        });

        pokedexViewModel.getErrorData().observe(getViewLifecycleOwner(), error->{
            Snackbar.make(getView(), error.getError(), BaseTransientBottomBar.LENGTH_LONG).show();
            error.setHandled(true);
            Log.e("ERROR", getClass().getName(), new Throwable(error.getError()));

        });

        setCaptureBtnUI(); //Adaptamos al btn de captura para que se ajuste a la pantalla. Lo que
        //hace este método es medir el heigth de la bottom navigation y darle al botón de capturar
        //ese margen. Así se adapta a cualquier pantalla

        Button btn = binding.btnCapture;
        btn.setOnClickListener(view -> { // El botón de capturar recupera primero los datos de la API
            pokedexViewModel.capture(); // y luego inserta en FireStore
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        spService = null;
    }

    /**
     * Este método mide la barra de navegación inferior y en función de esa medida establece un
     * padding para el botón de capture. Cómo cada móvil tiene unas dimensiones así me aseguro de
     * que la proporción será correcta
     */
    private void setCaptureBtnUI() {

        BottomNavigationView navigation =
                requireActivity().findViewById(R.id.nav_view_bottom);
        navigation.post(new Runnable() {
            @Override
            public void run() {
                int height = (int) navigation.getMeasuredHeight();
                View v = binding.btnCapture;
                ViewGroup.MarginLayoutParams btnBottomMargin = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                btnBottomMargin.bottomMargin = height;
                v.setLayoutParams(btnBottomMargin);

            }
        });
    }

    /**
     * Esto se creó para cuando el dispositivo se pone en forma apaisada que el botón de
     * capturar se adapte de nuevo a las dimensiones de la pantalla
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCaptureBtnUI();
    }


    /**
     * Este listener se envía al Adapter cuando es construido para que nos avise de que pokemon ha
     * sido pulsado. Sólo son clickables los no capturados
     *
     * @param pokemon
     */
    @Override
    public void onClick(Pokemon pokemon) {
        Pokemon.State state = pokemon.getState() == Pokemon.State.WANTED ? Pokemon.State.FREE : Pokemon.State.WANTED;
        pokemon.setState(state);
    }

    private PokedexViewModel pokedexViewModel;
    private ArrayList<Pokemon> pokemons;
    private FragmentPokedexBinding binding;
    PokedexAdapter adapter;
    RecyclerView recycler;
    SharedPreferenceService spService;


}