
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.prototypes
    (:require [candy.api                         :refer [param]]
              [engines.item-handler.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:display-progress? (boolean)
  ;  :item-path (vector)
  ;  :suggestions-path (vector)
  ;  :transfer-id (keyword)}
  [handler-id body-props]
  (merge {:display-progress? true
          :item-path         (core.helpers/default-item-path        handler-id)
          :suggestions-path  (core.helpers/default-suggestions-path handler-id)
          ; XXX#8173
          :transfer-id handler-id}
         (param body-props)))
