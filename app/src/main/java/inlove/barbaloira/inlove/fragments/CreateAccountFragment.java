package inlove.barbaloira.inlove.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import inlove.barbaloira.inlove.R;
import inlove.barbaloira.inlove.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireaseAuthListener;
    private EditText mUserName, mEmail, mPassword;
    private FloatingActionButton mAccountAdd;
    private String mUid;
    private DatabaseReference databaseReference;

    public CreateAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);
        mUserName = (EditText) v.findViewById(R.id.et_create_account_user_name);
        mEmail = (EditText) v.findViewById(R.id.et_create_account_email);
        mPassword = (EditText) v.findViewById(R.id.et_create_account_password);
        mAccountAdd = (FloatingActionButton) v.findViewById(R.id.fab_create_account_add);

        mAuth = FirebaseAuth.getInstance();


        mAccountAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Create account error", Toast.LENGTH_SHORT);
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    mUid = user.getUid();

                                    User u = new User(mUserName.getText().toString());
                                    databaseReference.child("users").child("infoPerfil").child(mUid).setValue(u);



                                    replaceToMapPage();
                                }
                            }
                        });
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private void replaceToMapPage() {
        getFragmentManager().beginTransaction().replace(R.id.rl_content_main, new MapFragment()).addToBackStack(null).commit();
    }

}
