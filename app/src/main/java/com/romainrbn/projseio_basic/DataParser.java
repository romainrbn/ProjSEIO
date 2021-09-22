package com.romainrbn.projseio_basic;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataParser {

    private Context context;

    public DataParser(Context context) {
        this.context = context;
    }

    /**
     * Permet de lire le fichier JSON contenu dans res/raw et le convertir en chaîne.
     * @return Le fichier JSON formatté en chaîne de caractères.
     */
    private String readJSONStringFromResources() {
        InputStream XmlFileInputStream = context.getResources().openRawResource(context.getResources().getIdentifier(context.getString(R.string.jsonFileName), "raw", context.getPackageName()));
        String jsonString = readTextFile(XmlFileInputStream);

        return jsonString;
    }

    /**
     * Permet de convertir un InputStream (XML...) en chaîne.
     * @param inputStream l'entrée des données.
     * @return Une chaîne de caractères correspondant aux données passées en paramètre.
     */
    private String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toString();
    }

    /**
     * Permet d'obtenir la liste des questions.
     * @return La liste des questions contenues dans le fichier JSON.
     */
    public List<Question> getQuestions() {
        Gson gson = new Gson();
        TypeToken<List<Question>> token = new TypeToken<List<Question>>() {};
        List<Question> questions = gson.fromJson(readJSONStringFromResources(), token.getType());

        return questions;
    }
}
