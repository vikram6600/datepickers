package com.example.vikrampatel.mydemo;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vikrampatel.mydemo.ApiInterface.APIInterface;
import com.example.vikrampatel.mydemo.ApiManager.ApiClient;
import com.example.vikrampatel.mydemo.ApiResponse.RegisterResponse;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {
    /**
     * ButterKnife Code
     **/
    @NotEmpty
    @BindView(R.id.fname)
    EditText fname;
    @NotEmpty
    @BindView(R.id.lname)
    EditText lname;
    @NotEmpty
    @BindView(R.id.e1)
    EditText e1;
    @NotEmpty
    @BindView(R.id.e2)
    EditText e2;
    @Length(max = 10, min = 10, message = "enter 10 degit numbers")
    @BindView(R.id.mobile)
    EditText mobile;
    @NotEmpty
    @BindView(R.id.email)
    EditText email;
    @Length(message = "minimum 6 digits are requirre")
    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.btnsubmit)
    Button btnsubmit;

    Validator validator;
    String gender;


    APIInterface apiInterface;

    /**
     * ButterKnife Code
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);


        validator = new Validator(this);
        validator.setValidationListener(this);

        e2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = 2000;
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            selectedmonth = selectedmonth + 1;
                            e2.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.show();
                }
            }
        });

    }
        @OnClick(R.id.btnsubmit)
        void Btnsubmit(){
            validator.validate();

        }

            @Override
            public void onValidationSucceeded() {
               registration();
            }


   private void registration() {
       apiInterface = ApiClient.getClient().create(APIInterface.class);
       Call<RegisterResponse> registerResponseCall = apiInterface.REGISTER_RESPONSE_CALL(
               "registers",
               fname.getText().toString(),
               lname.getText().toString(),
               e1.getText().toString(),
               e2.getText().toString(),
               mobile.getText().toString(),
               email.getText().toString(),
               password.getText().toString()
       );
       registerResponseCall.enqueue(new Callback<RegisterResponse>() {
           @Override
           public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
               if (response.body().getStatus() == 1) {
                   Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(MainActivity.this, Main_register.class);
                   startActivity(intent);
               } else {
                   Toast.makeText(MainActivity.this, "faliure", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<RegisterResponse> call, Throwable t) {
               Log.e("## Message", t.getMessage().toString());
               Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

   }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
// validation fail

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, "fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

}