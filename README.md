# PhotoPick
图片选择器

##使用
    new PickConfig.Builder(this)
                .maxPickSize(5)             // 最大选择图片数量\<br>
                .showGif(true)              // 是否展现Gif图片\<br>
                .spanCount(3)               // Grid的列数\<br>
                .setSelImage(mSelImages)    // 已选择的图片（避免重复选择或覆盖之前的选择）\<br>
                .build();    
                               
##AndroidManifest.xml
<activity android:name="com.zero.photopicklib.ui.PickerActivity"/>
其他属性随意（但是Theme一定是要居于NoActionBar，否则会报错，因为这里用Toolbar来实现，
为了以后实现自定义toolbar）

##返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (PickConfig.PICK_REQUEST_CODE == requestCode) {
            mSelImages = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Log.i("选择长度", mSelImages.size() + "");
        }
    }

##感谢
[Android-PickPhotos](https://github.com/crosswall/Android-PickPhotos) \<br>
[TODO-MVP-RXJAVA](https://github.com/googlesamples/android-architecture/tree/dev-todo-mvp-rxjava/)
