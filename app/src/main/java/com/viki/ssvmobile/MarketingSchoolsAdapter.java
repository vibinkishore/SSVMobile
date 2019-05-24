package com.viki.ssvmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MarketingSchoolsAdapter extends RecyclerView.Adapter<MarketingSchoolsAdapter.msHolder>{

    List<MarketingSchoolsModel> msList;
    Context context;

    public MarketingSchoolsAdapter(List<MarketingSchoolsModel> msList, Context context) {
        this.msList = msList;
        this.context = context;
    }

    @Override
    public msHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.marketing_schools_card,parent,false);
        msHolder myHoder = new msHolder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MarketingSchoolsAdapter.msHolder holder, int position) {
        MarketingSchoolsModel list = msList.get(position);
        holder.msName.setText(list.getMsName());
        holder.msContactEmail.setText(list.getMsContactEmail());
        holder.msContactNumber.setText(list.getMsContactNumber());
        holder.msContactPerson.setText(list.getMsContactPerson());
        holder.msRemarks.setText(list.getMsRemarks());
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        if(msList.size()==0){arr = 0;}
        else{arr=msList.size();}
        return arr;
    }

    class msHolder extends RecyclerView.ViewHolder{
        TextView msName,msContactPerson,msContactNumber,msContactEmail,msRemarks;


        public msHolder(View itemView) {
            super(itemView);
            msName = itemView.findViewById(R.id.marketingSchoolName);
            msContactPerson = itemView.findViewById(R.id.contactPerson);
            msContactNumber = itemView.findViewById(R.id.contactNumber);
            msContactEmail = itemView.findViewById(R.id.contactEmail);
            msRemarks = itemView.findViewById(R.id.remarks);

        }
    }
}
