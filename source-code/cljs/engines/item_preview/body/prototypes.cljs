
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.body.prototypes
    (:require [candy.api                         :refer [param]]
              [engines.item-preview.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:display-progress? (boolean)
  ;  :items-path (vector)
  ;  :transfer-id (keyword)}
  [preview-id body-props]
  (merge {:display-progress? false
          :items-path        (core.helpers/default-items-path preview-id)
          ; XXX#8173 (source-code/cljs/engines/engine_handler/transfer/README.md)
          :transfer-id preview-id}
         (param body-props)))
