
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.validators
    (:require [plugins.item-lister.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [resolver-id    (r download.subs/get-resolver-id db lister-id :get-items)
        document-count (get-in server-response [resolver-id :document-count])
        documents      (get-in server-response [resolver-id :documents])]
       (and (integer? document-count)
            (vector?  documents))))
