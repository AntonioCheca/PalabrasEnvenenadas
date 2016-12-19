# Torneo de bots: Palabras Envenenadas

Estas son las reglas del torneo de bots con el juego de las Palabras Envenenadas. Para poder participar basta con subir un programa a la carpeta de Bots. Se dan plantillas de ejemplo para que prácticamente solo se tenga que pensar en el aspecto más importante del bot. 

El objetivo del torneo es poder ver las diferentes aproximaciones al mismo problema por parte de mucha gente de la carrera, así que cuanta más gente participe y más raras sean las ideas que se os ocurran, mejor. Por otro lado, tampoco se quiere que tengáis que leer dos libros de inteligencia artificial para poder participar: no es necesario saber nada aparte de hacer un simple programa en C++ para poder crear el bot.

A continuación se ponen las reglas del juego, y necesitais hacer el código de un bot que juegue él solo. Como aún hay algunas cosas pendientes, es posible que modifique las reglas en poco tiempo. Por ahora, estamos en la versión 1. Si se hacen cambios no serán muy bruscos, así que podéis leerlo con tranquilidad.

## Reglas (Versión 1)

Cada jugador tiene un número fijo de vidas, y empiezan a jugar a las palabras encadenadas. Las reglas de ese juego se mantienen en este:

- No se puede repetir palabra. Si se repite alguna palabra, ese jugador pierde la partida.
- Hay que empezar de la misma forma que acaba la anterior palabra. Para simplificar el juego, se hace con las letras: la palabra que se diga en ese turno debe empezar por la que terminaba la del turno anterior.

Además, se añaden reglas relacionadas con un conjunto de palabras envenenadas:

- Un árbitro (el servidor que hace host del juego) crea una proposición lógica que cumplen algunas palabras y otras no. Al conjunto de palabras que cumplen esta proposición las llamamos *palabras envenenadas*. (En la sección de Aspectos básicos de la implementación se incluye lo que puede elegirse como proposición lógica y lo que no).
- Cada vez que un jugador dice una palabra envenenada, el árbitro le comunica a ese jugador que acaba de ser envenenado, y por tanto pierde una vida.
- Cuando un jugador diga una palabra que no está en el diccionario, que se haya dicho antes o se quede sin vidas, se acaba el juego.

La idea básica es pensar en cómo distinguir palabras envenenadas de palabras no envenenadas, que es a lo que hice referencia antes cuando hablaba del aspecto más importante del bot.

## Creación de un bot

Se presenta un bot de ejemplo en PWClienteEjemplo.java y PWClienteEjemplo.cpp, uno en Java y otro en C++. Los dos hacen exactamente lo mismo, dicen una palabra al azar entre todas las que pueden decir. Si queréis escribir el programa en alguno de estos dos lenguajes, no tenéis más que coger estas plantillas y cambiar cómo se escoge la nueva palabra. Sin embargo, se puede hacer perfectamente en cualquier otro lenguaje, porque al final vuestros programas se comunican con la salida y entrada estándar.

Los programas necesitan:

- Leer el diccionario de palabras listado-general-new.txt. Si cogéis alguna de las dos plantillas, este paso ya lo tenéis hecho.
- Escribir vuestro nick para poder distinguiros dentro del juego. En los ejemplos están puestos Charmander y Squirtle, pero estaría bien que los cambiarais.
- Leer un número. Si es un 1, empezáis vosotros; si es un 0, no empezáis.
- Cuando sea vuestro turno tendréis que leer una palabra, eliminarla del diccionario para no repetirla, y escribir una palabra que empiece por la última letra de la palabra que habéis leído. Si empezáis la partida, la primera palabra que digáis puede ser cualquiera del diccionario. 
- Leer un número. Si es un 1, la palabra que habéis dicho estaba envenenada; si es un 0 no lo estaba. Lo suyo es mantener un vector o un set de las palabras envenenadas, y otro de las que no.
- Repetís vuestro turno hasta que os llega la palabra *GAME OVER*, que indica el fin de la partida. Justo después, se os comunica en otro string cómo habéis quedado: *YOU WIN* si habéis ganado, *YOU LOST* si habéis perdido o *YOU WIN, PERFECT* si habéis ganado con todas las vidas.

