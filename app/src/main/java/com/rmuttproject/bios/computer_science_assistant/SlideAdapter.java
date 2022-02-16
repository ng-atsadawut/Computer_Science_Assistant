package com.rmuttproject.bios.computer_science_assistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context){
        this.context = context;
    }

    ///Array Image// add picture for add new page
    public int[] slide_image = {
            R.drawable.page1,
            R.drawable.page2,
            R.drawable.page3,
            R.drawable.page4
    };
    ///add new text for add new page
    public String[] slide_heading = {
            "สอบถามข้อมูลเกี่ยวกับสาขา\nได้ง่ายและรวดเร็ว",
            "รายละเอียดข้อมูลบุคลากร \nและกิจกรรมต่างๆของสาขา",
            "รองรับภาษาไทย",
            "สั่งงานได้ด้วยการพิมพ์ ตอบกลับทั้งข้อความและเสียง"
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_slide_adapter,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_header);

        slideImageView.setImageResource(slide_image[position]);
        slideHeading.setText(slide_heading[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
