package com.example.AccountingAppAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AccountingAppAdmin.Adapter.ShowBillAdapter;
import com.example.AccountingAppAdmin.DataModel.PurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;

import java.util.ArrayList;

public class ReturnShowBillAdapter  extends RecyclerView.Adapter<ReturnShowBillAdapter.ViewHolder> {

    Context context;
    ArrayList<RetunPurchaseModel> arrayList;



    public ReturnShowBillAdapter(Context context, ArrayList<RetunPurchaseModel> arrayList ) {
        this.context = context;
        this.arrayList = arrayList;


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_bill_eturn, parent, false);
        ReturnShowBillAdapter.ViewHolder viewHolder = new ReturnShowBillAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ReturnShowBillAdapter.ViewHolder holder, int position) {

        final RetunPurchaseModel retunPurchaseModel= arrayList.get(position);
        holder.no.setText(retunPurchaseModel.getCount());
        holder.productname.setText(retunPurchaseModel.getName());
        holder.stock.setText(retunPurchaseModel.getStock());
        holder.price.setText(retunPurchaseModel.getBuyerPrice()+".Rs");
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
