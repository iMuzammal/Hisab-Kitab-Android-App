package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.Adapter.BillViewAdapter;
import com.example.AccountingAppAdmin.Adapter.VedenProductListAdapter;
import com.example.AccountingAppAdmin.DataModel.BuyerBalance;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseTemplate;
import com.example.AccountingAppAdmin.DataModel.VenderBalance;
import com.example.AccountingAppAdmin.DataModel.VenderBillCounter;
import com.example.AccountingAppAdmin.DataModel.VenderBillModelProduct;
import com.example.AccountingAppAdmin.Fragment.MyAccountFragment;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BillView extends AppCompatActivity {

    Intent intent;
    String vender, payment, tax, address, Final_total, tod, discount, getVender,createvendername;
    DatabaseReference databaseReference;
    TextView name, clientname, id, adress, contact, timeanddate2, gst, subtotal, finaltotal, discountMargin;
    CreateVenderModel createVenderModel;
    VenderBillModelProduct venderBillModelProduct;
    ArrayList<VenderBillModelProduct> arrayList = new ArrayList<>();
    BillViewAdapter billViewAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    InventryModel inventryModel;
    long total;
    Button updateVender;
    PurchaseBillModel purchaseBillModel;
    VenderBillCounter venderBillCounter;
    int venderCounter;
    BuyerBalance buyerBalance;
    int balance, venderacount;
    VenderBalance venderBalance;
    String discountvalue,sumvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        intent = getIntent();
        vender = intent.getStringExtra("vender");
        payment = intent.getStringExtra("payment");
        tax = intent.getStringExtra("tax");
        address = intent.getStringExtra("address");
        discount = intent.getStringExtra("discount");
        createvendername = intent.getStringExtra("createvendername");
        name = findViewById(R.id.name);
        clientname = findViewById(R.id.clientname);
        id = findViewById(R.id.id);
        adress = findViewById(R.id.adress);
        subtotal = findViewById(R.id.total);
        finaltotal = findViewById(R.id.finaltotal);
        contact = findViewById(R.id.contact);
        discountMargin = findViewById(R.id.discount);
        discountMargin.setText(discount + "%");
        gst = findViewById(R.id.gst);
        updateVender = findViewById(R.id.updateVender);
        gst.setText(tax + "%");
        timeanddate2 = findViewById(R.id.timeanddate2);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat sdf = new SimpleDateFormat("'Date-   'dd.MM.yyyy'  Time-' HH:mm:ss");
        tod = sdf.format(new Date());
        recyclerView = (RecyclerView) findViewById(R.id.addMoreList_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);




        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("BuyerBalance").child("Vender-Acount").child(vender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    venderBalance = dataSnapshot.getValue(VenderBalance.class);
                    if (venderBalance != null) {

                        venderacount = venderBalance.getBalance();


                    } else {

                    }

                } else {

                    venderBalance = new VenderBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), 0);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Vender-Acount").child(vender).setValue(venderBalance);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("BuyerBalance").child("Total-Balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    buyerBalance = dataSnapshot.getValue(BuyerBalance.class);
                    if (buyerBalance != null) {

                        balance = buyerBalance.getBalance();


                    } else {

                    }

                } else {

                    buyerBalance = new BuyerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), 0);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Total-Balance").setValue(buyerBalance);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("VenderBill-Counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    venderBillCounter = dataSnapshot.getValue(VenderBillCounter.class);
                    if (venderBillCounter != null) {
                        venderCounter = venderBillCounter.getCount();


                    } else {

                    }

                } else {

                    venderBillCounter = new VenderBillCounter(0);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("VenderBill-Counter").setValue(venderBillCounter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Vender").child(vender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    createVenderModel = dataSnapshot.getValue(CreateVenderModel.class);
                    if (createVenderModel != null) {


                        name.setText(createVenderModel.getShopeName());
                        clientname.setText(createVenderModel.getVenderName());
                        adress.setText(createVenderModel.getAdress());
                        contact.setText(createVenderModel.getContactNo());
                        timeanddate2.setText(tod);


                    } else {


                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Vender-Billing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    venderBillModelProduct = snapshot.getValue(VenderBillModelProduct.class);
                    if (venderBillModelProduct != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(venderBillModelProduct);
                        if (venderBillModelProduct.getBuyerPrice() != null) {
                            id.setText(venderBillModelProduct.getCount());

                            long stock = Integer.parseInt(venderBillModelProduct.getStock());
                            long buyerprice = Integer.parseInt(venderBillModelProduct.getBuyerPrice());
                            total = total + stock * buyerprice;
//
                        } else {

                        }

                        subtotal.setText(total + "-/Rs");
                        long discountvalue = Integer.parseInt(discount);
                        long totalvalue = total * discountvalue / 100;
                        sumvalue=Long.toString(total-totalvalue);
                        finaltotal.setText(total - totalvalue + "-/Rs");
//
                    }


                }


                billViewAdapter = new BillViewAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(billViewAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                billViewAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        updateVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Vender-Billing").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            venderBillModelProduct = snapshot.getValue(VenderBillModelProduct.class);
                            if (venderBillModelProduct != null) {
//                        msg.setVisibility(View.GONE);

                                arrayList.add(venderBillModelProduct);
                                if (venderBillModelProduct.getBuyerPrice() != null) {

                                    final String stockget = venderBillModelProduct.getStock();
                                    String token = venderBillModelProduct.getCount();
                                    String nameid = venderBillModelProduct.getName();
                                    getVender = venderBillModelProduct.getVender();
                                    final String node = token + "-  " + nameid;
                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            inventryModel = dataSnapshot.getValue(InventryModel.class);
                                            long stock_allProduct = Integer.parseInt(inventryModel.getStock());
                                            long stock_billing = Integer.parseInt(stockget);
                                            stock_allProduct = stock_allProduct + stock_billing;
                                            String sum_stock = Long.toString(stock_allProduct);
                                            dataSnapshot.getRef().child("stock").setValue(sum_stock);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }


                                    });

                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(venderBillModelProduct.getCategory()).child(venderBillModelProduct.getSubcategory()).child(venderBillModelProduct.getSubcategoryto()).child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            inventryModel = dataSnapshot.getValue(InventryModel.class);
                                            String s_stock = inventryModel.getStock();
                                            int stock_allProduct = Integer.parseInt(s_stock);
                                            int stock_billing = Integer.parseInt(stockget);
                                            stock_allProduct = stock_allProduct + stock_billing;
                                            String sum_stock = Integer.toString(stock_allProduct);
                                            dataSnapshot.getRef().child("stock").setValue(sum_stock);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




                                } else {

                                }

                                Final_total = Long.toString(total);
                                purchaseBillModel = new PurchaseBillModel(SharedPreferencesManager.getSomeStringValue(getApplication()), venderBillModelProduct.getBuyerPrice(), venderBillModelProduct.getName(), venderBillModelProduct.getSalerPrice(), venderBillModelProduct.getStock(), venderBillModelProduct.getCount(), tod, venderBillModelProduct.getVender(), venderBillModelProduct.getCategory(), venderBillModelProduct.getSubcategory(), venderBillModelProduct.getSubcategoryto(), venderBillModelProduct.getUnit(), Final_total, venderCounter + "", vender, discount, tax);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("All-Bill").child(venderCounter + "").child(venderBillModelProduct.getCount() + "-  " + venderBillModelProduct.getName()).setValue(purchaseBillModel);

                                purchaseBillModel = new PurchaseBillModel(SharedPreferencesManager.getSomeStringValue(getApplication()), venderBillModelProduct.getBuyerPrice(), venderBillModelProduct.getName(), venderBillModelProduct.getSalerPrice(), venderBillModelProduct.getStock(), venderBillModelProduct.getCount(), tod, venderBillModelProduct.getVender(), venderBillModelProduct.getCategory(), venderBillModelProduct.getSubcategory(), venderBillModelProduct.getSubcategoryto(), venderBillModelProduct.getUnit(), Final_total, venderCounter + "", vender, discount, tax);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("Vender-Name").child(venderBillModelProduct.getVender()).child(venderCounter + "").child(venderBillModelProduct.getCount() + "-  " + venderBillModelProduct.getName()).setValue(purchaseBillModel);
                            }


                        }



                         discountvalue= sumvalue;
                        final int buyerTotal = Integer.parseInt(discountvalue);
                        int Totalbalance = balance + buyerTotal;
                        buyerBalance = new BuyerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), Totalbalance);
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Total-Balance").setValue(buyerBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                int venderbalanceacount= buyerTotal + venderacount;
                                venderBalance = new VenderBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), venderbalanceacount);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Vender-Acount").child(vender).setValue(venderBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        venderBillCounter = new VenderBillCounter(venderCounter + 1);
                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("VenderBill-Counter").setValue(venderBillCounter).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Vender-Billing").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        PurchaseTemplate purchaseTemplate = new PurchaseTemplate(SharedPreferencesManager.getSomeStringValue(getApplication()), getVender,discountvalue , tod, venderCounter + "", vender);
                                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Template").child(venderCounter + "").setValue(purchaseTemplate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                intent = new Intent(getApplication(), Dashboard.class);
                                                                startActivity(intent);
                                                            }
                                                        });


                                                    }
                                                });
                                            }
                                        });
                                    }
                                });



                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }
                });
            }
        });

    }
}
