package com.example.parly;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class login extends Fragment {
    private Button loginbtn;
    private EditText email, password;
    FirebaseAuth auth;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        loginbtn = (Button) root.findViewById(R.id.loginbtn);
        auth = FirebaseAuth.getInstance();
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
//                    EditText editText = (EditText) findViewById(R.id.editText);
//                    String message = editText.getText().toString();
//                    intent.putExtra(EXTRA_MESSAGE, message);
                //               startActivity(intent);
                String txtemail = email.getText().toString();
                String txtpassword = password.getText().toString();
                if (TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword)) {

                } else {
                    auth.signInWithEmailAndPassword(txtemail, txtpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                         Toast.makeText(getActivity(),"authuntification feildes",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });



        return root;
    }





    }


