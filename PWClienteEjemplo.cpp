#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <cstdlib>

using namespace std;

vector<string> PalabrasQueEmpiecenPor(const vector<string>& diccionario, char x){
  vector<string> palabras;
  for(int i=0; i<diccionario.size();i++)
    if( diccionario[i][0] == x )
      palabras.push_back(diccionario[i]);
  return palabras;
}

int main(){
  srand(time(NULL));  //Inicializamos el generador de números aleatorios
  string name = "Squirtle";
  vector<string> diccionario;

  string buferEnvio, buferRecepcion;
  ifstream file_dic("listado-general-new.txt");
  if(!file_dic){
    cerr << "Error al abrir el archivo del diccionario\n";
    return 0;
  }

  //Leemos el diccionario
  string word;
  while(getline(file_dic, word)){
    diccionario.push_back(word);
  }
  file_dic.close();

  cout << name << "\n"; //Decimos nuestro nombre
  getline(cin, buferRecepcion);
  if(buferRecepcion == "1"){
    cout << "alabar\n"; //Si empezamos, decimos alabar
    getline(cin, buferRecepcion);
    //Como este cliente es muy básico, no hace nada lea o no que alabar está envenenada
  }

  bool game_on = true;
  while(game_on && cin.good()){
    getline(cin, buferRecepcion);
    if(buferRecepcion == "GAME OVER"){
      game_on = false;
    }
    else{
      char ultimaLetra = buferRecepcion[buferRecepcion.size() -1];
      //Borramos la palabra del diccionario
      diccionario.erase(find(diccionario.begin(), diccionario.end(), buferRecepcion));

      vector<string> palabras = PalabrasQueEmpiecenPor(diccionario, ultimaLetra);
      //Coges las posibles palabras que puedes decir, y coge una al azar
      if(palabras.size() > 0){
        int rand_int = rand()%palabras.size();
        buferEnvio = palabras[rand_int];
        diccionario.erase(find(diccionario.begin(), diccionario.end(), palabras[rand_int]));
      }
      else
        buferEnvio = "_NOMOREWORDS";	//Cuando no tenemos nada que decir

      cout << buferEnvio << "\n"; //Enviamos la palabra

      // Leemos si está o no envenenada
      getline(cin, buferRecepcion);
    }
  }
  //Vemos si hemos ganado o no
  //Las posibilidades son YOU WIN, YOU LOST o YOU WIN, PERFECT
  getline(cin, buferRecepcion);
}
