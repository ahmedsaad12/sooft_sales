package com.sooft_sales.uis.activity_add_product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sooft_sales.R;
import com.sooft_sales.adapter.SpinnerCategoryAdapter;
import com.sooft_sales.databinding.ActivityAddProductBinding;
import com.sooft_sales.local_database.AccessDatabase;
import com.sooft_sales.local_database.DataBaseInterfaces;
import com.sooft_sales.model.AddProductModel;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.share.Common;
import com.sooft_sales.uis.activity_base.BaseActivity;
import com.sooft_sales.uis.activity_home.fragments_home_navigaion.FragmentHome;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AddProductActivity extends BaseActivity implements DataBaseInterfaces.LastProductInterface, DataBaseInterfaces.CategoryInterface, DataBaseInterfaces.ProductInsertInterface {
    private ActivityAddProductBinding binding;
    private AddProductModel addProductModel;
    private ActivityResultLauncher<Intent> launcher;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private int selectedReq = 0;
    private Uri uri = null;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private List<DepartmentModel> departmentModelList;
    private AccessDatabase accessDatabase;
    private double id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();

    }


    private void initView() {
        accessDatabase = new AccessDatabase(this);

        departmentModelList = new ArrayList<>();
        spinnerCategoryAdapter = new SpinnerCategoryAdapter(departmentModelList, this);
        addProductModel = new AddProductModel();
        binding.setModel(addProductModel);
        binding.spinner.setAdapter(spinnerCategoryAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (departmentModelList.size() > 0) {
                    addProductModel.setCategory_id(departmentModelList.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSheet();
            }
        });

        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });

        binding.nextBtn.setOnClickListener(view -> {
            if (addProductModel.isDataValid(this)) {
                ProductModel productModel = new ProductModel();
                productModel.setType("local");
                double min = 1000;
                double max = 100000;

                //Generate random int value from 50 to 100
                double random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
                id = random_int;
                productModel.setId(id);
                productModel.setCategory_id(addProductModel.getCategory_id());
                if(addProductModel.getPrice().isEmpty()){
                    productModel.setPrice(0);
                }
                else{
                    productModel.setPrice(Double.parseDouble(addProductModel.getPrice()));
                }
                if (getUserModel().getData().getUser().getLang().equals("ar")) {
                    productModel.setTitle(addProductModel.getArabic_title());
                    productModel.setTitle2(addProductModel.getEnglish_title());
                } else {
                    productModel.setTitle(addProductModel.getEnglish_title());
                    productModel.setTitle2(addProductModel.getArabic_title());
                }
                setImageBitmap(productModel);

            }
        });

        binding.btnCancel.setOnClickListener(view -> closeSheet());


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (selectedReq == READ_REQ) {

                    uri = result.getData().getData();
                    File file = new File(Common.getImagePath(this, uri));
                    if (file.isFile()) {
                        Picasso.get().load(file).fit().into(binding.image1);
                    } else {
                        Picasso.get().load(uri).fit().into(binding.image1);
                    }
                    addProductModel.setImage(uri);
                    binding.setModel(addProductModel);


                } else if (selectedReq == CAMERA_REQ) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    uri = getUriFromBitmap(bitmap);
                    if (uri != null) {
                        String path = Common.getImagePath(this, uri);

                        if (path != null) {
                            Picasso.get().load(new File(path)).fit().into(binding.image1);
                            addProductModel.setImage(uri);
                            binding.setModel(addProductModel);


                        } else {
                            Picasso.get().load(uri).fit().into(binding.image1);
                            addProductModel.setImage(uri);
                            binding.setModel(addProductModel);


                        }
                    }
                }
            }
        });
        try {
            accessDatabase.getlastProduct(this);

        } catch (Exception e) {

        }
    }

    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }

    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }


    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {
        selectedReq = req;
        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            launcher.launch(intent);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "filename", ""));
    }


    @Override
    public void onProductDataInsertedSuccess(Boolean bol) {
        if (bol) {
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.ch_name), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCategoryDataSuccess(List<DepartmentModel> categoryModelList) {
        departmentModelList.addAll(categoryModelList);
        spinnerCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLastProductDataSuccess(ProductModel productModel) {
        if (productModel != null) {
            id = productModel.getId() + 1;
            try {
                accessDatabase.getCategory(this);

            } catch (Exception e) {

            }
        }
    }

    public void setImageBitmap(ProductModel productModel) {


        Glide.with(this)
                .asBitmap()
                .load(addProductModel.getImage())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        productModel.setImageBitmap(stream.toByteArray());
                        try {
                            accessDatabase.insertSingleProduct(productModel, AddProductActivity.this);

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // super.onLoadFailed(errorDrawable);

                    }
                });
    }

}