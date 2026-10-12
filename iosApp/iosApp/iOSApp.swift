import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        MainViewControllerKt.installUnhandledExceptionLogger()
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
