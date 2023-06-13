package com.example.myc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    private FirebaseAuth mFirebaseAuth;             // 파이어베이스 인증
    private FirebaseStorage storage;
    private StorageReference storageRef;          // 파이어베이스 스토리지

    private DatabaseReference mDatabaseRef;         // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtNickName; // 회원가입 입력필드
    private TextView mBtnRegister;                  // 회원가입 버튼
    private ImageView mImageUser;                    // 프로필 이미지
    private Uri imageUri;                           // ??
    private Intent imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // auth인스턴스와 DBref 가져오기
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mycommunity");
        storage = FirebaseStorage.getInstance();

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd); //비밀번호는 무조건 6자리 넘게 넣기
        mEtNickName = findViewById(R.id.et_nickname);
        mBtnRegister = findViewById(R.id.btn_register);
        mImageUser = findViewById(R.id.image_user);

        mImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if(id == R.id.image_user){
                    loadAlbum();
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strNickName = mEtNickName.getText().toString();

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            account.setNickName(strNickName);
                            account.setImage("gs://mycproject-f539c.appspot.com/userphoto/"+firebaseUser.getUid()+".png");

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            //이미지 업로드 실행
                            imageUri = imageData.getData();
                            StorageReference storageRef = storage.getReference();
                            StorageReference riversRef = storageRef.child("userphoto/" + firebaseUser.getUid() +".png");
                            riversRef.putFile(imageUri);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    // 갤러리 앱 실행 함수
    private void loadAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }
    //사진 업로드 처리
    protected void onActivityResult (int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            imageData = data;
            try {
                InputStream in =
                        getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                //mImageUser.setImageBitmap(img);
                Glide.with(this).load(img).circleCrop().into(mImageUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(RegisterActivity.this, " 사진이 정상적으로 업로드 되지 않았습니다." , Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegisterActivity.this, " 사진이 정상적으로 업로드 되었습니다." , Toast.LENGTH_SHORT).show();
                }
            });
            */
        }
    }
}
