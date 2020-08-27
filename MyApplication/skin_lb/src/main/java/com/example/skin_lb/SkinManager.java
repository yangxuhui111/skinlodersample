package com.example.skin_lb;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;

/**
 * 加载皮肤资源对象
 */
public class SkinManager {
    //单例模式
    public static SkinManager skinManager;
    //上下文
    private Context context;
    //资源包的包名
    private String packageName;
    //资源包的资源对象
    private Resources resources;
    private SkinManager() {}

    public static SkinManager getInstance(){
        if(skinManager==null){
            skinManager=new SkinManager();
        }
        return skinManager;
    }

    public void setContext(Context context){
        this.context=context;
    }
    /**
     * 根据路径去加载皮肤包
     * @param path
     */
    public void loadApk(String path){
        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //获取资源包的包信息类
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        packageName=packageArchiveInfo.packageName;
        try {
            //通过反射获取到AssetManager的对象
            AssetManager assetManager=AssetManager.class.newInstance();
            //通过反射获取到addAssetPath方法
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,path);
            resources=new Resources(assetManager,context.getResources().getDisplayMetrics(),context.getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取颜色
     * @param resId  当前APP中要更换资源的资源ID
     * @return   匹配到的资源对象中的资源ID
     */
    public int getColor(int resId){
        if(resourceIsNull()){
            return resId;
        }
        //获取到资源id的类型
        String resourceTypeName = context.getResources().getResourceTypeName(resId);
        //获取到资源id的名字
        String resourceEntryName = context.getResources().getResourceEntryName(resId);
        //去匹配
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier==0){
            return resId;
        }
        return resources.getColor(identifier);
    }

    /**
     * 从资源包中拿到drawable的资源id
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId){
        if(resourceIsNull()){
            return ContextCompat.getDrawable(context,resId);
        }
        String resourceTypeName = context.getResources().getResourceTypeName(resId);
        String resourceEntryName = context.getResources().getResourceEntryName(resId);
        //去匹配
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier==0){
            return ContextCompat.getDrawable(context,resId);
        }
        return resources.getDrawable(identifier);
    }

    public int getDrawableId(int resId){
        if(resourceIsNull()){
            return resId;
        }
        String resourceTypeName = context.getResources().getResourceTypeName(resId);
        String resourceEntryName = context.getResources().getResourceEntryName(resId);
        //去匹配
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier==0){
            return resId;
        }
        return identifier;
    }
    /**
     * 判断资源包的资源对象是否为空
     * @return
     */
    public boolean resourceIsNull(){
        if(resources==null){
            return true;
        }
        return false;
    }
}
