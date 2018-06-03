package com.example.mehrab_patwary.bindas;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventUpload extends Fragment {
    // for calender start
    private EditText sDate, eDate;
    private Calendar calendar;
    private int year, month, day;
    private String date, createDate;
    // for calender end
 //   private static final int PICK_IMAGE_REQUEST = 1;
    private EditText mEditTextEventName;
    private EditText mEditTextLocation;
    private EditText mEditTextDetails;
  //  private ImageView mImageView;
  //  private TextView mTextViewShowUpload;
  //  private Button mButtonUpload;
    private Button mButtonSave;
 //   private ProgressBar mProgressBar;

//   private  Uri mImageUri;
    private DatabaseReference eventTbleRef;
 //   private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    public EventUpload() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.event_form, container, false);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month= calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        sDate = v.findViewById(R.id.formStartDate);
        eDate = v.findViewById(R.id.formEndDate);
        mEditTextEventName = v.findViewById(R.id.formName);
        mEditTextLocation = v.findViewById(R.id.formLocation);
        mEditTextDetails = v.findViewById(R.id.formDescription);
        mButtonSave = v.findViewById(R.id.formSaveBtn);

     //   mImageView = v.findViewById(R.id.formImage);
     //   mTextViewShowUpload = v.findViewById(R.id.formShowUpload);
     //   mButtonUpload = v.findViewById(R.id.formUploadBtn);
     //   mProgressBar = v.findViewById(R.id.formProgressBar);
       // mStorageRef = FirebaseStorage.getInstance().getReference("event_save");
       // mDatabaseRef = FirebaseDatabase.getInstance().getReference("event_save");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        eventTbleRef = mDatabaseRef.child("event_table");

        // Date picker event listner start **********************************************
        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                calendar.set(i,i1,i2);
                                date = sdf.format(calendar.getTime());
                                Calendar c = Calendar.getInstance();
                                createDate = sdf.format(c.getTime());
                                sDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                calendar.set(i,i1,i2);
                                date = sdf.format(calendar.getTime());
                                Calendar c = Calendar.getInstance();
                                createDate = sdf.format(c.getTime());
                                eDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
// Date picker event listner end *****************************************************
// Data save button for save data in firebase Start ***************************
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonOfEventFormDAta();
            }
        });
// Data save button for save darta in firebase End ****************************
// Select, show and upload image start ****************************************
       /* mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTextViewShowUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        // Select , show and upload image end ****************************************
        return v;

    }

    // This method created by mButtonSave onClickListner for data save +== Start ******
    private void saveButtonOfEventFormDAta() {
        String name = mEditTextEventName.getText().toString();
        String location = mEditTextLocation.getText().toString();
        String description = mEditTextDetails.getText().toString();
        String startDate = sDate.getText().toString();
        String endData = eDate.getText().toString();

        String id = eventTbleRef.push().getKey();
        Event e = new Event(id, name, location, description, startDate, endData);
        eventTbleRef.child(currentUser.getUid()).child(id).setValue(e);

    }
// This method created by mButtonSave onClickListner for data save +== End ******
    // method for file selection of image file
  /*  private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       // getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        getActivity().startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data !=null && data.getData() !=null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
           // mImageView.setImageURI(mImageUri);
        }

    }*/
}
