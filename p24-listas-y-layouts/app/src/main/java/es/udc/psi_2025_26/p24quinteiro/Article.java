package es.udc.psi_2025_26.p24quinteiro;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;



public class Article implements Parcelable {
    String title;
    String subtitle;
    String description;

    // Mejor que no sólo dependa de i sino, ...
    // Constructor ad-hoc(no real del todo), al final dejar así

    // Al no haber getString en una clase java, ya que no es una actividad, se deciden
    // pasar los recursos por parámetros ya convertidos a strings.
    public Article  (int i, String artTitle, String artSubtitle, String artDescription){
        title =  artTitle + i;// Cambiar, format y recurso(...)
        subtitle = artSubtitle + i;
        description = artDescription + i;
    }
    // Desempaquetado de clase article serializada
    // Importante, se deben de ir leyendo en el orden en el que se introdujeron los campos
    protected Article(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<>() {
        // Paso de parcel a article, con el constructor de arriba
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    // Empaquetado, debe desempaquetarse en el mismo orden
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
    }

    // getters ...
    public  String getTitle(){
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
