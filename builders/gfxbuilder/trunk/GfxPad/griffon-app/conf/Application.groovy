application {
    title='GfxPad'
    startupGroups = ['root']

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "GfxPad"
    root {
        model = 'MainModel'
        view = 'MainView'
        controller = 'MainController'
    }
}