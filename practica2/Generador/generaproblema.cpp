/* 
 * File:   main.cpp
 * Author: jumacasni
 *
 * Created on 24 de abril de 2019, 13:52
 */

#include <cstdlib>
#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include "predicados.h"

using namespace std;

/*
 * 
 */

int main(int argc, char** argv) {
    
    if(argc != 3){
    	cout << "Modo de ejecución: ./generaproblema <fichero_entrada> <fichero_salida>" << endl;

    	return 0;
    }    

    predicados p;

    ifstream fichero;
    string cadena; 

    /*********************/
    /* LEER PROBLEMA TXT */
    /*********************/

    /*
    * Se lee el fichero
    */
    fichero.open(argv[1]);

    if(fichero.fail()){
        cerr << "Error al abrir el archivo" << endl;

        return 0;
    }

    string dominio, problema, nzonas, njugadores, ptotales;

    dominio = "Ejercicio";
    problema= "Problema";

    bool tiene_dominio = false;
    bool tiene_problema = false;
    bool tiene_nzonas = false;
    bool tiene_njugadores = false;
    bool tiene_ptotales = false;
    bool tiene_bolsillo = false;
    bool tiene_pjugadores = false;
    bool ejercicio3_o_superior = false;
    bool tiene_distancia = false;
    /*
    * Se ignoran las líneas vacías
    */
    do{
        getline(fichero,cadena,'\n'); 

        if(cadena != ""){
            int inicio = 0;
            int fin = cadena.find(":");
            int length = fin - inicio;

            string palabra = cadena.substr(inicio, length);

            if(palabra == "Dominio"){
                dominio = cadena.substr(fin+1, cadena.size());
                tiene_dominio = true;
            }

            else if(palabra == "Problema"){
                problema = cadena.substr(fin+1, cadena.size());
                tiene_problema = true;
            }

            else if(palabra == "numero de zonas"){
                nzonas = cadena.substr(fin+1, cadena.size());
                tiene_nzonas = true;
            }

            else if(palabra == "numero de jugadores"){
                njugadores = cadena.substr(fin+1, cadena.size());
                tiene_njugadores = true;
            }

            else if(palabra == "bolsillo"){
                int inicio_bolsillos = cadena.find("[");            // Encontrar la posición del símbolo "["
                int final_bolsillos = cadena.find("]");             // Encontrar la posición del símbolo "]"
                length = final_bolsillos - inicio_bolsillos - 1;    // Calcular longitud de la cadena

                string subcadena = cadena.substr(inicio_bolsillos+1, length);   // Extraer la subcadena con los bolsillos de los personajes

                p.procesar_bolsillos(subcadena);    // Procesar la subcadena para coger el bolsillo de cada personaje

                tiene_bolsillo = true;
            }

            else if(palabra == "puntos_totales"){
                ptotales = cadena.substr(fin+1, cadena.size());

                tiene_ptotales = true;
            }

            else if(palabra == "puntos_jugador"){
                int inicio_puntos_jugador = cadena.find("[");               // Encontrar la posición del símbolo "["
                int final_puntos_jugador = cadena.find("]");                // Encontrar la posición del símbolo "]"
                length = final_puntos_jugador - inicio_puntos_jugador - 1;  // Calcular la longitud de la cadena

                string puntos = cadena.substr(inicio_puntos_jugador+1, length);     // Extraer la subcadena con los puntos de los jugadores
                
                if(puntos != ""){
                    p.procesar_puntos_jugador(puntos);  // Procesar la subcadena para coger los puntos de cada jugador
                    tiene_pjugadores = true;
                }
            }

            else{
                int pos_igual = cadena.find("=");

                if(pos_igual == string::npos){
                    p.procesar_cadena_ejercicio1(cadena);
                }

                else{
                    int pos_primer_corchete = cadena.find("[");
                    int pos_segundo_corchete = cadena.find("[", pos_primer_corchete+1);

                    tiene_distancia = true;

                    if(pos_segundo_corchete > pos_igual){
                        p.procesar_cadena_ejercicio2(cadena);
                    }

                    else{
                        p.procesar_cadena(cadena);
                        ejercicio3_o_superior = true;
                    }
                }  
            }
        }

    }while(!fichero.eof());

        /*
        * Se cierra el fichero
        */
        fichero.close();

        /**************************/
        /* ESCRIBIR PROBLEMA PDDL */
        /**************************/

        if(!njugadores.empty()){
            if(stoi(njugadores) < p.getJugadores().size()){
                cerr << "Error en el fichero. Especifica el número exacto de jugadores." << endl;

                return 0;
            }

            if(stoi(njugadores) < p.getPuntosJugadores().size()){
                cerr << "No puede haber más Dealer que Players" << endl;

                return 0;
            }

            if(stoi(njugadores) == p.getPuntosJugadores().size()){
                cerr << "No puede haber solo Dealer en el mapa" << endl;

                return 0;
            }

            if(p.getPuntosJugadores().size() == 0){
                cerr << "No puede haber solo Picker en el mapa" << endl;

                return 0;
            }
        }

        if(tiene_bolsillo){
            if(p.getBolsillosPersonajes().size() < p.getPersonajes().size()){
                cerr << "Faltan bolsillos de personajes por especificar" << endl;

                return 0;
            }
        }

        if(!nzonas.empty()){
	        if(stoi(nzonas) != p.getZonas().size()){
	        	cerr << "El número de zonas especificado no es el número real de zonas en el mapa" << endl;

	            return 0;
	        }
    	}
        ofstream salida;
        salida.open(argv[2]);

        /*
        * INICIO DEL PROBLEMA
        */
        salida << "(define (problem " << problema << ")" << endl;
        salida << "\t(:domain " << dominio << ")" << endl;

        /*
        * OBJETOS DEL PROBLEMA
        */
        salida << "\t(:objects ";

        // Zonas
        for(int i=0; i < p.getZonas().size(); ++i){
            salida << p.getZonas()[i] << " ";
        }

        salida << "- zona" << endl << "\t\t";

        // Jugadores
        vector<string> entrega_objetos, recoge_objetos;

        if(njugadores.empty()){
            for(int i=0; i < p.getJugadores().size(); ++i){
                salida << p.getJugadores()[i] << " ";
            }

            salida << "- player" << endl << "\t\t"; 
        }

        else if(stoi(njugadores) > p.getPuntosJugadores().size()){
            /*
            * JUGADORES PICKER Y DEALER
            */

            // Jugadores que entregan objetos
            for(int i=0; i < p.getPuntosJugadores().size(); ++i){
                entrega_objetos.push_back(p.getPuntosJugadores()[i].first);
            }

            // Jugadores que recogen objetos
            for(int i=0; i < p.getJugadores().size(); ++i){
                auto it = find(entrega_objetos.begin(), entrega_objetos.end(), p.getJugadores()[i]);
                if(it == entrega_objetos.end()){
                    recoge_objetos.push_back(p.getJugadores()[i]);
                }
            }

            for(int i=0; i < recoge_objetos.size(); ++i){
                salida << recoge_objetos[i] << " ";
            }

            salida << "- picker" << endl << "\t\t";

            for(int i=0; i < entrega_objetos.size(); ++i){
                salida << entrega_objetos[i] << " ";
            }

            salida << "- dealer" << endl << "\t\t";
        }

        else{  
            for(int i=0; i < p.getJugadores().size(); ++i){
                salida << p.getJugadores()[i] << " ";
            }

            salida << "- player" << endl << "\t\t";
        }

        // Personajes
        for(int i=0; i < p.getPersonajes().size(); ++i){
            salida << p.getPersonajes()[i] << " ";
        }

        salida << "- personajes" << endl << "\t\t";

        // Objetos
        for(int i=0; i < p.getObjetos().size(); ++i){
            salida << p.getObjetos()[i].first << " ";
        }

        salida << "- objetos)" << endl;

        /*
        * PREDICADOS INICIALES DEL PROBLEMA
        */
        salida << "\t(:init" << endl;

        // Conexiones de zonas
        for(int i=0; i < p.getConexiones().size(); ++i){
            salida << "\t\t(conexion " << p.getConexiones()[i].first.first << " " << p.getConexiones()[i].first.second << " " << p.getConexiones()[i].second << ")" << endl;
        }

        salida << endl;

        if(ejercicio3_o_superior){
            // Superficies de cada zona
            for(int i=0; i < p.getZonaSuperficie().size(); ++i){
                salida << "\t\t(zona " << p.getZonaSuperficie()[i].first << " " << p.getZonaSuperficie()[i].second << ")" << endl;
            }

            salida << endl;

            // Superficies que se pueden transitar
            salida << "\t\t(superficie-transitable Piedra)" << endl;
            salida << "\t\t(superficie-transitable Arena)" << endl;

            // Superficies de agua que necesitan bikini
            for(int i=0; i < p.getBikinis().size(); ++i){
                salida << "\t\t(superficie-transitable-si Agua " << p.getBikinis()[i] << ")" << endl;
            }

            // Superficies de bosque que necesitan zapatillas
            for(int i=0; i < p.getZapatillas().size(); ++i){
                salida << "\t\t(superficie-transitable-si Bosque " << p.getZapatillas()[i] << ")" << endl;
            }

            salida << endl;
        }

        if(tiene_distancia){
            // Distancia que hay entre las zonas
            for(int i=0; i < p.getArcos().size(); ++i){
                salida << "\t\t(= (distancia " << p.getArcos()[i].first.first << " " << p.getArcos()[i].first.second << ") " << p.getArcos()[i].second << ")" << endl;
            }

            salida << endl;

            // Inicialmente la distancia recorrida es 0
            salida << "\t\t(= (distancia-total) 0)" << endl << endl;
        }

        if(tiene_bolsillo){
            // Capacidad del bolsillo de cada personaje
            for(int i=0; i < p.getBolsillosPersonajes().size(); ++i){
                salida << "\t\t(= (capacidad-bolsillo " << p.getBolsillosPersonajes()[i].first << ") " << p.getBolsillosPersonajes()[i].second << ")" << endl;
            }

            salida << endl;

            // Inicializar los bolsillos de cada personaje a 0
            for(int i=0; i < p.getBolsillosPersonajes().size(); ++i){
                salida << "\t\t(= (bolsillo " << p.getBolsillosPersonajes()[i].first << ") 0)" << endl;
            }

            salida << endl;
        }

        if(tiene_ptotales || tiene_pjugadores){
            // Puntuación de los objetos cuando se entregan a personajes de tipo Bruja
            for(int i=0; i < p.getBrujas().size(); ++i){
                salida << "\t\t(= (puntuacion manzana " << p.getBrujas()[i] << ") 10)" << endl;
                salida << "\t\t(= (puntuacion oscar " << p.getBrujas()[i] << ") 4)" << endl;
                salida << "\t\t(= (puntuacion rosa " << p.getBrujas()[i] << ") 5)" << endl;
                salida << "\t\t(= (puntuacion algoritmo " << p.getBrujas()[i] << ") 1)" << endl;
                salida << "\t\t(= (puntuacion oro " << p.getBrujas()[i] << ") 3)" << endl;
            }

            // Puntuación de los objetos cuando se entregan a personajes de tipo Leonardo
            for(int i=0; i < p.getLeonardos().size(); ++i){
                salida << "\t\t(= (puntuacion manzana " << p.getLeonardos()[i] << ") 3)" << endl;
                salida << "\t\t(= (puntuacion oscar " << p.getLeonardos()[i] << ") 10)" << endl;
                salida << "\t\t(= (puntuacion rosa " << p.getLeonardos()[i] << ") 1)" << endl;
                salida << "\t\t(= (puntuacion algoritmo " << p.getLeonardos()[i] << ") 4)" << endl;
                salida << "\t\t(= (puntuacion oro " << p.getLeonardos()[i] << ") 5)" << endl;
            }

            // Puntuación de los objetos cuando se entregan a personajes de tipo Princesa
            for(int i=0; i < p.getPrincesas().size(); ++i){
                salida << "\t\t(= (puntuacion manzana " << p.getPrincesas()[i] << ") 1)" << endl;
                salida << "\t\t(= (puntuacion oscar " << p.getPrincesas()[i] << ") 5)" << endl;
                salida << "\t\t(= (puntuacion rosa " << p.getPrincesas()[i] << ") 10)" << endl;
                salida << "\t\t(= (puntuacion algoritmo " << p.getPrincesas()[i] << ") 3)" << endl;
                salida << "\t\t(= (puntuacion oro " << p.getPrincesas()[i] << ") 4)" << endl;
            }

            // Puntuación de los objetos cuando se entregan a personajes de tipo Profesor
            for(int i=0; i < p.getProfesores().size(); ++i){
                salida << "\t\t(= (puntuacion manzana " << p.getProfesores()[i] << ") 5)" << endl;
                salida << "\t\t(= (puntuacion oscar " << p.getProfesores()[i] << ") 3)" << endl;
                salida << "\t\t(= (puntuacion rosa " << p.getProfesores()[i] << ") 4)" << endl;
                salida << "\t\t(= (puntuacion algoritmo " << p.getProfesores()[i] << ") 10)" << endl;
                salida << "\t\t(= (puntuacion oro " << p.getProfesores()[i] << ") 1)" << endl;
            }

            // Puntuación de los objetos cuando se entregan a personajes de tipo Príncipe
            for(int i=0; i < p.getPrincipes().size(); ++i){
                salida << "\t\t(= (puntuacion manzana " << p.getPrincipes()[i] << ") 4)" << endl;
                salida << "\t\t(= (puntuacion oscar " << p.getPrincipes()[i] << ") 1)" << endl;
                salida << "\t\t(= (puntuacion rosa " << p.getPrincipes()[i] << ") 3)" << endl;
                salida << "\t\t(= (puntuacion algoritmo " << p.getPrincipes()[i] << ") 5)" << endl;
                salida << "\t\t(= (puntuacion oro " << p.getPrincipes()[i] << ") 10)" << endl;
            }

            salida << endl;

            // Inicializar la puntuación total del juego a 0
            salida << "\t\t(= (puntuacion-total) 0)" << endl;

            salida << endl;
        }
        
        // Posición inicial de cada jugador
        for(int i=0; i < p.getPosicionJugador().size(); ++i){
            salida << "\t\t(posicion-player " << p.getPosicionJugador()[i].first << " " << p.getPosicionJugador()[i].second << ")" << endl;
        }

        salida << endl;

        if(tiene_pjugadores){
            // Inicializar puntuación de cada jugador que entrega objetos a 0
            for(int i=0; i < p.getPuntosJugadores().size(); ++i){
                salida << "\t\t(= (puntuacion-player " << p.getPuntosJugadores()[i].first << ") 0)" << endl;
            }

            salida << endl;
        }

        // Posición inicial de cada personaje
        for(int i=0; i < p.getPosicionPersonaje().size(); ++i){
            salida << "\t\t(posicion-personaje " << p.getPosicionPersonaje()[i].first << " " << p.getPosicionPersonaje()[i].second << ")" << endl;
        }

        salida << endl;

        // Posición inicial de cada objeto
        for(int i=0; i < p.getPosicionObjeto().size(); ++i){
            salida << "\t\t(posicion-objeto " << p.getPosicionObjeto()[i].first << " " << p.getPosicionObjeto()[i].second << ")" << endl;
        }

        salida << endl;

        if(ejercicio3_o_superior){
            // Tipos de los objetos
            for(int i=0; i < p.getObjetos().size(); ++i){
                if(p.getObjetos()[i].second == "Manzana"){
                    salida << "\t\t(objeto-manzana " << p.getObjetos()[i].first << ")" << endl;
                }

                else if(p.getObjetos()[i].second == "Oscar"){
                    salida << "\t\t(objeto-oscar " << p.getObjetos()[i].first << ")" << endl;
                }

                else if(p.getObjetos()[i].second == "Rosa"){
                    salida << "\t\t(objeto-rosa " << p.getObjetos()[i].first << ")" << endl;
                }

                else if(p.getObjetos()[i].second == "Oro"){
                    salida << "\t\t(objeto-oro " << p.getObjetos()[i].first << ")" << endl;
                }

                else if(p.getObjetos()[i].second == "Algoritmo"){
                    salida << "\t\t(objeto-algoritmo " << p.getObjetos()[i].first << ")" << endl;
                }
            }

            salida << endl;
        }
        
        // Orientación inicial de cada jugador: todos miran al norte
        for(int i=0; i < p.getJugadores().size(); ++i){
            salida << "\t\t(orientacion-player " << p.getJugadores()[i] << " norte)" << endl;
        }

        salida << endl;

        // Todos los jugadores empiezan con las manos vacías
        for(int i=0; i < p.getJugadores().size(); ++i){
            salida << "\t\t(manovacia " << p.getJugadores()[i] << ")" << endl;
        }

        salida << endl;

        if(ejercicio3_o_superior){
            // Todos los jugadores empiezan con la mochila vacía
            for(int i=0; i < p.getJugadores().size(); ++i){
                salida << "\t\t(mochilavacia " << p.getJugadores()[i] << ")" << endl;
            }

            salida << endl;
        }

        salida << "\t)" << endl << endl;

        // OBJETIVO
        salida << "\t(:goal" << endl;

        if(tiene_ptotales){
            // Puntuación total
            salida << "\t\t(and (>= (puntuacion-total) " << ptotales << ")";

            if(!tiene_pjugadores){
                salida << ")" << endl;
            }

            else{
                // Puntuación de cada jugador
                for(int i=0; i < p.getPuntosJugadores().size(); ++i){
                    salida << " (>= (puntuacion-player " << p.getPuntosJugadores()[i].first << ") " << p.getPuntosJugadores()[i].second << ")";
                }
            
                salida << ")" << endl;
            }
        }

        else{
            if(tiene_pjugadores){
                salida << "\t\t(and";

                // Puntuación de cada jugador
                for(int i=0; i < p.getPuntosJugadores().size(); ++i){
                    salida << " (>= (puntuacion-player " << p.getPuntosJugadores()[i].first << ") " << p.getPuntosJugadores()[i].second << ")";
                }
            
                salida << ")" << endl;
            }
        }

        salida << "\t)" << endl << endl;

        if(tiene_distancia){
            salida << "\t(:metric minimize (distancia-total))" << endl;
        }

        salida << ")";

        return 0;
}

