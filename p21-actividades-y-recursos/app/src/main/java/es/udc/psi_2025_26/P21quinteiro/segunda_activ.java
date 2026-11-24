package es.udc.psi_2025_26.P21quinteiro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.P21quinteiro.R;

// Si la app se va a segundo plano entonces se mantiene los datos onStop() y onPause()
// Si sales de forma voluntaria para atrás no

// Una actividad no puede ser abstracta
public class segunda_activ extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "segunda_activ";
    Button but1, but2, but3;
    // Para almacenar el estado de los botones...
    private static final String STATE_BUTTON_1 = "cnt1";
    private static final String STATE_BUTTON_2 = "cnt2";
    private static final String STATE_BUTTON_3 = "cnt3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segunda);
        Log.d(TAG, "segunda_activ: onCreate()");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Riesgo en ejecuciones ...
        but1 = findViewById(R.id.but1);
        but2 = findViewById(R.id.but2);
        but3 = findViewById(R.id.but3);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);

        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "segunda_activ: onClick() - Botón 3");
                incrementarValor((Button) v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button tmp_but = (Button) v;
        if (v.getId() == R.id.but1) {
            Log.d(TAG, "segunda_activ: onClick() - Botón 1");
        } else if (v.getId() == R.id.but2) {
            Log.d(TAG, "segunda_activ: onClick() - Botón 2");
        }
        incrementarValor(tmp_but);
    }

    private void incrementarValor(Button btn) {
        int valorActual = Integer.parseInt(btn.getText().toString());
        valorActual++;
        btn.setText(String.valueOf(valorActual));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_BUTTON_1, Integer.parseInt(but1.getText().toString()));
        outState.putInt(STATE_BUTTON_2, Integer.parseInt(but2.getText().toString()));
        outState.putInt(STATE_BUTTON_3, Integer.parseInt(but3.getText().toString()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        but1.setText(String.valueOf(savedInstanceState.getInt(STATE_BUTTON_1)));
        but2.setText(String.valueOf(savedInstanceState.getInt(STATE_BUTTON_2)));
        but3.setText(String.valueOf(savedInstanceState.getInt(STATE_BUTTON_3)));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "segunda_activ: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "segunda_activ: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "segunda_activ: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "segunda_activ: onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "segunda_activ: onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "segunda_activ: onDestroy()");
    }
}

