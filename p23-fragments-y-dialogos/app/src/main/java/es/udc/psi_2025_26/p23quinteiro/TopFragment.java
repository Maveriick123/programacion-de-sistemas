package es.udc.psi_2025_26.p23quinteiro;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import es.udc.psi_2025_26.p23quinteiro.databinding.FragmentTopBinding;


public class TopFragment extends Fragment {


    private FragmentTopBinding binding;

    private static final String TAG = "_TAG";


    //private static final String STATE_BUT_CLEAR = "state_but_clear";

    // Eventos que el fragmento notificará al exterior.
    // Deben de ser implementados por la actividad en la que residen.
    public interface OnTopFragmentSelectedListener {
        public void onTextListenerSelected(String text, boolean enabled);

        public void onUrlListener(URL url);

        public void onClearListener();
    }

    OnTopFragmentSelectedListener listener;

    // Aquí se comprueba que la actividad implementa la interfaz, es decir que es
    // capaz de responder a los eventos del fragmento.

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnTopFragmentSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " implement OnTopFragmentSelectedListener");
        }
    }

    public TopFragment() {
        // Required empty public constructor
    }

    //private boolean stateButClear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(savedInstanceState != null){
            stateButClear = savedInstanceState.getBoolean(STATE_BUT_CLEAR);
        }*/
    }

    /*@Override
    public void onSaveInstanceState(Bundle conf) {
        super.onSaveInstanceState(conf);
        conf.putBoolean(STATE_BUT_CLEAR, binding.butClear.isEnabled());

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopBinding.inflate(inflater, container, false);
        return (binding.getRoot());
    }

    /*
     * Set user interface
     */
    private void setUI() {

        //binding.butClear.setEnabled(stateButClear);

        binding.butA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick - Boton A");
                listener.onTextListenerSelected(binding.butA.getText().toString(),binding.butClear.isEnabled());
            }
        });

        binding.butB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick - Boton B");
                listener.onTextListenerSelected(binding.butB.getText().toString(), binding.butClear.isEnabled());
            }
        });

        binding.butUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                try {
                    String textoUrl = binding.etUrl.getText().toString();
                    url = new URL(textoUrl);
                } catch (MalformedURLException e) {
                    Toast.makeText(getContext(), R.string.bad_url, Toast.LENGTH_SHORT).show();
                    try {
                        url = new URL(getString(R.string.default_url));
                        Toast.makeText(getContext(), R.string.fallback_url, Toast.LENGTH_SHORT).show();

                    } catch (MalformedURLException ex) {
                        Log.d(TAG, "URL por defecto a fallado");
                        return; // Sólo podemos llamar al listener con una URL válida no nula
                        // Reacción en cadena sólo comprobamos en el primero.
                    }
                }
                Log.d(TAG, "onClick - Boton Url");
                listener.onUrlListener(url);
            }
        });

        binding.butClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick - Boton Clear");
                listener.onClearListener();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // Poner en una función declarativa.
        setUI();
    }

    public void setClearButton(boolean enabled) {
        binding.butClear.setEnabled(enabled);
    }
}