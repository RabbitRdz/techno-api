-- Creación de tablas

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE marcas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais_origen VARCHAR(100)
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

CREATE TABLE personas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    apellido VARCHAR(120),
    fecha_nacimiento DATE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_baja TIMESTAMP NULL,
    permite_marketing BOOLEAN DEFAULT TRUE,
    permite_tratamiento_datos BOOLEAN DEFAULT TRUE
);

CREATE TABLE solicitudes_arco (
    id SERIAL PRIMARY KEY,
    id_persona INT NOT NULL,
    tipo_derecho VARCHAR(20) NOT NULL,
    motivo TEXT,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ATENDIDA',
    CONSTRAINT fk_solicitud_arco_persona FOREIGN KEY (id_persona) REFERENCES personas(id),
    CONSTRAINT chk_solicitud_arco_tipo CHECK (
        tipo_derecho IN ('ACCESO', 'RECTIFICACION', 'CANCELACION', 'OPOSICION')
    )
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    id_persona INT NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    hash_seguridad VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_persona FOREIGN KEY (id_persona) REFERENCES personas(id)
);

CREATE TABLE contacto (
    id SERIAL PRIMARY KEY,
    id_persona INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    valor VARCHAR(255) NOT NULL,
    CONSTRAINT fk_contacto_persona FOREIGN KEY (id_persona) REFERENCES personas(id)
);

CREATE TABLE usuarios_roles (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuariorol_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    CONSTRAINT fk_usuariorol_rol FOREIGN KEY (id_rol) REFERENCES roles(id)
);

CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    id_categoria INT NOT NULL,
    id_marca INT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id),
    CONSTRAINT fk_producto_marca FOREIGN KEY (id_marca) REFERENCES marcas(id)
);

CREATE TABLE ordenes (
    id SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_orden TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(50),
    CONSTRAINT fk_orden_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    id_orden INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago TIMESTAMP NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT fk_pago_orden FOREIGN KEY (id_orden) REFERENCES ordenes(id)
);

CREATE TABLE detalles_orden (
    id SERIAL PRIMARY KEY,
    id_orden INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_orden FOREIGN KEY (id_orden) REFERENCES ordenes(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES productos(id)
);

CREATE TABLE resenas (
    id SERIAL PRIMARY KEY,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    calificacion INT,
    comentario TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resena_producto FOREIGN KEY (id_producto) REFERENCES productos(id),
    CONSTRAINT fk_resena_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
