
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler.helpers
    (:require [re-frame.api :as r]
              [reitit.ring  :as reitit-ring]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-resource-handlers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (? in vector)
  []
  (let [resource-handlers @(r/subscribe [:core/get-resource-handlers])]
       (letfn [(f [resource-handlers handler-props]
                  (conj resource-handlers (reitit-ring/create-resource-handler handler-props)))]
              (reduce f [] resource-handlers))))
