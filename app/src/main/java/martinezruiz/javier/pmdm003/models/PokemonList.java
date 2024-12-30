package martinezruiz.javier.pmdm003.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokemonList {

    @SerializedName("results")
    ArrayList<Pokemon> pokemonList;

    public ArrayList<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(ArrayList<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }
}
