package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.AccountingAppAdmin.DataModel.Counter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.MainCategoryModel;
import com.example.AccountingAppAdmin.DataModel.SubCategoryModel;
import com.example.AccountingAppAdmin.DataModel.SubCategoryTwoModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventryManagementActivity extends AppCompatActivity {

    ImageView back;
    Spinner spinner, spinerCategory, spinnerSubCategory, spinnerSubtoCategory;
    DatabaseReference databaseReference;
    String selectVender;
    CreateVenderModel createVenderModel;
    MainCategoryModel mainCategoryModel;
    SubCategoryModel subCategoryModel;
    SubCategoryTwoModel subCategoryTwoModel;
    final List<String> categories_vender = new ArrayList<String>();
    final List<String> categories_Category = new ArrayList<String>();
    final List<String> categories_SubCategory = new ArrayList<String>();
    final List<String> categories_SubToCategory = new ArrayList<String>();
    String selectCateogry, selectSubCategory,selectSubToSubCategory;
    ArrayAdapter<String> dataAdapter_SubToCategory, dataAdapter_SubCategory, dataAdapter_Category;
    ImageView uploadimg,showimg;
    private static final int PICK_IMAGE = 100;
    Uri imguri;
    Button next;
    EditText price,name,salerprice,unit;
    String currentDateandTime;
    Counter counter;
    int counts;
    String count;
    InventryModel inventryModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_management);
        back = findViewById(R.id.back);

        spinerCategory = (Spinner) findViewById(R.id.spiner_category);
        spinnerSubCategory = (Spinner) findViewById(R.id.spiner_subcategory);
        spinnerSubtoCategory = (Spinner) findViewById(R.id.spiner_subtosubcategory);
        uploadimg =findViewById(R.id.uploadimg);
        showimg =findViewById(R.id.imgshow);
        next =findViewById(R.id.next);
        price =findViewById(R.id.price);
        name =findViewById(R.id.name);
        unit = findViewById(R.id.unit);

        salerprice = findViewById(R.id.salerprice);
        SimpleDateFormat sdf = new SimpleDateFormat("'Today Date-    'dd.MM.yyyy'    Time-' HH:mm:ss");
        currentDateandTime = sdf.format(new Date());
        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null){

                    counter = dataSnapshot.getValue(Counter.class);
                    if(counter!=null){

                        counts = counter.getCounts();

                    }else{

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengellery();
            }
        });



        categories_vender.add("-Select Vender-");
        categories_Category.add("--Select Category--");
        categories_SubCategory.add("--Select Item--");
        categories_SubToCategory.add("--Select Item--");

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                selectVender = (String) parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        spinerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCateogry = (String) parent.getItemAtPosition(position);

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Sub-Categories").child(selectCateogry).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        categories_SubCategory.clear();
                        categories_SubCategory.add(0,"-Select Item-");

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            subCategoryModel = snapshot.getValue(SubCategoryModel.class);
                            categories_SubCategory.add(subCategoryModel.getSubCategory());
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

        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSubCategory = (String) parent.getItemAtPosition(position);



                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("SubTwo-Category").child(selectCateogry).child(selectSubCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        categories_SubToCategory.clear();
                        categories_SubToCategory.add(0,"-Select Item-");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            subCategoryTwoModel = snapshot.getValue(SubCategoryTwoModel.class);
                            categories_SubToCategory.add(subCategoryTwoModel.getSubtoCategroy());
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

        spinnerSubtoCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectSubToSubCategory = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Dashboard.class);
                startActivity(intent);
            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Vender").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_vender.clear();
                categories_vender.add(0,"-Select Vender-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    createVenderModel = snapshot.getValue(CreateVenderModel.class);
                    categories_vender.add(createVenderModel.getShopeName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_vender);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_Category.clear();
                categories_Category.add(0,"-Select Category-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mainCategoryModel = snapshot.getValue(MainCategoryModel.class);
                    categories_Category.add(mainCategoryModel.getCategory_Name());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dataAdapter_Category = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_Category);
        dataAdapter_Category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCategory.setAdapter(dataAdapter_Category);

        dataAdapter_SubCategory = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_SubCategory);
        dataAdapter_SubCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategory.setAdapter(dataAdapter_SubCategory);

        dataAdapter_SubToCategory = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories_SubToCategory);
        dataAdapter_SubToCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubtoCategory.setAdapter(dataAdapter_SubToCategory);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (spinner.getSelectedItem().toString().trim().equals("--Select Vender--")) {
//
//                    Toast.makeText(InventryManagementActivity.this, "Please Select Vender", Toast.LENGTH_SHORT).show();
//
//                }
//
//               else
               if (spinerCategory.getSelectedItem().toString().trim().equals("--Select Category--")) {

                    Toast.makeText(InventryManagementActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();

                }

                else if(name.getText().length()==0){
                    name.setError("Enter Product Name");
                }

               else if (showimg.getDrawable() == null) {

                    Toast.makeText(InventryManagementActivity.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                }


              else if(price.getText().length()==0){
                  price.setError("Enter Buyer Price");
              }
              else  if (salerprice.getText().length() == 0) {
                    salerprice.setError("Enter Sale price");
                } else if (unit.getText().length() == 0) {
                    unit.setError("Enter Stock");
                }


                else {

//                    Intent intent = new Intent(getApplication(),InventrySystem.class);
//                    intent.putExtra("Vender", selectVender);
//                    intent.putExtra("selectCateogry", selectCateogry);
//                    intent.putExtra("selectSubCategory", selectSubCategory);
//                    intent.putExtra("selectSubToSubCategory", selectSubToSubCategory);
//                    intent.putExtra("imguri", imguri.toString());
//                    intent.putExtra("price", price.getText().toString());
//                    intent.putExtra("name", name.getText().toString());
//                    startActivity(intent);

                    uploadimgstorage();

                }
            }
        });

    }

    private void opengellery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int
            reusltCode, Intent data) {
        if (reusltCode == RESULT_OK && requestCode == PICK_IMAGE) {

            imguri = data.getData();
            showimg.setImageURI(imguri);

        }
    }


    public void uploadimgstorage() {





        StorageReference riversRef= FirebaseStorage.getInstance().getReference().child(SharedPreferencesManager.getSomeStringValue(getApplication())).child( "InventrySystem" ).child( counts+".jpg" );


        riversRef.putFile( imguri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get a URL to the uploaded content


                Task<Uri> abc = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                abc.addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url=uri.toString();

                        count= String.valueOf(counts);
                        inventryModel = new InventryModel(SharedPreferencesManager.getSomeStringValue(getApplication()),selectCateogry,selectSubCategory,selectSubToSubCategory,url,price.getText().toString(),name.getText().toString(),salerprice.getText().toString(),"0",unit.getText().toString(),count,currentDateandTime);
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(count+"-  "+name.getText().toString()).setValue(inventryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                counter= new Counter( counts+1);
                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").setValue(counter).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        inventryModel = new InventryModel(SharedPreferencesManager.getSomeStringValue(getApplication()),selectCateogry,selectSubCategory,selectSubToSubCategory,url,price.getText().toString(),name.getText().toString(),salerprice.getText().toString(),"0",unit.getText().toString(),count,currentDateandTime);
                                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(selectCateogry).child(selectSubCategory).child(selectSubToSubCategory).child(count+"-  "+name.getText().toString()).setValue(inventryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                inventryModel = new InventryModel(SharedPreferencesManager.getSomeStringValue(getApplication()),selectCateogry,selectSubCategory,selectSubToSubCategory,url,price.getText().toString(),name.getText().toString(),salerprice.getText().toString(),"0",unit.getText().toString(),count,currentDateandTime);
                                                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(selectVender).child(count+"-  "+name.getText().toString()).setValue(inventryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Intent intent = new Intent(getApplication(), AddProduct.class);
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
                } );

            }
        } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                } );
    }


}
