package org.wikipedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import android.support.v7.widget.RecyclerView;

import org.wikipedia.activity.detailview.DetailImageActivity;
import org.wikipedia.activity.detailview.DetailImageFragment;
import org.wikipedia.activity.search.MainActivity;
import org.wikipedia.activity.search.SearchResultFragment;
import org.wikipedia.model.WikiData;

import java.util.List;

/**
 * Created by SwethAmbati on 6/15/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private Context context;
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
    MainActivity mActivity;
    private List<WikiData> wikiDataList;
    LayoutInflater inflater;


    public ListAdapter(Context context, List<WikiData> wikiDataList) {
        this.context = context;
        this.wikiDataList = wikiDataList;


    }

    public ListAdapter(Context context, List<WikiData> wikiDataList, MainActivity mActivity) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.wikiDataList = wikiDataList;
        this.mActivity = mActivity;
    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ListViewHolder(convertView);
    }


    @Override
    public int getItemCount() {
        return wikiDataList.size();
    }


    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {


        holder.tv_title.setText(wikiDataList.get(position).getTitle());

        if (wikiDataList.get(position).getImgThumbnailUrl() != null) {
            Glide.with(context).load(wikiDataList.get(position).getImgThumbnailUrl()).into(holder.image);
        } else {

            holder.image.setImageResource(R.drawable.noimage);
        }
        holder.tv_title.setTag(position);
        holder.cell_layout.setTag(position);
        holder.cell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = wikiDataList.get((Integer) v.getTag()).getTitle();
                final String image = wikiDataList.get((Integer) v.getTag()).getImgThumbnailUrl();


                MainActivity activity = (MainActivity) context;
                Fragment fragment = new DetailImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(TITLE, title);
                bundle.putString(IMAGE, image);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_search, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView image;
        LinearLayout cell_layout;


        public ListViewHolder(View itemView) {
            super(itemView);
            cell_layout = (LinearLayout) itemView.findViewById(R.id.cell_layout);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}
