#include "predicados.h"

predicados::predicados(){
	
}

void predicados::procesar_lineas_objetos(string cadena, string nombre_zona){
    /*
    * La cadena es del tipo:
    *
    *    "bruja1-Bruja manzana1-Manzana"
    */

    int inicio = 0;
    int fin;
    int length;

    bool fin_linea = false;

    string objeto_tipo;

    while(!fin_linea){
        fin = cadena.find(" ", inicio);                 // Encontrar el espacio
        length = fin - inicio;                          // Longitud desde el inicio al espacio
        objeto_tipo = cadena.substr(inicio, length);    // Resultado -> "bruja1-Bruja"

        procesar_objeto_tipo(objeto_tipo, nombre_zona); // Procesar "bruja1-Bruja"
        
        if(fin == string::npos)         // Comprobar si es el fin de la cadena
            fin_linea = true;

        else
            inicio = fin + 1;
    }
}

void predicados::procesar_objeto_tipo(string zona, string nombre_zona){
    /*
    * La cadena es del tipo:
    *
    *    "bruja1-Bruja"
    */

    int inicio = 0;
    int fin;
    int length;

    string objeto, tipo;

    fin = zona.find("-", inicio);               // Encontrar el "-"
    length = fin - inicio;                      // Calcular longitud desde el inicio al "-"
    objeto = zona.substr(inicio, length);       // Resultado -> "bruja1"

    // Tipo
    inicio = fin + 1;

    fin = zona.find("\n", inicio);              // Encontrar el final de la cadena
    length = fin - inicio;                      // Calcular la longitud desde el final de antes al final de la cadena
    tipo = zona.substr(inicio, length);         // Resultado -> "Bruja"

    // TIPO OBJETO
    if(tipo == "Oscar" || tipo == "Manzana" || tipo == "Rosa" || tipo == "Algoritmo" || tipo == "Oro"){
        pair<string, string> aux;
        aux.first = objeto;
        aux.second = nombre_zona;

        posicion_objeto.push_back(aux);

        pair<string, string> aux2;
        aux2.first = objeto;
        aux2.second = tipo;

        objetos.push_back(aux2);
    }

    // TIPO PERSONAJE 
    else if(tipo == "Bruja" || tipo == "Principe" || tipo == "Princesa" || tipo == "Profesor" || tipo == "Leonardo"){
        pair<string, string> aux;
        aux.first = objeto;
        aux.second = nombre_zona;

        posicion_personaje.push_back(aux);

        personajes.push_back(objeto);

        if(tipo == "Bruja")
            brujas.push_back(objeto);

        else if(tipo == "Principe")
            principes.push_back(objeto);

        else if(tipo == "Leonardo")
            leonardos.push_back(objeto);

        else if(tipo == "Princesa")
            princesas.push_back(objeto);

        else if(tipo == "Profesor")
            profesores.push_back(objeto);
    }

    // TIPO JUGADOR
    else if(tipo == "Player"){
        pair<string, string> aux;
        aux.first = objeto;
        aux.second = nombre_zona;

        posicion_jugador.push_back(aux);

        aux.first = objeto;
        aux.second = "norte";

        orientacion_jugador.push_back(aux);

        jugadores.push_back(objeto);
    }

    // TIPO ZAPATILLAS
    else if(tipo == "Zapatilla"){
        zapatillas.push_back(objeto);

        pair<string, string> aux;
        aux.first = objeto;
        aux.second = nombre_zona;

        posicion_objeto.push_back(aux);

        pair<string, string> aux2;
        aux2.first = objeto;
        aux2.second = tipo;

        objetos.push_back(aux2);
    }

    // TIPO BIKINI
    else if(tipo == "Bikini"){
        bikinis.push_back(objeto);

        pair<string, string> aux;
        aux.first = objeto;
        aux.second = nombre_zona;

        posicion_objeto.push_back(aux);

        pair<string, string> aux2;
        aux2.first = objeto;
        aux2.second = tipo;

        objetos.push_back(aux2);
    }
}

