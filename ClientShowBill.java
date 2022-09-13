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

import com.example.AccountingAppAdmin.Adapter.ClientBillViewAdapter;
import com.example.AccountingAppAdmin.Adapter.ClientVBillAdapter;
import com.example.AccountingAppAdmin.Adapter.ShowBillAdapter;
import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.BuyerBalance;
import com.example.AccountingAppAdmin.DataModel.ClientPurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.ClientRetrunModel;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.RetrunTemplate;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.DataModel.SalerBalance;
import com.example.AccountingAppAdmin.DataModel.VenderBalance;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientShowBill extends AppCompatActivity {

    Intent intent;
    String count, venderNode, discountotal;
    DatabaseReference databaseReference;
    ClientPurchaseBillModel clientPurchaseBillModel;
    AddClientModel addClientModel;
    TextView name, clientname, id, adress, contact, timeanddate2, gst, subtotal, finaltotal, gstview, discount;
    ArrayList<ClientPurchaseBillModel> arrayList = new ArrayList<>();
    ClientVBillAdapter clientVBillAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    long total;
    Button Retrun;
    String getingstock, node, category, subcate, subtosubcate, shopname, finalstock;
    InventryModel inventryModel;
    int allstock, intstock;
    ClientRetrunModel clientRetrunModel;
    int totalbalance, venderbalance, balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_show_bill);
        intent = getIntent();
        venderNode = intent.getStringExtra("venderNode");
        count = intent.getStringExtra("count");
        discountotal = intent.getStringExtra("discountotal");
        balance=Integer.parseInt(discountotal);
        name = findViewById(R.id.name);
        clientname = findViewById(R.id.clientname);
        id = findViewById(R.id.id);
        adress = findViewById(R.id.adress);
        subtotal = findViewById(R.id.total);
        finaltotal = findViewById(R.id.finaltotal);
        finaltotal.setText(discountotal);
        contact = findViewById(R.id.contact);
        gst = findViewById(R.id.gst);
        timeanddate2 = findViewById(R.id.timeanddate2);
        Retrun = findViewById(R.id.retrunbill);
        discount = findViewById(R.id.discount);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.addMoreList_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Client").child(venderNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    addClientModel = dataSnapshot.getValue(AddClientModel.class);
                    if (addClientModel != null) {
                        name.setText(addClientModel.getShopeName());
                        shopname = addClientModel.getShopeName();
                        clientname.setText(addClientModel.getVenderName());
                        adress.setText(addClientModel.getAdress());
                        contact.setText(addClientModel.getContactNo());


                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Billing").child("All-Bill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    clientPurchaseBillModel = snapshot.getValue(ClientPurchaseBillModel.class);
                    if (clientPurchaseBillModel != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(clientPurchaseBillModel);

                        id.setText(clientPurchaseBillModel.getCount());
                        timeanddate2.setText(clientPurchaseBillModel.getTimeandDate());
                        gst.setText(clientPurchaseBillModel.getGst() + "%");
                        discount.setText(clientPurchaseBillModel.getDiscount() + "%");
                        subtotal.setText(clientPurchaseBillModel.getTotal() + "-/Rs");
                        getingstock = clientPurchaseBillModel.getStock();
                        String node1 = clientPurchaseBillModel.getCount();
                        String node2 = clientPurchaseBillModel.getName();
                        node = node1 + "-  " + node2;
                        category = clientPurchaseBillModel.getCategory();
                        subcate = clientPurchaseBillModel.getSubcategory();
                        subtosubcate = clientPurchaseBillModel.getSubcategoryto();


                    }


                }

                clientVBillAdapter = new ClientVBillAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(clientVBillAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                clientVBillAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        Retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Billing").child("All-Bill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            clientPurchaseBillModel = snapshot.getValue(ClientPurchaseBillModel.class);
                            if (clientPurchaseBillModel != null) {

                                arrayList.add(clientPurchaseBillModel);
                                getingstock = clientPurchaseBillModel.getStock();
                                String node1 = clientPurchaseBillModel.getCount();
                                String node2 = clientPurchaseBillModel.getName();
                                node = node1 + "-  " + node2;
                                Toast.makeText(ClientShowBill.this, ""+clientPurchaseBillModel.getVenderShop(), Toast.LENGTH_SHORT).show();
                                category = clientPurchaseBillModel.getCategory();
                                subcate = clientPurchaseBillModel.getSubcategory();
                                subtosubcate = clientPurchaseBillModel.getSubcategoryto();
                                intstock = Integer.parseInt(getingstock);

                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            inventryModel = dataSnapshot.getValue(InventryModel.class);
                                            if (inventryModel != null) {

                                                inventryModel.getStock();
                                                int inventryStock = Integer.parseInt(inventryModel.getStock());
                                                allstock = inventryStock + intstock;
                                                finalstock = Integer.toString(allstock);


                                            } else {

                                            }
                                        } else {
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        inventryModel = dataSnapshot.getValue(InventryModel.class);
                                        dataSnapshot.getRef().child("stock").setValue(finalstock);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(category).child(subcate).child(subtosubcate).child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        inventryModel = dataSnapshot.getValue(InventryModel.class);
                                        dataSnapshot.getRef().child("stock").setValue(finalstock);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(shopname).child(node).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        inventryModel = dataSnapshot.getValue(InventryModel.class);
//                                        dataSnapshot.getRef().child("stock").setValue(finalstock);
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });


                                clientRetrunModel = new ClientRetrunModel(SharedPreferencesManager.getSomeStringValue(getApplication()), clientPurchaseBillModel.getBuyerPrice(), clientPurchaseBillModel.getName(), clientPurchaseBillModel.getSalerPrice(), clientPurchaseBillModel.getStock(), clientPurchaseBillModel.getCount(), clientPurchaseBillModel.getTimeandDate(), clientPurchaseBillModel.getVender(), clientPurchaseBillModel.getCategory(), clientPurchaseBillModel.getSubcategory(), clientPurchaseBillModel.getSubcategoryto(), clientPurchaseBillModel.getUnit(), clientPurchaseBillModel.getTotal(), clientPurchaseBillModel.getVenderBillCounter(), clientPurchaseBillModel.getVenderShop(), clientPurchaseBillModel.getDiscount(), clientPurchaseBillModel.getGst(), discountotal);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Client-Billing").child("All-RetrunBill").child(count).child(clientPurchaseBillModel.getCount() + "-  " + clientPurchaseBillModel.getName()).setValue(clientRetrunModel);
                                clientRetrunModel = new ClientRetrunModel(SharedPreferencesManager.getSomeStringValue(getApplication()), clientPurchaseBillModel.getBuyerPrice(), clientPurchaseBillModel.getName(), clientPurchaseBillModel.getSalerPrice(), clientPurchaseBillModel.getStock(), clientPurchaseBillModel.getCount(), clientPurchaseBillModel.getTimeandDate(), clientPurchaseBillModel.getVender(), clientPurchaseBillModel.getCategory(), clientPurchaseBillModel.getSubcategory(), clientPurchaseBillModel.getSubcategoryto(), clientPurchaseBillModel.getUnit(), clientPurchaseBillModel.getTotal(), clientPurchaseBillModel.getVenderBillCounter(), clientPurchaseBillModel.getVenderShop(), clientPurchaseBillModel.getDiscount(), clientPurchaseBillModel.getGst(), discountotal);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Client-Billing").child(clientPurchaseBillModel.getVender()).child(count).child(clientPurchaseBillModel.getCount() + "-  " + clientPurchaseBillModel.getName()).setValue(clientRetrunModel);





                            }



                        }

                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Billing").child("All-Bill").child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Billing").child("Client-Bill").child(clientPurchaseBillModel.getVender()).child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Template").child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                RetrunTemplate retrunTemplate = new RetrunTemplate(SharedPreferencesManager.getSomeStringValue(getApplication()),clientPurchaseBillModel.getVender(),discountotal,clientPurchaseBillModel.getTimeandDate(),count,clientPurchaseBillModel.getVenderShop());
                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Client-Template").child(count).setValue(retrunTemplate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {











                                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SalerBalance").child("Total-Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.getValue() != null) {
                                                                    SalerBalance salerBalance = dataSnapshot.getValue(SalerBalance.class);
                                                                    if (salerBalance != null) {

                                                                        totalbalance = salerBalance.getBalance();
                                                                        int retrun_b = totalbalance - balance;
                                                                        salerBalance = new SalerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), retrun_b);
                                                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SalerBalance").child("Total-Balance").setValue(salerBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SalerBalance").child("Client-Acount").child(clientPurchaseBillModel.getVenderShop()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.getValue() != null) {
                                                                                            VenderBalance venderBalance= dataSnapshot.getValue(VenderBalance.class);
                                                                                            if(venderBalance!=null){
                                                                                                venderbalance =venderBalance.getBalance();

                                                                                                int retrunsaler_b= venderbalance-balance;
                                                                                                venderBalance=new VenderBalance(SharedPreferencesManager.getSomeStringValue(getApplication()),retrunsaler_b);
                                                                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SalerBalance").child("Client-Acount").child(clientPurchaseBillModel.getVenderShop()).setValue(venderBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        Toast.makeText(ClientShowBill.this, "Successfully Retrun Bill To Vender", Toast.LENGTH_SHORT).show();
                                                                                                        intent = new Intent(getApplication(),Dashboard.class);
                                                                                                        startActivity(intent);
                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                        } else {
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });

                                                                            }
                                                                        });


                                                                    } else {

                                                                    }

                                                                } else {

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                    //
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }
        });


    }
}
