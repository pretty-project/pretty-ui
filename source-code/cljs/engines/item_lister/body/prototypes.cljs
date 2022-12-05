
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.body.prototypes
    (:require [candy.api                        :refer [param]]
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
  ; {:display-progress? (boolean)
  ;  :download-limit (integer)
  ;  :items-path (vector)
  ;  :order-key (keyword)
  ;  :placeholder (metamorphic-content)
  ;  :transfer-id (keyword)}
  [lister-id body-props]
  (merge {:display-progress? true
          :download-limit    core.config/DEFAULT-DOWNLOAD-LIMIT
          :items-path        (core.helpers/default-items-path lister-id)
          :order-key         :order
          :placeholder       :no-items-to-show
          ; XXX#8173
          :transfer-id lister-id}
         (param body-props)))
