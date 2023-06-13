package com.example.myc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class BoardSelect extends AppCompatActivity {

    private ImageView igBack, igWriteIn;
    private EditText bTitle, bCont;
    private FirebaseAuth mFirebaseAuth;             // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;         // 실시간 데이터베이스
    private FirebaseUser user;
    private DatabaseReference userRef;
    private UserAccount currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_select);

        igBack = findViewById(R.id.ig_back);
        igWriteIn = findViewById(R.id.ig_writein);
        bTitle = findViewById(R.id.b_title);
        bCont = findViewById(R.id.b_cont);

        //DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("board");

        //User
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("mycommunity").child("UserAccount").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(UserAccount.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", String.valueOf(error.toException())); // 에러문 출력
            }
        });
        igWriteIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시물 등록 시작
                String inBTitle = bTitle.getText().toString();
                String inBCont = bCont.getText().toString();
                Board board = new Board();
                board.setTitle(inBTitle);
                board.setContext(inBCont);
                board.setUserName(currentUser.getNickName());

                mDatabaseRef.child(user.getUid()).setValue(board);

                Toast.makeText(BoardSelect.this, "게시글 등록이 완료됐습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}