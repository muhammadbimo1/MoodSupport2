package com.beemo.moodsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class SelfHealingActivity extends AppCompatActivity {
    private String[] textArray = {
            "Let's go out once in a while. it's good for your mental health and body."
            ,"Don't make things too complicated. Try to relax, enjoy every moment, get used to everything."
            ,"A holiday is an opportunity to journey within. It is also a chance to chill, to relax."
            ,"Once I see my mother's smiling face, I forget all the shooting pressures and tensions. I am the happiest person at that moment."
            ,"It was hard for me to have the initiatives to do things."
            ,"just relax, calm down, take a deep breath and try to see how you can make things work rather than complain about how they're wrong.",
            "Just as in sleep our brains relax and give us dreams, so at some time in the day we need to disconnect, reconnect, and look around us.",
            "Music - it's motivational and just makes you relax.",
            "Soak in a warm bath."
            ,"Write. Some people feel more relaxed after they write about their feelings. One way is to keep a journal."
            ,"Do yoga. You can get books and videos to do at home or take a yoga class.",
            "Get a massage or have someone give you a back rub.",
            "Have a warm drink that doesn't have alcohol or caffeine in it, such as herbal tea or warm milk."
            ,"“I’ve decided to be happy because it is good for my health.”\n –Voltaire",
            "“Life isn’t as serious as the mind makes it out to be.”\n –Eckhart Tolle",
            "“You don’t always need a plan.  Sometimes you just need to breathe, trust, let go and see what happens.”\n –Mandy Hale",
            "Try Praying? it helps when you don't know what to do.",
            "Happiness is part of who we are. Joy is the feeling",
            "Doing something positive will help turn your mood around. When you smile, your body relaxes. When you experience human touch and interaction, it eases tension in your body.",
            "Just breathing can be such a luxury sometimes."};
    Random rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_healing);
        TextView textView = findViewById(R.id.textView17);
        TextView textView2 = findViewById(R.id.textView18);
        int n = rand.nextInt(textArray.length);
        int a = rand.nextInt(textArray.length);
        textView.setText(textArray[n]);
        textView2.setText(textArray[a]);
    }
    public void startbreath(View v) {
        Intent intent = new Intent(SelfHealingActivity.this, BreathingActivity.class);
        startActivity(intent);
        //todo : finished
    }
}
