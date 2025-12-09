# E-Commerce Android App

Aplicación móvil de comercio electrónico desarrollada para Android que permite a los usuarios explorar productos, gestionar categorías y realizar compras. La aplicación incluye un sistema de roles con permisos de administrador para la gestión de productos y categorías.

## 📱 Características

### Funcionalidades Principales

- **Autenticación de Usuarios**
  - Registro de nuevos usuarios
  - Inicio de sesión con Firebase Authentication
  - Recuperación de contraseña
  - Gestión de perfil de usuario

- **Gestión de Categorías** (Solo Administradores)
  - Crear nuevas categorías
  - Editar categorías existentes
  - Eliminar categorías
  - Visualizar todas las categorías

- **Gestión de Productos** (Solo Administradores)
  - Agregar productos con imágenes
  - Editar productos existentes
  - Eliminar productos
  - Asociar productos a categorías
  - Subir múltiples imágenes por producto

- **Catálogo de Productos**
  - Visualización de productos por categoría
  - Búsqueda de productos
  - Detalles completos de productos
  - Galería de imágenes de productos

- **Carrito de Compras**
  - Agregar productos al carrito
  - Gestionar cantidades
  - Persistencia local del carrito
  - Sincronización con Firebase

- **Interfaz de Usuario**
  - Navegación con Navigation Component
  - Drawer Navigation
  - Material Design
  - Soporte para modo oscuro
  - ViewBinding para mejor rendimiento

## 🛠️ Tecnologías Utilizadas

### Framework y Lenguaje
- **Java** - Lenguaje de programación
- **Android SDK** - Plataforma de desarrollo
- **Gradle** - Sistema de construcción

### Bibliotecas Principales
- **Firebase**
  - Firebase Authentication - Autenticación de usuarios
  - Cloud Firestore - Base de datos NoSQL
  - Firebase Storage - Almacenamiento de imágenes
  - Firebase Realtime Database - Base de datos en tiempo real

- **Android Jetpack**
  - Navigation Component - Navegación entre pantallas
  - ViewModel - Gestión del ciclo de vida de datos
  - LiveData - Datos observables
  - ViewBinding - Binding de vistas

- **Material Design**
  - Material Components - Componentes de UI modernos

- **Utilidades**
  - Gson - Serialización JSON
  - Jackson - Procesamiento JSON
  - Shimmer Library - Efectos de carga

## 📋 Requisitos del Sistema

- **Android Studio** - Arctic Fox o superior
- **JDK** - Versión 8 o superior
- **Android SDK**
  - Min SDK: 24 (Android 7.0 Nougat)
  - Target SDK: 32 (Android 12L)
  - Compile SDK: 32
- **Gradle** - Versión 7.2.0 o superior
- **Cuenta de Firebase** - Configuración del proyecto Firebase

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd E-Commerce
```

### 2. Configurar Firebase

1. Crea un nuevo proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Agrega una aplicación Android al proyecto
3. Descarga el archivo `google-services.json`
4. Coloca el archivo en `app/google-services.json`

### 3. Configurar Permisos

La aplicación requiere los siguientes permisos (ya configurados en el AndroidManifest.xml):
- `CAMERA` - Para tomar fotos de productos
- `READ_EXTERNAL_STORAGE` - Para seleccionar imágenes de la galería
- `ACCESS_MEDIA_LOCATION` - Para acceder a la ubicación de medios

### 4. Sincronizar el Proyecto

1. Abre el proyecto en Android Studio
2. Espera a que Gradle sincronice las dependencias
3. Asegúrate de que todas las dependencias se descarguen correctamente

### 5. Ejecutar la Aplicación

1. Conecta un dispositivo Android o inicia un emulador
2. Haz clic en "Run" o presiona `Shift + F10`
3. La aplicación se instalará y ejecutará automáticamente

## 📁 Estructura del Proyecto

```
E-Commerce/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pucmm/e_commerce/
│   │   │   │   ├── database/          # Modelos de datos
│   │   │   │   │   ├── Category.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── CarritoCompras.java
│   │   │   │   │   └── ProductImage.java
│   │   │   │   ├── models/            # ViewModels
│   │   │   │   │   ├── CategoryViewModel.java
│   │   │   │   │   └── ProductViewModel.java
│   │   │   │   ├── repositories/      # Repositorios de datos
│   │   │   │   │   ├── FirebaseRepository.java
│   │   │   │   │   └── LocalRepository.java
│   │   │   │   └── ui/                # Actividades y Fragmentos
│   │   │   │       ├── LoginActivity.java
│   │   │   │       ├── RegisterActivity.java
│   │   │   │       ├── MainActivity.java
│   │   │   │       ├── ChargeActivity.java
│   │   │   │       ├── ForgetPasswordActivity.java
│   │   │   │       ├── HomeFragment.java
│   │   │   │       ├── CategoryFragment.java
│   │   │   │       ├── ProductFragment.java
│   │   │   │       ├── DetailsProductFragment.java
│   │   │   │       ├── RegisterCategoryFragment.java
│   │   │   │       └── RegisterProductFragment.java
│   │   │   ├── res/                   # Recursos
│   │   │   │   ├── layout/            # Layouts XML
│   │   │   │   ├── drawable/          # Iconos y drawables
│   │   │   │   ├── values/            # Strings, colors, themes
│   │   │   │   └── navigation/        # Navigation graph
│   │   │   └── AndroidManifest.xml
│   │   └── test/                      # Tests unitarios
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## 🎯 Funcionalidades Detalladas

