import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ConfigUtils {
    static init(Gradle gradle) {
        addCommonGradle(gradle)
        TaskDurationUtils.init(gradle)
    }
    static getApplyHost(String buildType) {
        GLog.d("getApplyHost...")
        def applyExports = Config.(buildType.toUpperCase() + "_HOST") as Map
        GLog.d("getApplyHost = ${GLog.object2String(applyExports)}")
        return applyExports
    }

    private static addCommonGradle(Gradle gradle) {
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {
                // 在 project 的 build.gradle 前 do sth.
                if (project.subprojects.isEmpty()) {
                    if (project.name == "app") {
                        GLog.l(project.toString() + " applies buildApp.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildApp.gradle"
                        }
                    } else {
                        GLog.l(project.toString() + " applies buildLib.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildLib.gradle"
                        }
                    }
                }
            }

            @Override
            void afterEvaluate(Project project, ProjectState state) {
                // 在 project 的 build.gradle 末 do sth.
            }
        })
    }

    static getApplyPlugins() {
        def plugins = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.plugin.entrySet()) {
            plugins.put(entry.key, entry.value)
        }
        GLog.d("getApplyPlugins = ${GLog.object2String(plugins)}")
        return plugins
    }
}