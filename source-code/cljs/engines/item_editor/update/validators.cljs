
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.update.validators
    (:require [engines.item-editor.core.subs   :as core.subs]
              [engines.item-editor.update.subs :as update.subs]
              [map.api                         :as map]
              [re-frame.api                    :refer [r]]))



;; -- Save item validators ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [new-item?  (r core.subs/new-item?             db editor-id)
        saved-item (r update.subs/get-mutation-answer db editor-id (if new-item? :add-item! :save-item!) server-response)]
       (map/namespaced? saved-item)))