void predicados::procesar_bolsillo_personaje(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "bruja1:4"
    */
    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena;
    pair<string, string> aux;

    fin = cadena.find(":", inicio);             // Encontrar el símbolo ":"
    length = fin - inicio;                      // Calcular longitud subcadena

    subcadena = cadena.substr(inicio, length);  // Resultado -> bruja1
    aux.first = subcadena;

    inicio = fin + 1;
    fin = cadena.find("\n", inicio);            // Encontrar final de la subcadena
    length = fin - inicio;                      // Longitud desde el símbolo ":" al final de la subcadena 

    subcadena = cadena.substr(inicio, length);  // Resultado -> 4
    aux.second = subcadena;

    bolsillo_personaje.push_back(aux);
}

void predicados::procesar_bolsillos(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "bruja1:4 princesa1:5"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena;

    while(!fin_cadena){
        fin = cadena.find(" ", inicio);             // Encontrar el espacio

        length = fin - inicio;                      // Calcular la longitud de la subcadena

        subcadena = cadena.substr(inicio, length);  // Resultado -> "bruja1:4"

        procesar_bolsillo_personaje(subcadena);     // Procesar "bruja1:4"  
        
        if(fin == string::npos)                     // Comprobar si es el fin de la cadena
            fin_cadena = true;

        else
            inicio = fin+1;
    }
}

void predicados::procesar_jugador(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "player1:20"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena;
    pair<string, string> aux;

    fin = cadena.find(":", inicio);                 // Encontrar el símbolo ":"
    length = fin - inicio;                          // Calcular longitud subcadena

    subcadena = cadena.substr(inicio, length);      // Resultado -> player1
    aux.first = subcadena;

    inicio = fin + 1;
    fin = cadena.find("\n", inicio);                // Encontrar final de la subcadena
    length = fin - inicio;                          // Longitud desde el símbolo ":" al final de la subcadena 

    subcadena = cadena.substr(inicio, length);      // Resultado -> 20
    aux.second = subcadena;

    puntos_jugador.push_back(aux);
}

void predicados::procesar_puntos_jugador(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "player1:20 player2:40"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena;

    while(!fin_cadena){
        fin = cadena.find(" ", inicio);             // Encontrar el espacio

        length = fin - inicio;                      // Calcular la longitud de la subcadena

        subcadena = cadena.substr(inicio, length);  // Resultado -> "player1:20"

        procesar_jugador(subcadena);                // Procesar "player1:20"  
        
        if(fin == string::npos)                     // Comprobar si es el fin de la cadena
            fin_cadena = true;

        else
            inicio = fin+1;
    }
}

