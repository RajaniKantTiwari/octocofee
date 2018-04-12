package me.prateeksaigal.ocotocoffee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;

public class ProfileActivity extends BaseActivity {

    private EditText email, username, phone;
    private ImageView user_img;
    private RelativeLayout change_user_img;
    private Button save;
    boolean isPermission = false;
    private String image = "";

    public final String[] EXTERNAL_PERMS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        LinearLayout back = (LinearLayout)findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        user_img  = (ImageView) findViewById(R.id.user_img);
        save = (Button) findViewById(R.id.save);

        change_user_img = (RelativeLayout) findViewById(R.id.change_user_img);

        new AskPermission.Builder(ProfileActivity.this)
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(new PermissionCallback() {
                    @Override
                    public void onPermissionsGranted(int requestCode) {
                        isPermission = true;
                    }
                    @Override
                    public void onPermissionsDenied(int requestCode) {
                        isPermission = false;
                    }
                })
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {
                        isPermission = false;
                    }

                    @Override
                    public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {
                        isPermission = false;
                    }
                })
                .request(1);

        change_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermission )
                    showFileChooser();
                else {
                    Toast.makeText(getApplicationContext(), "Please enable Storage permission from Settings and restart app", Toast.LENGTH_SHORT).show();

                }

            }
        });

        final ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());

        ArrayList<String> settings = profileDBHelper.getSettings();
        if(settings != null){
            username.setText(settings.get(0));
            email.setText(settings.get(1));
            phone.setText(settings.get(2));
            String image_tmp = settings.get(3);
            if( !image_tmp.equals("") ) {
                imagePathIntoImageView(user_img, image_tmp, ProfileActivity.this);
            }
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDBHelper.deleteALL();
                profileDBHelper.newSettings(username.getText().toString(),email.getText().toString(), phone.getText().toString(), image);
                Toast.makeText(getApplicationContext(),"Profile Saved",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showFileChooser() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file;
        if ( resultCode == RESULT_OK ) {

            if(requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                File[] te = f.listFiles();
                for (File temp : te) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    Log.d("hey", f.getAbsolutePath());
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    imagePathIntoImageView(user_img,f.getAbsolutePath(), ProfileActivity.this);


                    // f.delete();
                    OutputStream outFile = null;
                    try {
                        String path = "default" + String.valueOf(System.currentTimeMillis()) + ".jpg" ;
                        outFile = openFileOutput(path, getApplicationContext().MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        image = getApplicationContext().getFilesDir() + "/" + path;
                        f.delete();
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == 2 && data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                Log.d("path of image ", picturePath+"");
                //user_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(),picturePath, 140,140));
                //user_img.invalidate();
                //drawable = user_img.getDrawable();
                //Toast.makeText(getApplicationContext(),drawable.toString(),Toast.LENGTH_SHORT).show();
                imagePathIntoImageView(user_img, picturePath, ProfileActivity.this);
                image = picturePath;

            }

        }
    }
   private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void bitmapIntoImageView(ImageView imageView, Bitmap bitmap, Activity activity){
        Uri imageUri = getImageUri(bitmap);
        Picasso.with(activity).load(imageUri).into(imageView);
    }

    public void imagePathIntoImageView(ImageView imageView, String imagePath, Activity activity) {
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Picasso.with(activity).load(imageUri).into(imageView);
    }

    private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, String file,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(BaseResponse response, int requestCode) {

    }
}
