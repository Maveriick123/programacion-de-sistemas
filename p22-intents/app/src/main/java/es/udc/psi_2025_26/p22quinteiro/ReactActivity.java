package es.udc.psi_2025_26.p22quinteiro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p22quinteiro.databinding.ActivityNextBinding;
import es.udc.psi_2025_26.p22quinteiro.databinding.ActivityReactBinding;

public class ReactActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityReactBinding binding;


    // IMPORTANTE !!
    // Respondo con la clave publica si me llaman con la clave pública. Lo mismo con lo privada.
    private Boolean llamadaPrivada = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_react);
        binding = ActivityReactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Log.d("_TAG", "ReactActivity: onCreate()");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.butSota.setOnClickListener(this);
        binding.butCaballo.setOnClickListener(this);
        binding.butRey.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Usar la misma clave de bundle que se uso en el emisor (...)
            // Clave pública compatible y estandarizada de Android
            String text = bundle.getString(Intent.EXTRA_TEXT);
            llamadaPrivada = false;


            // Si la llamada fue pública, o con la clave pública esa clave estará vacía
            // Entonces comprobamos la privada
            if (text == null) {
                text = bundle.getString("MY_KEY");
                llamadaPrivada=true;

            }
            binding.tvOutput.setText(text);
            Toast.makeText(getApplicationContext(), llamadaPrivada? R.string.toast_request_private: R.string.toast_request_public, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Button tmp_button = (Button) v;
        Intent intent = new Intent();
        if(v.getId() == R.id.but_sota){
            Log.d("_TAG", "ReactActivity, onClick() Sota");
            intent.putExtra(llamadaPrivada? "MY_KEY": Intent.EXTRA_TEXT, tmp_button.getText().toString());
        } else if (v.getId() == R.id.but_caballo) {
            Log.d("_TAG", "ReactActivity, onClick() Caballo");
            intent.putExtra(llamadaPrivada? "MY_KEY": Intent.EXTRA_TEXT, tmp_button.getText().toString() );
        } else if(v.getId() == R.id.but_rey){
            Log.d("_TAG", "ReactActivity, onClick() Rey");
            intent.putExtra(llamadaPrivada? "MY_KEY": Intent.EXTRA_TEXT, tmp_button.getText().toString());
        }
        setResult(RESULT_OK, intent);
        finish(); // Harakiri
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("_TAG", "ReactActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("_TAG", "ReactActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("_TAG", "ReactActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("_TAG", "ReactActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("_TAG", "ReactActivity: onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("_TAG", "ReactActivity: onRestart()");
    }
}