string predicados::procesar_zona(string zona){
    /*
    * La cadena es del tipo:
    *
    *    "z1[bruja1-Bruja manzana1-Manzana][Bosque]"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string nombre_zona, objeto_tipo, superficie;

    // Nombre de la zona
    fin = zona.find("[", inicio);                   // Encontrar el símbolo "["
    length = fin - inicio;                          // Longitud desde el inicio hasta el "["
    nombre_zona = zona.substr(inicio, length);      // Resultado -> "z1"

    auto it = find(zonas.begin(), zonas.end(), nombre_zona);  // Si la zona no está repetida

	if(it == zonas.end()){
	    zonas.push_back(nombre_zona);

	    inicio = fin + 1;

	    // Objeto-Tipo
	    fin = zona.find("]", inicio);                   // Encontrar el símbolo "]"
	    length = fin - inicio;                          // Longitud desde el final de antes hasta el "]"
	    objeto_tipo = zona.substr(inicio, length);      // Resultado -> "bruja1-Bruja manzana1-Manzana"

	    if(objeto_tipo != ""){
	        procesar_lineas_objetos(objeto_tipo, nombre_zona);      // Procesar "bruja1-Bruja manzana1-Manzana"
	    }

	    inicio = fin + 2;
	    
	    // Superficie de la zona
	    fin = zona.find("]", inicio);                   // Encontrar el símbolo "]"
	    length = fin - inicio;                          // Longitud desde el final de antes hasta el "]"
	    superficie = zona.substr(inicio, length);       // Resultado -> "Bosque"

	    pair<string, string> aux;
        aux.first = nombre_zona;
        aux.second = superficie;

        zona_superficie.push_back(aux);

	    return nombre_zona;
	}

	return nombre_zona;
}

void predicados::procesar_cadena(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "V -> z1[bruja1-Bruja][Bosque]=10=z3[][Arena]=5=z6[][Piedra]"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena, orientacion_zona;
    vector<string> c, costo;

    while(!fin_cadena){
        fin = cadena.find(" ", inicio);             // Encontrar la posición del espacio

        length = fin - inicio;                      // Longitud desde el inicio hasta el primer espacio

        subcadena = cadena.substr(inicio, length);  // Extraer subcadena

        if(subcadena == "V" || subcadena == "H"){   // Si es V o H, es la orientación de la fila 
            orientacion_zona = subcadena;
        }

        else if(subcadena == "->"){                 // Ignorar flecha
        }

        else if(subcadena != ""){                   // Si no está vacía y no es ninguna de las cosas anteriores, es
                                                    // "z1[bruja1-Bruja][Bosque]=10=z3[][Arena]=5=z6[][Piedra]"

            fin = cadena.find("=", inicio);             // Separar por los "="
                                                        // z1[bruja1-Bruja][Bosque] - 10 - z3[][Arena]=5=z6[][Piedra]
            length = fin - inicio;
            subcadena = cadena.substr(inicio, length);

            if(subcadena.find("[") == string::npos){    // Si la subcadena no contiene un "[", significa que es número del costo
                costo.push_back(subcadena);
            }

            else{
                string nombre_zona = procesar_zona(subcadena);    // Procesar líneas del tipo "z1[bruja1-Bruja][Bosque]"
                c.push_back(nombre_zona);                                 		// Nombre de la zona
            }
        }
        
        if(fin == string::npos)             // Si la cadena ha llegado a su fin
            fin_cadena = true;

        else
            inicio = fin+1;
    }

    // REALIZAR CONEXIONES DE LAS ZONAS Y ARCOS
    pair< pair<string, string>, string > conexion;
    pair< pair<string, string>, string > arco;

    // Primera conexión
    conexion.first.first = c[0];
    conexion.first.second = c[1];
    arco.first.first = c[0];
    arco.first.second = c[1];

    if(orientacion_zona == "V")
        conexion.second = "sur";

    if(orientacion_zona == "H")
        conexion.second = "este";

    arco.second = costo[0];

    conexiones.push_back(conexion);
    arcos.push_back(arco);

    // 1 ... n-1 conexiones
    int i = 1;

    while(i < c.size()-1){
        // Conexión izquierda
        conexion.first.first = c[i];
        conexion.first.second = c[i-1];
        arco.first.first = c[i];
        arco.first.second = c[i-1];

        if(orientacion_zona == "V")
            conexion.second = "norte";

        if(orientacion_zona == "H")
            conexion.second = "oeste";

        arco.second = costo[i-1];

        conexiones.push_back(conexion);
        arcos.push_back(arco);

        // Conexión derecha
        conexion.first.first = c[i];
        conexion.first.second = c[i+1];
        arco.first.first = c[i];
        arco.first.second = c[i+1];

        if(orientacion_zona == "V")
            conexion.second = "sur";

        if(orientacion_zona == "H")
            conexion.second = "este";

        arco.second = costo[i];

        conexiones.push_back(conexion);
        arcos.push_back(arco);

        ++i;
    }

    // Última conexión
    conexion.first.first = c[i];
    conexion.first.second = c[i-1];
    arco.first.first = c[i];
    arco.first.second = c[i-1];

    if(orientacion_zona == "V")
        conexion.second = "norte";

    if(orientacion_zona == "H")
        conexion.second = "oeste";

    arco.second = costo[i-1];

    conexiones.push_back(conexion);
    arcos.push_back(arco);
}

string predicados::procesar_zona_ejercicio1(string zona){
    /*
    * La cadena es del tipo:
    *
    *    "z1[bruja1-Bruja manzana1-Manzana]"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string nombre_zona, objeto_tipo, superficie;

    // Nombre de la zona
    fin = zona.find("[", inicio);                   // Encontrar el símbolo "["
    length = fin - inicio;                          // Longitud desde el inicio hasta el "["
    nombre_zona = zona.substr(inicio, length);      // Resultado -> "z1"

    auto it = find(zonas.begin(), zonas.end(), nombre_zona);  // Si la zona no está repetida

    if(it == zonas.end()){
        inicio = fin + 1;

        // Objeto-Tipo
        fin = zona.find("]", inicio);                   // Encontrar el símbolo "]"
        length = fin - inicio;                          // Longitud desde el final de antes hasta el "]"
        objeto_tipo = zona.substr(inicio, length);      // Resultado -> "bruja1-Bruja manzana1-Manzana"

        if(objeto_tipo != ""){
            procesar_lineas_objetos(objeto_tipo, nombre_zona);      // Procesar "bruja1-Bruja manzana1-Manzana"
        }
    }
    
    return nombre_zona;
}

void predicados::procesar_cadena_ejercicio1(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "V -> z1[bruja1-Bruja] z3[] z6[]"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena, orientacion_zona;
    vector<string> c;

    while(!fin_cadena){
        fin = cadena.find(" ", inicio);             // Encontrar la posición del espacio

        length = fin - inicio;                      // Longitud desde el inicio hasta el primer espacio

        subcadena = cadena.substr(inicio, length);  // Extraer subcadena

        if(subcadena == "V" || subcadena == "H"){   // Si es V o H, es la orientación de la fila 
            orientacion_zona = subcadena;
        }

        else if(subcadena == "->"){                 // Ignorar flecha
        }

        else if(subcadena != ""){                   // Si no está vacía y no es ninguna de las cosas anteriores, es
                                                    // "z1[bruja1-Bruja] z3[] z6[]"

            string nombre_zona = procesar_zona_ejercicio1(subcadena);    // Procesar líneas del tipo "z1[bruja1-Bruja]"
            c.push_back(nombre_zona);                                             // Nombre de la zona

            auto it = find(zonas.begin(), zonas.end(), nombre_zona);  // Si la zona no está repetida
            if(it == zonas.end()){
                zonas.push_back(nombre_zona);
            }
        }
        
        if(fin == string::npos)             // Si la cadena ha llegado a su fin
            fin_cadena = true;

        else
            inicio = fin+1;
    }

    // REALIZAR CONEXIONES DE LAS ZONAS
    pair< pair<string, string>, string > conexion;
    pair< pair<string, string>, string > arco;

    // Primera conexión
    conexion.first.first = c[0];
    conexion.first.second = c[1];

    if(orientacion_zona == "V")
        conexion.second = "sur";

    if(orientacion_zona == "H")
        conexion.second = "este";

    conexiones.push_back(conexion);

    // 1 ... n-1 conexiones
    int i = 1;

    while(i < c.size()-1){
        // Conexión izquierda
        conexion.first.first = c[i];
        conexion.first.second = c[i-1];

        if(orientacion_zona == "V")
            conexion.second = "norte";

        if(orientacion_zona == "H")
            conexion.second = "oeste";

        conexiones.push_back(conexion);

        // Conexión derecha
        conexion.first.first = c[i];
        conexion.first.second = c[i+1];

        if(orientacion_zona == "V")
            conexion.second = "sur";

        if(orientacion_zona == "H")
            conexion.second = "este";

        conexiones.push_back(conexion);

        ++i;
    }

    // Última conexión
    conexion.first.first = c[i];
    conexion.first.second = c[i-1];

    if(orientacion_zona == "V")
        conexion.second = "norte";

    if(orientacion_zona == "H")
        conexion.second = "oeste";

    conexiones.push_back(conexion);
}

void predicados::procesar_cadena_ejercicio2(string cadena){
    /*
    * La cadena es del tipo:
    *
    *    "V -> z1[bruja1-Bruja]=10=z3[]=5=z6[]"
    */

    int inicio = 0;
    int fin;
    int length;
    bool fin_cadena = false;

    string subcadena, orientacion_zona;
    vector<string> c, costo;

    while(!fin_cadena){
        fin = cadena.find(" ", inicio);             // Encontrar la posición del espacio

        length = fin - inicio;                      // Longitud desde el inicio hasta el primer espacio

        subcadena = cadena.substr(inicio, length);  // Extraer subcadena

        if(subcadena == "V" || subcadena == "H"){   // Si es V o H, es la orientación de la fila 
            orientacion_zona = subcadena;
        }

        else if(subcadena == "->"){                 // Ignorar flecha
        }

        else if(subcadena != ""){                   // Si no está vacía y no es ninguna de las cosas anteriores, es
                                                    // "z1[bruja1-Bruja]=10=z3[]=5=z6[]"

            fin = cadena.find("=", inicio);             // Separar por los "="
                                                        // z1[bruja1-Bruja] - 10 - z3[] - 5 - z6[]
            length = fin - inicio;
            subcadena = cadena.substr(inicio, length);

            if(subcadena.find("[") == string::npos){    // Si la subcadena no contiene un "[", significa que es número del costo
                costo.push_back(subcadena);
            }

            else{
                string nombre_zona = procesar_zona_ejercicio1(subcadena);    // Procesar líneas del tipo "z1[bruja1-Bruja]"
                c.push_back(nombre_zona);                                   // Nombre de la zona

                auto it = find(zonas.begin(), zonas.end(), nombre_zona);  // Si la zona no está repetida
                if(it == zonas.end()){
                    zonas.push_back(nombre_zona);
                }
            }
        }
        
        if(fin == string::npos)             // Si la cadena ha llegado a su fin
            fin_cadena = true;

        else
            inicio = fin+1;
    }

    // REALIZAR CONEXIONES DE LAS ZONAS Y ARCOS
    pair< pair<string, string>, string > conexion;
    pair< pair<string, string>, string > arco;

    // Primera conexión
    conexion.first.first = c[0];
    conexion.first.second = c[1];
    arco.first.first = c[0];
    arco.first.second = c[1];

    if(orientacion_zona == "V")
        conexion.second = "sur";

    if(orientacion_zona == "H")
        conexion.second = "este";

    arco.second = costo[0];

    conexiones.push_back(conexion);
    arcos.push_back(arco);

    // 1 ... n-1 conexiones
    int i = 1;

    while(i < c.size()-1){
        // Conexión izquierda
        conexion.first.first = c[i];
        conexion.first.second = c[i-1];
        arco.first.first = c[i];
        arco.first.second = c[i-1];

        if(orientacion_zona == "V")
            conexion.second = "norte";

        if(orientacion_zona == "H")
            conexion.second = "oeste";

        arco.second = costo[i-1];

        conexiones.push_back(conexion);
        arcos.push_back(arco);

        // Conexión derecha
        conexion.first.first = c[i];
        conexion.first.second = c[i+1];
        arco.first.first = c[i];
        arco.first.second = c[i+1];

        if(orientacion_zona == "V")
            conexion.second = "sur";

        if(orientacion_zona == "H")
            conexion.second = "este";

        arco.second = costo[i];

        conexiones.push_back(conexion);
        arcos.push_back(arco);

        ++i;
    }

    // Última conexión
    conexion.first.first = c[i];
    conexion.first.second = c[i-1];
    arco.first.first = c[i];
    arco.first.second = c[i-1];

    if(orientacion_zona == "V")
        conexion.second = "norte";

    if(orientacion_zona == "H")
        conexion.second = "oeste";

    arco.second = costo[i-1];

    conexiones.push_back(conexion);
    arcos.push_back(arco);
}

