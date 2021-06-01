package com.romainrbn.projseio_basic;

import java.util.List;

/**
 * Créé une question contenant un identifiant, un titre, un intitulé, et un nombre de choix.
 * Aussi, chaque question est marquée comme étant "scorable" ou non. Par exemple, on ne veut
 * pas assigner de point lorsque le médecin répond si il est junior ou sénior.
 */
public class Question {
    int id;
    String titre, intitule;
    Boolean scorable;
    List<String> choix;

    public Question(int id, String titre, String intitule, Boolean scorable, List<String> choix) {
        this.id = id;
        this.titre = titre;
        this.intitule = intitule;
        this.scorable = scorable;
        this.choix = choix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Boolean getScorable() {
        return scorable;
    }

    public void setScorable(Boolean scorable) {
        this.scorable = scorable;
    }

    public List<String> getChoix() {
        return choix;
    }

    public void setChoix(List<String> choix) {
        this.choix = choix;
    }

    // Debug seulement.
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", intitule='" + intitule + '\'' +
                ", scorable=" + scorable +
                ", choix=" + choix +
                '}';
    }
}
