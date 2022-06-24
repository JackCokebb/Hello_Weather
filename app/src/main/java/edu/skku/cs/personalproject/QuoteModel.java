package edu.skku.cs.personalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class QuoteModel implements QuoteContract.ContractForModel{
    private String value;
    private ArrayList<String> quotes =new ArrayList<String>(Arrays.asList("Today is the pupil of yesterday.","Change the world by being yourself.","Die with memories, not dreams."));
    Random random = new Random();
    int i =0;
    public QuoteModel(String initVal){
        this.value = initVal;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void getQuote(OnValueChangeListener listener) {
        i = random.nextInt(quotes.size());
        this.value = this.quotes.get(i);
        listener.onChanged();
    }
}
