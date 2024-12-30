package martinezruiz.javier.pmdm003.ui.pokemons;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;
import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;

public class PokemonsFragment extends Fragment {

    private PokemonsViewModel mViewModel;

    private  String[] args;

    public PokemonsFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);

        View view = inflater.inflate(R.layout.fragment_pokemons, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Button btn = view.findViewById(R.id.btn_test);

        return view;
    }

    private PokedexViewModel pokedexViewModel;



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
}