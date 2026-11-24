package es.udc.psi_2025_26.p24quinteiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24quinteiro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_ARTICLE = "es.udc.psi_2025_26.KEY_ARTICLE";
    public static final String POSITION = "es.udc.psi_2025_26.POSITION";



    private ActivityMainBinding binding;

    private static final String TAG = "_TAG";
    private ArticlesAdapter mAdapter;

    ArrayList<Article> initialData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.butDelete.setOnClickListener(this);
        binding.butAdd.setOnClickListener(this);

        // inicialización de datos
        for (int i=0; i< 10; i++)
            initialData.add(new Article(i, getString(R.string.title_tile), getString(R.string.subtitle_tile), getString(R.string.description_tile)));
        initRecycler(initialData);
    }

    private final ActivityResultLauncher<Intent> my_startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                switch (resultCode) {
                    case Tile.RESULT_ITEM_DELETED:
                        if (data != null) {
                            int position = data.getIntExtra(POSITION, -1);
                            if (position != -1) {
                                initialData.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                Toast.makeText(getApplicationContext(), getString(R.string.item_deleted, position, initialData.size()), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    case Tile.RESULT_ITEM_CHANGED:
                        if (data != null) {
                            int position = data.getIntExtra(POSITION, -1);
                            Article updatedArticle = data.getParcelableExtra(KEY_ARTICLE);
                            if (position != -1 && updatedArticle != null) {
                                initialData.set(position, updatedArticle);
                                mAdapter.notifyItemChanged(position);
                                Toast.makeText(getApplicationContext(), getString(R.string.item_edited, position, initialData.size()), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    case RESULT_OK:
                        Log.d(TAG, "RESULT_OK");
                        if (data != null) {
                            Log.d(TAG, "RESULT_OK, data not null");
                            int position = data.getIntExtra(POSITION, -1);
                            if (position != -1)
                                Toast.makeText(getApplicationContext(), getString(R.string.tile_not_changed, position, initialData.size()), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        break;
                }
            });


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.but_delete){
            int size = initialData.size();
            initialData.clear();
            // notifyDataSetChanged deprecado
            // Además es mejor especificar lo que a cambiado, en vez de que lo tenga que descubrir
            mAdapter.notifyItemRangeRemoved(0, size);
            Toast.makeText(getApplicationContext(), getString(R.string.but_delete_action, size, 0),
                    Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.but_add){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title);
            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setText(R.string.dialog_elements_default); // 10 elementos por defecto
            builder.setView(input);
            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int elements = Integer.parseInt(input.getText().toString());
                    int init_index = initialData.size();
                    for (int ii = 0; ii < elements; ii++) {
                        initialData.add(new Article(init_index + ii, getString(R.string.title_tile), getString(R.string.subtitle_tile), getString(R.string.description_tile)));
                    }
                    mAdapter.notifyItemRangeInserted(init_index, elements);
                    Toast.makeText(getApplicationContext(), getString(R.string.but_add_action, elements, initialData.size()), Toast.LENGTH_SHORT).show();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void initRecycler(ArrayList<Article> articles) {
        mAdapter = new ArticlesAdapter (articles);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.articlesRv.setLayoutManager(linearLayoutManager);

        // onClick no funciona, ya que es un elemento posicional(...)
        // Crear el listner, que el usuario del adaptador pueda adaptar
        // Lisener hardcodeado en el adaptador
        // La forma correcta es declarar un interfaz en el adaptador, evento con método,
        // el view y el position
        // Clase adaptador referencia al lisener en el mismo adaptador
        // Objeto para asociar el lisener que yo quiera (diapo35)
        // Dentro del adaptador en el holder asocirlo a ese objeto
        // Cada elemento del tile podria haber botones distintos
        // Podría hacer un botón nulo
        // Cuando se toque cualquier sitio de la ventana se asocia ese listener
        // En esta práctica evento tocar la ventana. (Diapo 36)
        // Diapo 37
        // (...) Se dice el tile que se toca




        // Se pasa además del article serializado, la posición
        // Si bien es verdad que cada posición podría sacarse si guardamos el índice en cada article
        // Pero creo que un objeto de una lista no debe guardar su índice
        // Entoces se lo pasamos explícitamente ya que el objeto no tiene porque saber que está contenido en una lista
        mAdapter.setClickListener(new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Article article = mAdapter.getItem(position);
                Log.d("_TAG", " Item " + position  + " Title: " +
                        mAdapter.getItem(position).getTitle());
                Toast.makeText(getApplicationContext(), getString(R.string.tile_postion,position),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Tile.class);
                intent.putExtra(KEY_ARTICLE, article);
                intent.putExtra(POSITION, position);
                my_startActivityForResult.launch(intent);

            }
        });
        binding.articlesRv.setAdapter(mAdapter);
    }
}

