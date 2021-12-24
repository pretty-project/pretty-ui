
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!

(ns project-emulator.core
    (:require [x.boot-loader]
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
 ;[:div#your-app [ui-structure]]
  [ui-structure])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn boot-app!   [] (x.boot-loader/boot-app!   #'app))
(defn render-app! [] (x.boot-loader/render-app! #'app))
