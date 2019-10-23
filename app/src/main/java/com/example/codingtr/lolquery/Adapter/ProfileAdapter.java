package com.example.codingtr.lolquery.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codingtr.lolquery.Management.Profile;
import com.example.codingtr.lolquery.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

    private Context mCtx;
    private List<Profile> profileList;

    public ProfileAdapter(Context mCtx, List<Profile> profileList) {
        this.mCtx = mCtx;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout_profil, null);

        return new ProfileAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder profileViewHolder, int position) {
        Profile profile = profileList.get(position);

        final String photoCharacter = profile.getPermaChampionPhotoUrl();

        final String photo0Url = profile.getPermaItem0Url();
        final String photo1Url = profile.getPermaItem1Url();
        final String photo2Url = profile.getPermaItem2Url();
        final String photo3Url = profile.getPermaItem3Url();
        final String photo4Url = profile.getPermaItem4Url();
        final String photo5Url = profile.getPermaItem5Url();
        final String photo6Url = profile.getPermaItem6Url();
        final String photo7Url = profile.getPermaSpell1Url();
        final String photo8Url = profile.getPermaSpell2Url();
        final Long gameTime = profile.getPermaTimeStamp();
        final boolean win = profile.isPermaWin();

        final int textViewKills = profile.getKills();
        final int textViewDeath = profile.getDeaths();
        final int textViewAssist = profile.getAssists();

        final String permaLaneUrl = profile.getPermaLaneUrl();


        if (win) {
            profileViewHolder.profileWin.setText("Zafer");
            profileViewHolder.profileWin.setTextColor(mCtx.getResources().getColor(R.color.colorWin));
        } else {
            profileViewHolder.profileWin.setText("Bozgun");
            profileViewHolder.profileWin.setTextColor(mCtx.getResources().getColor(R.color.colorDefeat));
        }

        profileViewHolder.lane.setText(permaLaneUrl);

        if(permaLaneUrl.equals("MID")) {
            profileViewHolder.imageViewLane.setImageResource(R.drawable.gold_mid);
        }

        if(permaLaneUrl.equals("BOTTOM")) {
            profileViewHolder.imageViewLane.setImageResource(R.drawable.gold_bot);
        }

        if(permaLaneUrl.equals("TOP")) {
            profileViewHolder.imageViewLane.setImageResource(R.drawable.gold_top);
        }

        if(permaLaneUrl.equals("JUNGLE")) {
            profileViewHolder.imageViewLane.setImageResource(R.drawable.gold_jungle);
        }

        Date date = new Date(gameTime);
        profileViewHolder.gameTime.setText(toTimeStr(date));

        try {
            profileViewHolder.kills.setText(String.valueOf(textViewKills)+ "/");
            profileViewHolder.deaths.setText(String.valueOf(textViewDeath) + "/");
            profileViewHolder.assists.setText(String.valueOf(textViewAssist));
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Log.e("Kills", String.valueOf(textViewKills));
            Log.e("Deaths", String.valueOf(textViewDeath));
            Log.e("Assist", String.valueOf(textViewAssist));
        }

        Picasso.get()
                .load(photoCharacter)
                .into(profileViewHolder.imageViewCharacter);

        Picasso.get()
                .load(photo0Url)
                .into(profileViewHolder.imageViewItem0);

        Picasso.get()
                .load(photo1Url)
                .into(profileViewHolder.imageViewItem1);

        Picasso.get()
                .load(photo2Url)
                .into(profileViewHolder.imageViewItem2);

        Picasso.get()
                .load(photo3Url)
                .into(profileViewHolder.imageViewItem3);

        Picasso.get()
                .load(photo4Url)
                .into(profileViewHolder.imageViewItem4);

        Picasso.get()
                .load(photo5Url)
                .into(profileViewHolder.imageViewItem5);

        Picasso.get()
                .load(photo6Url)
                .into(profileViewHolder.imageViewItem6);

        Picasso.get()
                .load(photo7Url)
                .into(profileViewHolder.imageViewSpell1);

        Picasso.get()
                .load(photo8Url)
                .into(profileViewHolder.imageViewSpell2);


    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageViewCharacter;

        private ImageView imageViewItem0;
        private ImageView imageViewItem1;
        private ImageView imageViewItem2;
        private ImageView imageViewItem3;
        private ImageView imageViewItem4;
        private ImageView imageViewItem5;
        private ImageView imageViewItem6;
        private ImageView imageViewSpell1;
        private ImageView imageViewSpell2;

        private ImageView imageViewLane;

        private TextView profileWin;
        private TextView gameTime;
        private TextView lane;

        private TextView kills;
        private TextView deaths;
        private TextView assists;

        public ProfileViewHolder(View itemView) {
            super(itemView);

            imageViewCharacter = itemView.findViewById(R.id.listLayoutProfileImageViewCharacter);
            imageViewItem0 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem0);
            imageViewItem1 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem1);
            imageViewItem2 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem2);
            imageViewItem3 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem3);
            imageViewItem4 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem4);
            imageViewItem5 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem5);
            imageViewItem6 = itemView.findViewById(R.id.listLayoutProfileImageViewPermaItem6);
            imageViewSpell1 = itemView.findViewById(R.id.listLayoutProfileImageViewSpell1);
            imageViewSpell2 = itemView.findViewById(R.id.listLayoutProfileImageViewSpell2);

            profileWin = itemView.findViewById(R.id.listLayoutProfileTextViewWin);
            gameTime = itemView.findViewById(R.id.listLayoutProfileTextViewGameTime);

            lane = itemView.findViewById(R.id.listLayoutProfileImageViewLaneText);
            imageViewLane = itemView.findViewById(R.id.listLayoutProfileImageViewLane);

            kills = itemView.findViewById(R.id.listLayoutProfileTextViewKill);
            deaths = itemView.findViewById(R.id.listLayoutProfileTextViewDeath);
            assists = itemView.findViewById(R.id.listLayoutProfileTextViewAssist);
        }
    }

    public static String toTimeStr(Date time) {

        int hour = time.getHours();
        int min = time.getMinutes();
        StringBuilder sbTime = new StringBuilder();
        if (hour < 10) {
            sbTime.append(0).append(hour);
        } else {
            sbTime.append(hour);
        }
        sbTime.append(":");
        if (min < 10) {
            sbTime.append(0).append(min);
        } else {
            sbTime.append(min);
        }
        return sbTime.toString();
    }

}

