package martinezruiz.javier.pmdm003.models;

import com.google.gson.annotations.SerializedName;

public class ImgUrl {
    @SerializedName("front_default")
    String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
