package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.AccountingAppAdmin.Adapter.ClientBIllView;
import com.example.AccountingAppAdmin.Adapter.ClientProductListAdapter;
import com.example.AccountingAppAdmin.Adapter.VedenProductListAdapter;
import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.ClientBillModelProduct;
import com.example.AccountingAppAdmin.DataModel.Counter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PaymentMethodModel;
import com.example.AccountingAppAdmin.DataModel.VenderBillModelProduct;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientBilling extends AppCompatActivity {

    ImageView back;
    Spinner spinner, spinnerproduct, spinnerpayment;
    DatabaseReference databaseReference;
    AddClientModel addClientModel;
    InventryModel inventryModel;
    final List<String> categories_Client = new ArrayList<String>();
    final List<String> categories_Product = new ArrayList<String>();
    final List<String> categories_Payment = new ArrayList<String>();
    String selectClent, selectproduct, selectPayment;
    ArrayAdapter<String> dataAdapter_client, dataAdapter_product, DataAdapter_payment;
    Counter counter;
    int counts;
    String currentDateandTime;
    PaymentMethodModel paymentMethodModel;
    Button Addmore;
    String Productname, stock, buyerprice, salerprice, count, timeanddate, unit,vender,category,subcategory,subcategoryto,unitget;
    ClientBillModelProduct clientBillModelProduct;
    ArrayList<ClientBillModelProduct> arrayList = new ArrayList<>();
    ClientProductListAdapter clientProductListAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button next;
    long total = 0;
    EditText tax,discount;
    TextView address;
    String createvendername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_billing);
        back = findViewById(R.id.back);
        SimpleDateFormat sdf = new SimpleDateFormat("'Today Date-    'dd.MM.yyyy'    Time-' HH:mm:ss");
        currentDateandTime = sdf.format(new Date());
        spinner = (Spinner) findViewById(R.id.spiner_vender);
        spinnerproduct = (Spinner) findViewById(R.id.spiner_product);
        spinnerpayment = (Spinner) findViewById(R.id.spiner_payment);
        categories_Client.add("-Select Vender-");
        categories_Product.add("-Select Product-");
        categories_Payment.add("-Select Payment-");
        Addmore = findViewById(R.id.addmore);
        address = findViewById(R.id.address);
        tax = findViewById(R.id.tax);
        discount = findViewById(R.id.discount);
        next = findViewById(R.id.next);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.addMoreList_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Client-Billing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    clientBillModelProduct = snapshot.getValue(ClientBillModelProduct.class);
                    if (clientBillModelProduct != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(clientBillModelProduct);
                        if (clientBillModelProduct.getBuyerPrice() != null) {
//                            long buyer = Integer.parseInt(venderBillModelProduct.getBuyerPrice());
//                            total = total + buyer;
                        } else {

                        }


//
                    }


                }

                clientProductListAdapter = new ClientProductListAdapter(getApplication(), arrayList, new ClientProductListAdapter.Delete() {
                    @Override
                    public void pressdel(String counts) {
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Client-Billing").child(counts).removeValue();

                    }
                });
                recyclerView.setAdapter(clientProductListAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                clientProductListAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        Addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clientBillModelProduct = new ClientBillModelProduct(SharedPreferencesManager.getSomeStringValue(getApplication()), buyerprice, Productname, salerprice, "0", count, timeanddate,createvendername,category,subcategory,subcategoryto,unitget);
                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Client-Billing").child(count).setValue(clientBillModelProduct);


            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    counter = dataSnapshot.getValue(Counter.class);
                    if (counter != null) {

                        counts = counter.getCounts();

                    } else {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_Client.clear();
                categories_Client.add(0, "-Select Client-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addClientModel = snapshot.getValue(AddClientModel.class);
                    categories_Client.add(addClientModel.getCounter() + "-  " + addClientModel.getShopeName());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_Product.clear();
                categories_Product.add(0, "-Select Product-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    inventryModel = snapshot.getValue(InventryModel.class);
                    categories_Product.add(inventryModel.getCount() + "-  " + inventryModel.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("PaymentMethod").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_Payment.clear();
                categories_Payment.add(0, "-Select Payment Method-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    paymentMethodModel = snapshot.getValue(PaymentMethodModel.class);
                    categories_Payment.add(paymentMethodModel.getPayment());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectClent = (String) parent.getItemAtPosition(position);

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(selectClent).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            addClientModel = dataSnapshot.getValue(AddClientModel.class);
                            if (addClientModel != null) {

                                address.setText(addClientModel.getAdress());
                                createvendername=addClientModel.getShopeName();

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter_client = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_Client);
        dataAdapter_client.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter_client);

        dataAdapter_product = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_Product);
        dataAdapter_product.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerproduct.setAdapter(dataAdapter_product);

        DataAdapter_payment = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_Payment);
        DataAdapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerpayment.setAdapter(DataAdapter_payment);


        spinnerproduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectproduct = (String) parent.getItemAtPosition(position);
                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(selectproduct).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            InventryModel inventryModel = dataSnapshot.getValue(InventryModel.class);
                            if (inventryModel != null) {

                                count = inventryModel.getCount();
                                Productname = inventryModel.getName();
                                stock = inventryModel.getStock();
                                buyerprice = inventryModel.getBuyerPrice();
                                salerprice = inventryModel.getSalerPrice();
                                timeanddate = inventryModel.getTimeandDate();
                                unit = inventryModel.getUnit();
//                                vender=inventryModel.getVender();
                                category=inventryModel.getCategory();
                                subcategory=inventryModel.getSubCategory();
                                subcategoryto=inventryModel.getSubToCategory();
                                unitget=inventryModel.getUnit();


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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerpayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPayment = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tax.getText().length() == 0) {

                    tax.setError("Enter Tax Margin");

                } else {

                    Intent intent = new Intent(getApplication(), ClientBIllView.class);
                    intent.putExtra("vender", selectClent);
                    intent.putExtra("createvendername", createvendername);
                    intent.putExtra("payment", selectPayment);
                    intent.putExtra("tax", tax.getText().toString());
                    intent.putExtra("discount", discount.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }
}
