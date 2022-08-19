

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page.effects
    (:require [x.app-core.api                :as a]
              [x.app-views.error-page.config :as error-page.config]
              [x.app-views.error-page.views  :as error-page.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-error-page!
  ; @param (keyword) error-id
  ;  :no-connection, :no-permission, :page-not-found, :under-construction, :under-maintenance
  ;
  ; @usage
  ;  [:views/render-error-page!]
  (fn [_ [_ error-id]]
      (let [content-props (get error-page.config/ERROR-CONTENT error-id)]
           {:dispatch-n [[:ui/restore-default-title!]
                         [:ui/render-surface! :views.error-page/view
                                              {:content [error-page.views/view :views.error-page/view content-props]}]]})))
