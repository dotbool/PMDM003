package martinezruiz.javier.pmdm003;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import martinezruiz.javier.pmdm003.models.Pokemon;

public class SharedPreferenceService {


    public SharedPreferenceService(Context ctx) {

        sp =  ctx.getSharedPreferences(ctx.getString(R.string.preference_file_capture),
                Context.MODE_PRIVATE);
        editor = sp.edit();

    }

    public void escribirCapturado(String name, String state){
        editor.putString(name, state);
        editor.apply();

    }

    public void liberarCapturado(String name){
        editor.remove(name);
        editor.apply();
    }

    public List<Pokemon> leerCapturados(List<Pokemon> pokemons){
        for (Pokemon p: pokemons){
            String state = sp.getString(p.getNombre(), "FREE");
            if(state.equals(Pokemon.State.CAPTURED.name())){
                p.setState(Pokemon.State.CAPTURED);
            }
        }
        return pokemons;
    }

    public ArrayList<String> iniciarCapturados(){
        ArrayList<String> captures = new ArrayList<>();
        sp.getAll().forEach((key, value) -> {
            if (value.equals(Pokemon.State.CAPTURED.name())){
                captures.add(key);
            }
        });
        return captures;
    }
    SharedPreferences sp;
    SharedPreferences.Editor editor;
}
