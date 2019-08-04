package com.example.parly;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class signup_setting extends Fragment {
    RelativeLayout relativeLayout;
    ImageView chenge_cover_icon;
    CircleImageView chenge_photo_profile;
    Uri imgaeUri;
    Uri coverUri;
    EditText chenge_username, chenge_bio;
    String username = "", bio = "";
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button save, apply;
    private StorageReference Cover_Store, Photo_Store;
    private ProgressBar progressBar;

    private static final int COVER_IMG = 100;
    private static final int PROFILE_IMG = 200;
    Bitmap thumb_bitmap;

    public signup_setting() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == COVER_IMG) {
            coverUri = data.getData();
            chenge_cover_icon.setImageURI(null);
            chenge_cover_icon.setImageURI(coverUri);


        }
        if (resultCode == Activity.RESULT_OK && requestCode == PROFILE_IMG) {
            imgaeUri = data.getData();
            chenge_photo_profile.setBackgroundResource(0);
            chenge_photo_profile.setImageURI(null);
            chenge_photo_profile.setImageURI(imgaeUri);
            /*
            try {


                CropImage.activity(coverUri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(getActivity());

                //   Toast.makeText(setting.this, imageUri, Toast.LENGTH_LONG).show();*



          //  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);


                Uri resultUri = result.getUri();

                final File thumb_filePathUri = new File(resultUri.getPath());


                try {
                    thumb_bitmap = new Compressor(getContext())
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePathUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();




                final String nameimg = firebaseUser.getUid() + "_Photo_th." + getFileExtension(imgaeUri);
                Toast.makeText(getContext(), "xxxx", Toast.LENGTH_SHORT).show();
                StorageReference fileReference = Photo_Store.child(nameimg);
                Toast.makeText(getContext(), "000000000000", Toast.LENGTH_SHORT).show();
                UploadTask uploadTask = fileReference.putBytes(thumb_byte);
                Toast.makeText(getContext(), "ddddddddd", Toast.LENGTH_SHORT).show();
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getContext(), "sssssss", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 500);

                        Toast.makeText(getContext(), "Uplod done", Toast.LENGTH_SHORT).show();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

        //    }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
            }


        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup_setting, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        Cover_Store = FirebaseStorage.getInstance().getReference("Covers");
        Photo_Store = FirebaseStorage.getInstance().getReference("Photos");

        chenge_cover_icon = (ImageView) root.findViewById(R.id.chenge_cover_icon);
        chenge_photo_profile = (CircleImageView) root.findViewById(R.id.chenge_img_profile);
        chenge_username = (EditText) root.findViewById(R.id.chenge_username);
        chenge_bio = (EditText) root.findViewById(R.id.chenge_bio);
        save = (Button) root.findViewById(R.id.save);
        apply = (Button) root.findViewById(R.id.apply);

        progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.container_);
        chenge_cover_icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent galley = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                galley.setType("image/*");
                galley.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galley, COVER_IMG);
                return false;
            }
        });
        chenge_photo_profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent galley = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                 galley.setType("image/*");
                startActivityForResult(galley, PROFILE_IMG);
                return false;
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    User user = dataSnapshot.getValue(User.class);
                    username = user.getUsername();
                    chenge_username.setText(username);
                    bio = user.getBio();
                    chenge_bio.setText(bio);/*
                    if (user.getImgUrl() != "default") {

                        Glide.with(getActivity()).load(user.getImgUrl()).into(chenge_photo_profile);
                    }
                    if (user.getCoverUrl() != "default") {
                        Glide.with(getActivity()).load(user.getCoverUrl()).into(chenge_cover_icon);

                    }
                    */

                    if (!user.getImgUrl().equals("default")) {
                        chenge_photo_profile.setBackgroundResource(0);
                        Glide.with(getActivity()).load(user.getImgUrl()).into(chenge_photo_profile);
                    }

                    if (!user.getCoverUrl().equals("default")) {
                        Glide.with(getActivity()).load(user.getCoverUrl()).into(chenge_cover_icon);
                    }


                } else Toast.makeText(getActivity(), "mkach", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, new profile());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return root;
    }

    void Save() {
        progressBar.setProgress(0);
        if (!(username == chenge_username.getText().toString())) {
            reference.child("username").setValue(chenge_username.getText().toString());
        }
        progressBar.setProgress(30);
        if (!(bio == chenge_bio.getText().toString()))
            reference.child("bio").setValue(chenge_bio.getText().toString());

        progressBar.setProgress(60);
        if (coverUri != null) {
            //uploadCover();
            final String nameimg = firebaseUser.getUid() + "_Cover." + getFileExtension(coverUri);

            StorageReference fileReference = Cover_Store.child(nameimg);
            UploadTask uploadTask = fileReference.putFile(coverUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);

                    Toast.makeText(getContext(), "Uplod done", Toast.LENGTH_SHORT).show();
                    Cover_Store.child(nameimg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            reference.child("coverUrl").setValue(uri.toString());
                            //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } //else {Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show(); }
        progressBar.setProgress(80);

        if (imgaeUri != null) {
            //uploadPhoto();

            final String nameimg = firebaseUser.getUid() + "_Photo." + getFileExtension(imgaeUri);

            StorageReference fileReference = Photo_Store.child(nameimg);
            UploadTask uploadTask = fileReference.putFile(imgaeUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);

                    Toast.makeText(getContext(), "Uplod done", Toast.LENGTH_SHORT).show();
                    Photo_Store.child(nameimg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            reference.child("imgUrl").setValue(uri.toString());
                            //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } //else { Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show(); }


        Toast.makeText(getContext(), "Changes have been made", Toast.LENGTH_LONG).show();

        progressBar.setProgress(100);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadCover() {
        final String nameimg = firebaseUser.getUid() + "_Cover." + getFileExtension(coverUri);

        StorageReference fileReference = Cover_Store.child(nameimg);
        UploadTask uploadTask = fileReference.putFile(coverUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(0);
                    }
                }, 500);

                Toast.makeText(getContext(), "Uplod done", Toast.LENGTH_SHORT).show();
                Cover_Store.child(nameimg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        reference.child("coverUrl").setValue(uri.toString());
                        //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadPhoto() {
        final String nameimg = firebaseUser.getUid() + "_Photo." + getFileExtension(imgaeUri);

        StorageReference fileReference = Photo_Store.child(nameimg);
        UploadTask uploadTask = fileReference.putFile(imgaeUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(0);
                    }
                }, 500);

                Toast.makeText(getContext(), "Uplod done", Toast.LENGTH_SHORT).show();
                Photo_Store.child(nameimg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        reference.child("imgUrl").setValue(uri.toString());
                        //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

/*
            final String nameimg = firebaseUser.getUid() + "_Cover." + getFileExtension(coverUri);


            final File thumb_filePathUri = new File(coverUri.getPath());


            try {
                thumb_bitmap = new Compressor(getContext())
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePathUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumb_byte = baos.toByteArray();


            StorageReference fileReference = Cover_Store.child(nameimg);
            UploadTask uploadTask = fileReference.putBytes(thumb_byte);*/
