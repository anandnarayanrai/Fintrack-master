package com.edge.fintrack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edge.fintrack.account_detail.ProfileViewActivity;
import com.edge.fintrack.account_detail.UpdateProfileActivity;
import com.edge.fintrack.calculator.CalculatorActivity;
import com.edge.fintrack.ckyc.CkycUpdateActivity;
import com.edge.fintrack.dashboard.DeshboardFragment;
import com.edge.fintrack.product.ProductFragment;
import com.edge.fintrack.utility.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_getInvestorProfilePhoto;
import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_updateInvestorProfilePhoto;
import static com.edge.fintrack.utility.Api_Class.NAMESPACE;
import static com.edge.fintrack.utility.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.utility.Api_Class.URL_InvestorProfilePhoto;
import static com.edge.fintrack.utility.Constant.ShowDilog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public final String TAG = "MainActivity";
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    ImageView imageView_user;
    // CircleImageView imageView_profile_image;
    // Session Manager Class
    SessionManager session;
    AlertDialog alert;
    Context mContext;

    //use to set background color
    RelativeLayout app_header;
    ImageView iv_menu_item, iv_more_menu_item;
    Button bt_portfolio, bt_product;
    SoapPrimitive resultString;
    String imageString;
    CircleImageView imageView_profile_image;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    private boolean doubleBackToExitPressedOnce = false;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private Image bgImage;
    private TextView tv_username, textViewEmail;
    private String Registration_Id;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // setTitle(getResources().getString(R.string.app_name));

        // making notification bar transparent
        //changeStatusBarColor();

        //app_header = (RelativeLayout) findViewById(R.id.app_header);
        // app_header.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        iv_more_menu_item = (ImageView) findViewById(R.id.iv_more_menu_item);
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv_menu_item.setImageDrawable(getDrawable(R.drawable.ic_menu));
        }
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(getBaseContext(), v);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        // Handle action bar item clicks here. The action bar will
                        // automatically handle clicks on the Home/Up button, so long
                        // as you specify a parent activity in AndroidManifest.xml.
                        int id = item.getItemId();
                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_ckyc) {
                            Intent intentUpdateProfile = new Intent(MainActivity.this, CkycUpdateActivity.class);
                            intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentUpdateProfile);
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                            return true;
                        } else if (id == R.id.action_settings) {
                            Intent intentUpdateProfile = new Intent(MainActivity.this, SettingsActivity.class);
                            intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentUpdateProfile);
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                            return true;
                        }
                        return true;
                    }
                });

                /** Showing the popup menu */
                popup.show();

            }
        };
        //iv_more_menu_item.setVisibility(View.VISIBLE);
        //iv_more_menu_item.setOnClickListener(listener);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        // tx.replace(R.id.content_frame, new ProductFragment());
        tx.replace(R.id.content_frame, new DeshboardFragment());
        tx.commit();
        mContext = getApplicationContext();
        Constant.STORAGEPermission(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        Registration_Id = user.get(SessionManager.KEY_Registration_Id);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_username = (TextView) headerView.findViewById(R.id.tv_username);
        tv_username.setText(user.get(SessionManager.KEY_NAME));
        textViewEmail = (TextView) headerView.findViewById(R.id.textViewEmail);
        textViewEmail.setText(user.get(SessionManager.KEY_EMAIL));

        imageView_user = (ImageView) headerView.findViewById(R.id.imageView_user);
        imageView_user.setOnClickListener(this);

        imageView_profile_image = (CircleImageView) headerView.findViewById(R.id.imageView_profile_image);
        imageView_profile_image.setOnClickListener(this);

        // confMenu();

        /*chart = (LineChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        // Generate some random values.
        generateValues();

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();*/

        bt_portfolio = (Button) findViewById(R.id.bt_portfolio);
        bt_portfolio.setOnClickListener(this);
        bt_product = (Button) findViewById(R.id.bt_product);
        bt_product.setOnClickListener(this);

        getProfilePhoto task = new getProfilePhoto();
        task.execute();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

            /*// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/

            View decor = getWindow().getDecorView();
            // decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast toast = Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
            View view = toast.getView();
            toast.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ckyc) {
            //session.logoutUser();
            Intent intentUpdateProfile = new Intent(MainActivity.this, CkycUpdateActivity.class);
            // intentUpdateProfile.putExtra("layout", "cardview_Presonal");
            intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentUpdateProfile);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            return true;
        } else if (id == R.id.action_settings) {
            //session.logoutUser();
            Intent intentUpdateProfile = new Intent(MainActivity.this, SettingsActivity.class);
            intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // intentUpdateProfile.putExtra("layout", "cardview_Presonal");
            startActivity(intentUpdateProfile);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        Intent intentUpdateProfile = new Intent(MainActivity.this, UpdateProfileActivity.class);
        Intent intentCalculatorActivity = new Intent(MainActivity.this, CalculatorActivity.class);
        switch (id) {
            case R.id.nav_profile:
                Intent intentProfileView = new Intent(MainActivity.this, ProfileViewActivity.class);
                intentProfileView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfileView);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_home:
                tx.replace(R.id.content_frame, new DeshboardFragment());
                tx.commit();
                break;
            case R.id.nav_product:
                tx.replace(R.id.content_frame, new ProductFragment());
                tx.commit();
                break;
            case R.id.nav_investor:
                Toast.makeText(this, "This Functionality will be update soon", Toast.LENGTH_SHORT).show();
                /*intentUpdateProfile.putExtra("layout", "layout_investor");
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/
                break;
            case R.id.nav_account:
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentUpdateProfile.putExtra("layout", "layout_bank");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_communication:
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentUpdateProfile.putExtra("layout", "layout_address");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_nominee:
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentUpdateProfile.putExtra("layout", "layout_nominee");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;

            case R.id.nav_sCalculator:
                intentCalculatorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentCalculatorActivity.putExtra("CurrentItem", "0");
                startActivity(intentCalculatorActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;

            case R.id.nav_nCalculator:
                intentCalculatorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentCalculatorActivity.putExtra("CurrentItem", "1");
                startActivity(intentCalculatorActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_fCalculator:
                intentCalculatorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentCalculatorActivity.putExtra("CurrentItem", "2");
                startActivity(intentCalculatorActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_hCalculator:
                intentCalculatorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentCalculatorActivity.putExtra("CurrentItem", "3");
                startActivity(intentCalculatorActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_manage:
                Intent intentSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                intentSettingsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSettingsActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.nav_logout:
                OnLogoutDilog();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.imageView_user:
                selectImage();
                break;
            case R.id.imageView_profile_image:
                selectImage();
                break;
            case R.id.bt_product:
                tx.replace(R.id.content_frame, new ProductFragment());
                tx.commit();
                break;
        }
    }

    // Select image from camera and gallery
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    boolean CAMERAPermission = Constant.CAMERAPermission(MainActivity.this);
                    if (CAMERAPermission) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    }
                } else if (options[item].equals("Choose From Gallery")) {
                    boolean STORAGEPermission = Constant.STORAGEPermission(MainActivity.this);
                    if (STORAGEPermission) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] imageBytes = bytes.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                updateProfileCallWS task = new updateProfileCallWS(destination);
                task.execute();
                imageView_user.setImageBitmap(bitmap);
                imageView_profile_image.setImageBitmap(bitmap);

             /*   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);*/
               /* byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
                //Toast.makeText(MainActivity.this, "Profile Image are change", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                byte[] imageBytes = bytes.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                updateProfileCallWS task = new updateProfileCallWS(destination);
                task.execute();
                imageView_user.setImageBitmap(bitmap);
                imageView_profile_image.setImageBitmap(bitmap);
                // Toast.makeText(MainActivity.this, "Profile Image are change", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void OnLogoutDilog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage("Are you sure for logout Edge Fintrack ?");
        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                alert.dismiss();
            }
        });
        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                session.logoutUser();
                MainActivity.this.finish();
            }
        });
        alert = dialog.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("StaticFieldLeak")
    private class getProfilePhoto extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorProfilePhoto);
                Request.addProperty("Registration_Id", Registration_Id);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfilePhoto);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorProfilePhoto, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //Log.i(TAG, "getProfilePhoto_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getProfilePhoto Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getProfilePhoto Result " + resultString.toString() + " " + Registration_Id);
                /**
                 * [{"Registration_Id":"FIN201822772158860","Profile_Pic":"http://fintrackindia.com/FilesUploaded/Investor/profile/FIN20182277215886081navdeep.jpg"}] FIN201822772158860
                 * */

                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("Profile_Pic")) {
                        if (!jsonObject.getString("Profile_Pic").isEmpty()) {
                            if (jsonObject.getString("Profile_Pic").contains(".jpg")) {
                                try {
                                    Picasso.get()
                                            .load(jsonObject.getString("Profile_Pic"))
                                            .placeholder(R.drawable.user_icon)
                                            .error(R.drawable.user_icon)
                                            .into(imageView_user);
                                    Picasso.get()
                                            .load(jsonObject.getString("Profile_Pic"))
                                            .placeholder(R.drawable.user_icon)
                                            .error(R.drawable.user_icon)
                                            .into(imageView_profile_image);
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    String imageString = jsonObject.getString("Profile_Pic").replace("http://fintrackindia.com/FilesUploaded/Investor/profile/", "");
                                    //decode base64 string to image
                                    byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imageView_user.setImageBitmap(decodedImage);
                                    imageView_profile_image.setImageBitmap(decodedImage);
                                } catch (Exception e) {
                                    imageView_user.setImageResource(R.drawable.user_icon);
                                    imageView_profile_image.setImageResource(R.drawable.user_icon);
                                }
                            }
                        } else {
                            imageView_user.setImageResource(R.drawable.user_icon);
                            imageView_profile_image.setImageResource(R.drawable.user_icon);
                        }
                    }
                }

            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getProfilePhoto Error:- " + e.getMessage());
                ShowDilog(MainActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class updateProfileCallWS extends AsyncTask<Void, Void, Void> {
        File destination;

        updateProfileCallWS(File destination) {
            this.destination = destination;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_updateInvestorProfilePhoto);
                Request.addProperty("Registration_Id", Registration_Id);
                Request.addProperty("ProfilePhoto", imageString);

                Log.i(TAG, "SoapObject Request " + Request.toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfilePhoto);

                transport.call(SOAP_ACTION + METHOD_NAME_updateInvestorProfilePhoto, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "updateProfileCallWS_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "updateProfileCallWS Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "updateProfileCallWS Result " + resultString.toString());
                /*if (resultString.toString().equalsIgnoreCase("success")) {
                    Toast.makeText(MainActivity.this, "Profile Image are change", Toast.LENGTH_SHORT).show();
                    getProfilePhoto task = new getProfilePhoto();
                    task.execute();
                } else {
                    Toast.makeText(MainActivity.this, "" + resultString.toString(), Toast.LENGTH_SHORT).show();
                }*/

                getProfilePhoto task = new getProfilePhoto();
                task.execute();

            } catch (NullPointerException e) {
                Log.d(TAG, "updateProfileCallWS Error:- " + e.getMessage());
            }
        }
    }

    /* //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //decode base64 string to image
        imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image.setImageBitmap(decodedImage);*/
}
