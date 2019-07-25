package com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.DrawableUtils;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comentario;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_HQ_Producao  extends RecyclerView.Adapter<Adapter_HQ_Producao.MyViewHolder> implements DraggableItemAdapter<Adapter_HQ_Producao.MyViewHolder> {
    private static  final String ARQUIVO_PREFERENCIA_listaImagem ="Lista_imagem";
    private List<HQ_Model> listHQs;
    private Context context;


    public Adapter_HQ_Producao(List<HQ_Model> lista,Context c){
        setHasStableIds(true);
        this.listHQs=lista;
        this.context=c;

    }
    @Override
    public long getItemId(int position) {
        return listHQs.get(position).getImg_id(); // need to return stable (= not change even after reordered) value
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fanarts_pag_inicial, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         HQ_Model hq= listHQs.get(position);
            Log.i("lsditre7890", String.valueOf(hq.getImg_name()));
            Uri uri = Uri.parse("file:///" +hq.getImg_name());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.img.setController(controller);

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setProgressBarImage(new CircleProgressDrawable())
                    //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                    .build();
            holder.img.setHierarchy(hierarchy);

        //Log.i("lsditre789", String.valueOf(hq.getImg_id()));
        if(hq.getNova_pos()==null){
            holder.posicao.setText(hq.getAntiga_pos());
        }else{
            holder.posicao.setText(hq.getNova_pos());
        }

        // set background resource (target view ID: container)
        final DraggableItemState dragState = holder.getDragState();

        if (dragState.isUpdated()) {
            int bgResId;

            if (dragState.isActive()) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.container.getForeground());
            } else if (dragState.isDragging()) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.container.setBackgroundResource(bgResId);
        }

    }



    @Override
    public int getItemCount() {
        return listHQs == null ? 0 : listHQs.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        HQ_Model hq= listHQs.remove(fromPosition);
        listHQs.add(toPosition, hq);
       // if(toPosition===null){}
        hq.setAntiga_pos(String.valueOf(fromPosition));
        hq.setNova_pos(String.valueOf(toPosition));

    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public class MyViewHolder extends AbstractDraggableItemViewHolder {
        SimpleDraweeView img;
        TextView posicao;
        FrameLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
           img=itemView.findViewById(R.id.iconefanart);
           posicao=itemView.findViewById(R.id.textpos);
        }
    }
}
