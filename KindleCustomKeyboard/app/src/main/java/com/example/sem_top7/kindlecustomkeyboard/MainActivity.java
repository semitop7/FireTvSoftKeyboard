package com.example.sem_top7.kindlecustomkeyboard;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    private MenuItem searchItem;
    private PopupWindow pwindow;
    private View pwindowView;
    private int keyboard = R.layout.grid_layout_keyboard_ftv_ru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerEditText(R.id.editText);
        registerEditText(R.id.editText2);
        //registerEditText(R.id.editText3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Activity.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                v.setLayoutParams(layoutParams);
            }
        });

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showCustomKeyboard(View view) {
        popupWindow();
    }

    public void changeLangEn(View view) {
        keyboard=R.layout.grid_layout_keyboard_ftv_en;
        ViewGroup parent = (ViewGroup)pwindow.getContentView();
        View C = ruLang(keyboard);
        parent.removeAllViews();
        parent.addView(C, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void changeLangUaRu(View view) {
        keyboard=R.layout.grid_layout_keyboard_ftv_ru;
        ViewGroup parent = (ViewGroup)pwindow.getContentView();
        View C = ruLang(keyboard);
        parent.removeAllViews();
        parent.addView(C, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void btnPress(View view) {
        View focusCurrent = getWindow().getCurrentFocus();
        EditText edittext = (EditText) focusCurrent;
        Button btn = (Button) view;
        Object tag = btn.getTag();
        Editable editable = edittext.getText();
        int start = edittext.getSelectionStart();
        if (btn.getTag() != null) {
            switch (tag.toString()) {
                case "space":
                    editable.insert(start, " ");
                    break;
                case "delete":
                    if (editable != null && start > 0) {
                        editable.delete(start - 1, start);
                    }
                    break;
                case "clear":
                    if (editable != null) {
                        editable.clear();
                    }
                    break;
                case "cursorLeft":
                    if (start > 0) {
                        edittext.setSelection(start - 1);
                    }
                    break;
                case "cursorRight":
                    if (start < edittext.length()) {
                        edittext.setSelection(start + 1);
                    }
                    break;
                case "previos":
                    View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);
                    if (focusNew != null) focusNew.requestFocus();
                    pwindow.dismiss();
                    break;
                case "next":
                    View focusNew1 = edittext.focusSearch(View.FOCUS_BACKWARD);
                    if (focusNew1 != null) focusNew1.requestFocus();
                    pwindow.dismiss();
                    break;
                default:
                    break;
            }
        } else if (start == 0)
            editable.insert(start, btn.getText().toString().toUpperCase());
        else
            editable.insert(start, btn.getText());
    }

    private Button btnLang;
    private Button btnSpace;
    private Button btnBack;
    private Button btnDelete;
    private Button btnNext;

    public void popupWindow() {
        try {
            View layout = ruLang(keyboard);
            pwindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwindow.getContentView().setFocusableInTouchMode(true);
            pwindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View ruLang(int Layout) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pwindowView = inflater.inflate(Layout, null);

        btnBack = (Button) pwindowView.findViewById(R.id.back);
        btnLang = (Button) pwindowView.findViewById(R.id.lang);
        btnSpace = (Button) pwindowView.findViewById(R.id.space);
        btnDelete = (Button) pwindowView.findViewById(R.id.delete);
        btnNext = (Button) pwindowView.findViewById(R.id.next);

        ViewGroup parentView = (ViewGroup)pwindowView;
        for(int i=0; i < parentView.getChildCount(); i++) {
            View childView = parentView.getChildAt(i);
            childView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnBack.setPressed(true);
                                btnBack.invalidate();
                            } else {
                                btnBack.performClick();
                                btnBack.setPressed(false);
                                btnBack.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MENU:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnLang.setPressed(true);
                                btnLang.invalidate();
                            } else {
                                btnLang.performClick();
                                btnLang.setPressed(false);
                                btnLang.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnSpace.setPressed(true);
                                btnSpace.invalidate();
                                btnSpace.performClick();
                            } else {
                                btnSpace.setPressed(false);
                                btnSpace.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_REWIND:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnDelete.setPressed(true);
                                btnDelete.invalidate();
                                btnDelete.performClick();
                            } else {
                                btnDelete.setPressed(false);
                                btnDelete.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnNext.setPressed(true);
                                btnNext.invalidate();
                            } else {
                                btnNext.performClick();
                                btnNext.setPressed(false);
                                btnNext.invalidate();
                            }

                            return true;
                        default:
                            return false;
                    }
                }
            });
        }
        buttonDrawable(R.id.next, R.drawable.keyboard_bt_icon_play, pwindowView, 0.75);
        buttonDrawable(R.id.back, R.drawable.keyboard_bt_icon_back, pwindowView, 0.75);
        buttonDrawable(R.id.lang, R.drawable.keyboard_bt_icon_menu, pwindowView, 0.65);
        buttonDrawable(R.id.delete, R.drawable.keyboard_bt_icon_rewind, pwindowView, 0.65);
        buttonDrawable(R.id.space, R.drawable.keyboard_bt_icon_fast_forward, pwindowView, 0.65);
        return pwindowView;
    }

    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        final EditText edittext = (EditText) findViewById(resid);
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int inType = edittext.getInputType();
                    edittext.setInputType(InputType.TYPE_NULL);
                    edittext.setInputType(inType);
                    edittext.setInputType(inType);
                    edittext.setSelection(edittext.length());
                    showCustomKeyboard(v);
                } else pwindow.dismiss();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();// Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);
                edittext.setSelection(edittext.length()); // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public void buttonDrawable(int btnId, int drawableId, View view, double scale) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * scale),
                (int) (drawable.getIntrinsicHeight() * scale));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1, 1);
        Button btn = (Button) view.findViewById(btnId);
        btn.setCompoundDrawables(sd.getDrawable(), null, null, null);
    }
}
