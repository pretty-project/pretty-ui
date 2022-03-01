
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.side-effects
    (:require [app-fruits.dom     :as dom]
              [app-fruits.reagent :as reagent]
              [x.app-core.api     :as a]
              [x.app-ui.api       :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader.api/start-app! #'app)
  [app]
  (a/dispatch-sync [:boot-loader/start-app! app]))

(defn render-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader.api/render-app! #'app)
  [app]
  (reagent/render [app #'ui/structure]
                  (dom/get-element-by-id "x-app-container")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:boot-loader/render-app! #'app]
(a/reg-fx :boot-loader/render-app! render-app!)
