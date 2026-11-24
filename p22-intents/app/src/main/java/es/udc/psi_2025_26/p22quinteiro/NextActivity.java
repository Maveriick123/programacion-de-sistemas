package es.udc.psi_2025_26.p22quinteiro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p22quinteiro.databinding.ActivityMainBinding;
import es.udc.psi_2025_26.p22quinteiro.databinding.ActivityNextBinding;

public class NextActivity extends AppCompatActivity {

    ActivityNextBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_next);
        binding = ActivityNextBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Log.d("_TAG", "NextActivity: onCreate()");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Mejor sitio para recoger los datos en el onCreate()
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Boolean origenMsgPublic = true;
            // Usar la misma clave de bundle que se uso en el emisor (...)
            // Clave pública compatible y estandarizada de Android
            String text = bundle.getString(Intent.EXTRA_TEXT);

            // Si la llamada fue pública, o con la clave pública esa clave estará vacía
            // Entonces comprobamos la privada
            if (text == null) {
                text = bundle.getString("MY_KEY");
                origenMsgPublic = false;

            }
            binding.tvText.setText(text);
            Toast.makeText(getApplicationContext(), origenMsgPublic?R.string.toast_request_public:R.string.toast_request_private, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("_TAG", "NextActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("_TAG", "NextActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("_TAG", "NextActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("_TAG", "NextActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("_TAG", "NextActivity: onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("_TAG", "NextActivity: onRestart()");
    }
}