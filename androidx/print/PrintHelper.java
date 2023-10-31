package androidx.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PrintHelper {
  public static final int COLOR_MODE_COLOR = 2;
  
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
  
  private static final String LOG_TAG = "PrintHelper";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  final Object mLock = new Object();
  
  int mOrientation = 1;
  
  int mScaleMode = 2;
  
  static {
    int i = Build.VERSION.SDK_INT;
    boolean bool2 = false;
    if (i < 20 || Build.VERSION.SDK_INT > 23) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    PRINT_ACTIVITY_RESPECTS_ORIENTATION = bool1;
    boolean bool1 = bool2;
    if (Build.VERSION.SDK_INT != 23)
      bool1 = true; 
    IS_MIN_MARGINS_HANDLING_CORRECT = bool1;
  }
  
  public PrintHelper(Context paramContext) {
    this.mContext = paramContext;
  }
  
  static Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt != 1)
      return paramBitmap; 
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  private static PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes) {
    PrintAttributes.Builder builder = (new PrintAttributes.Builder()).setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      builder.setColorMode(paramPrintAttributes.getColorMode()); 
    if (Build.VERSION.SDK_INT >= 23 && paramPrintAttributes.getDuplexMode() != 0)
      builder.setDuplexMode(paramPrintAttributes.getDuplexMode()); 
    return builder;
  }
  
  static Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f1 = paramRectF.width();
    float f2 = paramInt1;
    f1 /= f2;
    if (paramInt3 == 2) {
      f1 = Math.max(f1, paramRectF.height() / paramInt2);
    } else {
      f1 = Math.min(f1, paramRectF.height() / paramInt2);
    } 
    matrix.postScale(f1, f1);
    matrix.postTranslate((paramRectF.width() - f2 * f1) / 2.0F, (paramRectF.height() - paramInt2 * f1) / 2.0F);
    return matrix;
  }
  
  static boolean isPortrait(Bitmap paramBitmap) {
    boolean bool;
    if (paramBitmap.getWidth() <= paramBitmap.getHeight()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri != null) {
      Context context = this.mContext;
      if (context != null) {
        BitmapFactory.Options options = null;
        try {
          InputStream inputStream = context.getContentResolver().openInputStream(paramUri);
        } finally {
          paramUri = null;
        } 
        if (paramOptions != null)
          try {
            paramOptions.close();
          } catch (IOException iOException) {
            Log.w("PrintHelper", "close fail ", iOException);
          }  
        throw paramUri;
      } 
    } 
    throw new IllegalArgumentException("bad argument to loadBitmap");
  }
  
  public static boolean systemSupportsPrint() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getColorMode() {
    return this.mColorMode;
  }
  
  public int getOrientation() {
    return (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) ? 1 : this.mOrientation;
  }
  
  public int getScaleMode() {
    return this.mScaleMode;
  }
  
  Bitmap loadConstrainedBitmap(Uri paramUri) throws FileNotFoundException {
    if (paramUri != null && this.mContext != null) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      loadBitmap(paramUri, options);
      int i = options.outWidth;
      int j = options.outHeight;
      if (i > 0 && j > 0) {
        int m = Math.max(i, j);
        int k;
        for (k = 1; m > 3500; k <<= 1)
          m >>>= 1; 
        if (k > 0 && Math.min(i, j) / k > 0)
          synchronized (this.mLock) {
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            this();
            this.mDecodeOptions = options1;
            options1.inMutable = true;
            this.mDecodeOptions.inSampleSize = k;
            options1 = this.mDecodeOptions;
            try {
              null = loadBitmap(paramUri, options1);
            } finally {
              null = null;
            } 
          }  
      } 
      return null;
    } 
    throw new IllegalArgumentException("bad argument to getScaledBitmap");
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap) {
    printBitmap(paramString, paramBitmap, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap, OnPrintFinishCallback paramOnPrintFinishCallback) {
    if (Build.VERSION.SDK_INT >= 19 && paramBitmap != null) {
      PrintAttributes.MediaSize mediaSize;
      PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
      if (isPortrait(paramBitmap)) {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
      } else {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
      } 
      PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
      printManager.print(paramString, new PrintBitmapAdapter(paramString, this.mScaleMode, paramBitmap, paramOnPrintFinishCallback), printAttributes);
    } 
  }
  
  public void printBitmap(String paramString, Uri paramUri) throws FileNotFoundException {
    printBitmap(paramString, paramUri, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(String paramString, Uri paramUri, OnPrintFinishCallback paramOnPrintFinishCallback) throws FileNotFoundException {
    if (Build.VERSION.SDK_INT < 19)
      return; 
    PrintUriAdapter printUriAdapter = new PrintUriAdapter(paramString, paramUri, paramOnPrintFinishCallback, this.mScaleMode);
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    int i = this.mOrientation;
    if (i == 1 || i == 0) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (i == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(paramString, printUriAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) {
    this.mColorMode = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setScaleMode(int paramInt) {
    this.mScaleMode = paramInt;
  }
  
  void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
    final PrintAttributes pdfAttributes;
    if (IS_MIN_MARGINS_HANDLING_CORRECT) {
      printAttributes = attributes;
    } else {
      printAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
    } 
    (new AsyncTask<Void, Void, Throwable>() {
        final PrintHelper this$0;
        
        final PrintAttributes val$attributes;
        
        final Bitmap val$bitmap;
        
        final CancellationSignal val$cancellationSignal;
        
        final ParcelFileDescriptor val$fileDescriptor;
        
        final int val$fittingMode;
        
        final PrintAttributes val$pdfAttributes;
        
        final PrintDocumentAdapter.WriteResultCallback val$writeResultCallback;
        
        protected Throwable doInBackground(Void... param1VarArgs) {
          try {
            if (cancellationSignal.isCanceled())
              return null; 
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument();
            this(PrintHelper.this.mContext, pdfAttributes);
            Bitmap bitmap = PrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
            boolean bool = cancellationSignal.isCanceled();
            if (bool)
              return null; 
            try {
              RectF rectF;
              PdfDocument.Page page = printedPdfDocument.startPage(1);
              if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                rectF = new RectF();
                this(page.getInfo().getContentRect());
              } else {
                PrintedPdfDocument printedPdfDocument1 = new PrintedPdfDocument();
                this(PrintHelper.this.mContext, attributes);
                PdfDocument.Page page1 = printedPdfDocument1.startPage(1);
                rectF = new RectF();
                this(page1.getInfo().getContentRect());
                printedPdfDocument1.finishPage(page1);
                printedPdfDocument1.close();
              } 
              Matrix matrix = PrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), rectF, fittingMode);
              if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                matrix.postTranslate(rectF.left, rectF.top);
                page.getCanvas().clipRect(rectF);
              } 
              page.getCanvas().drawBitmap(bitmap, matrix, null);
              printedPdfDocument.finishPage(page);
              bool = cancellationSignal.isCanceled();
              if (bool)
                return null; 
              FileOutputStream fileOutputStream = new FileOutputStream();
              this(fileDescriptor.getFileDescriptor());
              printedPdfDocument.writeTo(fileOutputStream);
              return null;
            } finally {
              printedPdfDocument.close();
              ParcelFileDescriptor parcelFileDescriptor = fileDescriptor;
              if (parcelFileDescriptor != null)
                try {
                  fileDescriptor.close();
                } catch (IOException iOException) {} 
              if (bitmap != bitmap)
                bitmap.recycle(); 
            } 
          } finally {}
        }
        
        protected void onPostExecute(Throwable param1Throwable) {
          if (cancellationSignal.isCanceled()) {
            writeResultCallback.onWriteCancelled();
          } else if (param1Throwable == null) {
            writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
          } else {
            Log.e("PrintHelper", "Error writing printed content", param1Throwable);
            writeResultCallback.onWriteFailed(null);
          } 
        }
      }).execute((Object[])new Void[0]);
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
  
  private class PrintBitmapAdapter extends PrintDocumentAdapter {
    private PrintAttributes mAttributes;
    
    private final Bitmap mBitmap;
    
    private final PrintHelper.OnPrintFinishCallback mCallback;
    
    private final int mFittingMode;
    
    private final String mJobName;
    
    final PrintHelper this$0;
    
    PrintBitmapAdapter(String param1String, int param1Int, Bitmap param1Bitmap, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback) {
      this.mJobName = param1String;
      this.mFittingMode = param1Int;
      this.mBitmap = param1Bitmap;
      this.mCallback = param1OnPrintFinishCallback;
    }
    
    public void onFinish() {
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      this.mAttributes = param1PrintAttributes2;
      param1LayoutResultCallback.onLayoutFinished((new PrintDocumentInfo.Builder(this.mJobName)).setContentType(1).setPageCount(1).build(), param1PrintAttributes2.equals(param1PrintAttributes1) ^ true);
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  private class PrintUriAdapter extends PrintDocumentAdapter {
    PrintAttributes mAttributes;
    
    Bitmap mBitmap;
    
    final PrintHelper.OnPrintFinishCallback mCallback;
    
    final int mFittingMode;
    
    final Uri mImageFile;
    
    final String mJobName;
    
    AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
    
    final PrintHelper this$0;
    
    PrintUriAdapter(String param1String, Uri param1Uri, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback, int param1Int) {
      this.mJobName = param1String;
      this.mImageFile = param1Uri;
      this.mCallback = param1OnPrintFinishCallback;
      this.mFittingMode = param1Int;
      this.mBitmap = null;
    }
    
    void cancelLoad() {
      synchronized (PrintHelper.this.mLock) {
        if (PrintHelper.this.mDecodeOptions != null) {
          if (Build.VERSION.SDK_INT < 24)
            PrintHelper.this.mDecodeOptions.requestCancelDecode(); 
          PrintHelper.this.mDecodeOptions = null;
        } 
        return;
      } 
    }
    
    public void onFinish() {
      super.onFinish();
      cancelLoad();
      AsyncTask<Uri, Boolean, Bitmap> asyncTask = this.mLoadBitmap;
      if (asyncTask != null)
        asyncTask.cancel(true); 
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
      Bitmap bitmap = this.mBitmap;
      if (bitmap != null) {
        bitmap.recycle();
        this.mBitmap = null;
      } 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_2
      //   4: putfield mAttributes : Landroid/print/PrintAttributes;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_3
      //   10: invokevirtual isCanceled : ()Z
      //   13: ifeq -> 22
      //   16: aload #4
      //   18: invokevirtual onLayoutCancelled : ()V
      //   21: return
      //   22: aload_0
      //   23: getfield mBitmap : Landroid/graphics/Bitmap;
      //   26: ifnull -> 64
      //   29: aload #4
      //   31: new android/print/PrintDocumentInfo$Builder
      //   34: dup
      //   35: aload_0
      //   36: getfield mJobName : Ljava/lang/String;
      //   39: invokespecial <init> : (Ljava/lang/String;)V
      //   42: iconst_1
      //   43: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   46: iconst_1
      //   47: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   50: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   53: aload_2
      //   54: aload_1
      //   55: invokevirtual equals : (Ljava/lang/Object;)Z
      //   58: iconst_1
      //   59: ixor
      //   60: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   63: return
      //   64: aload_0
      //   65: new androidx/print/PrintHelper$PrintUriAdapter$1
      //   68: dup
      //   69: aload_0
      //   70: aload_3
      //   71: aload_2
      //   72: aload_1
      //   73: aload #4
      //   75: invokespecial <init> : (Landroidx/print/PrintHelper$PrintUriAdapter;Landroid/os/CancellationSignal;Landroid/print/PrintAttributes;Landroid/print/PrintAttributes;Landroid/print/PrintDocumentAdapter$LayoutResultCallback;)V
      //   78: iconst_0
      //   79: anewarray android/net/Uri
      //   82: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
      //   85: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   88: return
      //   89: astore_1
      //   90: aload_0
      //   91: monitorexit
      //   92: aload_1
      //   93: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	89	finally
      //   90	92	89	finally
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  class null extends AsyncTask<Uri, Boolean, Bitmap> {
    final PrintHelper.PrintUriAdapter this$1;
    
    final CancellationSignal val$cancellationSignal;
    
    final PrintDocumentAdapter.LayoutResultCallback val$layoutResultCallback;
    
    final PrintAttributes val$newPrintAttributes;
    
    final PrintAttributes val$oldPrintAttributes;
    
    protected Bitmap doInBackground(Uri... param1VarArgs) {
      try {
        return PrintHelper.this.loadConstrainedBitmap(this.this$1.mImageFile);
      } catch (FileNotFoundException fileNotFoundException) {
        return null;
      } 
    }
    
    protected void onCancelled(Bitmap param1Bitmap) {
      layoutResultCallback.onLayoutCancelled();
      this.this$1.mLoadBitmap = null;
    }
    
    protected void onPostExecute(Bitmap param1Bitmap) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: invokespecial onPostExecute : (Ljava/lang/Object;)V
      //   5: aload_1
      //   6: astore_3
      //   7: aload_1
      //   8: ifnull -> 109
      //   11: getstatic androidx/print/PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION : Z
      //   14: ifeq -> 32
      //   17: aload_1
      //   18: astore_3
      //   19: aload_0
      //   20: getfield this$1 : Landroidx/print/PrintHelper$PrintUriAdapter;
      //   23: getfield this$0 : Landroidx/print/PrintHelper;
      //   26: getfield mOrientation : I
      //   29: ifne -> 109
      //   32: aload_0
      //   33: monitorenter
      //   34: aload_0
      //   35: getfield this$1 : Landroidx/print/PrintHelper$PrintUriAdapter;
      //   38: getfield mAttributes : Landroid/print/PrintAttributes;
      //   41: invokevirtual getMediaSize : ()Landroid/print/PrintAttributes$MediaSize;
      //   44: astore #4
      //   46: aload_0
      //   47: monitorexit
      //   48: aload_1
      //   49: astore_3
      //   50: aload #4
      //   52: ifnull -> 109
      //   55: aload_1
      //   56: astore_3
      //   57: aload #4
      //   59: invokevirtual isPortrait : ()Z
      //   62: aload_1
      //   63: invokestatic isPortrait : (Landroid/graphics/Bitmap;)Z
      //   66: if_icmpeq -> 109
      //   69: new android/graphics/Matrix
      //   72: dup
      //   73: invokespecial <init> : ()V
      //   76: astore_3
      //   77: aload_3
      //   78: ldc 90.0
      //   80: invokevirtual postRotate : (F)Z
      //   83: pop
      //   84: aload_1
      //   85: iconst_0
      //   86: iconst_0
      //   87: aload_1
      //   88: invokevirtual getWidth : ()I
      //   91: aload_1
      //   92: invokevirtual getHeight : ()I
      //   95: aload_3
      //   96: iconst_1
      //   97: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
      //   100: astore_3
      //   101: goto -> 109
      //   104: astore_1
      //   105: aload_0
      //   106: monitorexit
      //   107: aload_1
      //   108: athrow
      //   109: aload_0
      //   110: getfield this$1 : Landroidx/print/PrintHelper$PrintUriAdapter;
      //   113: aload_3
      //   114: putfield mBitmap : Landroid/graphics/Bitmap;
      //   117: aload_3
      //   118: ifnull -> 173
      //   121: new android/print/PrintDocumentInfo$Builder
      //   124: dup
      //   125: aload_0
      //   126: getfield this$1 : Landroidx/print/PrintHelper$PrintUriAdapter;
      //   129: getfield mJobName : Ljava/lang/String;
      //   132: invokespecial <init> : (Ljava/lang/String;)V
      //   135: iconst_1
      //   136: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   139: iconst_1
      //   140: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   143: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   146: astore_1
      //   147: aload_0
      //   148: getfield val$newPrintAttributes : Landroid/print/PrintAttributes;
      //   151: aload_0
      //   152: getfield val$oldPrintAttributes : Landroid/print/PrintAttributes;
      //   155: invokevirtual equals : (Ljava/lang/Object;)Z
      //   158: istore_2
      //   159: aload_0
      //   160: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   163: aload_1
      //   164: iconst_1
      //   165: iload_2
      //   166: ixor
      //   167: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   170: goto -> 181
      //   173: aload_0
      //   174: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   177: aconst_null
      //   178: invokevirtual onLayoutFailed : (Ljava/lang/CharSequence;)V
      //   181: aload_0
      //   182: getfield this$1 : Landroidx/print/PrintHelper$PrintUriAdapter;
      //   185: aconst_null
      //   186: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   189: return
      // Exception table:
      //   from	to	target	type
      //   34	48	104	finally
      //   105	107	104	finally
    }
    
    protected void onPreExecute() {
      cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            final PrintHelper.PrintUriAdapter.null this$2;
            
            public void onCancel() {
              this.this$2.this$1.cancelLoad();
              PrintHelper.PrintUriAdapter.null.this.cancel(false);
            }
          });
    }
  }
  
  class null implements CancellationSignal.OnCancelListener {
    final PrintHelper.PrintUriAdapter.null this$2;
    
    public void onCancel() {
      this.this$2.this$1.cancelLoad();
      this.this$2.cancel(false);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\print\PrintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */