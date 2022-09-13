package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.BuyerBalance;
import com.example.AccountingAppAdmin.DataModel.Counter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.PaidAmountModel;
import com.example.AccountingAppAdmin.DataModel.PaymentMethodModel;
import com.example.AccountingAppAdmin.DataModel.VenderBalance;
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
import java.util.List;

public class SalerPayment extends AppCompatActivity {

    EditText method;
    Spinner spinner, spinner2;
    final List<String> categories_vender = new ArrayList<String>();
    final List<String> categories_payment = new ArrayList<String>();
    AddClientModel addClientModel;
    ArrayAdapter<String> dataAdapter_vender, dataAdapter_payment;
    Button createpayment;
    DatabaseReference databaseReference;
    int count, totalbalance, venderbalance, paidamount;
    int currentamount;
    String counts, selectVender, selectPayment, tod;
    Counter counter;
    ImageView back;
    PaymentMethodModel paymentMethodModel;
    PaidAmountModel paidAmountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saler_payment);
        spinner = (Spinner) findViewById(R.id.spinervender);
        spinner2 = (Spinner) findViewById(R.id.spinerMethod);
        method = findViewById(R.id.amount);
        createpayment = findViewById(R.id.createpayment);
        categories_vender.add("-Select Vender-");
        categories_payment.add(0, "-Select Method-");
        categories_payment.add(1, "Cash By Hand");
        categories_payment.add(2, "Bank Payment");
        SimpleDateFormat sdf = new SimpleDateFormat("'Date-   'dd.MM.yyyy'  Time-' HH:mm:ss");
        tod = sdf.format(new Date());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    counter = dataSnapshot.getValue(Counter.class);
                    if (counter != null) {

                        count = counter.getCounts();

                    } else {

                        counter = new Counter(0);
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").setValue(counter);


                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (method.getText().length() == 0) {
                    method.setError("Enter Amount");
                } else {
                    counts = String.valueOf(count);
                    paymentMethodModel = new PaymentMethodModel(SharedPreferencesManager.getSomeStringValue(getApplication()), selectPayment, selectVender, method.getText().toString(), counts, tod);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Saler-PayHistory").child(selectVender).child(selectPayment).child(counts).setValue(paymentMethodModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            count = count + 1;
                            counter = new Counter(count);
                            databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").setValue(counter).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("Total-Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.getValue() != null) {
                                                BuyerBalance buyerBalance = dataSnapshot.getValue(BuyerBalance.class);
                                                if (buyerBalance != null) {


                                                    currentamount = Integer.parseInt(method.getText().toString());
                                                    totalbalance = buyerBalance.getBalance() - currentamount;
//
                                                    buyerBalance = new BuyerBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), totalbalance);
                                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SalerBalance").child("Total-Balance").setValue(buyerBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("Client-Acount").child(selectVender).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.getValue() != null) {
                                                                        VenderBalance venderBalance = dataSnapshot.getValue(VenderBalance.class);
                                                                        if (venderBalance != null) {

                                                                            venderbalance = venderBalance.getBalance() - currentamount;

                                                                            venderBalance = new VenderBalance(SharedPreferencesManager.getSomeStringValue(getApplication()), venderbalance);
                                                                            databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("Client-Acount").child(selectVender).setValue(venderBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

//
                                                                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("PaidAmount").child(selectVender).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.getValue() != null) {
                                                                                                paidAmountModel = dataSnapshot.getValue(PaidAmountModel.class);

                                                                                                if (paidAmountModel != null) {

                                                                                                    paidamount = paidAmountModel.getPaidAomount();
                                                                                                    paidamount = paidamount + currentamount;

                                                                                                    paidAmountModel = new PaidAmountModel(SharedPreferencesManager.getSomeStringValue(getApplication()), paidamount);
                                                                                                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("PaidAmount").child(selectVender).setValue(paidAmountModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            Toast.makeText(SalerPayment.this, "Done", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });


                                                                                                } else {

                                                                                                }


                                                                                            } else {


                                                                                                paidAmountModel = new PaidAmountModel(SharedPreferencesManager.getSomeStringValue(getApplication()), Integer.parseInt(method.getText().toString()));
                                                                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("SalerBalance").child("PaidAmount").child(selectVender).setValue(paidAmountModel);


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
            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_vender.clear();
                categories_vender.add(0, "-Select Client-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addClientModel = snapshot.getValue(AddClientModel.class);
                    categories_vender.add(addClientModel.getCounter() + "-  " + addClientModel.getShopeName());


                }

//                createVenderModel.getShopeName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectVender = (String) parent.getItemAtPosition(position);
                Toast.makeText(SalerPayment.this, "" + selectVender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectPayment = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter_vender = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_vender);
        dataAdapter_vender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter_vender);

        dataAdapter_payment = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_payment);
        dataAdapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter_payment);



    }
}
