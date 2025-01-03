package martinezruiz.javier.pmdm003.ui.pokemons;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.stream.Collectors;
import martinezruiz.javier.pmdm003.databinding.PokemonItemHolderBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;

public class PokemonAdapter  extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {


    public PokemonAdapter(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PokemonItemHolderBinding binding = PokemonItemHolderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Pokemon pokemon = PokemonAdapter.this.pokemons.get(position);
//        if(pokemon.getState().equals(Pokemon.State.CAPTURED)){
            holder.bind(position);
        System.out.println("on binviewholder");

//        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(pokemons != null) {
            size = pokemons.size();
        }
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull PokemonItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(int position){
            Pokemon pokemon = PokemonAdapter.this.pokemons.get(position);
                Glide.with(binding.getRoot())
                        .load(pokemon.getImgUrl().getFrontDefault())
                        .into(binding.pokemonImg);
                binding.pokemonName.setText(pokemon.getNombre());
                String types = pokemon.getTypes()
                        .stream()
                        .map(type -> type.getType().getName())
                        .collect(Collectors.joining(", "));
                binding.pokemonTypes.setText(types);
        }

        PokemonItemHolderBinding binding;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
        System.out.println(pokemons.hashCode()+ "en el set");
    }

    ArrayList<Pokemon> pokemons;
}
