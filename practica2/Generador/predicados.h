#ifndef __PREDICADOS_H
#define PREDICADOS

#include <string>
#include <iostream> 
#include <vector>
#include <algorithm>

enum class Objetos {OSCAR, MANZANA, ROSA, ALGORITMO, ORO};
enum class Personajes {PRINCESA, PRINCIPE, BRUJA, PROFESOR, LEONARDO};
enum class Jugador {JUGADOR};

using namespace std;

class predicados {
private:
	vector< pair< pair<string, string>, string > > conexiones;
	vector< pair< pair<string, string>, string > > arcos;
	vector< pair<string, string> > posicion_jugador;
	vector< pair<string, string> > orientacion_jugador;
	vector< pair<string, string> > posicion_personaje;
	vector< pair<string, string> > posicion_objeto;
	vector< pair<string, string> > player_tiene;
	vector< pair<string, string> > bolsillo_personaje;
	vector< pair<string, string> > puntos_jugador;
	vector< pair<string, string> > personaje_tiene;
	vector< pair<string, string> > zona_superficie;
	vector< pair<string, string> > objetos;
	vector<string> bikinis;
	vector<string> zapatillas;
	vector<string> personajes;
	vector<string> brujas;
	vector<string> leonardos;
	vector<string> profesores;
	vector<string> princesas;
	vector<string> principes;
	vector<string> jugadores;
	vector<string> zonas;

public:
	predicados();

	void procesar_cadena_ejercicio2(string cadena);
	void procesar_cadena_ejercicio1(string cadena);
	void procesar_cadena(string cadena);
	string procesar_zona_ejercicio1(string cadena);
	string procesar_zona(string cadena);
	void procesar_objeto_tipo(string cadena, string nombre_zona);
	void procesar_lineas_objetos(string cadena, string nombre_zona);
	void procesar_bolsillos(string cadena);
	void procesar_bolsillo_personaje(string cadena);
	void procesar_puntos_jugador(string cadena);
	void procesar_jugador(string cadena);
	vector<string> getZonas();
	vector<string> getJugadores();
	vector<string> getPersonajes();
	vector< pair<string, string> > getObjetos();
	vector<string> getBikinis();
	vector<string> getZapatillas();
	vector<string> getBrujas();
	vector<string> getProfesores();
	vector<string> getPrincesas();
	vector<string> getPrincipes();
	vector<string> getLeonardos();
	vector< pair< pair<string, string>, string > > getConexiones();
	vector< pair< pair<string, string>, string > > getArcos();
	vector< pair<string, string> > getPosicionJugador();
	vector< pair<string, string> > getPosicionPersonaje();
	vector< pair<string, string> > getPosicionObjeto();
	vector< pair<string, string> > getZonaSuperficie();
	vector< pair<string, string> > getBolsillosPersonajes();
	vector< pair<string, string> > getPuntosJugadores();

};

#endif