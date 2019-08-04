package com.example.parly;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class signup extends Fragment {

    private EditText username, email, password;
    private Button signup;
    FirebaseAuth auth;
    DatabaseReference refrence;
    ProgressDialog dialog;

    public signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        username = (EditText) root.findViewById(R.id.username);
        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        signup = (Button) root.findViewById(R.id.signup);


        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(getActivity(), "dakhal data ta3ak", Toast.LENGTH_SHORT).show();


                } else if (txt_password.length() < 6) {
                    Toast.makeText(getActivity(), "lazam 6 character min", Toast.LENGTH_SHORT).show();

                } else {
                     dialog = ProgressDialog.show(getContext(), "Loading...", "Please wait...", true);
                    dialog.show();
                     register(txt_username, txt_email, txt_password);
                     dialog.dismiss();


                }




            }
        });
        return root;
    }


    private void register(final String username, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userId = firebaseUser.getUid();
                    refrence = FirebaseDatabase.getInstance().getReference("User").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username);
                    hashMap.put("bio", "");
                    hashMap.put("imgUrl", "default");
                    hashMap.put("coverUrl", "default");
                    hashMap.put("background", "2");
                    hashMap.put("compte", "Actif");
                    refrence.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               /* Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();*/
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.putExtra("open_signup_setting", true);
                                startActivity(i);


                            } else {
                                Toast.makeText(getActivity(), "you can't upload data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "you can't register woth email &amp password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
