package com.tanyiqu.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    static final int STORAGE_CALL_BACK_CODE = 0;
    static final String[] perms_storage = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        if (EasyPermissions.hasPermissions(this, perms_storage)) {
            Toast.makeText(this, "已经有存储权限", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "请给我权限", STORAGE_CALL_BACK_CODE, perms_storage);
        }
    }

    void pass() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (requestCode) {
            // 如果存储权限申请通过
            case STORAGE_CALL_BACK_CODE:
                Toast.makeText(this, "已同意存储权限", Toast.LENGTH_SHORT).show();
                // pass为通过后执行的方法，注意下面有个finish()，pass()中就不要再加finish()了
                pass();
                finish();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (requestCode) {
            case STORAGE_CALL_BACK_CODE:
                Toast.makeText(this, "已拒绝存储权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, perms_storage, STORAGE_CALL_BACK_CODE);
                break;
        }
        // 如果权限被永久拒绝，就提示到设置页面中开启
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "权限已被永久拒绝", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("权限已被永久拒绝")
                    .setRationale("该应用需要此权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消")
                    .build()
                    .show();
        }
    }
}
