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

import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.databinding.FragmentPokedexBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;

/**
 *
 */
public class PokedexFragment extends Fragment implements ClickListener{


    public PokedexFragment() {
        this.pokemons = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);


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



        //The first method where it is safe to access the view lifecycle is onCreateView
        //Aunque google dice también onCreate pero salta la exception

        pokedexViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            if(adapter.getItemCount() == 0){
                this.pokemons.clear();
                this.pokemons.addAll(pokemons);

            }
            else{
                this.pokemons = (ArrayList<Pokemon>) pokemons;
            }
            adapter.notifyDataSetChanged();
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
    private void setCaptureBtnUI(){

        BottomNavigationView navigation =
                requireActivity().findViewById(R.id.nav_view_bottom);
        navigation.post(new Runnable() {
            @Override
            public void run() {
                int height = (int) navigation.getMeasuredHeight();
                View v = binding.btnCapture;
                ViewGroup.MarginLayoutParams btnBottomMargin = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                btnBottomMargin.bottomMargin=height;
                v.setLayoutParams(btnBottomMargin);

            }
        });
    }

    /**
     * Este listener se envía al Adapter cuando es construido para que nos avise de que pokemon ha
     * sido pulsado
     * @param pokemon
     */
    @Override
    public void onClick(Pokemon pokemon) {
        Pokemon.State state = pokemon.getState() == Pokemon.State.WANTED ? Pokemon.State.FREE: Pokemon.State.WANTED;
        pokemon.setState(state);
    }

    private PokedexViewModel pokedexViewModel;
    private ArrayList<Pokemon> pokemons;
    private FragmentPokedexBinding binding;
    private String TAG = getClass().getName();
    PokedexAdapter adapter;
    RecyclerView recycler;


}