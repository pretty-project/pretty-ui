
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.body.prototypes
    (:require [candy.api                        :refer [param]]
              [engines.file-editor.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:content-path (vector)
  ;  :display-progress? (boolean)
  ;  :transfer-id (keyword)}
  [editor-id body-props]
  (merge {:content-path      (core.helpers/default-content-path editor-id)
          :display-progress? true
          ; XXX#8173 (source-code/cljs/engines/engine_handler/transfer/README.md)
          :transfer-id editor-id}
         (param body-props)))
