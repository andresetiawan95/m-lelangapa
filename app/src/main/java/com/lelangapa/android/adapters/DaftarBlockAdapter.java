package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.ownerblocklist.UnblockToggler;
import com.lelangapa.android.resources.BlockResources;

import java.util.ArrayList;

/**
 * Created by andre on 19/04/17.
 */

public class DaftarBlockAdapter extends RecyclerView.Adapter<DaftarBlockAdapter.ViewHolder>{
    private Context context;
    private ArrayList<BlockResources> listBlock;
    private UnblockToggler unblockToggler;

    private PopupMenu popupMenuOptions;
    public DaftarBlockAdapter(Context context, ArrayList<BlockResources> listBlock, UnblockToggler unblockToggler)
    {
        this.context = context;
        this.listBlock = listBlock;
        this.unblockToggler = unblockToggler;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_namaUser;
        public ImageView icon_options;
        public ViewHolder(View view) {
            super(view);
            textView_namaUser = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_block_name);
            icon_options = (ImageView) view.findViewById(R.id.fragment_detail_barang_daftar_block_option);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_barang_daftar_block_noempty_layout_items, parent, false);
        return new DaftarBlockAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BlockResources blockResources = listBlock.get(position);
        holder.textView_namaUser.setText(blockResources.getNamaUserBlocked());
        holder.icon_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenuOptions = new PopupMenu(context, v);
                popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.unblock_popup_menu:
                                unblockToggler.setBidderIDToUnblock(blockResources.getIdUserBlocked());
                                unblockToggler.showAlertDialog();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenuOptions.inflate(R.menu.unblock_popup_menu);
                popupMenuOptions.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBlock.size();
    }

    public void updateDataset(ArrayList<BlockResources> listBlock)
    {
        this.listBlock = listBlock;
        notifyDataSetChanged();
    }
}
