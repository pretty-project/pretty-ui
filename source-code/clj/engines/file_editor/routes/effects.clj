
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.routes.effects
    (:require [engines.file-editor.routes.helpers :as routes.helpers]
              [mid-fruits.uri                     :as uri]
              [re-frame.api                       :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route (uri/valid-path base-route)]
           [:router/add-route! (routes.helpers/route-id editor-id :base)
                               {:client-event   [:file-editor/handle-route! editor-id]
                                :restricted?    true
                                :route-template base-route}])))
