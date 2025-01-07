package martinezruiz.javier.pmdm003.ui.pokemons;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.stream.Collectors;

import martinezruiz.javier.pmdm003.SharedPreferenceService;
import martinezruiz.javier.pmdm003.databinding.FragmentPokemonsBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.ui.ClickListener;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;
import martinezruiz.javier.pmdm003.R;

/**
 * Clase que muestra pokemons capturados y posibilita borrarlos.
 * Cuando arranca averigua si se puede o no borrar, que es un valor que se guarda en el sharedPreferences
 * general. También se abre el sharedpreference del usuario logado para eliminar los pokemons que
 * borre. El deslizamiento de cada pokemon es posible si es posible borrar
 */
public class PokemonsFragment extends Fragment implements ClickListener {


    public PokemonsFragment(){ this.pokemons = new ArrayList<>();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spSettings = requireActivity().
                getSharedPreferences(getString(R.string.preference_file_settings), Context.MODE_PRIVATE);
        allowDelete = spSettings.getBoolean(getString(R.string.delete_preference_key), false);

        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        spService = new SharedPreferenceService(requireContext(), pokedexViewModel.getUserEmail());


        helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                    int movement;
                    if(allowDelete){
                        movement = makeMovementFlags(0, ItemTouchHelper.END);
                    }
                    else{
                        movement = makeMovementFlags(0,0);
                        Snackbar.make(getView(),
                                "Habilite el borrado en Ajustes",
                                BaseTransientBottomBar.LENGTH_SHORT)
                                .show();
                    }
                    return movement;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(allowDelete) {
                    Pokemon pokemon = pokemons.get(viewHolder.getAdapterPosition());
                    pokedexViewModel.delete(pokemon); //borramos base datos
                    pokemons.remove(viewHolder.getAdapterPosition()); //borramos vista
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition()); //avisamos al adapter
                    spService.liberarCapturado(pokemon.getNombre()); //quitamos del sharedPreference
                }
            }
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPokemonsBinding.inflate(inflater, container, false);
        //instanciamos el layout
        recycler = binding.rvPokemonCaptured; // obtemos la ref del recycler
        LinearLayoutManager llm = new LinearLayoutManager(getContext()); //creamos un layout para el recycler
        recycler.setLayoutManager(llm);
        adapter = new PokemonAdapter(this.pokemons, this);

        recycler.setAdapter(adapter);
        helper.attachToRecyclerView(recycler);


        pokedexViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            if(adapter.getItemCount() == 0){ //la primera vez siempre es 0

                this.pokemons.addAll(pokemons.stream()
                        .filter(p->p.getState().equals(Pokemon.State.CAPTURED))
                        .collect(Collectors.toList()));
                adapter.notifyItemRangeInserted(0, this.pokemons.size());
            }
        });

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        return binding.getRoot();
    }

    /**
     * Cuando hay click creamos un bundle con el nombre y se lo pasamos al details para que allí
     * se rescate la info
     * @param pokemon
     */
    @Override
    public void onClick(Pokemon pokemon) {
        Bundle bundle = new Bundle();
        bundle.putString("name", pokemon.getNombre());
        navController.navigate(R.id.action_navigation_pokemons_to_details_navigation, bundle);
    }


    SharedPreferenceService spService;
    SharedPreferences spSettings;
    PokedexViewModel pokedexViewModel;
    FragmentPokemonsBinding binding;
    RecyclerView recycler;
    PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    ItemTouchHelper helper;
    NavController navController;
    Boolean allowDelete = false;


}