package edu.skku.cs.personalproject;

public class QuotePresenter implements QuoteContract.ContractForPresenter,QuoteContract.ContractForModel.OnValueChangeListener{

    private QuoteContract.ContractForModel model;
    private QuoteContract.ContractForView view;

    public QuotePresenter(QuoteContract.ContractForView view, QuoteContract.ContractForModel model){
        this.model = model;
        this.view = view;
    }

    @Override
    public void onChanged() {
        if(view != null) view.displayValue(model.getValue());
    }

    @Override
    public void onGetQuoteBtnClicked() {
        model.getQuote(this);
    }
}
