package inlove.barbaloira.inlove.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import inlove.barbaloira.inlove.MainActivity;

import inlove.barbaloira.inlove.R;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireaseAuthListener;

    private EditText mEmail, mPassword;
    private Button btn_sign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEmail = (EditText) v.findViewById(R.id.et_sign_email);
        mPassword = (EditText) v.findViewById(R.id.et_sign_password);
        mAuth = FirebaseAuth.getInstance();


        fireaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    replaceToMapPage();
                }
            }
        };


        btn_sign = (Button) v.findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Sigin error", Toast.LENGTH_SHORT);
                                } else {
                                    replaceToMapPage();
                                }

                            }
                        });
            }
        });

        Button btn_create_account = (Button) v.findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.rl_content_main, new CreateAccountFragment()).addToBackStack(null).commit();
            }
        });


        return v;
    }

    @Override
    public void onResume() {

        super.onResume();
        ((MainActivity) getActivity()).setToolbarBottomVisible(false);

    }


    private void replaceToMapPage() {
        getFragmentManager().beginTransaction().remove(this).add(R.id.rl_content_main, new MapFragment()).addToBackStack(null).commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            mAuth.addAuthStateListener(fireaseAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(fireaseAuthListener);
        }
    }

}
