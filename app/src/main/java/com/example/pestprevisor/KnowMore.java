package com.example.pestprevisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class KnowMore extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    ArrayList<Model> models =   new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationKnowMenuId);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottomNavigationHomeMenuId:
                                Toast.makeText(getApplicationContext(), "Home screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(KnowMore.this, MainActivity.class));
                                return true;

                            case R.id.bottomNavigationCameraMenuId:
                                Toast.makeText(getApplicationContext(), "Detection screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(KnowMore.this, ClassifierActivity.class));
                                return  false;

                            case R.id.bottomNavigationKnowMenuId:
                                Toast.makeText(getApplicationContext(), "Know More screen", Toast.LENGTH_LONG).show();
                               // startActivity (new Intent(KnowMore.this, KnowMore.class));
                                return true;

                        }
                        return false;
                    }
                });










        models.add(new Model("WhiteFly", "Adult whiteflies are are small sap-feeding insects between 1-3mm in length. Their bodies and wings are covered in a white waxy powder although there are species that have a dark grey covering, they are known as blackflies.","Imidacloprid, Neem Oil", R.drawable.whitefly));
        models.add(new Model("Spotted Bollworm", "Forewing bright apple-green with paler longitudinal streaks, the anterior third of the wing pale apple-green often tinged with pink. Hindwing silky white suffused distally with pale brownish grey.","Esfenvalerate, Spinosad",R.drawable.spotted2));
        models.add(new Model("Spodoptera", "The life cycle is completed in about 30 days during the summer, but 60 days in the spring and autumn, and 80 to 90 days during the winter. The number of generations occurring in an area.","Boxer, Malathion",R.drawable.spodoptera));
        models.add(new Model("Pinkbollworm", "Pinkbollworm adults are small, dark-brown moths measuring about 12-20 mm across the wings. The head is reddish brown in colour with pale, iridescent scales. Antennae are brown and the basal segment bears a pecten of five or six long,hair-like scales.","Decamethrin",R.drawable.pinkballworm));
        models.add(new Model("Jassid", "Jassids are about 3.5 mm in length. They are elongate and wedge shaped with pale green body. Forewings and vertex have black spots. Adults are very active with sideway movements but quick to hop and fly when disturbed.","Imidacloprid",R.drawable.jassid));
        models.add(new Model("American Bollworm", "Moths are brownish yellow colored with fore wings having a prominent black spot and a broad black patch on the hind wing margin. Larvae are greenish with longitudinal streaks along the sides of the insect.","Methomyl",R.drawable.americanballworm));
        models.add(new Model("Aphids", "Aphids are tiny (adults are under Â¼-inch), and often nearly invisible to the naked eye. Various species can appear white, black, brown, gray, yellow, light green, or even pink! Some may have a waxy or woolly coating.","Neem Oil",R.drawable.aphid));
        models.add(new Model("Blight", "Blight is a rapid and complete chlorosis, browning, then death of plant tissues such as leaves, branches, twigs, or floral organs.Brown spots on the margins of the cotyledons characterize plants infected early in the growing season.","Bresthanol",R.drawable.blight));
        models.add(new Model("MealyFly", "Mealybugs are small, soft-bodied insects which have sucking mouthparts. The females are oval in shape and can be up to 5mm long. They are white or whitish-pink in colour and are generally covered in a white waxy material. ","Isopropyl alcohol",R.drawable.mealybug));
        models.add(new Model("Miridbug", "Miridae are small, terrestrial insects, usually oval-shaped or elongate and measuring less than 12 millimetres in length. Many of them have a hunched look, because of the shape of the prothorax, which carries the head bent down.","Apolygus lucorum ",R.drawable.mirid));
        models.add(new Model("LeafBlight", "Bacterial leaf blight. Leaves with undulated yellowish white or golden yellow marginal necrosis, drying of leaves back from tip and curling, leaving mid rib intact are the major symptoms.","Brestanol",R.drawable.bacterialblight));
        models.add(new Model("Thrips", "Thrips are very small (1.25-mm or less), fringe winged, and yellowish brown to amber with an orange thorax. The male is slightly smaller and lighter in color than the female.","Spinosad ",R.drawable.thrips));


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this,  models);
        mRecyclerView.setAdapter(myAdapter);
    }



}
