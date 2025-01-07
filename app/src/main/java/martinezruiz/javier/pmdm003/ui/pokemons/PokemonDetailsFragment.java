package martinezruiz.javier.pmdm003.ui.pokemons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import java.util.stream.Collectors;
import martinezruiz.javier.pmdm003.databinding.FragmentPokemonDetailsBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;


/**
 * Clase que muestra los detalles de un pokemon capturado. EL fragmento recibe el nombre del
 * pokemon a través de un argumento cuando se navega. Si el argumento contiene un name, se pide
 * la lista del viewmodel y se busca el pokemon con el name recibido en el argumento y se
 * muestra en la vista
 */
public class PokemonDetailsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        String args;
        if(getArguments()!=null) {
            args = String.valueOf(getArguments().get("name"));
            pokemon = viewModel.getPokemons().getValue()
                    .stream()
                    .filter(p-> p.getNombre().equals(args))
                    .findFirst()
                    .orElse(null);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentPokemonDetailsBinding binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false);
        Glide.with(binding.getRoot())
                .load(pokemon.getImgUrl().getFrontDefault())
                .into(binding.detailsImg);
        binding.detailsName.setText(pokemon.getNombre());
        binding.detailsIndex.setText(String.valueOf(pokemon.getIndice()));
        String types = pokemon.getTypes()
                .stream()
                .map(type-> type.getType().getName())
                .collect(Collectors.joining(", "));
//
        binding.detailsTypes.setText(types);
        binding.detailsHeight.setText(String.valueOf(pokemon.getAltura()));
        binding.detailsWeight.setText(String.valueOf(pokemon.getPeso()));
        return binding.getRoot();
    }

 

    PokedexViewModel viewModel;
    Pokemon pokemon;
}