vector<string> predicados::getZonas(){
    return zonas;
}

vector<string> predicados::getJugadores(){
    return jugadores;
}

vector<string> predicados::getPersonajes(){
    return personajes;
}

vector< pair<string, string> > predicados::getObjetos(){
    return objetos;
}

vector<string> predicados::getBikinis(){
    return bikinis;
}

vector<string> predicados::getZapatillas(){
    return zapatillas;
}

vector<string> predicados::getBrujas(){
    return brujas;
}

vector<string> predicados::getLeonardos(){
    return leonardos;
}

vector<string> predicados::getProfesores(){
    return profesores;
}

vector<string> predicados::getPrincesas(){
    return princesas;
}

vector<string> predicados::getPrincipes(){
    return principes;
}

vector< pair< pair<string, string>, string > > predicados::getConexiones(){
    return conexiones;
}

vector< pair< pair<string, string>, string > > predicados::getArcos(){
    return arcos;
}

vector< pair<string, string> > predicados::getPosicionJugador(){
    return posicion_jugador;
}

vector< pair<string, string> > predicados::getPosicionPersonaje(){
    return posicion_personaje;
}

vector< pair<string, string> > predicados::getPosicionObjeto(){
    return posicion_objeto;
}

vector< pair<string, string> > predicados::getZonaSuperficie(){
    return zona_superficie;
}

vector< pair<string, string> > predicados::getBolsillosPersonajes(){
    return bolsillo_personaje;
}

vector< pair<string, string> > predicados::getPuntosJugadores(){
    return puntos_jugador;
}