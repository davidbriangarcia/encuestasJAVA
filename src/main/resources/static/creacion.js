document.addEventListener('DOMContentLoaded', function() {
    let preguntaCounter = 0;

    document.getElementById('btn-agregar-pregunta').addEventListener('click', agregarPregunta);
    document.getElementById('form-encuesta').addEventListener('submit', crearEncuesta);

    function agregarPregunta() {
        preguntaCounter++;
        const divPregunta = document.createElement('div');
        divPregunta.className = 'p-3 mb-3 border rounded';
        divPregunta.id = `pregunta-${preguntaCounter}`;
        divPregunta.innerHTML = `
            <h5>Pregunta ${preguntaCounter}</h5>
            <input type="text" class="form-control mb-2" placeholder="Escribe tu pregunta" required>
            <div class="opciones-container">
                <div class="input-group mb-2">
                    <input type="text" class="form-control" placeholder="Opción 1" required>
                </div>
                 <div class="input-group mb-2">
                    <input type="text" class="form-control" placeholder="Opción 2" required>
                </div>
            </div>
            <button type="button" class="btn btn-sm btn-outline-primary btn-agregar-opcion">Añadir Opción</button>
        `;
        document.getElementById('contenedor-preguntas').appendChild(divPregunta);
        
        divPregunta.querySelector('.btn-agregar-opcion').addEventListener('click', () => agregarOpcion(divPregunta));
    }

    function agregarOpcion(divPregunta) {
        const opcionesContainer = divPregunta.querySelector('.opciones-container');
        const opcionCount = opcionesContainer.children.length + 1;
        const divOpcion = document.createElement('div');
        divOpcion.className = 'input-group mb-2';
        divOpcion.innerHTML = `<input type="text" class="form-control" placeholder="Opción ${opcionCount}" required>`;
        opcionesContainer.appendChild(divOpcion);
    }

    async function crearEncuesta(event) {
        event.preventDefault();
        
        const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        const encuestaData = {
            titulo: document.getElementById('titulo-encuesta').value,
            preguntas: []
        };

        const divsPregunta = document.querySelectorAll('[id^="pregunta-"]');
        divsPregunta.forEach(div => {
            const preguntaTexto = div.querySelector('input[placeholder^="Escribe"]').value;
            const opciones = [];
            div.querySelectorAll('.opciones-container input').forEach(inputOpcion => {
                opciones.push({ texto: inputOpcion.value });
            });
            encuestaData.preguntas.push({ texto: preguntaTexto, opciones: opciones });
        });

        // Aquí sí usas el objeto headers con el token CSRF
        const headers = {
          'Content-Type': 'application/json'
        };
        headers[header] = token;

        try {
            const response = await fetch('/api/encuestas', {
                method: 'POST',
                headers: headers,
                credentials: 'same-origin',
                body: JSON.stringify(encuestaData)
            });

            if (!response.ok) throw new Error('Error al crear la encuesta');

            const data = await response.json();
            console.log(data);

            // Verifica que data.id exista
            if (!data.id) throw new Error('No se recibió el ID de la encuesta');

            mostrarLinks(data.id); // Asegúrate de tener esta función definida
        } catch (error) {
            console.error(error);
            alert('Hubo un error al crear la encuesta.');
        }
    }

    function mostrarLinks(id) {
        const linkVotacion = document.getElementById('link-votacion');
        const linkResultados = document.getElementById('link-resultados');
        const urlBase = window.location.origin;

        linkVotacion.href = `${urlBase}/votacion.html?id=${id}`;
        linkVotacion.textContent = linkVotacion.href;
        
        linkResultados.href = `${urlBase}/resultados.html?id=${id}`;
        linkResultados.textContent = linkResultados.href;
        
        document.getElementById('resultado-creacion').style.display = 'block';
        document.getElementById('form-encuesta').style.display = 'none';
    }

    // Agregar la primera pregunta al cargar
    agregarPregunta();
});