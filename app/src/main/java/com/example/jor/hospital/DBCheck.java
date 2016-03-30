package com.example.jor.hospital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jor.hospital.db.Tables;
import com.example.jor.hospital.db.adapter.DoctorAdapter;
import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.adapter.RoomAdapter;
import com.example.jor.hospital.db.adapter.TreatmentAdapter;
import com.example.jor.hospital.db.objects.Doctor;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.Patient;
import com.example.jor.hospital.db.objects.Room;
import com.example.jor.hospital.db.objects.Treatment;

import java.util.Arrays;
import java.util.List;

public class DBCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbcheck);

        //createRooms();
        //createTreatments();
        //createDoctors();
        //createPatients();
    }

    private void createPatients() {
        PatientAdapter pa = new PatientAdapter(this);

        pa.deleteAllPatients();

        Patient pat1 = new Patient(true, "Kaufmann", "Tobias", "25.09.1992","Avenue d'Ouchy 91","1737 Plasselb","756.9868.4565.00");
        Patient pat2 = new Patient(true, "Ebersbacher", "Marco", "06.06.1957","Obere Bahnhofstrasse 60","9602 Bazenheid","746.9841.5432.00");
        Patient pat3 = new Patient(false, "Dresdner", "Sabine", "09.09.1932","Kornquaderweg 4,","6285 Retschwil","123.4853.1152.00");
        Patient pat4 = new Patient(false, "Fruehauf", "Angelika", "28.04.1965","Rasenstrasse 88","3153 Hirschhorn","561.2175.3369.00");
        Patient pat5 = new Patient(false, "Gerste", "Leah", "23.11.1940","Boldistrasse 101","3504 Niederhünigen","685.1782.8874.00");
        Patient pat6 = new Patient(false, "Schwarz", "Ute", "19.09.1947","Kronwiesenweg 26","4581 Küttigkofen","175.3682.6971.00");
        Patient pat7 = new Patient(true, "Reinhard", "Thorsten", "25.04.1944","Langwiesstrasse 45","6383 Wiesenberg","589.1382.1368.00");
        Patient pat8 = new Patient(true, "Goldschmidt", "Maik", "25.09.1992","Via Muraccio 82","7472 Surava","586.3698.1368.00");
        Patient pat9 = new Patient(true, "Winkel", "Kevin", "06.07.1960","Spinatsch 37","8815 Morschwand","584.7458.1475.00");
        Patient pat10 = new Patient(true, "Reinhard", "Stefan", "21.05.1938","Via Altisio 100","4509 Solothurn","253.6351.3698.00");
        Patient pat11 = new Patient(false, "Neudorf", "Franziska", "07.11.1940","Postfach 90","6982 Agno","697.2478.4452.00");
        Patient pat12 = new Patient(true, "Kaufmann", "Tobias", "25.09.1992","Avenue d'Ouchy 91,","Plasselb","742.3698.1136.00");
        Patient pat13 = new Patient(true, "Baumgartner", "David", "28.11.1984","Brunnenstrasse 53","8932 Rossau","352.1478.1826.00");
        Patient pat14 = new Patient(true, "Bachmeier", "Alexander", "27.09.1966","Im Sandbüel 30","1708 Fribourg","397.1256.7469.00");
        Patient pat15 = new Patient(false, "Fuchs", "Laura", "02.01.1968","Via Luzzas 29","6145 Fischbach","425.1365.3698.00");

        pa.createPatient(pat1);
        pa.createPatient(pat2);
        pa.createPatient(pat3);
        pa.createPatient(pat4);
        pa.createPatient(pat5);
        pa.createPatient(pat6);
        pa.createPatient(pat7);
        pa.createPatient(pat8);
        pa.createPatient(pat9);
        pa.createPatient(pat10);
        pa.createPatient(pat11);
        pa.createPatient(pat12);
        pa.createPatient(pat13);
        pa.createPatient(pat14);
        pa.createPatient(pat15);
    }

    private void createDoctors() {
        DoctorAdapter da = new DoctorAdapter(this);

        da.deleteAllDoctors();

        Doctor doc1 = new Doctor(true, "Johner", "Robert", "robert.johner@gmail.com", Login.getMD5("banana"));
        Doctor doc2 = new Doctor(true, "Mathier", "Sandro", "mathier.sandro@gmail.com", Login.getMD5("fotze16"));
        Doctor doc3 = new Doctor(true, "Doc1", "Ken", "doc1@gmail.com", Login.getMD5("doktor1"));
        Doctor doc4 = new Doctor(false, "Doc2", "Ken", "doc2@gmail.com", Login.getMD5("doktor2"));
        Doctor doc5 = new Doctor(false, "Doc3", "Ken", "doc3@gmail.com", Login.getMD5("doktor3"));
        Doctor doc6 = new Doctor(true, "Doc4", "John", "doc4@gmail.com", Login.getMD5("doktor4"));
        Doctor doc7 = new Doctor(false, "Doc5", "John", "doc5@gmail.com", Login.getMD5("doktor5"));
        Doctor doc8 = new Doctor(true, "Doc6", "John", "doc6@gmail.com", Login.getMD5("doktor6"));
        Doctor doc9 = new Doctor(false, "Doc7", "Charles", "doc7@gmail.com", Login.getMD5("doktor7"));
        Doctor doc10 = new Doctor(true, "Doc8", "Charles", "doc8@gmail.com", Login.getMD5("doktor8"));

        da.createDoctor(doc1);
        da.createDoctor(doc2);
        da.createDoctor(doc3);
        da.createDoctor(doc4);
        da.createDoctor(doc5);
        da.createDoctor(doc6);
        da.createDoctor(doc7);
        da.createDoctor(doc8);
        da.createDoctor(doc9);
        da.createDoctor(doc10);
    }

    private void createTreatments() {
        String [] treatmentsDesc = {
                "Auscultation",
                "Medical inspection",
                "Palpation",
                "Percussion",
                "Vital signs",
                "Blood test",
                "Urinalysis",
                "Stool test",
                "Biopsy test",
                "Cardiac stress test",
                "Electrocardiography",
                "Electroencephalography",
                "Electrocorticography",
                "Electromyography",
                "Electroneuronography",
                "Electronystagmography",
                "Electrooculography",
                "Electroretinography",
                "Endoluminal capsule monitoring",
                "Endoscopy",
                "Colonoscopy",
                "Colposcopy",
                "Cystoscopy",
                "Gastroscopy",
                "Laparoscopy",
                "Laryngoscopy",
                "Ophthalmoscopy",
                "Otoscopy",
                "Sigmoidoscopy",
                "Esophageal motility study",
                "Evoked potential",
                "Magnetoencephalography",
                "Medical imaging",
                "Angiography",
                "Aortography",
                "Cerebral angiography",
                "Coronary angiography",
                "Lymphangiography",
                "Pulmonary angiography",
                "Ventriculography",
                "Chest photofluorography",
                "Computed tomography",
                "Radiography",
                "Scintillography",
                "Ultrasonography",
                "Gynecologic ultrasonography",
                "Obstetric ultrasonography",
                "Contrast-enhanced ultrasound",
                "Intravascular ultrasound",
                "Thermography",
                "Virtual colonoscopy",
                "Neuroimaging",
                "Posturography",
                "Biopsy",
                "Stereotactic surgery",
                "Radiosurgery",
                "Endoscopic surgery",
                "Lithotomy",
                "Image-guided surgery",
                "Facial rejuvenation",
                "Neovaginoplasty",
                "Vaginoplasty",
                "Ablation",
                "Amputation",
                "Cardiopulmonary resuscitation (CPR)",
                "Cryosurgery",
                "General surgery",
                "Hand surgery",
                "Laminectomy",
                "Hemilaminectomy",
                "Laparoscopic surgery",
                "Lithotriptor",
                "Lobotomy",
                "Knee cartilage replacement therapy",
                "Xenotransplantation",
                "Local anesthesia",
                "Topical anesthesia",
                "Epidural",
                "Spinal anesthesia",
                "Regional anesthesia",
                "General anesthesia"
        };
        TreatmentAdapter ta = new TreatmentAdapter(this);
        Arrays.sort(treatmentsDesc);
        ta.deleteAllTreatments();
        for(int i = 0 ; i < treatmentsDesc.length ; i++){
            Treatment tr = new Treatment();
            tr.setTreatment(treatmentsDesc[i]);
            ta.createTreatment(tr);
        }
    }

    private void createRooms() {
        RoomAdapter ra = new RoomAdapter(this);
        ra.deleteAllRooms();
        for (int i = 0 ; i <  101 ; i ++){
            Room room = new Room();
            room.setId(i);
            ra.createRoom(room);
        }
    }
}
