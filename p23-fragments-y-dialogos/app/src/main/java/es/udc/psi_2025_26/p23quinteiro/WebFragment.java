package es.udc.psi_2025_26.p23quinteiro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

import es.udc.psi_2025_26.p23quinteiro.databinding.FragmentWebBinding;


public class WebFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private static final String TAG = "_TAG";

    private String mParam1;

    private FragmentWebBinding binding;

    public WebFragment() {
        // Required empty public constructor
    }

    // En nuestra aplicación siempre llegará una url válida al fragmento
    // Pero este debe de ser independiente e interoperable, por lo que se comprueba si le llega una url
    // válida
    public static WebFragment newInstance(URL url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        String urlString;
        if(url != null){
            urlString= url.toString();
        } else{
            urlString = "https://fic.udc.es";
            Log.d(TAG, "Se ha intentado crear el Web Fragment con una URL no válida");
            Log.d(TAG, "Valor por defecto: " + urlString);
        }
        args.putString(ARG_PARAM1, urlString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWebBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.webview.loadUrl(mParam1);
    }

    // El componente que la llame debe verificar si la url válida y no nula.
    // Sea nuestra aplicación o cualquier otra.
    // Además será válido ya que tiene como parámetro un objeto tipo url
    // Java comprueba esto en su creación pero no que no sea nulo.
    // Pero aún así, como tiene que ser interoperable y no siempre controlamos lo que pasa al
    // otro lado ponemos la comprobación por si acaso.
    public void loadUrl(URL url) { // Se asume url válida...
        if(url!=null) {
            binding.webview.loadUrl(url.toString());
            Log.d(TAG, "loadUrl(): Actualizando URL");
        }
        Log.d(TAG, "loadUrl(): url nula");
    }
}
