package es.udc.psi_2025_26.p32quinteiro.domain;

import com.google.gson.annotations.SerializedName;

// Aunque en el JSON vienen más campos
// Gson, el converter, ignora los que no declaramos

// El recycler sólo usa make, model y year
// En el fragmento específico el resto
public class Moto {

    @SerializedName("make") // Nombre exacto del campo en el JSON
    private String make; // A la variable le podemos poner un nombre distinto si queremos

    @SerializedName("model")
    private String model;

    @SerializedName("year")
    private String year;

    @SerializedName("type")
    private String type;

    @SerializedName("displacement")
    private String displacement;

    @SerializedName("engine")
    private String engine;

    @SerializedName("power")
    private String power;

    @SerializedName("torque")
    private String torque;

    @SerializedName("gearbox")
    private String gearbox;

    @SerializedName("cooling")
    private String cooling;

    @SerializedName("total_weight")
    private String totalWeight;

    @SerializedName("seat_height")
    private String seatHeight;

    @SerializedName("fuel_capacity")
    private String fuelCapacity;

    // No hay setters, de eso ya se encarga el converter

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getDisplacement() {
        return displacement;
    }

    public String getEngine() {
        return engine;
    }

    public String getPower() {
        return power;
    }

    public String getTorque() {
        return torque;
    }

    public String getGearbox() {
        return gearbox;
    }

    public String getCooling() {
        return cooling;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public String getSeatHeight() {
        return seatHeight;
    }

    public String getFuelCapacity() {
        return fuelCapacity;
    }
}
