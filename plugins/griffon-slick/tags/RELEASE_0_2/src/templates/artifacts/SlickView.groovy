@artifact.package@application(title: '@griffon.project.name@',
  size: [800, 600],
  icon: 'griffon-icon-48x48.png',
  icons: ['griffon-icon-48x48.png',
          'griffon-icon-32x32.png',
          'griffon-icon-16x16.png']) {
    app.game.onRender = { container, g ->
        // render game contents
    }
}
