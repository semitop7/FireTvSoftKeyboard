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
    private View layout;
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
        popupWindow(keyboard);
    }

    public void changeLangEn(View view) {
        keyboard=R.layout.grid_layout_keyboard_ftv_en;
        pwindow.dismiss();
        pwindow=null;
        popupWindow(keyboard);
    }

    public void changeLangUaRu(View view) {
        keyboard=R.layout.grid_layout_keyboard_ftv_ru;
        pwindow.dismiss();
        pwindow=null;
        popupWindow(keyboard);
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

    public void popupWindow(int Layout) {
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(Layout, null);
            layout.setFocusable(true);
            layout.setFocusableInTouchMode(true);

            pwindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwindow.setFocusable(true);
            pwindow.setOutsideTouchable(true);
            pwindow.setBackgroundDrawable(new BitmapDrawable());
            pwindow.getContentView().setFocusableInTouchMode(true);
            pwindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            event.startTracking();
                            Button btnBack = (Button) v.findViewById(R.id.btn_prev);
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnBack.setPressed(true);
                                btnBack.invalidate();
                                btnBack.performClick();
                            } else {
                                btnBack.setPressed(false);
                                btnBack.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MENU:
                            event.startTracking();
                            Button btnMenu = (Button) v.findViewById(R.id.space);
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnMenu.setPressed(true);
                                btnMenu.invalidate();
                                btnMenu.performClick();
                            } else {
                                btnMenu.setPressed(false);
                                btnMenu.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                            event.startTracking();
                            Button btnCursorRight = (Button) v.findViewById(R.id.cursorRight);
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnCursorRight.setPressed(true);
                                btnCursorRight.invalidate();
                                btnCursorRight.performClick();
                            } else {
                                btnCursorRight.setPressed(false);
                                btnCursorRight.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_REWIND:
                            event.startTracking();
                            Button btnCursorLeft = (Button) v.findViewById(R.id.cursorLeft);
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnCursorLeft.setPressed(true);
                                btnCursorLeft.invalidate();
                                btnCursorLeft.performClick();
                            } else {
                                btnCursorLeft.setPressed(false);
                                btnCursorLeft.invalidate();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            event.startTracking();
                            Button btnNext = (Button) v.findViewById(R.id.btn_next);
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                btnNext.setPressed(true);
                                btnNext.invalidate();
                                btnNext.performClick();
                            } else {
                                btnNext.setPressed(false);
                                btnNext.invalidate();
                            }

                            return true;
                        default:
                            return false;
                    }
                }
            });
            buttonDrawable(R.id.btn_next, R.drawable.keyboard_bt_icon_play, layout, 0.75);
            buttonDrawable(R.id.btn_prev, R.drawable.keyboard_bt_icon_back, layout, 0.75);
            buttonDrawable(R.id.space, R.drawable.keyboard_bt_icon_menu, layout, 0.65);
            buttonDrawable(R.id.cursorLeft, R.drawable.keyboard_bt_icon_rewind, layout, 0.65);
            buttonDrawable(R.id.cursorRight, R.drawable.keyboard_bt_icon_fast_forward, layout, 0.65);
            pwindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /*@Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                event.startTracking();
                Button btnBack = (Button) findViewById(R.id.button);
                btnBack.setPressed(true);
                btnBack.invalidate();
                btnBack.performClick();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                event.startTracking();
                Button btnBack = (Button) findViewById(R.id.button);
                btnBack.setPressed(false);
                btnBack.invalidate();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }*/
}
