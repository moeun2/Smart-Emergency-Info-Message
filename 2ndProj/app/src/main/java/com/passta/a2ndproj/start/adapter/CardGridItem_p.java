package com.passta.a2ndproj.start.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.CardVO;


public class CardGridItem_p extends LinearLayout {
    TextView cardName;
    ImageView cardImage;

    public CardGridItem_p(Context context){
        super(context);
        init(context);
    }

    public void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_company, this);
        cardName = (TextView)findViewById(R.id.text_card_company);
        cardImage = (ImageView)findViewById(R.id.image_card_company);
    }

    public void setData(CardVO one, boolean visible){

        cardName.setText(one.getName());
        cardImage.setImageResource(one.getImage());

        cardImage.setVisibility(VISIBLE);


    }

}
