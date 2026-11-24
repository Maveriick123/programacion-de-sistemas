package es.udc.psi_2025_26.p32quinteiro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.udc.psi_2025_26.p32quinteiro.data.RetrofitClient;
import es.udc.psi_2025_26.p32quinteiro.databinding.FragmentListaMotosBinding;
import es.udc.psi_2025_26.p32quinteiro.domain.Moto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaMotosFragment extends Fragment {

    // Los fragmentos no se hablan entre ellos, la Activity hace de gestora
    // Tendrá que implementar el método
    // Los fragmentos deben de ser interoperables
    // Es decir pueden pertenecer a cualquier actividad
    // Si implementa los métodos requeridos
    public interface OnMotoSeleccionadaListener {
        void onMotoSeleccionada(Moto moto);
    }

    private OnMotoSeleccionadaListener listener;

    private FragmentListaMotosBinding binding;
    private MotosAdapter mAdapter;

    private final ArrayList<Moto> motos = new ArrayList<>();

    public ListaMotosFragment() { } // Constructor vacío obligatorio

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // La Activity tiene que implementar la interfaz
        try {
            listener = (OnMotoSeleccionadaListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " implement OnMotoSeleccionadaListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // El false es para que se encargue FragmentManager de incrustar el fragmento en la vista
        // de la actividad
        binding = FragmentListaMotosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler();

        // Usamos lambda: View.OnClickListener
        binding.butSearch.setOnClickListener(v -> buscar());

        // Primera vez: buscamos la marca por defecto. Si ya hay motos (volvemos
        // del detalle) no se repite la petición
        if (motos.isEmpty()) buscar();
        else showList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Que no se quede colgando la vista vieja
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initRecycler() {
        mAdapter = new MotosAdapter(motos);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.motosRv.setLayoutManager(linearLayoutManager);
        // Rayita entre filas, como en el git de las diapositivas
        binding.motosRv.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // Al pulsar no abrimos nada aquí, se lo decimos a la Activity
        // y ella cambia al fragmento del detalle
        mAdapter.setClickListener((v, position) -> {
            Moto moto = mAdapter.getItem(position);
            Log.d(MainActivity.TAG, "Item " + position + " Model: " + moto.getModel());
            listener.onMotoSeleccionada(moto);
        });
        binding.motosRv.setAdapter(mAdapter);
    }

    // PETICIÓN 1
    private void buscar() {
        String marca = binding.etMake.getText() != null
                ? binding.etMake.getText().toString().trim() : "";

        if (marca.isEmpty()) {
            binding.etMake.setError(getString(R.string.error_make_empty));
            return;
        }
        if (BuildConfig.API_NINJAS_KEY.isEmpty()) {
            showEmptyView(R.string.error_no_api_key);
            return;
        }

        // Progress Bar
        showLoading();
        Log.d(MainActivity.TAG, "Buscando motos de: " + marca);

        RetrofitClient.getInstance().getMotosByMake(marca).enqueue(new Callback<List<Moto>>() {

            @Override
            public void onResponse(@NonNull Call<List<Moto>> call,
                                   @NonNull Response<List<Moto>> response) {
                // Si el fragmento ya no esta
                // No se toca nada
                if (binding == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(MainActivity.TAG, "Response OK: " + response.code());

                    int size = motos.size();
                    motos.clear();
                    mAdapter.notifyItemRangeRemoved(0, size);
                    motos.addAll(response.body());
                    mAdapter.notifyItemRangeInserted(0, motos.size());

                    if (motos.isEmpty()) showEmptyView(R.string.list_no_results);
                    else showList();
                } else {
                    // Error HTTP (401 clave mal, 400 parámetros...)
                    Log.d(MainActivity.TAG, "Response fails: " + response.code());
                    // Para evitar errores ocultos, mostrarselos al usuario
                    showError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Moto>> call, @NonNull Throwable t) {
                // Sin red, timeout o el JSON no cuadra con Moto
                Log.d(MainActivity.TAG, "Response fails: " + t.getMessage());
                if (binding == null) return;
                // Para evitar errores ocultos, ...
                showError();
            }
        });
    }

    // Estados de la UI: cargando, lista, sin resultados y error
    private void showLoading() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.emptyView.setVisibility(View.GONE);
        binding.motosRv.setVisibility(View.GONE);
    }

    private void showList() {
        binding.progress.setVisibility(View.GONE);
        binding.emptyView.setVisibility(View.GONE);
        binding.motosRv.setVisibility(View.VISIBLE);
    }

    private void showEmptyView(@StringRes int message) {
        binding.emptyView.setText(message);
        binding.progress.setVisibility(View.GONE);
        binding.emptyView.setVisibility(View.VISIBLE);
        binding.motosRv.setVisibility(View.GONE);
    }

    private void showError() {
        showEmptyView(R.string.list_no_results);
        Toast.makeText(requireContext(), R.string.error_general, Toast.LENGTH_SHORT).show();
    }
}
