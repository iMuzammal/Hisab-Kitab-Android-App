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
import com.example.AccountingAppAdmin.Adapter.ShowBillAdapter;
import com.example.AccountingAppAdmin.DataModel.BuyerBalance;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.RetrunTemplate;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.DataModel.SalerBalance;
import com.example.AccountingAppAdmin.DataModel.VenderBillModelProduct;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowBill extends AppCompatActivity {

    Intent intent;
    String count, venderNode, discountotal;
    DatabaseReference databaseReference;
    PurchaseBillModel purchaseBillModel;
    CreateVenderModel createVenderModel;
    TextView name, clientname, id, adress, contact, timeanddate2, gst, subtotal, finaltotal, gstview, discount;
    ArrayList<PurchaseBillModel> arrayList = new ArrayList<>();
    ShowBillAdapter showBillAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    long total;
    Button Retrun;
    String getingstock, node, category, subcate, subtosubcate, shopname, finalstock;
    InventryModel inventryModel;
    int allstock, intstock;
    RetunPurchaseModel retunPurchaseModel;
    int totalbalance, venderbalance, balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill);
        intent = getIntent();
        venderNode = intent.getStringExtra("venderNode");
        count = intent.getStringExtra("count");
        discountotal = intent.getStringExtra("discountotal");
        balance = Integer.parseInt(discountotal);
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


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Vender").child(venderNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    createVenderModel = dataSnapshot.getValue(CreateVenderModel.class);
                    if (createVenderModel != null) {
                        name.setText(createVenderModel.getShopeName());
                        shopname = createVenderModel.getShopeName();
                        clientname.setText(createVenderModel.getVenderName());
                        adress.setText(createVenderModel.getAdress());
                        contact.setText(createVenderModel.getContactNo());


                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("All-Bill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    purchaseBillModel = snapshot.getValue(PurchaseBillModel.class);
                    if (purchaseBillModel != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(purchaseBillModel);

                        id.setText(purchaseBillModel.getCount());
                        timeanddate2.setText(purchaseBillModel.getTimeandDate());
                        gst.setText(purchaseBillModel.getGst() + "%");
                        discount.setText(purchaseBillModel.getDiscount() + "%");
                        subtotal.setText(purchaseBillModel.getTotal() + "-/Rs");
                        getingstock = purchaseBillModel.getStock();
                        String node1 = purchaseBillModel.getCount();
                        String node2 = purchaseBillModel.getName();
                        node = node1 + "-  " + node2;
                        category = purchaseBillModel.getCategory();
                        subcate = purchaseBillModel.getSubcategory();
                        subtosubcate = purchaseBillModel.getSubcategoryto();


                    }


                }

                showBillAdapter = new ShowBillAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(showBillAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                showBillAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        Retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("All-Bill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            purchaseBillModel = snapshot.getValue(PurchaseBillModel.class);
                            if (purchaseBillModel != null) {

                                arrayList.add(purchaseBillModel);
                                getingstock = purchaseBillModel.getStock();
                                String node1 = purchaseBillModel.getCount();
                                String node2 = purchaseBillModel.getName();
                                node = node1 + "-  " + node2;
                                category = purchaseBillModel.getCategory();
                                subcate = purchaseBillModel.getSubcategory();
                                subtosubcate = purchaseBillModel.getSubcategoryto();
                                intstock = Integer.parseInt(getingstock);

                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            inventryModel = dataSnapshot.getValue(InventryModel.class);
                                            if (inventryModel != null) {

                                                inventryModel.getStock();
                                                int inventryStock = Integer.parseInt(inventryModel.getStock());
                                                allstock = inventryStock - intstock;
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


                                retunPurchaseModel = new RetunPurchaseModel(SharedPreferencesManager.getSomeStringValue(getApplication()), purchaseBillModel.getBuyerPrice(), purchaseBillModel.getName(), purchaseBillModel.getSalerPrice(), purchaseBillModel.getStock(), purchaseBillModel.getCount(), purchaseBillModel.getTimeandDate(), purchaseBillModel.getVender(), purchaseBillModel.getCategory(), purchaseBillModel.getSubcategory(), purchaseBillModel.getSubcategoryto(), purchaseBillModel.getUnit(), purchaseBillModel.getTotal(), purchaseBillModel.getVenderBillCounter(), purchaseBillModel.getVenderShop(), purchaseBillModel.getDiscount(), purchaseBillModel.getGst(), discountotal);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Billing").child("All-RetrunBill").child(count).child(purchaseBillModel.getCount() + "-  " + purchaseBillModel.getName()).setValue(retunPurchaseModel);
                                retunPurchaseModel = new RetunPurchaseModel(SharedPreferencesManager.getSomeStringValue(getApplication()), purchaseBillModel.getBuyerPrice(), purchaseBillModel.getName(), purchaseBillModel.getSalerPrice(), purchaseBillModel.getStock(), purchaseBillModel.getCount(), purchaseBillModel.getTimeandDate(), purchaseBillModel.getVender(), purchaseBillModel.getCategory(), purchaseBillModel.getSubcategory(), purchaseBillModel.getSubcategoryto(), purchaseBillModel.getUnit(), purchaseBillModel.getTotal(), purchaseBillModel.getVenderBillCounter(), purchaseBillModel.getVenderShop(), purchaseBillModel.getDiscount(), purchaseBillModel.getGst(), discountotal);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Billing").child(purchaseBillModel.getVender()).child(count).child(purchaseBillModel.getCount() + "-  " + purchaseBillModel.getName()).setValue(retunPurchaseModel);


                            }


                        }

                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("All-Bill").child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Billing").child("Vender-Name").child(purchaseBillModel.getVender()).child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Template").child(count).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                RetrunTemplate retrunTemplate = new RetrunTemplate(SharedPreferencesManager.getSomeStringValue(getApplication()), purchaseBillModel.getVender(), discountotal, purchaseBillModel.getTimeandDate(), count, purchaseBillModel.getVenderShop());
                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Template").child(count).setValue(retrunTemplate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Total-Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.getValue() != null) {
                                                                    BuyerBalance buyerBalance = dataSnapshot.getValue(BuyerBalance.class);
                                                                    if (buyerBalance != null) {

                                                                        totalbalance = buyerBalance.getBalance();
                                                                        int retrun_b = totalbalance - balance;
                                                                        buyerBalance = new BuyerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), retrun_b);
                                                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Total-Balance").setValue(buyerBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Vender-Acount").child(purchaseBillModel.getVenderShop()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.getValue() != null) {
                                                                                            SalerBalance salerBalance= dataSnapshot.getValue(SalerBalance.class);
                                                                                            if(salerBalance!=null){
                                                                                                venderbalance =salerBalance.getBalance();

                                                                                                int retrunsaler_b= venderbalance-balance;
                                                                                                salerBalance=new SalerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()),retrunsaler_b);
                                                                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("BuyerBalance").child("Vender-Acount").child(purchaseBillModel.getVenderShop()).setValue(salerBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        Toast.makeText(ShowBill.this, "Successfully Retrun Bill To Vedner", Toast.LENGTH_SHORT).show();
                                                                                                        intent = new Intent(getApplication(), Dashboard.class);
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