### Sistema de Autenticación

La aplicación utiliza Firebase Authentication para gestionar usuarios:
- Registro con email y contraseña
- Inicio de sesión seguro
- Recuperación de contraseña por email
- Gestión de sesión persistente

### Roles de Usuario

- **Usuario Regular**: Puede ver productos, agregar al carrito y gestionar su perfil
- **Administrador**: Acceso completo incluyendo gestión de categorías y productos

### Gestión de Productos

- Cada producto puede tener:
  - Código único (UUID)
  - Descripción
  - Precio
  - Múltiples imágenes
  - Asociación a una categoría

### Gestión de Categorías

- Cada categoría contiene:
  - ID único (UUID)
  - Nombre
  - Imagen representativa
  - Lista de productos asociados

### Carrito de Compras

- Almacenamiento local usando SharedPreferences
- Sincronización con Firebase
- Gestión de cantidades por producto
- Persistencia entre sesiones

## 🔧 Configuración de Firebase

### Estructura de Firestore

```
Firestore/
├── Users/
│   └── {userId}/
│       ├── name: String
│       ├── email: String
│       ├── user: String
│       ├── telephoneNumber: String
│       ├── admin: Boolean
│       └── imagen: String
├── Categories/
│   └── {categoryId}/
│       ├── id: String
│       ├── nombre: String
│       ├── imagen: String
│       └── productList: Array<Product>
└── Compras/
    └── {compraId}/
        ├── id: String
        ├── userID: String
        └── productArrayList: HashMap<String, Integer>
```

### Estructura de Storage

```
Storage/
└── images/
    ├── {userId}.jpg          # Imágenes de perfil
    ├── {categoryId}.jpg      # Imágenes de categorías
    └── {productImageId}.jpg  # Imágenes de productos
```

## 🧪 Testing

El proyecto incluye:
- Tests unitarios en `app/src/test/`
- Tests de instrumentación en `app/src/androidTest/`

Para ejecutar los tests:
```bash
./gradlew test          # Tests unitarios
./gradlew connectedAndroidTest  # Tests de instrumentación
```

## 📝 Notas de Desarrollo

### Arquitectura

La aplicación sigue una arquitectura basada en:
- **Repository Pattern**: Separación de lógica de datos
- **ViewModel**: Gestión del estado de la UI
- **LiveData**: Observación de cambios en datos
- **Singleton Pattern**: Para repositorios compartidos

### Mejores Prácticas Implementadas

- Uso de ViewBinding para evitar `findViewById`
- Navigation Component para navegación tipo-safe
- Separación de responsabilidades (UI, Lógica, Datos)
- Uso de Firebase para backend como servicio
- Persistencia local para mejor UX

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

## 👥 Autores

- **PUCMM** - Desarrollo inicial

## 🙏 Agradecimientos

- Firebase por proporcionar una plataforma robusta de backend
- Android Jetpack por las herramientas de desarrollo
- Material Design por los componentes de UI

## 📞 Soporte

Para reportar problemas o sugerencias, por favor abre un issue en el repositorio.

---

**Versión**: 1.0  
**Última actualización**: 2024


