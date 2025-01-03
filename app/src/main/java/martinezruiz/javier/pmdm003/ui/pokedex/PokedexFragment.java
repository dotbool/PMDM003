package martinezruiz.javier.pmdm003.ui.pokedex;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.SharedPreferenceService;
import martinezruiz.javier.pmdm003.databinding.FragmentPokedexBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;

/**
 *
 */
public class PokedexFragment extends Fragment implements ClickListener {


    public PokedexFragment() {
        this.pokemons = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        spService = new SharedPreferenceService(requireContext());
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




//        adapter = new PokedexAdapter(this.pokemons, this); //le metemos la lista
//        recycler.setAdapter(adapter); //la primera vez la lista está vacía pero lo que nos importa
        //es el objeto al que va a apuntar el adapter. Queremos mantener el mismo objeto
        //durante todo el ciclo de vida del fragmento

        //The first method where it is safe to access the view lifecycle is onCreateView
        //Aunque google dice también onCreate pero salta la exception
//
//        pokedexViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons ->{
//
//            this.pokemons = (ArrayList<Pokemon>) pokemons; // si no es la primera vez es que se
//            spService = new SharedPreferenceService(requireContext());
//            if(adapter == null){ //el adapter no es destruido en los cambios de configuración pero
//                //parece ser que que es dettached
//                spService.leerCapturados(this.pokemons);
//                adapter = new PokedexAdapter(this.pokemons, this);
//                recycler.setAdapter(adapter);
//            }
//            else{
//                recycler.setAdapter(adapter); //como es dettached hay que volver a establecerlo
//                this.pokemons.stream() //escribimos en el SharedPreferences los nuevos capturados
//                        .filter(p -> p.getState().equals(Pokemon.State.CAPTURED))
//                        .forEach(p -> spService.escribirCapturado(p.getNombre(),
//                                Pokemon.State.CAPTURED.name()));
//            }
////            adapter.notifyDataSetChanged(); //notificamos los cambios al adapter
//
//        });
//
        pokedexViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            if (spService == null) {
                spService = new SharedPreferenceService(requireContext());
            }
            if (adapter.getItemCount() == 0) { //la primera vez, la lista está vacía
                System.out.println("pokemodex 0");

                spService.leerCapturados(pokemons); //este método setea el valor state de los pokemons
                //que vienen en Captured, si es que estaban en el sharedpreference. No se crean nuevos
                // objetos Pokemon, sino que se setean las propiedades de lo que traiga el LiveData
                // Si creo nuevos objetos cambia la referencia de la lista completa y ya no sería al
                // que apunta el Adapter por lo que las notificacines de cambio no funcionarían
                this.pokemons.addAll(pokemons); //añadimos todos y llevamos el service a null para
                //ayudar al garbage

            }
            else {
                this.pokemons = (ArrayList<Pokemon>) pokemons; // si no es la primera vez es que se
                //han producido cambios en la lista, esto es , que hay nuevos Capturados
                this.pokemons.stream() //escribimos en el SharedPreferences los nuevos capturados
                        .filter(p -> p.getState().equals(Pokemon.State.CAPTURED))
                        .forEach(p -> spService.escribirCapturado(p.getNombre(),
                                Pokemon.State.CAPTURED.name()));
            }
            spService = null;
            adapter.notifyDataSetChanged(); //notificamos los cambios al adapter
        });

        setCaptureBtnUI(); //Adaptamos al btn de captura para que se ajuste a la pantalla

        Button btn = binding.btnCapture;
        btn.setOnClickListener(view -> {
            pokedexViewModel.capture();
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ": destruido");
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
     * Este listener se envía al Adapter cuando es construido para que nos avise de que pokemon ha
     * sido pulsado
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
    private String TAG = getClass().getName();
    PokedexAdapter adapter;
    RecyclerView recycler;
    SharedPreferenceService spService;


}