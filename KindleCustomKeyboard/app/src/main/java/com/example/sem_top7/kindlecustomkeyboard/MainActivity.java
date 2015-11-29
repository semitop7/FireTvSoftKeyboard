package com.example.sem_top7.kindlecustomkeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    private MenuItem searchItem;
    private PopupWindow pwindow;
    private View keyboardView;
    DialogFragment dlg1;
    private boolean keyboard = true;

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dlg1 = new Dialog1();

        registerEditText(R.id.editText, 1);
        registerEditText(R.id.editText2, 2);
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

    final int ru = R.layout.grid_layout_keyboard_ftv_ru;
    final int en = R.layout.grid_layout_keyboard_ftv_en;

    //public void showCustomKeyboard(View view) {
    public void  PWindow(View view) {
        try {
            keyboardView = (keyboard==true) ? setLang(ru, R.id.cr00) : setLang(en, R.id.cr00);
            pwindow = new PopupWindow(keyboardView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            pwindow.setFocusable(true);
            pwindow.getContentView().setFocusableInTouchMode(true);
            pwindow.showAtLocation(keyboardView, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog alertKeyboard;

    public void DFragment(View view) {
        keyboardView = (keyboard==true) ? setLang(ru, R.id.cr00) : setLang(en, R.id.cr00);
        dlg1.show(getFragmentManager(), "dlg1");
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(keyboardView);
        alertKeyboard = builder.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertKeyboard.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertKeyboard.show();
        alertKeyboard.getWindow().setAttributes(lp);*/
    }

    private int focus;

    public ViewGroup getViewGroup() {
        ViewGroup parent = null;
        if(pwindow.isShowing() && pwindow!=null) {
            parent = (ViewGroup) pwindow.getContentView();
            return parent;
        }
        if(alertKeyboard.isShowing() && alertKeyboard!=null) {
            parent = (ViewGroup) alertKeyboard.getListView();
            return parent;
        }
        return parent;
    }

    public void changeLang(View view) {
        ViewGroup focusParent = (ViewGroup) keyboardView;
        ViewGroup parent = getViewGroup();

        View v = focusParent.getFocusedChild();

        focus = (v==null) ? R.id.cr00 : v.getId();
        //C.findViewById(focus).requestFocus();
        switch (keyboardView.getId()) {
            case R.id.grid_ru:
                keyboardView = setLang(R.layout.grid_layout_keyboard_ftv_en, focus);
                keyboard=false;
                break;
            default:
                keyboardView = setLang(R.layout.grid_layout_keyboard_ftv_ru, focus);
                keyboard=true;
                break;
        }

        parent.removeAllViewsInLayout();
        parent.addView(keyboardView);
        //pwindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //pwindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void btnPress(View view) {
        View focusCurrent = getWindow().getCurrentFocus();
        EditText editText = (EditText) focusCurrent;
        Button btn = (Button) view;
        Object tag = btn.getTag();
        Editable editable1 = editText.getText();

        int start = editText.getSelectionStart();
        if (btn.getTag() != null) {
            switch (tag.toString()) {
                case "space":
                    editable1.insert(start, " ");
                    break;
                case "delete":
                    if (editable1 != null && start > 0) {
                        editable1.delete(start - 1, start);
                    }
                    break;
                case "clear":
                    if (editable1 != null) {
                        editable1.clear();
                    }
                    break;
                case "cursorLeft":
                    if (start > 0) {
                        editText.setSelection(start - 1);
                    }
                    break;
                case "cursorRight":
                    if (start < editText.length()) {
                        editText.setSelection(start + 1);
                    }
                    break;
                case "previos":
                    View focusNew = editText.focusSearch(View.FOCUS_BACKWARD);
                    if (focusNew != null) focusNew.requestFocus();
                    pwindow.dismiss();
                    pwindow = null;
                    break;
                case "next":
                    View focusNew1 = editText.focusSearch(View.FOCUS_FORWARD);
                    if (focusNew1 != null) focusNew1.requestFocus();

                        pwindow.dismiss();
                        pwindow = null;
                    break;
                default:
                    if (start == 0)
                        editable1.insert(start, btn.getText().toString().toUpperCase());
                    else
                        editable1.insert(start, btn.getText());
                    break;
            }
        }
    }

    private Button btnLang;
    private Button btnSpace;
    private Button btnBack;
    private Button btnDelete;
    private Button btnNext;
    private Button btnCursRight;
    private Button btnCursLeft;

    public View setLang(int Layout, int focusId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyboardView = inflater.inflate(Layout, null);

        btnBack = (Button) keyboardView.findViewById(R.id.back);
        btnLang = (Button) keyboardView.findViewById(R.id.lang);
        btnSpace = (Button) keyboardView.findViewById(R.id.space);
        btnDelete = (Button) keyboardView.findViewById(R.id.delete);
        btnNext = (Button) keyboardView.findViewById(R.id.next);
        btnCursRight = (Button) keyboardView.findViewById(R.id.cursorRight);
        btnCursLeft = (Button) keyboardView.findViewById(R.id.cursorLeft);

        ViewGroup parentView = (ViewGroup)keyboardView;
        for(int i=0; i < parentView.getChildCount(); i++) {
            View childView = parentView.getChildAt(i);
            childView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            //event.startTracking();
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                ((ViewGroup) keyboardView).getFocusedChild().clearFocus();
                                btnBack.requestFocus();
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
                                ((ViewGroup) keyboardView).getFocusedChild().clearFocus();
                                btnNext.requestFocus();
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
        buttonDrawable(R.id.next, R.drawable.keyboard_bt_icon_play, keyboardView, 0.75);
        buttonDrawable(R.id.back, R.drawable.keyboard_bt_icon_back, keyboardView, 0.75);
        buttonDrawable(R.id.lang, R.drawable.keyboard_bt_icon_menu, keyboardView, 0.65);
        buttonDrawable(R.id.delete, R.drawable.keyboard_bt_icon_rewind, keyboardView, 0.65);
        buttonDrawable(R.id.space, R.drawable.keyboard_bt_icon_fast_forward, keyboardView, 0.65);

        keyboardView.findViewById(focusId).requestFocus();

        return keyboardView;
    }

    public void registerEditText(int resid, final int key) {
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
                    edittext.setSelection(edittext.length());
                    if(key==1) PWindow(v); if(key==2) DFragment(v);
                } else pwindow.dismiss();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                if(key==1) PWindow(v); if(key==2) DFragment(v);
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
