
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.download.validators
    (:require [plugins.file-editor.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-content-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [resolver-id  (r download.subs/get-resolver-id db editor-id :get-content)
        file-content (get server-response resolver-id)]
       (map? file-content)))
