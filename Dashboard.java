package com.example.AccountingAppAdmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.AccountingAppAdmin.DataModel.ChildModel;
import com.example.AccountingAppAdmin.DataModel.HeaderModel;
import com.example.AccountingAppAdmin.DataModel.OwnerProfileModel;
import com.example.AccountingAppAdmin.DataModel.UserProfile;
import com.example.AccountingAppAdmin.Fragment.MyAccountFragment;
import com.example.AccountingAppAdmin.Fragment.MyProfileFragment;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.AccountingAppAdmin.View.ExpandableNavigationListView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    TextView name, email;
    ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private ExpandableNavigationListView navigationExpandableListView;
    DrawerLayout drawer;
    private Context context;
    OwnerProfileModel  ownerProfileModel;
    MyAccountFragment myAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = Dashboard.this;

        myAccountFragment = new MyAccountFragment();
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.replacePage, myAccountFragment).commitAllowingStateLoss();


        mDatabase.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Owner-Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    ownerProfileModel = dataSnapshot.getValue(OwnerProfileModel.class);
                    if(ownerProfileModel != null){



                    }else{


                    }

                }else{

                    UpdateData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        navigationExpandableListView = (ExpandableNavigationListView) findViewById(R.id.expandable_navigation);
        drawer = findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);

        name = (TextView) v.findViewById(R.id.userName);
        email = (TextView) v.findViewById(R.id.userEmail);
        imageView = (ImageView) v.findViewById(R.id.showdp);


        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel("Dashboard"))
                .addHeaderModel(new HeaderModel("My Profile"))
                .addHeaderModel(new HeaderModel("Payment Method"))
                .addHeaderModel(
                        new HeaderModel("Buyer", R.drawable.add, true)
                                .addChildModel(new ChildModel("Add Vender"))
                                .addChildModel(new ChildModel("Create Category"))
                                .addChildModel(new ChildModel("Vender Billing"))
                                .addChildModel(new ChildModel("Purchase Invoice"))
                                .addChildModel(new ChildModel("Retrun Invoice"))
                )
                .addHeaderModel(new HeaderModel("Saler", R.drawable.add, true)
                        .addChildModel(new ChildModel("Create Customer"))
                        .addChildModel(new ChildModel("Customer Billing"))
                        .addChildModel(new ChildModel("Sale Invoice"))
                        .addChildModel(new ChildModel("Retrun Invoice")))

                .addHeaderModel(new HeaderModel("Add Product"))
                .addHeaderModel(new HeaderModel("Report"))

                .build()

                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition);

                        //drawer.closeDrawer(GravityCompat.START);
                        if (id == 0) {

                           myAccountFragment = new MyAccountFragment();
                            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                            fm.replace(R.id.replacePage, myAccountFragment).commitAllowingStateLoss();
                            drawer.closeDrawer(GravityCompat.START);

                        }
                        else if(id==1){
                            MyProfileFragment myProfileFragment = new MyProfileFragment();
                            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                            fm.replace(R.id.replacePage, myProfileFragment).commitAllowingStateLoss();
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        else if (id == 2) {

                            Intent intent = new Intent(getApplication(), PaymentOption.class);
                            startActivity(intent);
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        /*else if (id == 2) {
                            //Categories Menu
                            Common.showToast(context, "Categories  Select");
                        }*/
//                        }
//                        else if (id == 3) {
//                            Toast.makeText(Dashboard.this, "Msg", Toast.LENGTH_SHORT).show();
//
////                            Intent intent = new Intent(getApplication(), MyProfile.class);
////                            startActivity(intent);
//
//                            drawer.closeDrawer(GravityCompat.START);
//                        }
                        else if (id == 5) {
                            Intent intent = new Intent(getApplication(), AddProduct.class);
                            startActivity(intent);
                              drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 6) {

                        }

                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition, childPosition);


                        if (groupPosition == 3) {

                            if (id == 0) {
                                Intent intent = new Intent(getApplication(),VenderActivity.class);
                                startActivity(intent);

                            } else if (id == 1) {

                                Intent intent = new Intent(getApplication(),TabularActivity.class);
                                startActivity(intent);

//


                            } else if (id == 2) {
                                Intent intent = new Intent(getApplication(),VenderBill.class);
                                startActivity(intent);


                            } else if (id == 3) {

                                Intent intent = new Intent(getApplication(),PurchaseInovice.class);
                                startActivity(intent);


                            } else if (id == 4) {

                                Intent intent = new Intent(getApplication(),RetrunInvoice.class);
                                startActivity(intent);

                            }
                        } else if (groupPosition == 4) {

                            if (id == 0) {

                                Intent intent = new Intent(getApplication(),CreateCustomer.class);
                                startActivity(intent);

                            } else if (id == 1) {

                                Intent intent = new Intent(getApplication(),ClientBilling.class);
                                startActivity(intent);
//
                            } else if (id == 2) {

                                Intent intent = new Intent(getApplication(),ClientPurchaseInvoice.class);
                                startActivity(intent);
//
//
                            } else if (id == 3) {

                                Intent intent = new Intent(getApplication(),ClientReturnInvoice.class);
                                startActivity(intent);

                            }

                        }

                        drawer.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });
        navigationExpandableListView.expandGroup(2);


        mDatabase.child("Users").child(SharedPreferencesManager.getSomeStringValue(getBaseContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    UserProfile model = dataSnapshot.getValue(UserProfile.class);
                    if (model != null) {

                        name.setText(model.getName());
                        email.setText(model.getEmail());
                        Glide.with(Dashboard.this).load(model.getImage()).into(imageView);
//

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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.Dashboard) {
//            // Handle the camera actio
//        } else if (id == R.id.Part1) {
//
//        } else if (id == R.id.Part2) {
//
//        } else if (id == R.id.Notic) {
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void UpdateData() {

//        AlertDialog alertDialog = new AlertDialog.Builder(VenderDetail.this).create();
//     final AlertDialog.Builder alertDialog;
//        alertDialog = new android.support.v7.app.AlertDialog.Builder( getApplication() );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Dashboard.this);

        alertDialog.setTitle( "Profile..." );


        alertDialog.setMessage( "Complete Your Profile" );

//
        alertDialog.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            Intent intent = new Intent(getApplication(), OwnerProfile.class);
            startActivity(intent);


            }


        } );

//         Setting Negative "NO" Button
        alertDialog.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText( getApplication(), "Please Complete Your Owner Profile", Toast.LENGTH_SHORT ).show();
                dialog.cancel();
            }
        } );

        // Showing Alert Message
        alertDialog.show();


    }
}
