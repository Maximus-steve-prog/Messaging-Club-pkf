package com.steven.Messaging.Club.Services;
import java.util.Scanner;
public class Conjuguate {

    // Method  to conjugate a verb a present tense
    // Tableaux des sujets
    private static final String[] SUJETS = {"je", "tu", "il", "elle", "on", "nous", "vous", "ils", "elles"};

    // Méthode pour conjuguer un verbe au présent
    public static void conjuguerPresent(String verbe) {
        String racine = verbe.substring(0, verbe.length() - 2);
        for (String sujet : SUJETS) {
            String terminaison = "";
            switch (sujet) {
                case "je":
                    terminaison = "e";
                    break;
                case "tu":
                    terminaison = "es";
                    break;
                case "il":
                case "elle":
                case "on":
                    terminaison = "e";
                    break;
                case "nous":
                    terminaison = "ons";
                    break;
                case "vous":
                    terminaison = "ez";
                    break;
                case "ils":
                case "elles":
                    terminaison = "ent";
                    break;
            }
            String MyRacine = verbe.substring(0,verbe.length() - 2);
            System.out.println(sujet + " " + racine + terminaison);
        }
    }

    // Méthode pour conjuguer un verbe au passé composé
    public static void conjuguerPasseCompose(String verbe) {
        String participePasse = verbe.substring(0, verbe.length() - 2) + "é";
        for (String sujet : SUJETS) {
            System.out.println(sujet + " a " + participePasse);
        }
    }

    // Méthode pour conjuguer un verbe à l'imparfait
    public static void conjuguerImparfait(String verbe) {
        String racine = verbe.substring(0, verbe.length() - 2);
        for (String sujet : SUJETS) {
            String terminaison = "";
            switch (sujet) {
                case "je":
                case "tu":
                    terminaison = "ais";
                    break;
                case "il":
                case "elle":
                case "on":
                    terminaison = "ait";
                    break;
                case "nous":
                    terminaison = "ions";
                    break;
                case "vous":
                    terminaison = "iez";
                    break;
                case "ils":
                case "elles":
                    terminaison = "aient";
                    break;
            }
            System.out.println(sujet + " " + racine + terminaison);
        }
    }

    // Méthode pour conjuguer un verbe au futur simple
    public static void conjuguerFuturSimple(String verbe) {
        String racine = verbe;
        for (String sujet : SUJETS) {
            String terminaison = "";
            switch (sujet) {
                case "je":
                    terminaison = "ai";
                    break;
                case "tu":
                    terminaison = "as";
                    break;
                case "il":
                case "elle":
                case "on":
                    terminaison = "a";
                    break;
                case "nous":
                    terminaison = "ons";
                    break;
                case "vous":
                    terminaison = "ez";
                    break;
                case "ils":
                case "elles":
                    terminaison = "ont";
                    break;
            }
            System.out.println(sujet + " " + racine + terminaison);
        }
    }

    // Méthode pour conjuguer un verbe au conditionnel présent
    public static void conjuguerConditionnel(String verbe) {
        String racine = verbe;
        for (String sujet : SUJETS) {
            String terminaison = "";
            switch (sujet) {
                case "je":
                    terminaison = "ais";
                    break;
                case "tu":
                    terminaison = "ais";
                    break;
                case "il":
                case "elle":
                case "on":
                    terminaison = "ait";
                    break;
                case "nous":
                    terminaison = "ions";
                    break;
                case "vous":
                    terminaison = "iez";
                    break;
                case "ils":
                case "elles":
                    terminaison = "aient";
                    break;
            }
            System.out.println(sujet + " " + racine + terminaison);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Demande à l'utilisateur d'entrer un verbe
        System.out.print("Entrez un verbe du premier groupe (finissant par -er) : ");
        String verbe = scanner.nextLine().trim();

        // Vérifie si le verbe se termine bien par -er
        if (!verbe.endsWith("er")) {
            System.out.println("Veuillez entrer un verbe du premier groupe (terminant par -er).");
        } else {
            // Conjugue le verbe pour tous les temps
            System.out.println("\nConjugaison de '" + verbe + "':");
            System.out.println("Présent :");
            conjuguerPresent(verbe);
            System.out.println("Passé Composé :");
            conjuguerPasseCompose(verbe);
            System.out.println("Imparfait :");
            conjuguerImparfait(verbe);
            System.out.println("Futur Simple :");
            conjuguerFuturSimple(verbe);
            System.out.println("Conditionnel Présent :");
            conjuguerConditionnel(verbe);
        }

        scanner.close();
    }
}