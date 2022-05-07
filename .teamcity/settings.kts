

version = "2022.04"

project {

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }
    
    steps {
        script {
            name = "Install npm packages"
            scriptContent = """ npm install """
        }
        
        scrpt {
            name = "Run tests"
            scriptContent = """ npm run verify """
            
        }
    }
    
    triggers {
        vcs {
        }
    }
})
