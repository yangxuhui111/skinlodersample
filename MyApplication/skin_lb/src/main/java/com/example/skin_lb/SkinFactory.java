package com.example.skin_lb;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {

    private static final String[] prxfixList={
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //收集需要换肤的控件的容器
    private List<SkinView> viewList=new ArrayList<>();

    public void apply(){
        for (SkinView skinView : viewList) {
            skinView.apply();
        }

    }



    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view=null;
        if(name.contains(".")){
            //带包名
            view = onCreateView(name,context,attrs);
        }else{
            //不带包名
            for (String s : prxfixList) {
                String viewName=s+name;
                view = onCreateView(viewName,context,attrs);
                if(view != null){
                    break;
                }
            }
        }
        //收集需要换肤的控件
        if(view != null){
            paserView(view,name,attrs);
        }
        return view;
    }

    /**
     * 收集需要换肤的控件
     * @param view
     * @param name
     * @param attrs
     */
    private void paserView(View view, String name, AttributeSet attrs) {
        List<SkinItem> skinItems=new ArrayList<>();
        //遍历的是当前进来的控件的所有的属性
        for(int i=0;i<attrs.getAttributeCount();i++){
            //得到属性的名字
            String attributeName = attrs.getAttributeName(i);
            if(attributeName.contains("background") || attributeName.contains("textColor") || attributeName.contains("src")){
                //资源ID
                String attributeValue = attrs.getAttributeValue(i);
                int resId=Integer.parseInt(attributeValue.substring(1));
                String resourceTypeName = view.getResources().getResourceTypeName(resId);
                String resourceEntryName = view.getResources().getResourceEntryName(resId);
                //认为是要收集的
                SkinItem skinItem=new SkinItem(attributeName,resourceTypeName,resourceEntryName,resId);
                skinItems.add(skinItem);
            }
        }
        //如果一个控件有需要换肤的属性
        if (skinItems.size()>0){
            SkinView skinView=new SkinView(view,skinItems);
            viewList.add(skinView);
        }
    }

    /**
     * 将控件实例化
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view =null;
        try {
            Class aClass = context.getClassLoader().loadClass(name);
            //首先获取到第二个构造方法
            Constructor<? extends View> constructor = aClass.getConstructor(Context.class, AttributeSet.class);
            view = constructor.newInstance(context, attrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 需要换肤的控件的封装对象
     */
    class SkinView{
        View view;
        List<SkinItem> skinItems;

        public SkinView(View view, List<SkinItem> skinItems) {
            this.view = view;
            this.skinItems = skinItems;
        }

        public void apply(){
            for (SkinItem skinItem : skinItems) {
                //判断这条属性是不是background
                if(skinItem.getName().equals("background")){
                    //1.设置的是color颜色 2.设置的是图片
                    if(skinItem.getTypeName().equals("color")){
                        //将资源ID 传给ResourceManager去进行资源匹配 如果匹配到了 就直接设置给控件
                        //如果没有匹配到，就把之前的资源ID 设置控件
                         if(SkinManager.getInstance().resourceIsNull()){
                             //去资源对象匹配
                             view.setBackgroundResource(SkinManager.getInstance().getColor(skinItem.getResId()));
                         }else{
                             view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                         }
                    }else if(skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }else{
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                }else if(skinItem.getName().equals("src")){
                    if(skinItem.getTypeName().equals("color")){
                        //将资源ID 传给ResourceManager去进行资源匹配 如果匹配到了 就直接设置给控件
                        //如果没有匹配到，就把之前的资源ID 设置控件
                        if(SkinManager.getInstance().resourceIsNull()){
                            view.setBackgroundResource(SkinManager.getInstance().getColor(skinItem.getResId()));
                        }else{
                            view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                        }
                    }else if(skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        ((ImageView)view).setImageResource(SkinManager.getInstance().getDrawableId(skinItem.getResId()));
                    }
                }else if(skinItem.getName().equals("textColor")){
                    ((TextView)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                }
            }

        }
    }


    /**
     * 每条属性的封装对象
     */
    class SkinItem{
        //属性名字
        String name;
        //属性的值的类型
        String typeName;
        //属性的值的名字
        String entryName;
        //属性的资源ID
        int resId;

        public SkinItem(String name, String typeName, String entryName, int resId) {
            this.name = name;
            this.typeName = typeName;
            this.entryName = entryName;
            this.resId = resId;
        }

        public String getName() {
            return name;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getEntryName() {
            return entryName;
        }

        public int getResId() {
            return resId;
        }
    }
}
