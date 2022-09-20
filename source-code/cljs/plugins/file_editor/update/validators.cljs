
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.update.validators
    (:require [plugins.item-editor.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



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
  (let [mutation-name (r update.subs/get-mutation-name db editor-id :save-content!)
        file-content  (get server-response (symbol mutation-name))]
       (map? file-content)))
