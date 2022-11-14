
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
              [x.ui.api     :as x.ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-app [ui-structure]])
  ;  (start-app! #'app)
  [app]
  (r/dispatch-sync [:boot-loader/start-app! app]))

(defn render-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-app [ui-structure]])
  ;  (render-app! #'app)
  [app]
  (reagent/render [app #'x.ui/structure]
                  (dom/get-element-by-id "x-app-container")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:boot-loader/render-app! #'app]
(r/reg-fx :boot-loader/render-app! render-app!)
