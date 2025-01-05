# PMDM003

## Introducción

El  propósito de la aplicación es mostrar una colección de pokemons para que puedan ser capturados y almacenados en FireBase

## Características principales

Para utililzar la aplicación es preciso autenticarse contra Firebase. Para ello será necesario un email y una contraseña
Una vez autenticados, la aplicación despliega 3 pestañas:   

Pokedex, una lista de 150 pokemons que pueden ser capturados
Mis Pokemons, una lista con aquellos que fueron capturados
Settings, un espacio en el que establecer una configuración. La configuración admite el cambio de idioma, entre Inglés y Castellano y el bloqueo de la eliminación de los pokemons capturados


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




