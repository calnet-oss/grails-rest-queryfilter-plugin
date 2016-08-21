class RestQueryfilterGrailsPlugin {
    def group = "edu.berkeley.calnet.grails.plugins"

    // the plugin version
    def version = "1.0.0-SNAPSHOT" // !!! Change in build.gradle too
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "REST query filter Plugin" // Headline display name of the plugin
    def author = "Brian Koehmstedt"
    def authorEmail = "bkoehmstedt@berkeley.edu"
    def description = '''\
Handle query filter strings for REST queries
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/calnet-oss/grails-rest-queryfilter-plugin/"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "BSD"

    // Details of company behind the plugin (if there is one)
    def organization = [name: "University of California, Berkeley", url: "http://www.berkeley.edu/"]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [system: "GitHub", url: "https://github.com/calnet-oss/grails-rest-queryfilter-plugin/issues"]

    // Online location of the plugin's browseable source code.
    def scm = [url: "https://github.com/calnet-oss/grails-rest-queryfilter-plugin"]

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}
