package edu.skku.cs.personalproject;

import android.widget.NumberPicker;

public interface QuoteContract {
    interface ContractForView{
        void displayValue(String value);
    }

    interface ContractForModel{
        String getValue();
        void getQuote(OnValueChangeListener listener);
        interface OnValueChangeListener{
            void onChanged();
        }
    }

    interface ContractForPresenter{
        void onGetQuoteBtnClicked();
    }


}
