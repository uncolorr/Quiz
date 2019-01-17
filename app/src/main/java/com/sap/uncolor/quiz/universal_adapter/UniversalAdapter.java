package com.sap.uncolor.quiz.universal_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.ItemModel;
import com.sap.uncolor.quiz.application.App;

import java.util.ArrayList;
import java.util.List;

public class UniversalAdapter extends RecyclerView.Adapter {

    private List<ItemModel> items = new ArrayList<>();

    @NonNull
    private final SparseArray<ViewRenderer> renderers = new SparseArray<>();

    @Override
    public
    RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final ViewRenderer renderer = renderers.get(viewType);
        if (renderer != null) {
            return renderer.createViewHolder(parent);
        }

        throw new RuntimeException("Not supported Item View Type: " + viewType);
    }

    public void registerRenderer(@NonNull final ViewRenderer renderer) {
        final int type = renderer.getType();
        App.Log("type added: " + type);

        if (renderers.get(type) == null) {
            renderers.put(type, renderer);
            App.Log("renderers size: " + renderers.size());
        } else {
            throw new RuntimeException("ViewRenderer already exist with this type: " + type);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public
    void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemModel item = items.get(position);
        final ViewRenderer renderer = renderers.get(item.getType());
        if (renderer != null) {
            renderer.bindView(item, holder);
        } else {
            throw new RuntimeException("Not supported View Holder: " + holder);
        }
    }

    public void add(ItemModel model){
        items.add(model);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(List<ItemModel> models){
        items.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
