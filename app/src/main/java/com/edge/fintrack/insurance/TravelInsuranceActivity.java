package com.edge.fintrack.insurance;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.edge.fintrack.R;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Calendar;

public class TravelInsuranceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public final String TAG = "TravelInsuranceActivity";
    SoapPrimitive resultString;
    ImageView iv_menu_item;

    TextView tv_family_member, tv_tripSta, tv_tripEnd;
    ArrayAdapter<String> pAge_adapter, sp_c1Age_adapter;
    DatePickerDialog datePickerDialog;
    String sp_sAge_item = "18", sp_spAge_item = "18", sp_c1Age_item = "3-12 Months", sp_c2Age_item = "3-12 Months";
    String sDate1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_travel_insurance);

        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_family_member = (TextView) findViewById(R.id.tv_family_member);
        tv_family_member.setOnClickListener(this);

        ArrayList<String> pAge_list = new ArrayList<>();
        for (int i = 18; i <= 100; i++) {
            pAge_list.add(String.valueOf(i));
        }
        pAge_adapter = new ArrayAdapter<>(TravelInsuranceActivity.this, android.R.layout.simple_spinner_dropdown_item, pAge_list);
        pAge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pAge_adapter.notifyDataSetChanged();

        ArrayList<String> cAge_list = new ArrayList<>();
        cAge_list.add("3-12 Months");
        for (int i = 1; i <= 17; i++) {
            cAge_list.add(String.valueOf(i));
        }
        sp_c1Age_adapter = new ArrayAdapter<>(TravelInsuranceActivity.this, android.R.layout.simple_spinner_dropdown_item, cAge_list);
        sp_c1Age_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_c1Age_adapter.notifyDataSetChanged();

        tv_tripSta = (TextView) findViewById(R.id.tv_tripSta);
        tv_tripSta.setOnClickListener(this);

        tv_tripEnd = (TextView) findViewById(R.id.tv_tripEnd);
        tv_tripEnd.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_family_member:
                SelectMember();
                break;
            case R.id.tv_tripSta:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(TravelInsuranceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                               /* tv_tripSta.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);*/
                                sDate1 = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                                System.out.println(sDate1);
                                tv_tripSta.setText(sDate1.toString());
                                /*try {
                                    String string = "January 2, 2010";
                                    DateFormat format = new SimpleDateFormat("MMM DD, YYYY");
                                    Date date = format.parse(string);
                                    System.out.println(date);
                                    tv_tripSta.setText(date.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*/
                                tv_tripSta.setTextColor(getResources().getColor(R.color.colorBlack));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;

            case R.id.tv_tripEnd:
                // date picker dialog
                String getfromdate = tv_tripSta.getText().toString().trim();
                String getfrom[] = sDate1.split("/");
                int year, month, day;
                year = Integer.parseInt(getfrom[2]);
                month = Integer.parseInt(getfrom[1]) - 1;
                day = Integer.parseInt(getfrom[0]);
                final Calendar cs = Calendar.getInstance();
                cs.set(year, month, day);
                datePickerDialog = new DatePickerDialog(TravelInsuranceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                tv_tripEnd.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                tv_tripEnd.setTextColor(getResources().getColor(R.color.colorBlack));
                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cs.getTimeInMillis());
                datePickerDialog.show();
                break;
        }
    }

    private void SelectMember() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View ageDialogView = factory.inflate(R.layout.layout_hincu_member, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(ageDialogView);

        final LinearLayout lay_self = (LinearLayout) ageDialogView.findViewById(R.id.lay_self);
        final LinearLayout lay_spou = (LinearLayout) ageDialogView.findViewById(R.id.lay_spou);
        final LinearLayout lay_c1 = (LinearLayout) ageDialogView.findViewById(R.id.lay_c1);
        final LinearLayout lay_c2 = (LinearLayout) ageDialogView.findViewById(R.id.lay_c2);

        final Spinner sp_sAge = (Spinner) ageDialogView.findViewById(R.id.sp_sAge);
        sp_sAge.setOnItemSelectedListener(this);
        sp_sAge.setAdapter(pAge_adapter);

        final CheckBox ckb_self = (CheckBox) ageDialogView.findViewById(R.id.ckb_self);
        ckb_self.setChecked(true);
        ckb_self.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (isChecked) {
                    lay_self.setVisibility(View.VISIBLE);
                } else {
                    lay_self.setVisibility(View.INVISIBLE);
                }
            }
        });

        final Spinner sp_spAge = (Spinner) ageDialogView.findViewById(R.id.sp_spAge);
        sp_spAge.setOnItemSelectedListener(this);
        sp_spAge.setAdapter(pAge_adapter);

        final CheckBox ck_spouse = (CheckBox) ageDialogView.findViewById(R.id.ck_spouse);
        ck_spouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (isChecked) {
                    lay_spou.setVisibility(View.VISIBLE);
                } else {
                    lay_spou.setVisibility(View.INVISIBLE);
                }
            }
        });

        Spinner sp_c1Age = (Spinner) ageDialogView.findViewById(R.id.sp_c1Age);
        sp_c1Age.setOnItemSelectedListener(this);
        sp_c1Age.setAdapter(sp_c1Age_adapter);

        final CheckBox ck_ch1 = (CheckBox) ageDialogView.findViewById(R.id.ck_ch1);
        ck_ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (isChecked) {
                    lay_c1.setVisibility(View.VISIBLE);
                } else {
                    lay_c1.setVisibility(View.INVISIBLE);
                }
            }
        });

        final Spinner sp_c2Age = (Spinner) ageDialogView.findViewById(R.id.sp_c2Age);
        sp_c2Age.setOnItemSelectedListener(this);
        sp_c2Age.setAdapter(sp_c1Age_adapter);

        final CheckBox ck_ch2 = (CheckBox) ageDialogView.findViewById(R.id.ck_ch2);
        ck_ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (isChecked) {
                    lay_c2.setVisibility(View.VISIBLE);
                } else {
                    lay_c2.setVisibility(View.INVISIBLE);
                }
            }
        });

        TextView tv_save = (TextView) ageDialogView.findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (ckb_self.isChecked()) {
                    sb.append("Self(" + sp_sAge_item + ") ");//now original string is changed
                }
                if (ck_spouse.isChecked()) {
                    sb.append("Spouse(" + sp_spAge_item + ") ");//now original string is changed
                }
                if (ck_ch1.isChecked()) {
                    sb.append("Son/Daughter(" + sp_c1Age_item + ") ");//now original string is changed
                }
                if (ck_ch2.isChecked()) {
                    sb.append("Son/Daughter(" + sp_c2Age_item + ")");//now original string is changed
                }
                tv_family_member.setText(sb);
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
