
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.update.validators
    (:require [engines.file-editor.update.subs :as update.subs]
              [re-frame.api                    :refer [r]]))

 

;; -- Save content validators -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-content-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [file-content (r update.subs/get-mutation-answer db editor-id :save-content! server-response)]
       (map? file-content)))
