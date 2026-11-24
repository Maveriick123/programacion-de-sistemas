package es.udc.psi_2025_26.p24quinteiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p24quinteiro.databinding.ActivityTileBinding;

public class Tile extends AppCompatActivity implements View.OnClickListener {

    // A partir de RESULT_FIRST_USER (1) en adelante podemos definir nuestros propios códigos
    // Los definimos en Tile por el principio de responsabilidad única, ya que es esta clase quien decide
    // los códigos de error que se van a generar.
    // También se podrían almacenar todas juntas en proyectos grandes en una clase auxiliar...
    public static final int RESULT_ITEM_CHANGED = RESULT_FIRST_USER + 1;
    public static final int RESULT_ITEM_DELETED = RESULT_FIRST_USER + 2;
    private ActivityTileBinding binding;

    private Article article;
    private int position;

    private String originalTitle;
    private String originalSubtitle;
    private String originalDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_tile);
        binding = ActivityTileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        article = getIntent().getParcelableExtra(MainActivity.KEY_ARTICLE);
        position = getIntent().getIntExtra(MainActivity.POSITION, -1);
        if (article != null) {
            binding.titleTileEt.setText(article.getTitle());
            binding.subtitleTileEt.setText(article.getSubtitle());
            binding.descriptionTileEt.setText(article.getDescription());
        }

        binding.butTileEliminar.setOnClickListener(this);
        binding.butTileEditar.setOnClickListener(this);
        binding.butTileOk.setOnClickListener(this);

        originalTitle = binding.titleTileEt.getText().toString();
        originalSubtitle = binding.subtitleTileEt.getText().toString();
        originalDescription = binding.descriptionTileEt.getText().toString();

    }

    @Override
    public void onClick(View v) {
        Intent resultIntent = new Intent();

        if (v.getId() == R.id.but_tile_eliminar) {
            resultIntent.putExtra(MainActivity.POSITION, position);
            setResult(RESULT_ITEM_DELETED, resultIntent);
            finish();

        } else if (v.getId() == R.id.but_tile_editar) {
            binding.titleTileEt.setEnabled(true);
            binding.subtitleTileEt.setEnabled(true);
            binding.descriptionTileEt.setEnabled(true);

            Toast.makeText(this, R.string.modo_edicion, Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.but_tile_ok) {

            // Independientemente de si se ha modificado o no retornaremos la posición...
            resultIntent.putExtra(MainActivity.POSITION, position);
            if (article != null) {
                String currentTitle = binding.titleTileEt.getText().toString();
                String currentSubtitle = binding.subtitleTileEt.getText().toString();
                String currentDescription = binding.descriptionTileEt.getText().toString();

                boolean tileChanged = !currentTitle.equals(originalTitle) ||
                        !currentSubtitle.equals(originalSubtitle) ||
                        !currentDescription.equals(originalDescription);

                if(tileChanged){
                    article.setTitle(currentTitle);
                    article.setSubtitle(currentSubtitle);
                    article.setDescription(currentDescription);

                    resultIntent.putExtra(MainActivity.KEY_ARTICLE, article);
                    setResult(RESULT_ITEM_CHANGED, resultIntent);
                }else
                    // Si no se modifico ni elimino el tile por defecto devolvemos RESULT_OK
                    setResult(RESULT_OK, resultIntent);

                finish(); // harakiri
            }
        }
    }
}