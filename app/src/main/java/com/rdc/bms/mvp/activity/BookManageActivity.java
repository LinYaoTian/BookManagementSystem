package com.rdc.bms.mvp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

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
    @BindView(R.id.iv_option1_layout_top)
    ImageView mIvShowAll;

    private BookManageFragment mBookManageFragment;
    private int mAllBookPage = -1;//显示全部书籍的Page
    private AlertDialog mBookDialog;
    public static final int ADD_BOOK_OPTION = 0;
    public static final int UPDATE_BOOK_OPTION = 1;
    private ImageView mIvCover;
    private String mImagePath;
    private boolean mIsChooseImage = false;
    private int mType = 0;
    private int mPage = 0;

    private boolean isShowALL = true;
    private String mOldKey = "";
    private int mOldType = mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_manage;
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
        mTvCancel.setText("书籍名");
        mTvTitle.setText("书籍管理");
        mIvShowAll.setImageResource(R.drawable.iv_show_all);
        mIvShowAll.setVisibility(View.VISIBLE);
        mIvAddBook.setVisibility(View.VISIBLE);
        mIvAddBook.setImageResource(R.drawable.iv_plus);
        mIvBack.setVisibility(View.VISIBLE);
        mEtSearch.setHint("按书籍名搜索");

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
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        mIvShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
                mPage = 0;
                mAllBookPage = -1;
                isShowALL = true;
                mBookManageFragment.setCanShowLoadMore(true);
                searchAllBook();
                Toast.makeText(BookManageActivity.this, "显示全部书籍！", Toast.LENGTH_SHORT).show();
            }
        });
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

