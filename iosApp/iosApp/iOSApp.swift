import SwiftUI
import Foundation
import ComposeApp

@main
class iOSApp: UIResponder,UIApplicationDelegate {
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)

        let uc = MainKt.ViewController()

        window?.rootViewController = uc
        window?.makeKeyAndVisible()

        return true
    }

}
