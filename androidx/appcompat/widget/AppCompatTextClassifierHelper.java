package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.core.util.Preconditions;

final class AppCompatTextClassifierHelper {
  private TextClassifier mTextClassifier;
  
  private TextView mTextView;
  
  AppCompatTextClassifierHelper(TextView paramTextView) {
    this.mTextView = (TextView)Preconditions.checkNotNull(paramTextView);
  }
  
  public TextClassifier getTextClassifier() {
    TextClassifier textClassifier2 = this.mTextClassifier;
    TextClassifier textClassifier1 = textClassifier2;
    if (textClassifier2 == null) {
      TextClassificationManager textClassificationManager = (TextClassificationManager)this.mTextView.getContext().getSystemService(TextClassificationManager.class);
      if (textClassificationManager != null)
        return textClassificationManager.getTextClassifier(); 
      textClassifier1 = TextClassifier.NO_OP;
    } 
    return textClassifier1;
  }
  
  public void setTextClassifier(TextClassifier paramTextClassifier) {
    this.mTextClassifier = paramTextClassifier;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatTextClassifierHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */