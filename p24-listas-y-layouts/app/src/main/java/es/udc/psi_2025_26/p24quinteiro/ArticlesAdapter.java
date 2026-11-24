package es.udc.psi_2025_26.p24quinteiro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24quinteiro.databinding.ItemBinding;


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onClick(View view, int position);
    } // Interfaz asociado al objeto que estoy manejando


    // -------------------------------------------------------------------
    // NOTA(Conclusión del static discutido en clase...)
    // Al quitar el static podemos tener adaptadores distintos, con distintos
    // listeners. Eso si el garbache colector al no tener static en la clase viewholder
    // interna, guarda una referencia oculta al adaptador(padre),
    // por lo que pueden haber fugas de memoria...
    // ---------------------------------------------------------------------
    private OnItemClickListener clickListener;

    // Se usa en el main para conectar la acción del click con el adaptador
    public void setClickListener(OnItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }
    private final ArrayList<Article> mDataset;

    // ViewHolder guarda las referencias a las vistas de cada elemento
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemBinding binding;

        // Se asigna el evento de clic al item entero
        public MyViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(Article article) {
            binding.itemTitle.setText(article.getTitle());
            binding.itemSubtitle.setText(article.getSubtitle());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getBindingAdapterPosition());
        }
    }

    public ArticlesAdapter(ArrayList<Article> myDataset) {

        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ArticlesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        ItemBinding binding = ItemBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Article getItem(int position) {
        return mDataset.get(position);
    }


}