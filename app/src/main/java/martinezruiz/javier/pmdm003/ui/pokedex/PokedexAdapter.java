package martinezruiz.javier.pmdm003.ui.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import martinezruiz.javier.pmdm003.R;
import martinezruiz.javier.pmdm003.databinding.PokedexItemHolderBinding;
import martinezruiz.javier.pmdm003.models.Pokemon;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokeViewHolder> implements ClickListener {

    public PokedexAdapter(ArrayList<Pokemon> pokemons, ClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.pokemons = pokemons;
    }

    @Override
    public void onClick(Pokemon pokemon) {
        onClickListener.onClick(pokemon);
    }

    class PokeViewHolder extends RecyclerView.ViewHolder {

        PokedexItemHolderBinding binding;

        public PokeViewHolder(PokedexItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        /**
         * @param pokemon
         * @param listener
         * @param position
         */
        public void bind(Pokemon pokemon, ClickListener listener, int position) {
            binding.listItemText.setText(pokemon.getNombre());

            View view = binding.getRoot();
            view.setSelected(pokemon.getState().equals(Pokemon.State.WANTED));
            view.setEnabled(!pokemon.getState().equals(Pokemon.State.CAPTURED));

            binding.getRoot().setOnClickListener(e -> {
                view.setSelected(!view.isSelected());
                listener.onClick(pokemon);
            });
        }

    }


    @NonNull
    @Override
    public PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = PokedexItemHolderBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new PokeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeViewHolder holder, int position) {

        Pokemon pokemon = this.pokemons.get(position);
        holder.bind(pokemon, this, position);

        if(pokemon.getState().equals(Pokemon.State.CAPTURED)){
            Glide.with(binding.getRoot())
                    .load(pokemon.getImgUrl().getFrontDefault())
                    .into(holder.binding.imgPokedex);

        }
        else{
            Glide.with(binding.getRoot()).clear(holder.binding.imgPokedex);
            holder.binding.imgPokedex.setImageResource(R.drawable.wanted);
        }
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }




    private ArrayList<Pokemon> pokemons;
    private
    PokedexItemHolderBinding binding;
    ClickListener onClickListener;


}

