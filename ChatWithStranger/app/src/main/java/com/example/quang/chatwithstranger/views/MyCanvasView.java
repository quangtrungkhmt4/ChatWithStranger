package com.example.quang.chatwithstranger.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.quang.chatwithstranger.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

public class MyCanvasView extends View{
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    private float strokeWidth;

    private Bitmap im;

    private Dialog dialogStroke;

    private ArrayList<Integer> arrColor = new ArrayList<>();
    private ArrayList<Float> arrStroke = new ArrayList<>();

    private ArrayList<Integer> undoColor = new ArrayList<>();
    private ArrayList<Float> undoStroke = new ArrayList<>();

    public MyCanvasView(Context context)
    {
        super(context);

//        int mBackgroundColor = ResourcesCompat.getColor(getResources(),
//                R.color.opaque_orange, null);
        int mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);

        strokeWidth = 6;
        setFocusable(true);
        setFocusableInTouchMode(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mDrawColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
        mCanvas = new Canvas();
        mPath = new Path();

        //initDialogStroke();

    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        im = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(im);
        mCanvas.drawColor(R.color.colorPrimaryDark);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //mPath = new Path();
        //canvas.drawPath(mPath, mPaint);
        for (int i=0; i<paths.size(); i++){
            mPaint.setStrokeWidth(arrStroke.get(i));
            canvas.drawPath(paths.get(i), paintCustomColor(arrColor.get(i)));
        }

        if(arrColor.size() != 0 && arrStroke.size() != 0)
        {
            mPaint.setStrokeWidth(arrStroke.get(arrStroke.size() - 1));
            canvas.drawPath(mPath, paintCustomColor(arrColor.get(arrColor.size() - 1)));
        }
        else
        {
            canvas.drawPath(mPath, mPaint);
            System.out.println("Mau: "+mPaint.getColor() + " Stroke: "+mPaint.getStrokeWidth());
        }


    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        System.out.println(""+mPaint.getColor());
        arrColor.add(mPaint.getColor());
        arrStroke.add(mPaint.getStrokeWidth());
        undonePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        System.out.println("start: "+ arrColor.size() + "---" + arrStroke.size());
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw

        paths.add(mPath);
        mPath = new Path();

    }

    public void onClickUndo () {
        System.out.println(arrColor.size() + "---" + arrStroke.size());
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            undoColor.add(arrColor.remove(arrColor.size() - 1));
            undoStroke.add(arrStroke.remove(arrStroke.size() - 1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }

    public void onClickRedo (){
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            arrColor.add(undoColor.remove(undoColor.size() - 1));
            arrStroke.add(undoStroke.remove(undoStroke.size() - 1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void setSWidth(){
        dialogStroke.show();
    }

    public void setBgColor(int color){
        mCanvas.drawColor(color);
    }

    int c = 0;
    public void setLineColor(){

        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mPaint.setColor(selectedColor);
                        System.out.println("new color: "+mPaint.getColor());

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();

    }

    private Paint paintCustomColor(int color){
        Paint p = mPaint;
        p.setColor(color);
        return p;
    }


//    private void initDialogStroke() {
//        dialogStroke = new Dialog(getContext());
//        dialogStroke.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogStroke.setCancelable(false);
//        dialogStroke.setContentView(R.layout.dialog_progressbar_strokewidth);
//
//        SeekBar seekBar = dialogStroke.findViewById(R.id.seekBarStroke);
//        final TextView tvStroke = dialogStroke.findViewById(R.id.tvDisplayStroke);
//        TextView tvCancel = dialogStroke.findViewById(R.id.tvCancelStroke);
//        TextView tvOk = dialogStroke.findViewById(R.id.tvOkStroke);
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                tvStroke.setText(i+"");
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                float oldStroke = mPaint.getStrokeWidth();
//                mPaint.setStrokeWidth(oldStroke);
//                dialogStroke.dismiss();
//            }
//        });
//
//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                strokeWidth = Float.parseFloat(tvStroke.getText().toString());
//                mPaint.setStrokeWidth(strokeWidth);
//                dialogStroke.dismiss();
//            }
//        });
//
//
//    }

}
