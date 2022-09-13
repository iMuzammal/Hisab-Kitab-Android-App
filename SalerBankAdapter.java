package com.example.AccountingAppAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AccountingAppAdmin.Adapter.BuyerBankAdapter;
import com.example.AccountingAppAdmin.DataModel.PaymentMethodModel;

import java.util.ArrayList;

public class SalerBankAdapter extends RecyclerView.Adapter<SalerBankAdapter.ViewHolder> {
    Context context;
    ArrayList<PaymentMethodModel> arrayList;

    public SalerBankAdapter(Context context, ArrayList<PaymentMethodModel> arrayList ) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SalerBankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saleradaper_itme, parent, false);
        SalerBankAdapter.ViewHolder viewHolder = new SalerBankAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalerBankAdapter.ViewHolder holder, int position) {
        final PaymentMethodModel paymentMethodModel= arrayList.get(position);
        holder.Amount.setText(paymentMethodModel.getAmount()+".Rs");
        holder.time.setText(paymentMethodModel.getTimeanddate());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Amount,time;
        Button View;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            Amount = itemView.findViewById(R.id.amountpaid);
        }
    }
}
