package com.example.data.quotes

data class Quote(
    val text: String,
    val author: String,
    val category: String = "Disciplina"
)

object MotivationalQuotes {
    val quotes = listOf(
        Quote("La disciplina tarde o temprano vencerá a la inteligencia.", "Kenji Yokoi"),
        Quote("No cuentes los días, haz que los días cuenten.", "Muhammad Ali"),
        Quote("El éxito es la suma de pequeños esfuerzos repetidos día tras día.", "Robert Collier"),
        Quote("Tu yo del futuro te agradecerá lo que decidas estudiar hoy.", "Anónimo"),
        Quote("La motivación te hace empezar; el hábito te hace continuar.", "Jim Ryun"),
        Quote("Un día o el día uno. Tú decides.", "Paulo Coelho"),
        Quote("La constancia convierte lo ordinario en extraordinario.", "Anónimo"),
        Quote("No tienes que ser grande para empezar, pero tienes que empezar para ser grande.", "Zig Ziglar"),
        Quote("El dolor de la disciplina pesa gramos; el dolor del arrepentimiento pesa toneladas.", "Jim Rohn"),
        Quote("Cada página leída y cada problema resuelto te acerca a tu meta.", "Anónimo"),
        Quote("Lo que hoy parece un sacrificio, mañana será tu mayor orgullo.", "Anónimo"),
        Quote("Sé más fuerte que tus excusas de hoy.", "Anónimo"),
        Quote("Las grandes obras no se realizan por la fuerza, sino por la perseverancia.", "Samuel Johnson"),
        Quote("Estudia mientras otros duermen; trabaja mientras otros descansan; vivirás lo que otros solo sueñan.", "Anónimo"),
        Quote("La excelencia no es un acto fortuito, es un hábito diario.", "Aristóteles"),
        Quote("La única mala sesión de estudio es la que no ocurrió.", "Anónimo"),
        Quote("El secreto de avanzar es comenzar. Hoy ya diste el primer paso.", "Mark Twain"),
        Quote("No mires el reloj; haz lo que él hace: sigue adelante.", "Sam Levenson"),
        Quote("La acción elimina la duda. Ponte a estudiar y la claridad llegará.", "Anónimo"),
        Quote("Los resultados que buscas están en los días que no tienes ganas.", "Anónimo"),
        Quote("Siembra hoy el conocimiento que cosecharás toda la vida.", "Anónimo"),
        Quote("Tu cerebro es un músculo: cada sesión de estudio lo vuelve imparable.", "Anónimo"),
        Quote("No bajes el ritmo ahora, tu racha es tu mayor trofeo.", "Anónimo"),
        Quote("El conocimiento es la única inversión que nunca pierde valor.", "Benjamin Franklin"),
        Quote("Un paso a la vez, una sesión a la vez, un día a la vez.", "Anónimo"),
        Quote("La pereza viaja tan despacio que la pobreza pronto la alcanza.", "Benjamin Franklin"),
        Quote("No pares cuando estés cansado, para cuando hayas terminado tu meta de hoy.", "David Goggins"),
        Quote("El talento sin disciplina es como un cohete sin combustible.", "Anónimo"),
        Quote("Hoy ganaste la batalla contra la procrastinación. ¡Sigue así!", "Anónimo"),
        Quote("Tu racha demuestra quién eres cuando nadie te está mirando.", "Anónimo"),
        Quote("Cree en ti mismo y todo lo que eres. Eres capaz de dominar cualquier tema.", "Christian D. Larson"),
        Quote("La energía fluye hacia donde va tu atención. Hoy tu atención estuvo en tu futuro.", "Anónimo"),
        Quote("Cada día que estudias amplías tus posibilidades en el mundo.", "Nelson Mandela"),
        Quote("El foco es saber decir que no a las distracciones.", "Steve Jobs"),
        Quote("La consistencia te lleva más lejos que cualquier golpe de suerte.", "Anónimo")
    )

    private var lastIndex: Int = -1

    fun getRandomQuote(): Quote {
        var newIndex: Int
        do {
            newIndex = quotes.indices.random()
        } while (newIndex == lastIndex && quotes.size > 1)
        lastIndex = newIndex
        return quotes[newIndex]
    }
}
