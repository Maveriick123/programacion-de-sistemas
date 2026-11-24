package es.udc.psi_2025_26.p32quinteiro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi_2025_26.p32quinteiro.databinding.RowMotoBinding;
import es.udc.psi_2025_26.p32quinteiro.domain.Moto;

public class MotosAdapter
        extends RecyclerView.Adapter<MotosAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private OnItemClickListener clickListener;

    // Se usa en el fragmento para conectar el click con el adaptador
    public void setClickListener(OnItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    private final ArrayList<Moto> mDataset;

    // ViewHolder guarda las referencias a las vistas de cada fila (row_moto.xml)
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RowMotoBinding binding;

        // Se asigna el evento de clic a la fila entera
        public MyViewHolder(RowMotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        // Cómo se pinta una moto en la fila: modelo, año y tipo debajo
        public void bind(Moto moto) {
            binding.rowModel.setText(moto.getModel());
            binding.rowSubtitle.setText(binding.getRoot().getContext().getString(
                    R.string.row_subtitle, moto.getYear(), moto.getType()));
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getBindingAdapterPosition());
        }
    }

    public MotosAdapter(ArrayList<Moto> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public MotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        RowMotoBinding binding = RowMotoBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Moto getItem(int position) {
        return mDataset.get(position);
    }
}
