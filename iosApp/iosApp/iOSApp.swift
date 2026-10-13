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
                .onOpenURL { url in
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}
