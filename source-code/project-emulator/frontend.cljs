
(ns frontend
    (:require [x.boot-loader.api]))
             ;[sente.api]



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
