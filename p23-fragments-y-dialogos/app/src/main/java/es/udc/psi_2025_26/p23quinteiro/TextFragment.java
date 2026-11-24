package es.udc.psi_2025_26.p23quinteiro;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.udc.psi_2025_26.p23quinteiro.databinding.FragmentTextBinding;
import es.udc.psi_2025_26.p23quinteiro.databinding.FragmentTopBinding;


public class TextFragment extends Fragment {

    private static final String TAG = "_TAG";

    private FragmentTextBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;

    private static final String STATE_TEXT = "state_text";


    public interface OnTextFragmentListener {
        void onEnableClicked(boolean enabled);
    }

    OnTextFragmentListener listener;



    public TextFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance(String param1, boolean param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnTextFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " implement OnTextFragmentListener");
        }
    }

    private boolean mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Probando persistencia en fragmentos con el primer parámetro
        // Al girar la pantalla se mantiene el nombre del último bóton clicado
        if(savedInstanceState != null){
            mParam1 = savedInstanceState.getString(STATE_TEXT);
        }
        else if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        mParam2 = getArguments().getBoolean(ARG_PARAM2);


    }
    // Crea y devuelve la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // El binding contiene referencias a todos los elementos que tengan android:id.
        // Inflater crea todos los elementos visuales, objetos java a partir del XML que Android
        // puede mostrar. El binding es una clase que da acceso a ellos, asigna las referencias
        // a los objetos cuando se van creando a variables de la clase FragmentTextBinding.
        // container es el elemento padre, el Frame Layout.
        // attachToParent = false -> FragmentManager es el encargado de incrustarla.
        binding = FragmentTextBinding.inflate(inflater, container, false);
        // Devuelves el contenedor principal en este caso el Linear Layout
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
            binding.tvText.setText(mParam1);

            binding.butEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParam2 = !mParam2;
                Log.d(TAG, "onClick() - Boton Enable");
                listener.onEnableClicked(mParam2);
            }
        });
    }

    public void updateText(String text){
        binding.tvText.setText(text);
    }
    @Override
    public void onSaveInstanceState(Bundle conf) {
        super.onSaveInstanceState(conf);
        conf.putString(STATE_TEXT, binding.tvText.getText().toString());
    }
}