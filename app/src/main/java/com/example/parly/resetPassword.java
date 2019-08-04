package com.example.parly;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class resetPassword extends Fragment {
    FirebaseUser user;
    EditText current_password ,newPassword, rePassword;
    Button savePassword;

    public resetPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reset_password, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        current_password = (EditText) root.findViewById(R.id.current_password);
        newPassword = (EditText) root.findViewById(R.id.new_password);
        rePassword = (EditText) root.findViewById(R.id.re_password);
        savePassword = (Button) root.findViewById(R.id.savepassword);
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                final String new_Password = newPassword.getText().toString();
                String re_Password = rePassword.getText().toString();
              String  cur_password=current_password.getText().toString();
                if (new_Password.length() > 6) {
                    if (new_Password.equals(re_Password)) {
                        newPassword.setText("");
                        rePassword.setText("");
                      /*  user.updatePassword(new_Password)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(),"User password updated.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });*/



// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.

                        final String email = user.getEmail();
                        AuthCredential credential = EmailAuthProvider.getCredential(email,cur_password);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    user.updatePassword(new_Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()){
                                                Snackbar snackbar_fail = Snackbar
                                                        .make(getView(), "Something went wrong. Please try again later", Snackbar.LENGTH_LONG);
                                                snackbar_fail.show();
                                            }else {
                                                Snackbar snackbar_su = Snackbar
                                                        .make(getView(), "Password Successfully Modified", Snackbar.LENGTH_LONG);
                                                snackbar_su.show();
                                            }
                                        }
                                    });
                                }else {
                                    Snackbar snackbar_su = Snackbar
                                            .make(getView(), "Authentication Failed", Snackbar.LENGTH_LONG);
                                    snackbar_su.show();
                                }
                            }
                        });


                    } else Toast.makeText(getContext(),"write the same password",Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(),"min 6 chars",Toast.LENGTH_SHORT).show();  }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

}
