
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.validators
    (:require [mid-fruits.map                    :as map]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [received-item        (r download.subs/get-resolver-answer db editor-id :get-item server-response)
        received-suggestions (get server-response :item-editor/get-item-suggestions)]
       (and (or (map? received-suggestions)
                (not (r core.subs/download-suggestions? db editor-id)))
            (or (map/namespaced? received-item)
                (not (r core.subs/download-item? db editor-id))))))
