package com.rdc.bms.mvp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rdc.bms.base.BaseActivity;
import com.rdc.bms.base.BasePresenter;
import com.rdc.bms.bookmanagementsystem.R;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.SearchBookDTO;
import com.rdc.bms.dto.SimpleDTO;
import com.rdc.bms.entity.Book;
import com.rdc.bms.mvp.fragment.BookManageFragment;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.ImageUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

import static com.rdc.bms.config.Constants.CUT_CAMERA_PICTURE;
import static com.rdc.bms.config.Constants.CUT_GALLERY_PICTURE;
import static com.rdc.bms.config.Constants.SHOW_PICTURE;

public class BookManageActivity extends BaseActivity {

    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.iv_option2_layout_top)
    ImageView mIvAddBook;
    @BindView(R.id.tv_cancel_layout_search)
    TextView mTvCancel;
    @BindView(R.id.et_search_layout_search)
    EditText mEtSearch;
    @BindView(R.id.iv_delete_layout_search)
    ImageView mIvDelete;

    private BookManageFragment mBookManageFragment;
    private boolean misNoneData = false;
    private int mAllReaderPage = -1;//显示全部书籍的Page
    private AlertDialog mBookDialog;
    private static final int ADD_BOOK_OPTION = 0;
    private static final int UPDATE_BOOK_OPTION = 1;
    private Uri mImageUri;
    private ImageView mIvCover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_reader_manage;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBookManageFragment = BookManageFragment.newInstance();
    }

    @Override
    protected void initView() {
        mTvCancel.setVisibility(View.INVISIBLE);
        mTvTitle.setText("书籍管理");
        mIvAddBook.setVisibility(View.VISIBLE);
        mIvAddBook.setImageResource(R.drawable.iv_plus);
        mIvBack.setVisibility(View.VISIBLE);
        mEtSearch.setHint("按书籍ID搜索书籍");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_act_book_manage, mBookManageFragment);
        transaction.commitNow();
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {
        mIvAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBookDialog(ADD_BOOK_OPTION,null);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {

            boolean isHidedList = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    mBookManageFragment.showList();
                    isHidedList = false;
                    mIvDelete.setVisibility(View.INVISIBLE);
                }else {
                    if (!isHidedList){
                        mBookManageFragment.hideList();
                        isHidedList = true;
                    }
                    mIvDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null
                        && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (!TextUtils.isEmpty(getString(mEtSearch))){
                        searchBook(getString(mEtSearch));
                    }
                }
                return true;
            }
        });

    }


    private void showAddBookDialog(final int option, final Book book){
        mBookDialog = new AlertDialog.Builder(this).show();
        //设置背景色为透明，解决设置圆角后有白色直角的问题
        Window window=mBookDialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View container = LayoutInflater.from(this).inflate(R.layout.dialog_add_book,null);
        mIvCover = container.findViewById(R.id.iv_cover_dialog_add_book);
        final EditText etBookName = container.findViewById(R.id.et_book_name_dialog_add_book);
        final EditText etAuthor= container.findViewById(R.id.et_book_author_dialog_add_book);
        final EditText etIsbn = container.findViewById(R.id.et_book_isbn_dialog_add_book);
        final EditText etLocation = container.findViewById(R.id.et_location_dialog_add_book);
        final EditText etPublishingHouse = container.findViewById(R.id.et_publishingHouse_dialog_add_book);
        final EditText etIntro = container.findViewById(R.id.et_intro_dialog_add_book);
        Button btnOk = container.findViewById(R.id.btn_ok_dialog_add_book);
        Button btnCancel = container.findViewById(R.id.btn_cancel_dialog_add_book);
        TextView tvTitle = container.findViewById(R.id.tv_title_dialog_add_book);

        if (option==UPDATE_BOOK_OPTION){
            Glide.with(mIvCover.getContext()).load(book.getCoverUrl()).into(mIvCover);
            etBookName.setText(book.getName());
            etAuthor.setText(book.getAuthor());
            etIsbn.setText(book.getIsbn());
            etLocation.setText(book.getLocation());
            etPublishingHouse.setText(book.getPublishingHouse());
            etIntro.setText(book.getIntro());
            tvTitle.setText("更新书籍信息");
        }else if (option == ADD_BOOK_OPTION){
            tvTitle.setText("添加书籍");
        }
        mIvCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BookManageActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BookManageActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, 1);
                }else {
                    choosePhoto();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book b = new Book();
                if (TextUtils.isEmpty(getString(etBookName))){
                    Toast.makeText(BookManageActivity.this, "书名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    b.setName(getString(etBookName));
                }
                if (TextUtils.isEmpty(getString(etLocation))){
                    Toast.makeText(BookManageActivity.this, "位置不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    b.setLocation(getString(etLocation));
                }
                b.setAuthor(getString(etAuthor));
                b.setIntro(getString(etIntro));
                b.setIsbn(getString(etIsbn));
                b.setPublishingHouse(getString(etPublishingHouse));
                b.setCoverUrl(book.getCoverUrl());
                if (option == UPDATE_BOOK_OPTION){
                    updateBook(b);
                }else {
                    addBook(b);
                }
            }
        });
        mBookDialog.setContentView(container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("BookManageActivity", "onActivityResult: requestCode="+requestCode);
        switch (requestCode) {
            case CUT_GALLERY_PICTURE:
                if (resultCode == RESULT_OK) {
                    ImageUtil.cropImageUri(BookManageActivity.this, data.getData(), mImageUri, 800, 400, SHOW_PICTURE);
                }
                break;
            case SHOW_PICTURE:
                try {
                    //该uri是上一个Activity返回的
                    Uri imageUri = data.getData();
                    if(imageUri!=null) {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Log.i("bit", String.valueOf(bit));
                        Glide.with(mIvCover.getContext()).load(imageUri).into(mIvCover);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CUT_CAMERA_PICTURE:
                ImageUtil.cropImageUri(BookManageActivity.this, mImageUri, mImageUri, 800, 400, SHOW_PICTURE);
                break;
            default:
                break;
        }
    }

    private void choosePhoto(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, SHOW_PICTURE);
    }



    public void addBook(Book book){
        String url  = Constants.BASE_URL + "books/add";
        Map<String,String> map = new HashMap<>();
        map.put("name",book.getName());
        map.put("isbn",book.getIsbn());
        map.put("location",book.getLocation());
        map.put("publishingHouse",book.getPublishingHouse());
        map.put("author",book.getAuthor());
        map.put("coverUrl",book.getCoverUrl());
        map.put("intro",book.getIntro());
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SimpleDTO dto = GsonUtil.gsonToBean(s,SimpleDTO.class);
                    if (dto.isSuccess()){
                        showToast("添加书籍成功！");
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        },map);
    }

    public void deleteBook(String bookId){
        String url  = Constants.BASE_URL + "books/delete?bookId="+bookId;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SimpleDTO dto = GsonUtil.gsonToBean(s,SimpleDTO.class);
                    if (dto.isSuccess()){
                        showToast("删除书籍成功！");
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }

    public void updateBook(Book book){
        String url  = Constants.BASE_URL + "books/update";
        Map<String,String> map = new HashMap<>();
        map.put("bookId",book.getBookId());
        map.put("name",book.getName());
        map.put("isbn",book.getIsbn());
        map.put("location",book.getLocation());
        map.put("publishingHouse",book.getPublishingHouse());
        map.put("author",book.getAuthor());
        map.put("coverUrl",book.getCoverUrl());
        map.put("intro",book.getIntro());
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SimpleDTO dto = GsonUtil.gsonToBean(s,SimpleDTO.class);
                    if (dto.isSuccess()){
                        showToast("更新书籍信息成功！");
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        },map);
    }

    private void searchBook(String bookId){
        String url = Constants.BASE_URL + "books/search?bookId="+bookId;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes)  {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO dto = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    if (dto.isSuccess()){
                        mBookManageFragment.setSearchResult(dto.getData());
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }

    public void searchAllBook(){
        if (misNoneData){
            return;
        }
        mAllReaderPage++;
        String url = Constants.BASE_URL + "books/searchAll?page="+ mAllReaderPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {

            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
                if (mAllReaderPage != 0){
                    mAllReaderPage--;
                }
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO dto = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    if (dto.isSuccess()){
                        mBookManageFragment.hideLoadMore();
                        if (dto.transform().size() < 15){
                            //少于每一页约定的item数，则说明没有更多数据了
                            misNoneData = true;
                            mBookManageFragment.setCanShowLoadMore(false);
                            showToast(mAllReaderPage ==0 ? "无数据！":"没有更多数据了！");
                        }
                        mBookManageFragment.appendResult(dto.transform());
                    }else {
                        showToast(dto.getMsg());
                        if (mAllReaderPage != 0){
                            mAllReaderPage--;
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                    if (mAllReaderPage != 0){
                        mAllReaderPage--;
                    }
                }
            }
        });
    }

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,BookManageActivity.class));
    }
}
