package com.example.myc;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class Frag4 extends Fragment {

    private View view;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private UserAccount currentUser;
    private ImageView userImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        userImage = view.findViewById(R.id.userimage1);
        TextView userName = view.findViewById(R.id.username);
        LinearLayout btn_logout = view.findViewById(R.id.btn_logout);
        LinearLayout btn_delete = view.findViewById(R.id.btn_delete);
        LinearLayout btn_list = view.findViewById(R.id.btn_list);

        Bundle bundle = getArguments();
        // 메인 액티비티에서 UserAccount객체가 담긴 번들 가져오기
        UserAccount user = (UserAccount) bundle.getSerializable("useraccount");
        userName.setText(user.getNickName());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("userphoto/"+user.getIdToken()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(getContext()).load(uri).circleCrop().into(userImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Log.d("test", "이미지 로드 실패");
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃하기
                mFirebaseAuth.signOut();
                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 탈퇴 처리
                mFirebaseAuth.getCurrentUser().delete();
                Toast.makeText(getContext(), "탈퇴처리 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserListActivity.class);
                startActivity(intent);
            }
        });

        //꼭 필요함
        return view;
    }
}
