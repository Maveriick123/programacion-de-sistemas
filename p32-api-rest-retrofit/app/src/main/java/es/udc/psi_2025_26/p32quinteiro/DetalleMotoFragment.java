package es.udc.psi_2025_26.p32quinteiro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.List;

import es.udc.psi_2025_26.p32quinteiro.data.RetrofitClient;
import es.udc.psi_2025_26.p32quinteiro.databinding.FragmentDetalleMotoBinding;
import es.udc.psi_2025_26.p32quinteiro.domain.Moto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleMotoFragment extends Fragment {

    private static final String ARG_MAKE = "es.udc.psi_2025_26.ARG_MAKE";
    private static final String ARG_MODEL = "es.udc.psi_2025_26.ARG_MODEL";
    private static final String ARG_YEAR = "es.udc.psi_2025_26.ARG_YEAR";

    private String mMake;
    private String mModel;
    private String mYear;

    private FragmentDetalleMotoBinding binding;

    public DetalleMotoFragment() { } // Constructor vacío obligatorio

    // Se crea con esto y no con new + parámetros: si Android recrea el fragmento
    // (girar la pantalla) usa el constructor vacío y los argumentos se conservan
    public static DetalleMotoFragment newInstance(String make, String model, String year) {
        DetalleMotoFragment fragment = new DetalleMotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MAKE, make);
        args.putString(ARG_MODEL, model);
        args.putString(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMake = getArguments().getString(ARG_MAKE);
            mModel = getArguments().getString(ARG_MODEL);
            mYear = getArguments().getString(ARG_YEAR);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleMotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // El título ya lo sabemos sin esperar al servidor
        binding.detailTitle.setText(getString(R.string.detail_title, mMake, mModel));
        getDetalle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // PETICIÓN 2
    private void getDetalle() {
        showLoading(true);
        Log.d(MainActivity.TAG, "Pidiendo detalle de: " + mMake + " " + mModel + " " + mYear);

        RetrofitClient.getInstance().getMotoDetail(mMake, mModel, mYear)
                .enqueue(new Callback<List<Moto>>() {

            @Override
            public void onResponse(@NonNull Call<List<Moto>> call,
                                   @NonNull Response<List<Moto>> response) {
                if (binding == null) return; // Ya se volvió atrás
                showLoading(false);

                if (response.isSuccessful() && response.body() != null
                        && !response.body().isEmpty()) {
                    Log.d(MainActivity.TAG, "Response OK: " + response.code());
                    showMoto(elegirMoto(response.body()));
                } else if (response.isSuccessful()) {
                    // 200 pero array vacío
                    Log.d(MainActivity.TAG, "Detalle vacío");
                    showMessage(R.string.detail_not_found);
                } else {
                    Log.d(MainActivity.TAG, "Response fails: " + response.code());
                    showMessage(R.string.detail_not_found);
                    Toast.makeText(requireContext(), R.string.error_general,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Moto>> call, @NonNull Throwable t) {
                Log.d(MainActivity.TAG, "Response fails: " + t.getMessage());
                if (binding == null) return;
                showLoading(false);
                showMessage(R.string.detail_not_found);
                Toast.makeText(requireContext(), R.string.error_general,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // El parámetro model de la API busca por "contiene": pidiendo "KLR 650"
    // también vienen "KLR 650 Adventure"... así que nos quedamos con la exacta
    // y si no está, con la primera
    private Moto elegirMoto(List<Moto> resultado) {
        for (Moto m : resultado) {
            if (mModel != null && mModel.equalsIgnoreCase(m.getModel())) return m;
        }
        return resultado.get(0);
    }

    private void showMoto(Moto moto) {
        setCampo(binding.detailYear, R.string.detail_year, moto.getYear());
        setCampo(binding.detailType, R.string.detail_type, moto.getType());
        setCampo(binding.detailDisplacement, R.string.detail_displacement, moto.getDisplacement());
        setCampo(binding.detailEngine, R.string.detail_engine, moto.getEngine());
        setCampo(binding.detailPower, R.string.detail_power, moto.getPower());
        setCampo(binding.detailTorque, R.string.detail_torque, moto.getTorque());
        setCampo(binding.detailGearbox, R.string.detail_gearbox, moto.getGearbox());
        setCampo(binding.detailCooling, R.string.detail_cooling, moto.getCooling());
        setCampo(binding.detailWeight, R.string.detail_weight, moto.getTotalWeight());
        setCampo(binding.detailSeatHeight, R.string.detail_seat_height, moto.getSeatHeight());
        setCampo(binding.detailFuel, R.string.detail_fuel, moto.getFuelCapacity());
        binding.detailFields.setVisibility(View.VISIBLE);
    }

    // La API manda campos a null a veces, para que no salga "null" en pantalla
    private void setCampo(TextView tv, @StringRes int formato, String valor) {
        String texto = (valor == null || valor.isEmpty()) ? getString(R.string.no_data) : valor;
        tv.setText(getString(formato, texto));
    }

    private void showLoading(boolean loading) {
        binding.detailProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) {
            binding.detailFields.setVisibility(View.GONE);
            binding.detailMessage.setVisibility(View.GONE);
        }
    }

    private void showMessage(@StringRes int message) {
        binding.detailMessage.setText(message);
        binding.detailMessage.setVisibility(View.VISIBLE);
        binding.detailFields.setVisibility(View.GONE);
    }
}