//            boolean isHidedList = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    mIvDelete.setVisibility(View.INVISIBLE);
                }else {
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
                    isShowALL = false;
                    String key = getString(mEtSearch);
                    if (!TextUtils.isEmpty(key)){
                        if (mOldKey.equals(key) && mOldType == mType){
                            mPage++;
                        }else {
                            mPage = 0;
                            mBookManageFragment.setCanShowLoadMore(true);
                        }
                        searchBook(key,mType,mPage);
                        mOldKey = key;
                        mOldType = mType;
                    }else {
                        Toast.makeText(BookManageActivity.this, "关键字不能为空！", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

    }

    public void onLoadMore(){
            if (isShowALL){
                searchAllBook();
            }else {
                mPage++;
                searchBook(mOldKey,mOldType,mPage);
            }
    }

    /**
     * 选择搜索关键字的类型的Dialog
     */
    private void showSelectDialog(){
        String[] items = {"书籍名","书籍ID","作者"};
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mTvCancel.setText("书籍名");
                                        mEtSearch.setHint("按书籍名搜索");
                                        mType = 0;
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        //书籍ID
                                        mTvCancel.setText("书籍ID");
                                        mEtSearch.setHint("按书籍ID搜索");
                                        mType = 1;
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        mTvCancel.setText("作者");
                                        mEtSearch.setHint("按作者搜索");
                                        mType = 2;
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }).setTitle("选择");
        builder.show();
    }


    public void showAddBookDialog(final int option, final Book book){
        mBookDialog = new AlertDialog.Builder(this).show();
        mBookDialog.setCancelable(false);
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
            tvTitle.setText("书籍ID："+book.getBookId());
            mImagePath = book.getCoverUrl();
        }else if (option == ADD_BOOK_OPTION){
            tvTitle.setText("添加书籍");
        }
        mIvCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.openPhoto(BookManageActivity.this);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookDialog.dismiss();
                mIsChooseImage = false;
                mImagePath = "";
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
                if (option == UPDATE_BOOK_OPTION){
                    b.setCoverUrl(mIsChooseImage?mImagePath:book.getCoverUrl());
                    b.setBookId(book.getBookId());
                    updateBook(b);
                }else {
                    b.setCoverUrl(mImagePath);
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
            case SHOW_PICTURE:
                try {
                    //该uri是上一个Activity返回的
                    Uri imageUri = data.getData();
                    if(imageUri!=null) {
                        mImagePath = ImageUtil.formatUri(this,imageUri);
                        if (mImagePath != null){
                            mIsChooseImage = true;
                            Glide.with(mIvCover.getContext()).load(imageUri).into(mIvCover);
                        }else {
                            showToast("获取图片路径失败！");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void updateBook(Book book){
        String url  = Constants.BASE_URL + "books/update";
        Map<String,String> map = new HashMap<>();
        map.put("name",book.getName());
        map.put("isbn",book.getIsbn());
        map.put("location",book.getLocation());
        map.put("publishingHouse",book.getPublishingHouse());
        map.put("author",book.getAuthor());
        map.put("intro",book.getIntro());
        map.put("bookId",book.getBookId());
        map.put("coverUrl",book.getCoverUrl());
        List<File> fileList = new ArrayList<>();
        if (mIsChooseImage){
            fileList.add(new File(book.getCoverUrl()));
        }

        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO dto = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    if (dto.isSuccess()){
                        mIsChooseImage = false;
                        mImagePath = "";
                        showToast("更新书籍成功！");
                        if (dto.transform().size() > 0 ){
                            mBookManageFragment.updateCurrentOptionalItem(dto.transform().get(0));
                        }
                        mBookDialog.dismiss();
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        },map,fileList);

    }

    public void addBook(Book book){
        String url  = Constants.BASE_URL + "books/add";
        Map<String,String> map = new HashMap<>();
        map.put("name",book.getName());
        map.put("isbn",book.getIsbn());
        map.put("location",book.getLocation());
        map.put("publishingHouse",book.getPublishingHouse());
        map.put("author",book.getAuthor());
        map.put("intro",book.getIntro());
        map.put("coverUrl",book.getCoverUrl());
        List<File> fileList = new ArrayList<>();
        if (mIsChooseImage){
            fileList.add(new File(book.getCoverUrl()));
        }
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO dto = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    if (dto.isSuccess()){
                        mIsChooseImage = false;
                        mImagePath = "";
                        showToast("添加书籍成功！");
                        mBookDialog.dismiss();
                    }else {
                        showToast(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        },map,fileList);
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
                        mBookManageFragment.removeCurrentOptionalItem();
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

    private void searchBook(String key, int type, final int page){
        String url = Constants.BASE_URL + "books/searchByKey?key="+key+"&page="+page+"&type="+type;
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
                    mBookManageFragment.hideLoadMore();
                    if (dto.isSuccess()){
                        if (dto.transform().size() < 15){
                            //少于每一页约定的item数，则说明没有更多数据了
                            mBookManageFragment.setCanShowLoadMore(false);
                            showToast(mAllBookPage ==0 ? "无数据！":"没有更多数据了！");
                        }
                        if (page == 0){
                            mBookManageFragment.setSearchResult(dto.getData());
                        }else {
                            mBookManageFragment.appendResult(dto.getData());
                        }
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
        mAllBookPage++;
        String url = Constants.BASE_URL + "books/searchAll?page="+ mAllBookPage;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {

            @Override
            public void onError(Call call, Exception e) {
                showToast(e.getMessage());
                if (mAllBookPage != 0){
                    mAllBookPage--;
                }
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    SearchBookDTO dto = GsonUtil.gsonToBean(s,SearchBookDTO.class);
                    mBookManageFragment.hideLoadMore();
                    if (dto.isSuccess()){
                        if (dto.transform().size() < 15){
                            //少于每一页约定的item数，则说明没有更多数据了
                            mBookManageFragment.setCanShowLoadMore(false);
                            showToast(mAllBookPage ==0 ? "无数据！":"没有更多数据了！");
                        }
                        if (mAllBookPage == 0){
                            mBookManageFragment.setSearchResult(dto.transform());
                        }else {
                            mBookManageFragment.appendResult(dto.transform());
                        }


                    }else {
                        showToast(dto.getMsg());
                        if (mAllBookPage != 0){
                            mAllBookPage--;
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                    if (mAllBookPage != 0){
                        mAllBookPage--;
                    }
                }
            }
        });
    }

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,BookManageActivity.class));
    }
}
