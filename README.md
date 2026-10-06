# Kallpa WhatsApp API (Java / Quarkus)

API REST que recibe un mensaje y una lista de negocios en JSON. Devuelve un reporte JSON con la misma envoltura de respuesta usada por el servicio de origen (`statusCode`, `message`, `data`, `errorCode`, `details`).

## Estado de esta primera etapa

Esta versión **prepara mensajes, pero no los envía**. Un negocio autorizado se devuelve con `estado: "PREPARADO"`; uno no autorizado se devuelve como `OMITIDO`. No se automatiza WhatsApp Web. Para habilitar envíos reales se debe conectar un proveedor autorizado y registrar el consentimiento del destinatario.

`autorizado` es obligatorio en cada negocio. WhatsApp indica que solo se puede contactar a una persona si dio su número y expresó su consentimiento para recibir mensajes de la empresa por WhatsApp; también deben respetarse sus solicitudes para dejar de recibirlos. [Política de mensajes de WhatsApp Business](https://business.whatsapp.com/policy/preview?lang=es_LA)

El saludo personalizado siempre utiliza `razonSocial`.

## Requisitos

- JDK 21
- Maven 3.9+
- Quarkus 3.40.1 LTS

Quarkus REST con Jackson procesa JSON y Hibernate Validator valida los datos de entrada. Quarkus recomienda usar la última versión LTS para producción. [Versiones de Quarkus](https://quarkus.io/releases/)

## Ejecutar localmente

Con JDK 21 y Maven instalados, ejecuta desde `Java_Version`:

```powershell
mvn quarkus:dev
```

La API escucha en `http://localhost:8080`.

## Endpoint

`POST /api/v1/notificaciones/whatsapp`

### Request

```json
{
  "mensaje": "Tenemos una promoción para ti. Vuelve a Kallpa y te damos un mes gratis",
  "negocios": [
    {
      "ruc": "20614685353",
      "razonSocial": "DISTRIBUIDORA DE GOLOSINAS IMPORTADAS/NACIONALES JTC E.I.R.L.",
      "celular": "947241291",
      "autorizado": true,
      "fechaCulminacion": "2026-11-01T05:03:14.537+00:00"
    }
  ]
}
```

`razonSocial` se usa en el saludo. `autorizado` es obligatorio: si es `false`, el mensaje se omite. La fecha puede ser `null` o una cadena ISO-8601, y se conserva en la respuesta.

### Response

```json
{
  "statusCode": 200,
  "message": "Se procesó 1 negocio; no se realizó ningún envío.",
  "data": {
    "cantidad": 1,
    "negocios": [
      {
        "ruc": "20614685353",
        "razonSocial": "DISTRIBUIDORA DE GOLOSINAS IMPORTADAS/NACIONALES JTC E.I.R.L.",
        "celular": "947241291",
        "autorizado": true,
        "fechaCulminacion": "2026-11-01T05:03:14.537+00:00",
        "estado": "PREPARADO",
        "mensaje": "Hola, DISTRIBUIDORA DE GOLOSINAS IMPORTADAS/NACIONALES JTC E.I.R.L. Te saludamos de Kallpa Facturación.\n\nTenemos una promoción para ti. Vuelve a Kallpa y te damos un mes gratis",
        "detalle": "Mensaje preparado; no se envió. No hay un proveedor de mensajería configurado."
      }
    ]
  },
  "errorCode": null,
  "details": null
}
```

La API acepta hasta 1000 negocios por solicitud, valida RUC de 11 dígitos y celular peruano de 9 dígitos (opcional prefijo `51`), y rechaza RUC duplicados. Las solicitudes inválidas devuelven el mismo sobre con `data: null`, un `errorCode` y `details`.

## Arquitectura

- `NotificationResource`: endpoint REST y validaciones de la lista.
- `MessagePreparationService`: personalización del saludo y creación del estado por negocio.
- DTOs: contratos JSON de entrada y salida.
- `ValidationExceptionMapper`: errores de validación con el mismo formato de respuesta.

Antes de exponerla fuera de un entorno local, agrega autenticación y límites de solicitudes. Para habilitar envíos, integra un canal autorizado y agrega persistencia para solicitudes de baja y prevención de duplicados si el cliente reintenta un request.
