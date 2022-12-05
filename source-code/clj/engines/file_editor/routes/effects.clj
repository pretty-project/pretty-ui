
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.routes.effects
    (:require [engines.file-editor.routes.helpers :as routes.helpers]
              [re-frame.api                       :as r]
              [uri.api                            :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route (uri/valid-path base-route)]
           [:x.router/add-route! (routes.helpers/route-id editor-id :base)
                                 {:client-event   [:file-editor/handle-route! editor-id]
                                  :js-build       :app
                                  :restricted?    true
                                  :route-template base-route}])))
