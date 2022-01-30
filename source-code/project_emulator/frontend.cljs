
(ns project-emulator.frontend
    (:require ;[sente.api]
              [x.boot-loader]
              [app-extensions.clients.api]
              [app-extensions.home.api]
              [app-extensions.media.api]
              [app-extensions.products.api]
              [app-extensions.settings.api]
              [app-extensions.storage.api]
              [app-extensions.trader.api]
              ; TEMP
              [playground.api]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app
  ; @param (component) ui-structure
  ;
  ; @usage
  ;  (defn- ui-structure [] [:div ...])
  ;  [app #'ui-structure]
  ;
  ; @return (component or hiccup)
  [ui-structure]
  [ui-structure])
 ;[:div#your-app [ui-structure]]



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!  [] (x.boot-loader/start-app!  #'app))
(defn render-app! [] (x.boot-loader/render-app! #'app))
