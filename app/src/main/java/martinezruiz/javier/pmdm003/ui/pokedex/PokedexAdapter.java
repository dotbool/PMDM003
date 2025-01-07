package martinezruiz.javier.pmdm003.ui.pokedex;

import android.util.Log;
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
import martinezruiz.javier.pmdm003.ui.ClickListener;


/**
 * Tipo que adapta los datos que vamos a manejar para que encajen en el RecyclerView
 */
public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokeViewHolder> implements ClickListener {

    /**
     * el Adaptador es llamado desde el Fargmento Pokedex y el listener que recibe sirve sencillamente
     * para devolver el pokemon que ha sido clikado
     * @param pokemons
     * @param onClickListener
     */
    public PokedexAdapter(ArrayList<Pokemon> pokemons, ClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.pokemons = pokemons;
    }

    /**
     *
     * @param pokemon
     */
    @Override
    public void onClick(Pokemon pokemon) {
        onClickListener.onClick(pokemon);
    }

    static class PokeViewHolder extends RecyclerView.ViewHolder {

        PokedexItemHolderBinding binding;

        public PokeViewHolder(PokedexItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        /**
         * El método se encarga de colorear los items en función del state del pokemon.
         * Si el pokemon tiene stated WANTED, éste método torna selected el item de la lista
         * Y la view tiene un stilo bindeado para que en función del estado del item list adopte un color
         * Si el pokemon está capturado, torna el item list amarillo fuerte
         * @param pokemon Es el dato que será mostrado en el item de la lista
         * @param listener Devuleve el pokemon clickado al framento
         * @param position testimonio del progreso
         */
        public void bind(Pokemon pokemon, ClickListener listener, int position) {
            binding.listItemText.setText(pokemon.getNombre());

            View view = binding.getRoot();
            view.setSelected(pokemon.getState().equals(Pokemon.State.WANTED)); //cambia de color el selected
            view.setEnabled(!pokemon.getState().equals(Pokemon.State.CAPTURED)); //torna disabled el capture

            binding.getRoot().setOnClickListener(e -> {
                view.setSelected(!view.isSelected());
                listener.onClick(pokemon);
            });
        }

    }


    /**
     * Este método es invocado por el layoutmanager que supongo que entrará en acción cuando se
     * notifica al adapter que hay cambios
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = PokedexItemHolderBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new PokeViewHolder(binding);
    }

    /**
     * Recupera el pokemon en función de la posición que le haya asignado el adapter, que no es otra
     * que la posición de la lista que alimenta al adapter. Es preciso coordinar el estado de la
     * lista de pokemosn alojada en el pokedexviewmodel y la lista local del adapter
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PokeViewHolder holder, int position) {

        Pokemon pokemon = this.pokemons.get(position);
        holder.bind(pokemon, this, position);

        if(pokemon.getState().equals(Pokemon.State.CAPTURED)){ // si el estado es capturado se renderiza la foto
            Glide.with(binding.getRoot())
                    .load(pokemon.getImgUrl().getFrontDefault())
                    .into(holder.binding.imgPokedex);

        }
        else{ //si no es capturado se limpia y se le pone la img por defecto
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

