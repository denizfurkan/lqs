package com.example.codingtr.lolquery.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.codingtr.lolquery.Adapter.ProfileAdapter;
import com.example.codingtr.lolquery.Management.Profile;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profil_Fragment extends Fragment {

    private SessionManagement sessionManagement;
    private RecyclerView recyclerView;
    private static String getEmail;

    private String getId;
    private String game_username;
    private String game_server;
    private String game_status;

    private int intItem0;
    private int intItem1;
    private int intItem2;
    private int intItem3;
    private int intItem4;
    private int intItem5;
    private int intItem6;
    private int spell1Id;
    private int spell2Id;
    private int champion;

    private long permaTimeStamp;
    private boolean permaWin;

    private String permaChampion;
    private String permaSpell1;
    private String permaSpell2;
    private String permaSpell1Url;
    private String permaSpell2Url;
    private String permaItem0Url;
    private String permaItem1Url;
    private String permaItem2Url;
    private String permaItem3Url;
    private String permaItem4Url;
    private String permaItem5Url;
    private String permaItem6Url;
    private String permaChampionPhotoUrl;

    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;

    private static final String URL_READ = "https://tr1.api.riotgames.com/lol/match/v4/matchlists/by-account/NXCMQ6llR-uSDLgAToSOcbGoT3TRysNjzv7knkrYLfmK?api_key=RGAPI-bf45a82b-39d1-4248-a9a3-93c35e583914";
    private static final String URL_PROFILE = "http://lolquery.codingtr.com/webservice/read_detail.php";
    private static final String URL_GAME = "http://lolquery.codingtr.com/webservice/gamer_read_detail.php";
    private static final String URL_SPELL = "http://ddragon.leagueoflegends.com/cdn/9.13.1/data/tr_TR/summoner.json";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profil, container,false);
        sessionOperation();

        profileList = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.fragmentProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readGameSettings();
        readGameIn();

        return rootView;
    }

    private void sessionOperation() {
        sessionManagement = new SessionManagement(getContext());
        sessionManagement.checkLogin();

        sessionManagement.getUserDetail();
        HashMap<String, String> user  = sessionManagement.getUserDetail();
        getId = user.get(sessionManagement.ID);
        getEmail = user.get(sessionManagement.EMAIL);
    }

    public void readGameSettings() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        JSONArray jsonArray = jsonObject.getJSONArray("read");
                        for(int i =0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            game_username = jsonObject1.getString("gamer_username");
                            game_server = jsonObject1.getString("gamer_server");
                            game_status = jsonObject1.getString("onay");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_email", getEmail);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void readGameIn() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray matches = root.getJSONArray("matches");

                    for(int i = 0; i<9; i++) {

                        JSONObject jsonObject1 = matches.getJSONObject(i);
                        int gameId = jsonObject1.getInt("gameId");
                        champion = jsonObject1.getInt("champion");
                        permaTimeStamp = jsonObject1.getLong("timestamp");
                        String role = jsonObject1.getString("role");
                        String lane = jsonObject1.getString("lane");

                        readGameOut(gameId, permaTimeStamp, champion, lane);

                    }
                } catch (JSONException e) {
                    Log.e("Hata", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void readGameOut(int gameId, final long permaTimeStamp, final int champion, final String lane) {
        final String gameOutQuery = "https://tr1.api.riotgames.com/lol/match/v4/matches/" + gameId + "?api_key=RGAPI-bf45a82b-39d1-4248-a9a3-93c35e583914";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, gameOutQuery, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                int tempParticipantId;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("participantIdentities");

                    for(int i = 0; i<jsonArray.length(); i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("player");

                        String summonerName = jsonObject2.getString("summonerName");

                        if(game_username.equals(summonerName)){
                            tempParticipantId = i+1;

                            JSONArray jsonArray1 = jsonObject.getJSONArray("participants");

                            for(int j=0; j<jsonArray1.length(); j++) {
                                JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                int tempParticipantId2 = jsonObject3.getInt("participantId");

                                if(tempParticipantId == tempParticipantId2 ){

                                    int teamId = jsonObject3.getInt("teamId");
                                    spell1Id = jsonObject3.getInt("spell1Id");
                                    spell2Id = jsonObject3.getInt("spell2Id");

                                    for(int k=0; k<jsonArray1.length(); k++){

                                        JSONObject jsonObject4 =  jsonArray1.getJSONObject(k);
                                        JSONObject jsonObject5 = jsonObject4.getJSONObject("stats");
                                        int tempId = jsonObject5.getInt("participantId");

                                        if(tempId == tempParticipantId){

                                            permaWin = jsonObject5.getBoolean("win");
                                            intItem0 = jsonObject5.getInt("item0");
                                            intItem1 = jsonObject5.getInt("item1");
                                            intItem2 = jsonObject5.getInt("item2");
                                            intItem3 = jsonObject5.getInt("item3");
                                            intItem4 = jsonObject5.getInt("item4");
                                            intItem5 = jsonObject5.getInt("item5");
                                            intItem6 = jsonObject5.getInt("item6");

                                            int kills = jsonObject5.getInt("kills");
                                            int deaths = jsonObject5.getInt("deaths");
                                            int assists = jsonObject5.getInt("assists");
                                            int totalMinionsKilled = jsonObject5.getInt("totalMinionsKilled");
                                            int goldEarned = jsonObject5.getInt("goldEarned");

                                            readItem();
                                            readSpell();
                                            readSpell2();
                                            readCharacterPhoto(champion);

                                            profileList.add(new Profile(permaSpell1Url,
                                                    permaSpell2Url,
                                                    permaTimeStamp,
                                                    permaWin,
                                                    lane,
                                                    kills,
                                                    deaths,
                                                    assists,
                                                    permaItem0Url,
                                                    permaItem1Url,
                                                    permaItem2Url,
                                                    permaItem3Url,
                                                    permaItem4Url,
                                                    permaItem5Url,
                                                    permaItem6Url,
                                                    permaChampionPhotoUrl));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                profileAdapter = new ProfileAdapter(getContext(), profileList);
                recyclerView.setAdapter(profileAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    private void readCharacterPhoto(int champion) {
        switch(champion){
            case 517: permaChampion = "Sylas"; break;
            case 518: permaChampion = "Neeko"; break;
            case 246: permaChampion = "Qiyana";break;
            case 350: permaChampion = "Yuumi"; break;
            case 497: permaChampion = "Rakan"; break;
            case 498: permaChampion = "Xayah"; break;
            case 142: permaChampion = "Zoe"; break;
            case 555: permaChampion = "Pyke"; break;
            case 145: permaChampion = "Kaisa";	break;
            case 164: permaChampion = "Camille"; break;
            case 266: permaChampion = "Aatrox"; break;
            case 412: permaChampion = "Thresh"; break;
            case 23:  permaChampion = "Tryndamere"; break;
            case 79:  permaChampion = "Gragas";	break;
            case 69:  permaChampion = "Cassiopeia"; break;
            case 136: permaChampion = "AurelionSol"; break;
            case 13:  permaChampion = "Ryze"; break;
            case 78:  permaChampion = "Poppy"; break;
            case 14:  permaChampion = "Sion"; break;
            case 1:   permaChampion = "Annie";	break;
            case 202: permaChampion = "Jhin"; break;
            case 43:  permaChampion = "Karma"; break;
            case 111: permaChampion = "Nautilus"; break;
            case 240: permaChampion = "Kled"; break;
            case 99:  permaChampion = "Lux"; break;
            case 103: permaChampion = "Ahri";	break;
            case 2:   permaChampion = "Olaf"; break;
            case 112: permaChampion = "Viktor";	break;
            case 34:  permaChampion = "Anivia";	break;
            case 27:  permaChampion = "Singed"; break;
            case 86:  permaChampion = "Garen"; break;
            case 127: permaChampion = "Lissandra"; break;
            case 57:  permaChampion = "Maokai"; break;
            case 25:  permaChampion = "Morgana";	break;
            case 28:  permaChampion = "Evelynn"; break;
            case 105: permaChampion = "Fizz"; break;
            case 74:  permaChampion = "Heimerdinger";break;
            case 238: permaChampion = "Zed"; break;
            case 68:  permaChampion = "Rumble"; break;
            case 82:  permaChampion = "Mordekaiser"; break;
            case 37:  permaChampion = "Sona";	break;
            case 96:  permaChampion = "KogMaw";	break;
            case 55:  permaChampion = "Katarina"; break;
            case 117: permaChampion = "Lulu"; break;
            case 22:  permaChampion = "Ashe"; break;
            case 30:  permaChampion = "Karthus";	break;
            case 12:  permaChampion = "Alistar"; break;
            case 122: permaChampion = "Darius"; break;
            case 67:  permaChampion = "Vayne"; break;
            case 110: permaChampion = "Varus"; break;
            case 77:  permaChampion = "Udyr"; break;
            case 89:  permaChampion = "Leona"; break;
            case 126: permaChampion = "Jayce"; break;
            case 134: permaChampion = "Syndra"; break;
            case 80:  permaChampion = "Pantheon";	break;
            case 92:  permaChampion = "Riven"; break;
            case 121: permaChampion = "Khazix"; break;
            case 42:  permaChampion = "Corki"; break;
            case 268: permaChampion = "Azir"; break;
            case 51:  permaChampion = "Caitlyn";	break;
            case 76:  permaChampion = "Nidalee"; break;
            case 85:  permaChampion = "Kennen"; break;
            case 3:   permaChampion = "Galio"; break;
            case 45:  permaChampion = "Veigar"; break;
            case 432: permaChampion = "Bard"; break;
            case 150: permaChampion = "Gnar"; break;
            case 90:  permaChampion = "Malzahar"; break;
            case 104: permaChampion = "Graves"; break;
            case 254: permaChampion = "Vi"; break;
            case 10:  permaChampion = "Kayle"; break;
            case 39:  permaChampion = "Irelia"; break;
            case 64:  permaChampion = "LeeSin"; break;
            case 420: permaChampion = "Illaoi"; break;
            case 60:  permaChampion = "Elise"; break;
            case 106: permaChampion = "Volibear"; break;
            case 20:  permaChampion = "Nunu"; break;
            case 4:   permaChampion = "TwistedFate"; break;
            case 24:  permaChampion = "Jax"; break;
            case 102: permaChampion = "Shyvana"; break;
            case 429: permaChampion = "Kalista"; break;
            case 141: permaChampion = "Kayn";	break;
            case 36:  permaChampion = "DrMundo";	break;
            case 427: permaChampion = "Ivern"; break;
            case 131: permaChampion = "Diana"; break;
            case 223: permaChampion = "Tahm Kench"; break;
            case 63:  permaChampion = "Brand"; break;
            case 113: permaChampion = "Sejuani"; break;
            case 8:   permaChampion = "Vladimir";	break;
            case 154: permaChampion = "Zac"; break;
            case 421: permaChampion = "RekSai"; break;
            case 133: permaChampion = "Quinn"; break;
            case 84:  permaChampion = "Akali"; break;
            case 163: permaChampion = "Taliyah"; break;
            case 18:  permaChampion = "Tristana"; break;
            case 120: permaChampion = "Hecarim"; break;
            case 15:  permaChampion = "Sivir"; break;
            case 236: permaChampion = "Lucian"; break;
            case 107: permaChampion = "Rengar"; break;
            case 19:  permaChampion = "Warwick"; break;
            case 72:  permaChampion = "Skarner"; break;
            case 54:  permaChampion = "Malphite"; break;
            case 157: permaChampion = "Yasuo"; break;
            case 101: permaChampion = "Xerath"; break;
            case 17:  permaChampion = "Teemo"; break;
            case 75:  permaChampion = "Nasus"; break;
            case 58:  permaChampion = "Renekton"; break;
            case 119: permaChampion = "Draven"; break;
            case 35:  permaChampion = "Shaco"; break;
            case 50:  permaChampion = "Swain"; break;
            case 91:  permaChampion = "Talon"; break;
            case 40:  permaChampion = "Janna"; break;
            case 115: permaChampion = "Ziggs"; break;
            case 245: permaChampion = "Ekko"; break;
            case 61:  permaChampion = "Orianna"; break;
            case 114: permaChampion = "Fiora"; break;
            case 9:   permaChampion = "Fiddlesticks";break;
            case 31:  permaChampion = "Chogath"; break;
            case 33:  permaChampion = "Rammus"; break;
            case 7:   permaChampion = "Leblanc"; break;
            case 16:  permaChampion = "Soraka"; break;
            case 26:  permaChampion = "Zilean"; break;
            case 56:  permaChampion = "Nocturne"; break;
            case 222: permaChampion = "Jinx"; break;
            case 83:  permaChampion = "Yorick"; break;
            case 6:   permaChampion = "Urgot"; break;
            case 203: permaChampion = "Kindred"; break;
            case 21:  permaChampion = "MissFortune"; break;
            case 62:  permaChampion = "MonkeyKing"; break;
            case 53:  permaChampion = "Blitzcrank"; break;
            case 98:  permaChampion = "Shen"; break;
            case 201: permaChampion = "Braum"; break;
            case 516: permaChampion = "Ornn"; break;
            case 5:   permaChampion = "XinZhao"; break;
            case 29:  permaChampion = "Twitch"; break;
            case 11:  permaChampion = "MasterYi"; break;
            case 44:  permaChampion = "Taric"; break;
            case 32:  permaChampion = "Amumu"; break;
            case 41:  permaChampion = "Gangplank"; break;
            case 48:  permaChampion = "Trundle"; break;
            case 38:  permaChampion = "Kassadin"; break;
            case 161: permaChampion = "Velkoz"; break;
            case 143: permaChampion = "Zyra"; break;
            case 267: permaChampion = "Nami"; break;
            case 59:  permaChampion = "JarvanIV"; break;
            case 81:  permaChampion = "Ezreal"; break;
        }
        permaChampionPhotoUrl = "https://cdn.communitydragon.org/9.13.1/champion/"+permaChampion+"/splash-art/centered";
    }

    private void readItem() {

        if(intItem0 == 0){
            permaItem0Url="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else {
            permaItem0Url="http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem0+".png";
        }
        if(intItem1 == 0){
            permaItem1Url ="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem1Url ="http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem1+".png";
        }
        if(intItem2 == 0){
            permaItem2Url="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem2Url = "http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem2+".png";
        }
        if(intItem3 == 0){
            permaItem3Url="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem3Url = "http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem3+".png";
        }
        if(intItem4 == 0){
            permaItem4Url="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem4Url = "http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem4+".png";
        }
        if(intItem5 == 0){
            permaItem5Url ="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem5Url = "http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem5+".png";
        }
        if(intItem6 == 0){
            permaItem6Url ="http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell/SummonerDarkStarChampSelect1.png";
        }else{
            permaItem6Url = "http://ddragon.leagueoflegends.com/cdn/9.13.1/img/item/"+intItem6+".png";
        }

    }

    private void readSpell(){

        switch(spell2Id){
            case 14:      permaSpell2 = "SummonerDot";                 break;
            case 07:      permaSpell2 = "SummonerHeal";                   break;
            case 04:      permaSpell2 = "SummonerFlash";                 break;
            case 21:      permaSpell2 = "SummonerBarrier";             break;
            case 64:      permaSpell2 = "SummonerBoost";                 break;
            case 12:      permaSpell2 = "SummonerTeleport";             break;
            case 03:      permaSpell2 = "SummonerExhaust";                break;
            case 06:      permaSpell2 = "SummonerHaste";                 break;
            case 13:      permaSpell2 = "SummonerMana";                   break;
            case 11:      permaSpell2 = "SummonerSmite";                 break;
            case 32:      permaSpell2 = "SummonerSnowball";             break;
            case 31:      permaSpell2 = "SummonerThrow";                   break;
            case 30:      permaSpell2 = "SummonerPoroRecall";        break;
        }

        permaSpell2Url = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/spell/" + permaSpell2 + ".png";
    }

    private void readSpell2() {

        switch(spell1Id){
            case 14:      permaSpell1 = "SummonerDot";                 break;
            case 07:      permaSpell1 = "SummonerHeal";                   break;
            case 04:      permaSpell1 = "SummonerFlash";                 break;
            case 21:      permaSpell1 = "SummonerBarrier";             break;
            case 64:      permaSpell1 = "SummonerBoost";                 break;
            case 12:      permaSpell1 = "SummonerTeleport";             break;
            case 03:      permaSpell1 = "SummonerExhaust";                break;
            case 06:      permaSpell1 = "SummonerHaste";                 break;
            case 13:      permaSpell1 = "SummonerMana";                   break;
            case 11:      permaSpell1 = "SummonerSmite";                 break;
            case 32:      permaSpell1 = "SummonerSnowball";             break;
            case 31:      permaSpell1 = "SummonerThrow";                   break;
            case 30:      permaSpell1 = "SummonerPoroRecall";        break;
        }

        permaSpell1Url = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/spell/" + permaSpell1 + ".png";

    }

}

