package com.unir.myyard.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// DTO (Data Transfer Object)/ POJO (Plain Old Java Object)
// molde de mapeamento para o Gson converter string Json em objeto
/* Corpo da resposta da API
{
    "query": {
        "project": "all",
        "images": [
            "image_1",
            "image_2"
        ],
        "organs": [
            "flower",
            "leaf"
        ]
    },
    "language": "en",
    "preferedReferential": "k-southwestern-europe",
    "results": [
        {
            "score": 0.9952006530761719,
            "species": {
                "scientificNameWithoutAuthor": "Hibiscus rosa-sinensis",
                "scientificNameAuthorship": "L.",
                "genus": {
                    "scientificNameWithoutAuthor": "Hibiscus",
                    "scientificNameAuthorship": "L."
                },
                "family": {
                    "scientificNameWithoutAuthor": "Malvaceae",
                    "scientificNameAuthorship": "Juss."
                },
                "commonNames": [
                    "Chinese hibiscus",
                    "Hawaiian hibiscus",
                    "Hibiscus"
                ]
            }
        }
    ],
    "remainingIdentificationRequests": 1228
}
 */
public class PlantNetResponse {
    @SerializedName("results") // ponte entre o nome do campo no JSON e a variável
    private List<Result> results;

    @SerializedName("remainingIdentificationRequests")
    private int remainingRequests;

    public List<Result> getResults(){
        return results;
    }

    public int getRemainingRequests(){
        return remainingRequests;
    }

    public static class Result{
        @SerializedName("score")
        private double score;

        @SerializedName("species")
        private Species species;

        public double getScore() {
            return score;
        }

        public Species getSpecies() {
            return species;
        }

    }

    public static class Species {
        @SerializedName("scientificNameWithoutAuthor")
        private String scientificName;

        @SerializedName("commonNames")
        private List<String> commonNames;

        @SerializedName("family")
        private Family family;

        public String getScientificName() {
            return scientificName;
        }

        public List<String> getCommonNames() {
            return commonNames;
        }

        public Family getFamily() {
            return family;
        }
    }

    public static class Family {
        @SerializedName("scientificNameWithoutAuthor")
        private String familyName;

        public String getFamilyName() {
            return familyName;
        }
    }
}
