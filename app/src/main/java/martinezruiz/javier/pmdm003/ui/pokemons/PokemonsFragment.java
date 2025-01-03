package martinezruiz.javier.pmdm003.ui.pokemons;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;
import java.util.stream.Collectors;

import martinezruiz.javier.pmdm003.databinding.FragmentPokemonsBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;
import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;

public class PokemonsFragment extends Fragment {

    private PokemonsViewModel mViewModel;

    private  String[] args;

    public PokemonsFragment(){ this.pokemons = new ArrayList<>();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);

        helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags( ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                itemRemoved = viewHolder.getAdapterPosition();
                Pokemon pokemon = pokemons.get(viewHolder.getAdapterPosition());
                viewModel.delete(pokemon);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

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
//        adapter = new PokemonAdapter(pokemons); //creamos el adaptar con la lista de pokemons que estará
        //vacía la primera vez porque la data llega de forma asíncrona. Lo que nos importa es el objeto
        //arrayList al que va a apuntar el adapter
        adapter = new PokemonAdapter(this.pokemons);

//        adapter.setPokemons(this.pokemons); //si no metemos el setAdapter en el createview, nos da
//        un error que no detiene la app, indicando qskipping layout, no adapter attached
        // Si queremos eviatr eso, hay que vincular el recyler con el adapter aquí
        // Si no le metemos una lista al adapter, siempre nos devolverá la primera vez un itemcount == 0
        // si le metemos una lista, ya no podemos perder esa referencia por lo que todo lo que venga
        // del mutableLiveData habrá que insertarlo en el list al que apunte el adapter
        //

        //el apter no es destruido en los cambios de configuracion o cuando se cambia de fragmento
        //pero si es attached al fragmento  y si no tiene una referencia a una lista pues se hace sin
        //lista

        //si queremos hacer uno del notifyitemchanged del adapter tenemos que saber que item ha sido
        //modificado para o hacer un addAll

        recycler.setAdapter(adapter);
        helper.attachToRecyclerView(recycler);


        viewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            if(adapter.getItemCount() == 0){

//                this.pokemons.addAll(pokemons);
                this.pokemons.addAll(pokemons.stream()
                        .filter(p->p.getState().equals(Pokemon.State.CAPTURED))
                        .collect(Collectors.toList()));
                adapter.notifyItemRangeInserted(0, this.pokemons.size());
            }
            else{
                this.pokemons.remove(itemRemoved);
            }
        });

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        return binding.getRoot();
    }


    //pa recibir argumentos
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        args = PokemonsFragmentArgs.fromBundle(getArguments()).getPokemonsCapturados();
//        if(args!=null){
//            for(String a: args){
//                System.out.println(a);
//            }
//        }
//
//    }

    PokedexViewModel viewModel;
    FragmentPokemonsBinding binding;
    RecyclerView recycler;
    PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    ItemTouchHelper helper;

    int itemRemoved = -1;

}