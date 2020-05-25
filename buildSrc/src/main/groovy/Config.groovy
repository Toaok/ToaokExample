class Config {
    /**
     * app version config info
     */
    static applicationId = 'indi.toaok.example'
    static appName = 'Example'
    static compileSdkVersion = 29
    static minSdkVersion = 24
    static targetSdjVersion = 29
    static versionCode = 1_0_0
    static versionName = '1.0.0' // E.g. 1.9.72 => 1,009,072
    static flavorDimensions = 'default'

    /**
     * plugin version
     */
    static gradle_version = '3.6.3'
    //https://github.com/JakeWharton/butterknife
    static butterknife_version = '10.2.0'

    /**
     * base library version info
     */
    static kotlin_version = '1.3.71'
    //https://github.com/kotlin/kotlinx.coroutines/blob/master/README.md#using-in-your-projects
    static kotlin_coroutines_version = '1.3.7'
    //android library
    static appcompat_version = '1.0.0'
    static material_version = '1.0.0'
    static corektx_version = '1.2.0'
    static constraintlayout_version = '1.1.3'
    //test kibrary
    static junit_version = '4.12'
    static runner_version = '1.1.0'
    static espresso_core_version = '3.1.0'
    //multidex
    static multidex_version = '2.0.0'


    /**
     *  appConfig 配置的是可以跑 app 的模块，git 提交务必只包含 launcher
     */
    static appConfig = ['launcher']

    /**
     * pkgConfig 配置的是要依赖的功能包，为空则依赖全部，git 提交务必为空
     */
    static pkgConfig = []

    static depConfig = [
            //插件库
            plugin : [
                    gradle     : new DepConfig("com.android.tools.build:gradle:$gradle_version"),
                    kotlin     : new DepConfig("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"),
                    butterknife: new DepConfig("com.jakewharton:butterknife-gradle-plugin:$butterknife_version")
            ],

            //Android 支持库
            android: [
                    appcompat       : new DepConfig("androidx.appcompat:appcompat:$appcompat_version"),
                    material        : new DepConfig("com.google.android.material:material:$material_version"),
                    constraintlayout: new DepConfig("androidx.constraintlayout:constraintlayout:$constraintlayout_version"),
                    multidex        : new DepConfig("androidx.multidex:multidex:$multidex_version"),
                    corektx         : new DepConfig("androidx.core:core-ktx:$corektx_version"),
            ],
            //kotlin
            kotlin : [
                    standard_library  : new DepConfig("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"),
                    coroutines_core   : new DepConfig("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version"),
                    coroutines_android: new DepConfig("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_version"),
            ],

            //test
            test   : [
                    junit        : new DepConfig("junit:junit:$junit_version"),
                    runner       : new DepConfig("androidx.test:runner:$runner_version"),
                    espresso_core: new DepConfig("androidx.test.espresso:espresso-core:$espresso_core_version"),
            ]

    ]


    /**
     * 正式地址
     * 此处的命名与buildType对应
     * 命名格式为($buildType(大写)+"_HOST")
     */
    static RELEASE_HOST = [
            //接口地址
            API       : "http://154.213.25.14:8080",
            //文件服务器地址
            FileServer: "http://154.213.25.14",
    ]
    /**
     * 测试地址
     *
     */
    static DEBUG_HOST = [
            API       : "http://47.97.107.232:9090",
            FileServer: "http://47.97.107.232",
    ]


}