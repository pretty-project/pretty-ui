
(ns frontend
    (:require ;[sente.api]
              [app-extensions.clients.api]
              [app-extensions.home-screen.api]
              [app-extensions.settings.api]
              [app-extensions.storage.api]
              [app-extensions.trader.api]
              [x.boot-loader.api]
              ; DEBUG
              [playground.api]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app
  ; @param (component) ui-structure
  ;
  ; @usage
  ;  (defn- ui-structure [] [:div ...])
  ;  [app #'ui-structure]
  [ui-structure]
  [ui-structure])
 ;[:div#your-app [ui-structure]]



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!  [] (x.boot-loader.api/start-app!  #'app))
(defn render-app! [] (x.boot-loader.api/render-app! #'app))
