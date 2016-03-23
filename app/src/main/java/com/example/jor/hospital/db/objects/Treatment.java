package com.example.jor.hospital.db.objects;

/**
 * Created by jor on 22.03.2016.
 */
public class Treatment {
    private int treatment_id;
    private String treatment;

    public int getTreatment_id() {
        return treatment_id;
    }

    public void setTreatment_id(int treatment_id) {
        this.treatment_id = treatment_id;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "treatment_id=" + treatment_id +
                ", treatment='" + treatment + '\'' +
                '}';
    }
}

