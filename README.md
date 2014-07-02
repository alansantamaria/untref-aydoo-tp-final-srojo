# Procesador estadístico del uso de Bicicletas.

## Introducción.

A partir de la información de los recorridos de las bicicletas de la ciudad de Buenos Aires [[link](http://data.buenosaires.gob.ar/dataset/bicicletas-publicas)], se desea construir una aplicación para obtener información estadística del uso de las bicicletas.

El analisis de los archivos, provistos en formato ZIP, genera un archivo en formato YAML con los siguientes datos:

* Bicicletas utilizadas más veces.
* Bicicletas utilizadas menos veces.
* Recorridos realizados más veces.
* Tiempo promedio de uso de las bicicletas.

### Enunciado del ejercicio.

Para mas detalles sobre el enunciado del ejercicio, acceda [aquí](https://github.com/tiagox/untref-aydoo-tp-final-srojo/blob/master/docs/enunciado.md).

### Diagramas para entender la implementación.

[Aquí](https://github.com/tiagox/untref-aydoo-tp-final-srojo/blob/master/docs/diagramas.md) puede verse el diagrama de dominio y los diagramas de secuencias de los procesos más importantes.

## Compilación de la aplicación.

Desde el directorio raíz del proyecto, y asumiendo que el sistema cuenta con el JDK necesario y Ant instalados, debemos ejecutar el siguiente comando.

```shell
$ ant empaquetar
```

Esto va a generar el archivo `build/dist/procesador_estadistico.jar`; además de copiar en el mismo directorio el archivo de configuracion de Log4J (Importante: este archivo es necesario para el correcto funcionamiento de la aplicación).

### Mover la aplicación.

En caso de que se desee mover la aplicación a una ubicación diferente, es necesario tener en cuenta que siempre debe estar el archivo `log4j.properties` en el mismo directorio que el archivo `procesador_estadistico.jar`.

## Utilizando la aplicación.

Existen dos modos para ejecutar la aplicación. Por un lado, existe el modo _On Demand_, el cual dado un directorio procesará todos los archivos que este contenga y generará un archivo de estadisticas, finalizando su ejecución. Por otro lado, se puede ejecutar en modo _Daemon_, por lo que la aplicación seguirá escuchando continuamente sobre un directorio y frente a cualquier archivo detectado, lo procesará y generará un archivo de estadisticas por cada uno.

### Modo _On Demand_

Para ejecutar la aplicación en modo _On Demand_ solo basta con proveer un directorio valido que contenga los archivos a analizar. De esta forma, desde el directorio donde compilamos el proyecto vamos a ejecutar:

```shell
$ java -jar build/dist/procesador_estadistico.jar directorio/a/procesar
```

Donde `directorio/a/procesar` es la ruta (absoluta o relativa) donde se encuentran los archivos Zip a procesar.

### Modo _Daemon_

La ejecución en modo _Daemon_ solo requiere un argumento adicional. De la siguiente manera se le indica a la aplicación que monitoree un directorio y que procese en caso de que encuentre algún archivo Zip.

```shell
$ java -jar build/dist/procesador_estadistico.jar -d directorio/a/procesar
```

Con el argumento `-d` se indica el nuevo modo de funcionamiento. Es importante respetar el orden de los argumentos.

### Optimizacion del tiempo insumido para el procesamiento (segunda parte del trabajo practico)
La aplicacion maneja distintos hilos para cada uno de los archivos .csv que van a ser procesados, optimizando el tiempo de procesamiento. Sin embargo el proceso de calculo demoraba demasiado tiempo. Luego de indagar, me di cuenta que al descomprimir el zip, cada archivo que iba extrayendo se iba incrementando muy gradualmente, lo que dilataba mucho la operacion. Identifique el metodo encargado puntualmente de realizar dicha tarea y descubri que se estaba untilizando el metodo read() de la clase ZipInputStream. El mismo si no recibe parametros, arma el archivo que se descomprime byte a byte. La solucion a esto, es enviarle como parametro un vector de bytes de un tamaño mayor a modo de buffer:

codigo viejo:
	int chunk;
	while ((chunk = zis.read()) != -1) {
		os.write(chunk);
	}

codigo nuevo:
	int readed;
	byte [] buffer = new byte[1024];
	while (0 < (readed = zis.read(buffer))){
		os.write(buffer,0, readed);
	}

Una vez implementado esto, los tiempos de procesamiento cayeron drasticamente.
Las pruebas se realizaron con un archivo zip que contenia tres arhivos .csv de 60 MB cada uno (aproximadamente 1 millon de registros cada uno). Los tiempos previos a la modificacion rondaban los 18 minutos en tren intentos.
A partir de los nuevos cambios la aplicacion es capaz de procesar la misma informacion en un promedio de 11 segundos en tres intentos.
Para medir el tiempo utilize el propio log de la aplicacion, que almacena el horario preciso de arranque y finalizacion de ejecucion.

