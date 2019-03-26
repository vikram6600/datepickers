package com.example.vikrampatel.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vikrampatel.mydemo.ApiInterface.APIInterface;
import com.example.vikrampatel.mydemo.ApiManager.ApiClient;
import com.example.vikrampatel.mydemo.ApiResponse.LoginResponse;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity2 extends AppCompatActivity implements Validator.ValidationListener {
    /**
     * ButterKnife Code
     **/

    @Length(max = 10, min = 10, message = "enter 10 degit numbers")
    @BindView(R.id.mobile)
    EditText mobile;
    @Length(message = "minimum 6 digits are requirre")
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btnsubmit1)
    Button btnsubmit1;
    @BindView(R.id.btnsubmit2)
    Button btnsubmit2;

    Validator validator;

    APIInterface apiInterface;

    /**
     * ButterKnife Code
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        validator = new Validator(this);
        validator.setValidationListener(this);

    }
    @OnClick(R.id.btnsubmit1)
    void btnsubmit1(){
        validator.validate();

    }
    @OnClick(R.id.btnsubmit2)
    void setBtnsubmit2(){
        Intent a = new Intent(MainActivity2.this,MainActivity.class);
        startActivity(a);
    }

    @Override
    public void onValidationSucceeded() {
        registration();
    }


    private void registration() {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<LoginResponse> Login_ResponseCall = apiInterface.LOGIN_RESPONSE_CALL("Login",
                mobile.getText().toString(),
                password.getText().toString()
        );
        Login_ResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.body().getSuccess() == 1) {
                    Toast.makeText(MainActivity2.this, "success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity2.this, Main_register.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity2.this, "faliure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("## Message", t.getMessage().toString());
                Toast.makeText(MainActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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