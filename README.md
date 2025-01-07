# PMDM003

## Introducción

El  propósito de la aplicación es mostrar una colección de pokemons para que puedan ser capturados y almacenados en FireBase.
El modo de funcionamiento dista un poco del objetivo inicial propuesto en la tarea. En ésta se indica que tras un click debe generarse una captura. En esta app, pueden seleccionarse varios pokemons que no serán capturados
hasta que se pulse un botón. 
La app trabaja sólo con una lista de pokemons y lo que se muestra en cada vista depende del atributo state de cada objeto pokemon. Así en la vista pokedex se muestran todos los pokemons y el color del objeto que soporta el 
dato depende del state del polemon. En la vista pokemon  y en details sólo se muestran aquellos cuyo state es CAPTURED.

## Características principales

Para utililzar la aplicación es preciso autenticarse contra Firebase. Para ello será necesario un email y una contraseña
Una vez autenticados, la aplicación despliega 3 pestañas:   

Pokedex, una lista de 150 pokemons que pueden ser capturados
Mis Pokemons, una lista con aquellos que fueron capturados
Settings, un espacio en el que establecer una configuración. La configuración admite el cambio de idioma, entre Inglés y Castellano y el bloqueo de la eliminación de los pokemons capturados


## Aspectos pendientes de Revisión

#### SharedPreferences en diferentes dispositivos:

La aplicación no tiene en cuenta que un usuario pueda logarse en un dispositivo diferente en el que no exista un archivo de preferencias guardado. Si el archivo de preferencias no tuviese nada guardado
habría que preguntar a la base de datos si hay algo, en cuyo caso habría que hacer ese fecth para cargar el estado de la ui.

#### Pantalla de loading para operaciones en backGround:

La aplicación no tiene una pantalla de "loading" que sirva de transición entre fragmentos cuando hay operaciones de escritura. Estas operaciones se hacen en background con o sin errores. Puede
ocurrir que un usuario capture 18 pokemons al mismo tiempo y que inmediatamente decida ir a los pokemons capturados. No se mostrarán en esta pantalla los pokemons cuyas operaciones no se hayan 
realizado aún

#### Landscape de pantalla Settings Y libreria androidx Preference

La pantalla de settings se hizo al principio del desarrollo de la app y no se ha utilizado la librería que se aconseja para este componente. Además no se ha diseñado un layout para cuando 
el dispositivo está en forma apaisada y aunque son escasos los elementos que se muestra, el botón de logout no se visualiza al completo. La solución consiste o en  un diseño específico para
el landscape o un scrolllayout


## Tecnologías utilizadas

El acceso a la API de la que se hace el fecth inicial de Pokemons se ha hecho con la librería Retrofit. Se le ha añadido un adaptador para que transforme el response en un tipo Observable de RXjava
El almacenamiento y gestión de los pokemons capturados se ha realizado a través de FireBase y dentro de esta tecnología, se ha usado Authentication y FireStoreDatabase.
El despliegue de la información se realiza a través del componente RecyclerView.
Las imágenes son tratadas con la librería Glide


## Instrucciones de uso

La aplicación puede desplegarse desde el IDE de Android Studio. Para clonar el repositorio: 

1. Es preciso copiar la dirección del mismo que puede encontrarse en este mismo repositorio en la pestaña coloreada de verde: 'code'
2. Una vez copiada podemos ir al IDE y en File -> new -> Project from version control -> clone
3. Será preciso instalar algún emulador. Puede hacerse en el IDE en: Menu -> tools- device manager. En la pestaña desplegada podemos agregar alguno pulsando el símbolo +, create virtual device y asignándole la versión de android preferida
4. Una vez instalado el IDE compila y ejecuta la app pulsando el 'play'


## Conclusiones del desarrollador

La carencia total de conocimiento relacionado con el framework de Android ha convertido el desarrollo de esta sencilla aplicación en una tarea exigente. Como se desconoce el funcionamiento de todos los componentes se ha tenido que realizar lectura y testeo de todo lo que se ha implementado. Y como es la primera vez, habrá que repetirlo muchas más veces para que el conocimiento que se pretende se consolide. Se ha tenido en cuenta para la construcción de la app, los ciclos de vida de actividades y fragmentos. Se ha intentado construir de forma sencilla, buscando crear la menor cantidad posible de objetos en la idea de que la aplicación pueda funcionar de forma fluida. Se ha explorado la necesidad de crearla con más de un hilo para que las peticiones de datos no interfieran con la interfaz del usuario.