Toda la información sobre cómo va la partida se refleja en la terminal en la que lanzáis el servidor, que comunica constantemente el estado de la partida con las vidas de los dos jugadores, la proposición lógica de las palabras envenenadas y el final de la partida.

Una vez creado vuestro programa y compilado, tenéis que meteros en PWWrapper.java y cambiar la línea que pone *Process process = Runtime.getRuntime().exec ("PWClienteEjemplo");* y poner dentro del exec el nombre del ejecutable de vuestro programa. No olvidéis compilar los archivos java con la orden _javac *.java_ en la carpeta donde estén todos los archivos. Para probar que funciona, lanzad el servidor en una terminal con la línea *java PWServidor* y el PWWrapper de vuestro programa con la línea *java PWWrapper*. Necesitais un contrincante, así que podéis lanzar el cliente ejemplo que juega azar con su respectivo PWWrapperClienteEjemplo con la orden *java PWWrapperClienteEjemplo*, o descargaros algún bot que ya haya subido en la carpeta Bots de alguien que ya participe.

Para las pruebas, una vez que empiece a funcionar todo correctamente, conviene que modifiquéis el retraso que mete el servidor en cada turno para que se pueda leer por pantalla, y lo quitéis, y así las partidas se hacen directamente. También es necesario modificar las vidas si queréis probar partidas largas, o las proposiciones si queréis que salgan algunas que habéis pensado vosotros. El código para ver si algo está envenenado está en la función *IsPoisoned* en ProcesadorPW.java, por si lo queréis modificar.

Hecho eso, si vuestro bot funciona, podéis subir el código a la carpeta Bots.


## Aspectos básicos de la implementación

El juego lo mantiene un árbitro, que en este caso es el servidor *PWServidor*. Cuando hay dos jugadores listos para jugar, hace que comience una partida entre ellos, y le deja ese trabajo al *ProcesadorPW*, que se encarga de todo hasta que la partida acaba. Será el que haga un recuento de las vidas, elija la proposición del envenenamiento y hada de enlace entre los dos jugadores. Una vez que la partida acaba, también es el encargado de comunicar si ese jugador ha perdido o ha ganado.

Tanto los clientes (jugadores) como el servidor leen el diccionario del mismo fichero: listado-general-new.txt. He cogido un diccionario de internet y le he quitado los caracteres con tilde y las palabras con ñ, para que no haya problemas en ir caracter a caracter por las palabras para analizarlas. 

Se incluyen solo cuatro posibles proposiciones lógicas al programa servidor como ejemplo básico de lo que puede haber en el torneo. Sin embargo, si supierais todas las posibles proposiciones lógicas que pueden haber, sería tan fácil como comprobar en cuál estáis y evitarla. Para que esto no se pueda hacer, se mantendrán en secreto hasta que llegue el día del torneo. Sí que podemos decir, por ejemplo, que *las proposiciones solo tienen en cuenta los caracteres que componen la palabra*. No hay ninguna proposición que tenga en cuenta la semántica de la palabra, ni la posición en el diccionario ni nada por el estilo. Sí que puede haber otras que digan que una palabra está envenenada si la suma de cada letra sume un total de 100 (con la A siendo el 0 y la Z siendo la 26). Sea cual sea la proposición, debe haber un mínimo y un máximo de palabras envenenadas, para que no haya 

La gracia de poner proposiciones imprevisibles es que las implementaciones que salgan con más puntos sean aquellas que realmente buscan un patrón abstracto en los envenenamientos, más que una serie de *IF/ELSE* con los patrones que creais que voy a poner. 

El programa está hecho por Iñaki Madinabeitia y Antonio Checa, para la asignatura de Fundamentos de Redes, que había que crear un cliente-servidor en Java. 

Contactadme si os surge alguna duda, y eso es todo. ¡Feliz programación, y que la suerte esté siempre, siempre de vuestra parte!
