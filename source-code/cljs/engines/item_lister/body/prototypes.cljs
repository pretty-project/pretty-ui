
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.body.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [engines.item-lister.core.config  :as core.config]
              [engines.item-lister.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:download-limit (integer)
  ;   :items-path (vector)
  ;   :order-key (keyword)
  ;   :placeholder (metamorphic-content)
  ;   :transfer-id (keyword)}
  [lister-id body-props]
  (merge {:items-path     (core.helpers/default-items-path lister-id)
          :download-limit core.config/DEFAULT-DOWNLOAD-LIMIT
          :order-key      :order
          :placeholder    :no-items-to-show
          ; XXX#8173
          :transfer-id lister-id}
         (param body-props)))
