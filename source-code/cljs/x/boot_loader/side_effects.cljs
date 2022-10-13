
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.side-effects
    (:require [dom.api      :as dom]
              [re-frame.api :as r]
              [reagent.api  :as reagent]
              [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader.api/start-app! #'app)
  [app]
  (r/dispatch-sync [:boot-loader/start-app! app]))

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
(r/reg-fx :boot-loader/render-app! render-app!)
