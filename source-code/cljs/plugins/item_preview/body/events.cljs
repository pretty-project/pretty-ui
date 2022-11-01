
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.body.events
    (:require [engines.engine-handler.body.events :as body.events]
              [plugins.item-preview.core.events   :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.body.events
(def store-body-props!  body.events/store-body-props!)
(def remove-body-props! body.events/remove-body-props!)
(def update-body-props! body.events/update-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ preview-id body-props]]
  (r store-body-props! db preview-id body-props))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (as-> db % (r core.events/remove-meta-items! % preview-id)
             (r core.events/reset-downloads!   % preview-id)
             (r remove-body-props!             % preview-id)))

(defn body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ preview-id body-props]]
  (r update-body-props! db preview-id body-props))
