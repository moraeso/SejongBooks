package com.example.sejongbooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sejongbooks.Helper.BackPressCloseHandler;
import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Helper.NetworkStatus;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Model.User;
import com.example.sejongbooks.Popup.ConfirmDialog;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.LoginTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.Singleton.MyInfo;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Button btnLogin, btnSignUp;
    private EditText editID, editPass;

    private BackPressCloseHandler backPressCloseHandler;
    private Dialog dialog;
    private ConfirmDialog errDialog;

    public static boolean isSignUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUser();

        if (BookManager.getInstance().getItems().isEmpty()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }

        initView();
        initListener();

        //loadBookData();

        backPressCloseHandler = new BackPressCloseHandler(this);

        connectNetwork();
        // postToken 내부에서 postBookList, initListener, callback으로 순차적 실행

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isSignUp){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            View customLayout=View.inflate(getApplicationContext(),R.layout.dialog_signup,null);
            builder.setView(customLayout);

            customLayout.findViewById(R.id.btn_ok_dialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog=builder.create();
            dialog.show();
            isSignUp = false;
        }

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_sign_up);
        editID = findViewById(R.id.edit_id_login);
        editPass = findViewById(R.id.edit_password_login);

        errDialog = new ConfirmDialog(this);
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(LoginActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(LoginActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 이미 퍼미션을 가지고 있다면
            // 위치 값을 가져올 수 있음

        } else {  //퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(LoginActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LoginActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LoginActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    private void connectNetwork(){
        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE) {
            Log.v("Network","모바일로 연결됨");
        } else if (status == NetworkStatus.TYPE_WIFI) {
            Log.v("Network","무선랜으로 연결됨");
        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
            View customLayout=View.inflate(getApplicationContext(),R.layout.dialog_network,null);
            builder.setView(customLayout);

            customLayout.findViewById(R.id.btn_cancel_network_dialog).setOnClickListener(this);
            customLayout.findViewById(R.id.btn_retry_network_dialog).setOnClickListener(this);

            dialog=builder.create();
            dialog.show();
        }
    }

    private void switchActivityToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                // TODO: 로그인
                postLogin();
                break;
            case R.id.btn_sign_up:
                // TODO: 회원가입
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cancel_network_dialog:
                dialog.dismiss();
                finish();
                break;
            case R.id.btn_retry_network_dialog:
                dialog.dismiss();
                connectNetwork();
                break;
        }
    }

    private void initUser() {
        MyInfo.getInstance().setUser(new User());
    }

    private void postLogin() {
        // ID PW 설정
        ContentValues values = new ContentValues();
        values.put("userID", editID.getText().toString());
        values.put("userPW", editPass.getText().toString());

        // 로그인 URL 설정
        String url = Constant.URL + "/user/login";

        // execute 및 MyInfo에 토큰 저장
        LoginTask loginTask = new LoginTask(url, values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                switchActivityToMain();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("mmee:LoginActivity",e.toString());
                errDialog.setErrorMessage("아이디 또는 비밀번호가 일치하지 않습니다.\n다시 입력해 주세요.");
                errDialog.show();
            }
        });
        loginTask.execute();
        //tokenTask.executeOnExecutor()
    }

    /*
    private void loadBookData() {
        // 산 URL 설정
        String url = Constant.URL + "/api/mntall";

        // execute, 산 리스트 생성 및 저장
        BookTask bookTask = new BookTask(Constant.GET_NEW, url, null, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                Log.d("mmee:bookTask", "get book resource success!");
            }

            @Override
            public void onFailure(Exception e) {
                //e.printStackTrace();
            }
        });
        bookTask.execute();
    }
    */
}
