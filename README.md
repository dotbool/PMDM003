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

La aplicación puede clonarse



