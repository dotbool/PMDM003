package martinezruiz.javier.pmdm003.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Cuando traemos datos de la API, para poder crear objetos que sean utilizables por la aplicación,
 * tenemos que asignar un tipo de dato con su valor correspondiente. Esto es lo que hacemos al
 * deserializar información que proviene de la web, convertir datos en objetos. Para este propósito
 * se ha incluido la dependencia de GSON y para que esta dependencia sepa como debe actuar, anotamos
 * los atributos de la clase con @SerializedName y entre los paréntesis, el nombre de la clave que
 * viene en el JSON. Entonces, cuando  la dependencia lea un item del JSON, al encontrar la clave 'name'
 * asociará el valor de esa clave al atributo nombre del objeto que cree
 * La anotación @exclude es de la API de Firebase e impide que un campo sea tratado
 */
public class Pokemon {


    public Pokemon() {

        state = state == null ? State.FREE: state;
        imgUrl = new ImgUrl();
        types = new ArrayList<>();
    }

    @SerializedName("name")
    private String nombre;
    @SerializedName("id")
    private int indice;

    @SerializedName("sprites")
    private ImgUrl imgUrl;
    @SerializedName("weight")
    private int peso;

    @SerializedName("height")
    private int altura;

    @SerializedName("types")
    private ArrayList<BigType> types;

    @Exclude
    private State state;


    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public ImgUrl getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(ImgUrl imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
//
    public String getNombre() {
        return nombre;
    }
//
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
//
    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }


    public ArrayList<BigType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<BigType> types) {
        this.types = types;
    }

    @Exclude
    public State getState() {
        return state;
    }
    @Exclude
    public void setState(State state) {
        this.state = state;
    }
    public enum State { FREE, WANTED, CAPTURED }

    @Override
    public String toString() {
        return "Pokemon{" +
                "altura=" + altura +
                ", nombre='" + nombre + '\'' +
                ", indice=" + indice +
                ", imgUrl=" + imgUrl.getFrontDefault() +
                ", peso=" + peso +
                ", types=" + types.stream().map(type-> type.getType().getName()).collect(Collectors.joining(", ")) +
                ", state=" + state +
                '}';
    }
}
