
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-preview.core.events       :as core.events]
              [re-frame.api                           :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received        download.events/data-received)
(def store-received-item! download.events/store-received-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (r core.events/reset-downloads! db preview-id))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ preview-id server-response]]
  (as-> db % (r data-received        % preview-id)
             (r store-received-item! % preview-id server-response)))
