
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.core.subs
    (:require [engines.engine-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-engine-prop core.subs/get-engine-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-preview-prop
  ; @param (keyword) preview-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ; (r get-preview-prop db :my-preview :my-prop)
  ;
  ; @return (map)
  [db [_ preview-id item-key]]
  (r get-engine-prop db preview-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-preview/get-preview-prop :my-preview :my-prop]
(r/reg-sub :item-preview/get-preview-prop get-preview-prop)
