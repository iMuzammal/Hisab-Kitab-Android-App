package com.example.AccountingAppAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AccountingAppAdmin.DataModel.ClientRetrunModel;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;

import java.util.ArrayList;

public class RetrunVeiwBillAdapter  extends RecyclerView.Adapter<RetrunVeiwBillAdapter.ViewHolder> {

    Context context;
    ArrayList<ClientRetrunModel> arrayList;

    public RetrunVeiwBillAdapter(Context context, ArrayList<ClientRetrunModel> arrayList ) {
        this.context = context;
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public RetrunVeiwBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout._show_bill_eturn, parent, false);
        RetrunVeiwBillAdapter.ViewHolder viewHolder = new RetrunVeiwBillAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RetrunVeiwBillAdapter.ViewHolder holder, int position) {


        final ClientRetrunModel clientRetrunModel= arrayList.get(position);
        holder.no.setText(clientRetrunModel.getCount());
        holder.productname.setText(clientRetrunModel.getName());
        holder.stock.setText(clientRetrunModel.getStock());
        holder.price.setText(clientRetrunModel.getBuyerPrice()+".Rs");



}

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView no,productname,stock,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.no);
            productname = itemView.findViewById(R.id.poductname);
            stock = itemView.findViewById(R.id.stock);
            price = itemView.findViewById(R.id.price);

        }
    }
}
